package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import methodesVerif.Verify;

/**
 *
 * Classe permettant de gérer la table Plan
 *
 * @author diguetjo, giraudeaup
 * @version 0.1
 */
public class M_Plan {

    /**
     * Nom de la table.
     */
    private static final String TABLE_NAME = "tal_plan";
    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS tal_plan (id INTEGER NOT NULL, libelle VARCHAR(100), image VARCHAR(200),largeur_place integer, hauteur_place integer, CONSTRAINT pk_tal_achat PRIMARY KEY (id));";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_plan (id, libelle, image, largeur_place, hauteur_place) VALUES(%d, '%s', '%s', %d, %d)";
    /**
     * Requête d'insertion sans ID dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_plan (libelle, image, largeur_place, hauteur_place) VALUES('%s', '%s', %d, %d)";
    /**
     * Requête de selection de l'id dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_plan WHERE id = %d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_plan WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_plan WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_plan SET libelle = '%s', image = '%s', largeur_place = %d, hauteur_place = %d WHERE id = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_plan WHERE id = %d";

    /**
     * Base de données SQLite.
     */
    private Db_sqlite db;
    /**
     * Identifiant de l'enregistrement.
     */
    private int id;
    /**
     * Libellé de l'enregistrement.
     */
    private String libelle;
    /**
     * Lien vers l'image utilisée.
     */
    private String image;

    /**
     * Largeur de la place sur le plan.
     */
    private int largeur_place;
    /**
     * Hauteur de la place sur le plan.
     */
    private int hauteur_place;

    /**
     * Valorise les attributs, ne fait aucune requête SQL. L'enregistrement est
     * supposé exister dans la base.
     *
     * @param db Base de donnée SQLite
     * @param id Identifiant de la réservation
     * @param libelle le libellé
     * @param image lien vers l'image
     * @param largeur_place
     * @param hauteur_place
     */
    public M_Plan(Db_sqlite db, int id, String libelle, String image, int largeur_place, int hauteur_place) {
        setDb(db);
        this.id = id;
        setLibelle(libelle);
        setImage(image);
        setLargeur_place(largeur_place);
        setHauteur_place(hauteur_place);

    }

    /**
     * Valorise les attributs, execute une requête SQL de type INSERT avec
     *
     * identifiant automatique.
     *
     * @param db Base de donnée SQLite
     * @param libelle libelle du plan
     * @param image image du plan
     * @param largeur_place
     * @param hauteur_place
     *
     */
    public M_Plan(Db_sqlite db, String libelle, String image, int largeur_place, int hauteur_place) {
        ResultSet res;

        setDb(db);
        setLibelle(libelle);
        setImage(image);
        setLargeur_place(largeur_place);
        setHauteur_place(hauteur_place);

        try {
            db.sqlExec(String.format(SQL_INSERT, getLibelle(), getImage(), getLargeur_place(), getHauteur_place()));
            res = db.sqlLastId(TABLE_NAME);
            this.id = res.getInt("id");
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Valorise les attributs, execute une requête SQL type INSERT si le
     * paramètre 'inserer' est vrai
     *
     * @param db Base de donnée SQLite
     * @param id Identifiant du plan
     * @param libelle libelle du plan
     * @param image image du plan
     * @param largeur_place
     * @param hauteur_place
     * @param inserer VRAI: l'enregistrement est ajouté dans la base de données
     */
    public M_Plan(Db_sqlite db, int id, String libelle, String image, int largeur_place, int hauteur_place, boolean inserer) {
        this(db, id, libelle, image, largeur_place, hauteur_place);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERT_ID, getId(), getLibelle(), getImage(), getLargeur_place(), getHauteur_place()));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Valorise les attributs, execute une requête SQL type SELECT WHERE.
     *
     *
     * @param db Base de donnée SQLite
     * @param id Identifiant de l'enregistrement recherché
     */
    public M_Plan(Db_sqlite db, int id) {
        ResultSet res;
        setDb(db);

        this.id = id;
        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, getId()));
            if (res.next()) {
                setLibelle(res.getString("libelle"));
                setImage(res.getString("image"));
                setLargeur_place(res.getInt("largeur_place"));
                setHauteur_place(res.getInt("hauteur_place"));
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Met à jour la base de donnée avec les attributs de la classe. Exécute une
     * requête de type UPDATE
     */
    public void update() {
        try {
            db.sqlExec(String.format(SQL_UPDATE, getLibelle(), getImage(), getLargeur_place(), getHauteur_place(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime l'enregistrement correspondant à l'id en attribut. Exécute une
     * requêtede type DELETE
     *
     * @throws SQLException Si l'intégrité référencielle n'est pas respectée.
     */
    public void delete() throws SQLException {
        if (M_Place.count(db, "id_plan = " + getId()) != 0) {
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
     * Fournit le contenu de la table M_Plan
     *
     * @param db Base de donnée SQLite
     * @return Collection de M_Plan correspondant à la table complète dans la
     * base de donnée
     */
    public static CopyOnWriteArrayList<M_Plan> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Fournit une partie de la table M_Plan
     *
     * @param db Base de donnée SQLite
     * @param where Condition de selection
     * @return Collection de M_Plan correspondant au critère 'where'
     */
    public static CopyOnWriteArrayList<M_Plan> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Plan> plan = new CopyOnWriteArrayList();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                plan.add(new M_Plan(db, res.getInt("id"), res.getString("libelle"), res.getString("image"), res.getInt("largeur_place"), res.getInt("hauteur_place")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return plan;
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_Plan
     *
     *
     * @param db Base de donnée SQLite
     * @return Retourne le nombre d'éléments dans la table
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_Plan correspondant au
     * critère demandé.
     *
     * @param db Base de donnée SQLite
     * @param where Condition de selection
     * @return Retourne le nombre d'éléments dans la table
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
     * Teste l'existence d'un enregistrement
     *
     * @param db Base de donnée SQLite
     * @param id Identifiant à vérifier
     * @return retourne VRAI si l'enregistrement correspondant au paramètre id
     * existe dans la table.
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id = " + id) == 1);
    }

    /**
     * Fournit la requête SQL de création de la table
     *
     * @return Requête SQL permettant de créer la table tal_plan
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Valorise la propriété db de l'objet M_Plan
     *
     * @param db Base de données SQLite
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Fournit l'objet représentant la base de données SQLite
     *
     * @return Base de données SQLite
     */
    public Db_sqlite getDb() {
        return this.db;
    }

    /**
     * Valorise le libellé de l'enregistrement.
     *
     * @param libelle
     */
    public void setLibelle(String libelle) {
        this.libelle = Verify.toString(libelle, 100);
    }

    /**
     * Retourne le libellé de l'enregistrement.
     *
     * @return
     */
    public String getLibelle() {
        return this.libelle;
    }

    /**
     * Valorise le lien de l'image pour cet enregistrement.
     *
     * @param image
     */
    public void setImage(String image) {
        this.image = Verify.toString(image, 200);
    }

    /**
     * Retourne le lien de l'image correspondant à cet enregistrement.
     *
     * @return
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Valorise la largeur de la place sur le plan.
     *
     * @param largeur_place
     */
    public void setLargeur_place(int largeur_place) {
        this.largeur_place = largeur_place;
    }

    /**
     * Retourne la largeur de cette place sur le plan.
     *
     * @return
     */
    public int getLargeur_place() {
        return largeur_place;
    }

    /**
     * Valorise la hauteur de la place sur le plan.
     *
     * @param hauteur_place
     */
    public void setHauteur_place(int hauteur_place) {
        this.hauteur_place = hauteur_place;
    }

    /**
     * Retourne la hauteur de la place sur le plan.
     *
     * @return
     */
    public int getHauteur_place() {
        return hauteur_place;
    }

    /**
     * Fournit l'identifiant de l'enregistrement.
     *
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Affiche sur la console le contenu de l'objet (mise au point).
     */
    private void print() {
        System.out.println();
        System.out.println(String.format("Table %s, enregistrement >%d<", TABLE_NAME, getId()));
        System.out.println(String.format(" Libelle [libelle] (%d): >%s<", getLibelle().length(), getLibelle()));
        System.out.println(String.format(" Image [image] (%d): >%s<", getImage().length(), getImage()));
        System.out.println(String.format(" Image [image] (%d): >%d<", getImage().length(), getImage()));
        System.out.println(String.format(" largeur_place [largeur_place]: >%d<", getLargeur_place()));
        System.out.println(String.format(" hauteur_place [hauteur_place]: >%d<", getHauteur_place()));

    }

    /**
     * Procédure de test de la classe
     *
     * @param args Paramètres à l'exécution de la classe (non utilisé)
     * @throws java.lang.Exception Exception en cas de problème.
     */
    public static void main(String[] args) throws Exception {
        /* System.out.println("\nTest de la classe M_Plan");
        System.out.println("=========================\n");
        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("/home/saiirod/tmp/database.sqlite"));
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Plan.getCreateSqlStatement());
        System.out.println(" OK.");
        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String libelle01 = "[23456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123";
        String image01 = "[23456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123";

        M_Plan plan01 = new M_Plan(db, libelle01, image01);
        plan01.print();
        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = plan01.getId() + 2;
        String libelle02 = " [2 espaces en début, 2 espaces enfin8901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678";
        String image02 = " [2 espaces en début, 2 espaces enfin8901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678";

        M_Plan plan02 = new M_Plan(db, id02, libelle02, image02, true);
        plan02.print();
        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String libelle03 = " [2 espaces en début, 2 espaces enfin8901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678";
        String image03 = " [2 espaces en début, 2 espaces enfin8901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678";

        M_Plan plan03 = new M_Plan(db, id03, libelle03, image03, false);
        plan03.print();
        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Plan plan04 = new M_Plan(db, plan02.getId());
        plan04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", plan02.getId() / 2, M_Plan.count(db, String.format("id < %d", plan02.getId() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Plan.count(db)));
        System.out.println(String.format("\nMéthode exist() [id= 9999] : %b", M_Plan.exists(db, 9999)));
        int id01 = plan01.getId();
        System.out.println(String.format("\nMéthode exist() [id= %d] : %b", id01, M_Plan.exists(db,
                id01)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        plan01.delete();
        plan01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Plan.exists(db, id01)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Plan.count(db)));
        CopyOnWriteArrayList<M_Plan> liste01 = M_Plan.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Plan unPlan : liste01) {
            unPlan.print();
        }
        System.out.println("FIN de la liste des enregistrements");
        CopyOnWriteArrayList<M_Plan> liste02 = M_Plan.records(db, String.format("id < %d", plan02.getId()
                / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)",
                plan02.getId() / 2, liste02.size()));
        for (M_Plan unPlan : liste02) {
            unPlan.print();
        }
        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        plan04.print();

        plan04.setLibelle("nouveau Libelle");
        plan04.setImage("nouvelle image");
        System.out.println("\nContenu de l'objet avant UPDATE");
        plan04.print();
        plan04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        plan04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Plan plan05 = new M_Plan(db, plan04.getId());
        plan05.print();
        System.out.println("\nFIN de la méthode update()");*/
    }
}
