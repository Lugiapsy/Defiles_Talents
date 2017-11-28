package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import methodesVerif.Verify;

/**
 * Classe permettant de créer différentes catégories de prix
 *
 * @author rousselotv, henrij
 * @version v1.0
 */
public class M_Categorie_Prix {

    /**
     * Nom de la table gérée.
     */
    private static final String TABLE_NAME = "tal_categorie_prix";
    /**
     * Requête SQL de création de la table.
     */
    private static final String SQL_CREATE = "CREATE table IF NOT EXISTS tal_categorie_prix (id INTEGER NOT NULL, libelle VARCHAR(100), CONSTRAINT pk_tal_categorie_prix PRIMARY KEY (id));";

    /**
     * Requete SQL d'insertion d'enregistrement avec identifiant.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_categorie_prix (id, libelle) VALUES(%d, '%s')";
    /**
     * Requete SQL d'insertion d'enregistrement avec identifiant automatique.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_categorie_prix (libelle) VALUES('%s')";

    /**
     * Requête SQL de sélection pour une identifiant donné.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_categorie_prix WHERE id = %d";
    /**
     * Requête SQL de sélection avec un critère de sélection.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_categorie_prix WHERE %s";
    /**
     * Requête SQL de comptage d'enregistrements.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_categorie_prix WHERE %s";

    /**
     * Requête SQL de mise à jour d'enregistrement.
     */
    private static final String SQL_UPDATE = "UPDATE tal_categorie_prix SET libelle = '%s' WHERE id = %d";
    /**
     * Requête SQL de suppression d'enregistrement.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_categorie_prix WHERE id = %d";

    /**
     * Base de données.
     */
    private Db_sqlite db;

    /**
     * Identifiant de l'enregistrement.
     */
    private int id;
    /**
     * Catégorie de prix.
     */
    private String libelle;

    /**
     * Valorise les attributs, ne fait aucune requête SQL. L'enregistrement est
     * supposé exister dans la base de données.
     *
     * @param db Base de données
     * @param id Identifiant de la catégorie
     * @param libelle Désignation de la catégorie
     */
    public M_Categorie_Prix(Db_sqlite db, int id, String libelle) {
        setDb(db);
        this.id = id;
        setLibelle(libelle);
    }

    /**
     * Constructeur avec accès à la base de données avec une requête INSERT puis
     * un accès à la base de données pour récupérer la valeur de l'identifiant
     *
     * @param db Base de données sqlite
     * @param libelle Désignation de la catégorie
     */
    public M_Categorie_Prix(Db_sqlite db, String libelle) {
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
     * Constructeur utile si on souhaite pouvoir utiliser un objet sans faire
     * d'accès à la base de données ou si on souhaite créer un objet avec une
     * clé primaire qui n'est pas un numéro automatique.
     *
     * @param db Base de données sqlite
     * @param id Identifiant de la categorie
     * @param libelle Designation de la categorie
     * @param inserer Booléen permettant d'utiliser ou non une requête
     * d'insertion
     */
    public M_Categorie_Prix(Db_sqlite db, int id, String libelle, boolean inserer) {
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
     * Constructeur pour un enregistrement existant dans la table et pour lequel
     * on connaît la valeur de l'identifiant.
     *
     * @param db base de données sqlite
     * @param id identifiant de la categorie
     */
    public M_Categorie_Prix(Db_sqlite db, int id) {
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
     * Mise à jour de l'enregistrement dans la table.
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
     * Suppression de l'enregistrement.
     *
     * @throws SQLException Si l'intégrité référencielle n'est pas respectée.
     */
    public void delete() throws SQLException {
        if (M_Prix.count(db, "id_categorie = " + getId()) != 0 || M_Ligne.count(db, "id_categorie = " + getId()) != 0) {
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
     * Fournit le contenu de la table
     *
     * @param db Base de donnée SQLite
     * @return Collection correspondant à la table complète dans la base de
     * donnée
     */
    public static CopyOnWriteArrayList<M_Categorie_Prix> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Fournit une partie de la table
     *
     * @param db base de données sqlite
     * @param where clause SQL pour filtrer les categories
     * @return collection de catégories selon la clause SQL
     */
    public static CopyOnWriteArrayList<M_Categorie_Prix> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Categorie_Prix> cat = new CopyOnWriteArrayList<M_Categorie_Prix>();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                cat.add(new M_Categorie_Prix(db, res.getInt("id"), res.getString("libelle")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return cat;
    }

    /**
     * Permet de connaître le nombre d'enregistrements
     *
     * @param db Base de données sqlite
     * @return Nombre de catégories au total
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Permet de connaître le nombre d'enregistrements répondant à un critère.
     *
     * @param db Base de données sqlite
     * @param where Clause SQL pour filtrer les catégories
     * @return Nombres de catégories selon la clause
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
     * Teste l'existence d'un enregistrement.
     *
     * @param db Base de données sqlite
     * @param id Identifiant de la base de données
     * @return VRAI si l'enregistrement est trouvé, FAUX sinon
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id=" + id) == 1);
    }

    /**
     * Valorise l'objet représentant la base de donnée SQLite.
     *
     * @param db Base de données SQLite
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Fournit l'objet représentant la base de donnée SQLite.
     *
     * @return Base de données SQLite
     */
    public Db_sqlite getDb() {
        return db;
    }

    /**
     * Valorise le libellé de la catégorie de prix
     *
     * @param libelle Libellé de la catégorie de prix
     */
    public void setLibelle(String libelle) {
        this.libelle = Verify.toString(libelle, 100);
    }

    /**
     * Fournit le libellé de la catégorie de prix
     *
     * @return Libellé de la catégorie de prix
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Fournit l'identifiant de la catégorie de prix
     *
     * @return Identifiant de la catégorie de prix
     */
    public int getId() {
        return id;
    }

    /**
     * Fournit la requête SQL de création de la table
     *
     * @return Requête SQL de création de table
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Affiche à la console les données de l'objet
     */
    public void print() {
        System.out.println();
        System.out.println(String.format("Table %s, enregistrement >%d<", TABLE_NAME, getId()));
        System.out.println(String.format("  Libellé de la catégorie de prix [libelle] (%d) : >%s<", getLibelle().length(), getLibelle()));
    }

    /**
     * Méthode pour tester les différentes méthodes de la classe
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        System.out.println("\nTest de la classe M_Categorie_Prix");
        System.out.println("==================================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite("__tmp__.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Categorie_Prix.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String libelle01 = "Un libelle correct";
        M_Categorie_Prix cate01 = new M_Categorie_Prix(db, libelle01);
        cate01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = cate01.getId() + 2;
        String libelle02 = "  [2 espaces en début, 2 espaces en fin89012345678901234567890123456789012345678901234567890123456789]  ";
        M_Categorie_Prix cate02 = new M_Categorie_Prix(db, id02, libelle02, true);
        cate02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String libelle03 = "  [2 espaces en début, 2 espaces en fin89012345678901234567890123456789012345678901234567890123456789]  ";
        M_Categorie_Prix cate03 = new M_Categorie_Prix(db, id03, libelle03, false);
        cate03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Categorie_Prix cate04 = new M_Categorie_Prix(db, cate02.getId());
        cate04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", cate02.getId() / 2, M_Categorie_Prix.count(db, String.format("id < %d", cate02.getId() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Categorie_Prix.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Categorie_Prix.exists(db, 9999)));

        int id01 = cate01.getId();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", id01, M_Categorie_Prix.exists(db, id01)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        cate01.delete();
        cate01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Categorie_Prix.exists(db, id01)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Categorie_Prix.count(db)));

        CopyOnWriteArrayList<M_Categorie_Prix> liste01 = M_Categorie_Prix.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Categorie_Prix uneCate : liste01) {
            uneCate.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Categorie_Prix> liste02 = M_Categorie_Prix.records(db, String.format("id < %d", cate02.getId() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", cate02.getId() / 2, liste02.size()));
        for (M_Categorie_Prix uneCate : liste02) {
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
        M_Categorie_Prix cate05 = new M_Categorie_Prix(db, cate04.getId());
        cate05.print();
        System.out.println("\nFIN de la méthode update()");
    }
}
