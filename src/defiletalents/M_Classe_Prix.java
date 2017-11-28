package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import methodesVerif.Verify;

/**
 * Classe permettant de gérer la table Classe prix.
 *
 * @author diguetjo, giraudeaup
 *
 * 13/11/2015
 */
public class M_Classe_Prix {

    /**
     * Nom de la table.
     */
    private static final String TABLE_NAME = "tal_classe_prix";
    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "create table if not exists tal_classe_prix (id integer not null, libelle varchar(100), constraint pk_tal_clase_prix primary key (id));";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_classe_prix (id, libelle) " + "VALUES(%d, '%s')";
    /**
     * Requête d'insertion sans ID dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_classe_prix (libelle) " + "VALUES('%s')";

    /**
     * Requête de selection de l'ID dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_classe_prix WHERE id = %d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_classe_prix WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_classe_prix WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_classe_prix SET libelle = '%s' WHERE id = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_classe_prix WHERE id = %d";

    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;
    /**
     * Clé primaire de la table.
     */
    private int id;
    /**
     * Le libellé correspondant à cette classe de prix.
     */
    private String libelle;

    /**
     * Valorise les attributs, ne fait aucun requête SQL. L'enregistrement est
     * supposé exister dans la base de données.
     *
     * @param db la base de données SQLite
     * @param id l'identifiant
     * @param libelle un libellé pour cette classe de prix
     */
    public M_Classe_Prix(Db_sqlite db, int id, String libelle) {
        setDb(db);
        this.id = id;
        setLibelle(libelle);
    }

    /**
     * Valorise les attributs, execute une requête SQL type INSERT INTO.
     *
     * @param db la base de données SQLite
     * @param libelle un libellé pour cette classe de prix
     */
    public M_Classe_Prix(Db_sqlite db, String libelle) {
        ResultSet res;
        setDb(db);
        setLibelle(libelle);

        try {
            db.sqlExec(String.format(SQL_INSERT, getLibelle()));
            res = db.sqlLastId(TABLE_NAME);

            this.id = res.getInt("id");

        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Valorise les attributs, execute une requête type insert into si le
     * boolean INSERER = vrai.
     *
     * @param db la base de données SQLite
     * @param id l'identifiant
     * @param libelle le libellé
     * @param inserer VRAI: INSERT INTO, FAUX: Ne fait aucune requête SQL
     */
    public M_Classe_Prix(Db_sqlite db, int id, String libelle, boolean inserer) {
        this(db, id, libelle);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERT_ID, getId(), getLibelle()));
            }
            catch (SQLException e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Valorise les attributs. Execute une requête SELECT pour récupérer le
     * reste de la table.
     *
     * @param db la base de données SQLite
     * @param id l'identifiant
     */
    public M_Classe_Prix(Db_sqlite db, int id) {
        ResultSet res;
        setDb(db);
        this.id = id;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, getId()));
            if (res.next()) {
                setLibelle(res.getString("libelle"));
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }

    }

    /**
     * Met à jour la table.
     */
    public void update() {
        try {
            db.sqlExec(String.format(SQL_UPDATE, getLibelle(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime l'enregistrement dans la table.
     *
     * @throws SQLException Si l'intégrité référencielle n'est pas respectée.
     */
    public void delete() throws SQLException {
        if (M_Prix.count(db, "id_classe = " + getId()) != 0 || M_Zone.count(db, "id_classe = " + getId()) != 0) {
            throw new SQLException("Intégrité référentielle");
        }
        else {
            try {
                db.sqlExec(String.format(SQL_DELETE, getId()));
            }
            catch (SQLException e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Retourne une collection d'enregistrement correspondante à l'intégralité
     * de la table
     *
     * @param db la base de données SQLite
     * @return Une collection de M_Classe_Prix
     */
    public static CopyOnWriteArrayList<M_Classe_Prix> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Retourne une collection d'enregistrement respectant la condition where
     *
     * @param db la base de données SQLite
     * @param where condition de selection
     * @return Une collection de M_Classe_prix
     */
    public static CopyOnWriteArrayList<M_Classe_Prix> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Classe_Prix> classePrix = new CopyOnWriteArrayList<M_Classe_Prix>();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                classePrix.add(new M_Classe_Prix(db, res.getInt("id"), res.getString("libelle")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return classePrix;
    }

    /**
     * Compte le nombre d'enregistrement dans la table.
     *
     * @param db la base de données SQLite
     * @return un entier représentant le nombre d'enregistrement dans la table
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Compte le nombre d'enregistrement dans la table.
     *
     * @param db la base de données SQLite
     * @param where Condition de selection
     * @return un entier représentant le nombre d'enregistrement dans la table
     * respectant la condition where
     */
    public static int count(Db_sqlite db, String where) {

        int nb = 0;
        ResultSet res;
        try {
            res = db.sqlSelect(String.format(SQL_SELECT_COUNT, where));
            if (res.next()) {
                nb = res.getInt("nb");
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return nb;
    }

    /**
     * Vérifie qu'un enresgitrement comportant l'id passée en paramètre existe.
     *
     * @param db la base de données SQLite
     * @param id l'identifiant à vérifier
     * @return VRAI s'il existe, FAUX sinon
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id =" + id) == 1);
    }

    /**
     * Valorise l'attribut db
     *
     * @param db la base de données
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Retourne la base de données.
     *
     * @return la base de données
     */
    public Db_sqlite getDb() {
        return this.db;
    }

    /**
     * Valorise le libellé de cet enregistrement
     *
     * @param libelle le libellé à valoriser
     */
    public void setLibelle(String libelle) {
        this.libelle = Verify.toString(libelle, 100);
    }

    /**
     * Retourne le libellé de l'enregistrement.
     *
     * @return le libellé
     */
    public String getLibelle() {
        return this.libelle;
    }

    /**
     * Retourne l'identifiant de cet enregistrement.
     *
     * @return l'identifiant
     */
    public int getId() {
        return this.id;
    }

    /**
     * Retourne la requête de création de la table.
     *
     * @return la requête de création de la table au format SQL
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Affichage console des champs de cet enregistrement.
     */
    private void print() {
        System.out.println("id : " + getId());
        System.out.println("Libelle : " + getLibelle());
    }

    /**
     * Methode de test de classe.
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Classe_prix");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/tmp/database.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Classe_Prix.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String libelle01 = "Un libelle avec un espace";
        M_Classe_Prix cate01 = new M_Classe_Prix(db, libelle01);
        cate01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = cate01.getId() + 2;
        String libelle02 = "  [2 espaces en début, 2 espaces en fin890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789]12345678  ";
        M_Classe_Prix cate02 = new M_Classe_Prix(db, id02, libelle02, true);
        cate02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String libelle03 = "  [2 espaces en début, 2 espaces en fin890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789]12345678  ";
        M_Classe_Prix cate03 = new M_Classe_Prix(db, id03, libelle03, false);
        cate03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Classe_Prix cate04 = new M_Classe_Prix(db, cate02.getId());
        cate04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", cate02.getId() / 2, M_Classe_Prix.count(db, String.format("id < %d", cate02.getId() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Classe_Prix.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Classe_Prix.exists(db, 9999)));

        int id01 = cate01.getId();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", id01, M_Classe_Prix.exists(db, id01)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        cate01.delete();
        cate01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Classe_Prix.exists(db, id01)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Classe_Prix.count(db)));

        CopyOnWriteArrayList<M_Classe_Prix> liste01 = M_Classe_Prix.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Classe_Prix uneCate : liste01) {
            uneCate.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Classe_Prix> liste02 = M_Classe_Prix.records(db, String.format("id < %d", cate02.getId() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", cate02.getId() / 2, liste02.size()));
        for (M_Classe_Prix uneCate : liste02) {
            uneCate.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        cate04.print();

        cate04.setLibelle("nouveau Libelle");
        System.out.println("\nContenu de l'objet avant UPDATE");
        cate04.print();
        cate04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        cate04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Classe_Prix cate05 = new M_Classe_Prix(db, cate04.getId());
        cate05.print();
        System.out.println("\nFIN de la méthode update()");
    }

}
