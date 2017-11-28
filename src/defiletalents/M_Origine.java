package defiletalents;

import java.sql.ResultSet;
import java.sql.SQLException;

import db_sqlite.Db_sqlite;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe permettant de gérer la table Origine.
 *
 * @author giraudeaup
 */
public class M_Origine {

    /**
     * Nom de la table.
     */
    private static final String TABLE_NAME = "tal_origine";

    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "create table if not exists tal_origine (id integer not null, code varchar(10), libelle varchar(100), constraint pk_tal_origine primary key (id));";

    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_origine (id, code, libelle) " + "VALUES(%d, '%s', '%s')";
    /**
     * Requête d'insertion sans ID dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_origine (code, libelle) " + "VALUES('%s', '%s')";
    /**
     * Requête de selection de l'id dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_origine WHERE id = %d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_origine WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_origine WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_origine SET code = '%s', libelle = '%s' WHERE id = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_origine WHERE id = %d";

    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;

    /**
     * La clé primaire de la table.
     */
    private int id;
    /**
     * Le code correspondant à cette origine.
     */
    private String code;
    /**
     * Le libellé de cette origine.
     */
    private String libelle;

    /**
     * Valorise les attributs, ne fait aucune requête SQL. L'enregistrement est
     * suposé exister dans la base de données.
     *
     * @param db la base de données SQLite
     * @param id clé primaire de la table
     * @param code le code
     * @param libelle le libellé
     */
    public M_Origine(Db_sqlite db, int id, String code, String libelle) {

        setDb(db);
        this.id = id;
        setCode(code);
        setLibelle(libelle);
    }

    /**
     * Valorise les attributs privés. Execute une requête SQL type INSERT avec
     * identifiant automatique.
     *
     * @param db la base de données SQLite
     * @param code le code
     * @param libelle le libellé
     */
    public M_Origine(Db_sqlite db, String code, String libelle) {
        ResultSet res;

        setDb(db);
        setCode(code);
        setLibelle(libelle);

        try {
            db.sqlExec(String.format(SQL_INSERT, getCode(), getLibelle()));
            res = db.sqlLastId(TABLE_NAME);
            this.id = res.getInt("id");
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Valorise les attributs. Execeute une requête SQL type INSERT si le
     * paramètre 'inserer' est VRAI
     *
     * @param db la base de données SQLite
     * @param id la clé primaire
     * @param code le code
     * @param libelle le libellé
     * @param inserer VRAI: l'enregistrement est ajouté dans la base de données
     */
    public M_Origine(Db_sqlite db, int id, String code, String libelle, boolean inserer) {
        this(db, id, code, libelle);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERT_ID, getId(), getCode(), getLibelle()));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Valorise les attributs privés. Execute une requête SQL type SELECT WHERE
     *
     * @param id La clé primaire
     */
    public M_Origine(Db_sqlite db, int id) {
        ResultSet res;
        setDb(db);
        this.id = id;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, getId()));
            if (res.next()) {
                setCode(res.getString("code"));
                setLibelle(res.getString("libelle"));
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Met à jour la table avec les valeurs contenus dans les attributs privés.
     * Execute une requête type UPDATE
     */
    public void update() {
        try {
            db.sqlExec(String.format(SQL_UPDATE, getCode(), getLibelle(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime le champs de la table. Execute une requête type DELETE FROM
     *
     * @throws SQLException si l'id origine est présente dans un enregistrement
     * client
     */
    public void delete() throws SQLException {
        if (M_Client.count(db, "id_origine = " + getId()) != 0) {
            throw new SQLException("Intégrité référentielle");
        }
        else {
            try {
                db.sqlExec(String.format(SQL_DELETE, getId()));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     *
     * Fournit le contenu de la table M_Origine
     *
     * @param db la base de donnée SQLite
     * @return collection de M_Origine contenant la table complète dans la base
     * de donnée
     */
    public static CopyOnWriteArrayList<M_Origine> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     *
     * @param db la base de donnée SQLite
     * @param where la condition de selection
     * @return retourne une collection de M_Origine contenant la table complète
     * respectant la condition de selection
     */
    public static CopyOnWriteArrayList<M_Origine> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Origine> origines = new CopyOnWriteArrayList<M_Origine>();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));

            while (res.next()) {
                origines.add(new M_Origine(db, res.getInt("id"), res.getString("code"), res.getString("libelle")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }

        return origines;
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_Origine
     *
     * @param db la base de donnée SQLite
     * @return retourne le nombre d'élément dans la table
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     *
     * @param db la base de donnée SQLite
     * @param where la condition de selection
     * @return retourne le nombre d'élement dans la base de donnée respectant la
     * condition de selection
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
     *
     * @param db la base de donnée SQLite
     * @param id l'id à vérifier
     * @return retourne vrai si l'id existe dans la table, faux si non
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id =" + id) == 1);
    }

    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Valorise l'attribut db de l'objet.
     *
     * @param db la base de données
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Fournit la base de données
     *
     * @return base de données SQLite
     */
    public Db_sqlite getDb() {
        return this.db;
    }

    /**
     * Valorise le code de l'objet
     *
     * @param code le code
     */
    public void setCode(String code) {
        this.code = methodesVerif.Verify.toString(code, 10).toUpperCase();
    }

    /**
     * Fournit le code
     *
     * @return le code stocké dans l'objet
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Valorise l'attribut libelle.
     *
     * @param libelle le libellé
     */
    public void setLibelle(String libelle) {
        this.libelle = methodesVerif.Verify.toString(libelle, 100);
    }

    /**
     * Fournit le libellé contenu dans l'objet
     *
     * @return le libellé
     */
    public String getLibelle() {
        return this.libelle;
    }

    /**
     * Fournit l'id (Clé primaire de la table)
     *
     * @return la clé primaire
     */
    public int getId() {
        return this.id;
    }

    /**
     * Méthode permettant d'afficher les valeurs contenus dans chacuns des
     * attributs.
     */
    public void print() {
        System.out.println();
        System.out.println(String.format("Table %s, enregistrement >%d<", TABLE_NAME, getId()));
        System.out.println(String.format("  Code [code] (chaine) : >%s<", getCode()));
        System.out.println(String.format("  Libellé [libelle] : >%s<", getLibelle()));
    }

    /**
     * Méthode de test de la classe.
     *
     * @param args non utilisé
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Origine");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/tmp/database.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Origine.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String code01 = "[45686365]555555";
        String libelle01 = "Un libelle avec un espace";
        M_Origine origine01 = new M_Origine(db, code01, libelle01);
        origine01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = origine01.getId() + 2;
        String code02 = "[5454548416551111155555551]";
        String libelle02 = "  [2 espaces en début, 2 espaces en fin890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789]12345678  ";
        M_Origine origine02 = new M_Origine(db, id02, code02, libelle02, true);
        origine02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String code03 = "   [545454841655111111]   ";
        String libelle03 = "  [2 espaces en début, 2 espaces en fin890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789]12345678  ";
        M_Origine origine03 = new M_Origine(db, id03, code03, libelle03, false);
        origine03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Origine origine04 = new M_Origine(db, origine02.getId());
        origine04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", origine02.getId() / 2, M_Origine.count(db, String.format("id < %d", origine02.getId() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Origine.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Origine.exists(db, 9999)));

        int id01 = origine01.getId();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", id01, M_Origine.exists(db, id01)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        origine01.delete();
        origine01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Origine.exists(db, id01)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Origine.count(db)));

        CopyOnWriteArrayList<M_Origine> liste01 = M_Origine.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Origine uneOrigine : liste01) {
            uneOrigine.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Origine> liste02 = M_Origine.records(db, String.format("id < %d", origine02.getId() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", origine02.getId() / 2, liste02.size()));
        for (M_Origine uneOrigine : liste02) {
            uneOrigine.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        origine04.print();
        origine04.setCode("Nouveau code trop long");
        origine04.setLibelle("nouveau Libelle");
        System.out.println("\nContenu de l'objet avant UPDATE");
        origine04.print();
        origine04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        origine04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Origine origine05 = new M_Origine(db, origine04.getId());
        origine05.print();
        System.out.println("\nFIN de la méthode update()");
    }
}
