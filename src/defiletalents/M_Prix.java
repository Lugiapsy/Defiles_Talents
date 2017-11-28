package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import methodesVerif.Verify;

/**
 * Classe permettant de gérer la table Prix.
 *
 * @author jauzelons, giraudeaup
 * @version 0.1
 *
 */
public class M_Prix {

    /**
     * Nom de la table.
     */
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS tal_prix (id_classe INTEGER NOT NULL, id_categorie INTEGER NOT NULL, prix NUMERIC(10,3), CONSTRAINT pk_tal_prix PRIMARY KEY(id_classe,id_categorie));";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_prix (id_classe, id_categorie, prix) " + "VALUES(%d, %d, %f)";
    /**
     * Requête de selection de l'id dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_prix WHERE id_classe = %d AND id_categorie = %d";
    /**
     * Requête de selection ave condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_prix WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_prix WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_prix SET prix = %f WHERE id_classe = %d AND id_categorie = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_prix WHERE id_classe = %d AND id_categorie = %d";

    /**
     * Base de données SQLite.
     */
    private Db_sqlite db;
    /**
     * Identifiant correspondant à la classe de prix. (1/2)
     */
    private int id_classe;
    /**
     * Identifiant correspondant à la catégorie de prix. (2/2)
     */
    private int id_categorie;
    /**
     * Prix pour cet enregistrement.
     */
    private float prix;

    /**
     * Valorise les attributs, ne fait aucune requête SQL.
     *
     * @param db
     * @param id_classe
     * @param id_categorie
     * @param prix
     *
     */
    public M_Prix(Db_sqlite db, int id_classe, int id_categorie, float prix) {
        setDb(db);
        this.id_classe = id_classe;
        this.id_categorie = id_categorie;
        setPrix(prix);
    }

    /**
     * Valorise les attributs, execute une requete INSERT si boolean est sur
     * VRAI.
     *
     * @param db
     * @param id_classe
     * @param id_categorie
     * @param prix
     * @param b si vrai alors insertion dans la base de données
     *
     */
    public M_Prix(Db_sqlite db, int id_classe, int id_categorie, float prix, boolean b) {
        this(db, id_classe, id_categorie, prix);

        if (b) {
            try {
                db.sqlExec(String.format(Locale.ENGLISH, SQL_INSERT_ID, getIdClasse(), getIdCategorie(), getPrix()));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Valorise les attributs, execute une requpete SELECT pour récupérer le
     * prix.
     *
     * @param db
     * @param id_classe
     * @param id_categorie
     *
     */
    public M_Prix(Db_sqlite db, int id_classe, int id_categorie) {
        ResultSet res;
        setDb(db);
        this.id_classe = id_classe;
        this.id_categorie = id_categorie;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, getIdClasse(), getIdCategorie()));
            if (res.next()) {
                setPrix(res.getFloat("prix"));
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }

    }

    /**
     * Met à jour la base de données avec l'objet courant.
     *
     */
    public void update() {
        try {
            db.sqlExec(String.format(Locale.ENGLISH, SQL_UPDATE, getPrix(), getIdClasse(), getIdCategorie()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime l'objet courant de la base de données.
     *
     */
    public void delete() {
        try {
            db.sqlExec(String.format(SQL_DELETE, getIdClasse(), getIdCategorie()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Retourne une collection de M_Prix correspondant à l'intégralité de la
     * table.
     *
     * @param db
     * @return l'ensemble des enregistrements
     */
    public static CopyOnWriteArrayList<M_Prix> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Retourne un sous ensemble de M_Prix correspondant à la table, respectant
     * la condition where.
     *
     * @param db
     * @param where la condition
     * @return Un sous-ensemble d'enregistrement en fonction de la condition
     */
    public static CopyOnWriteArrayList<M_Prix> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Prix> lesPrix = new CopyOnWriteArrayList<M_Prix>();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                lesPrix.add(new M_Prix(db, res.getInt("id_classe"), res.getInt("id_categorie"), res.getFloat("prix")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return lesPrix;
    }

    /**
     * Compte le nombre d'enregistrement dans la table.
     *
     * @param db
     * @return le nombre d'enregistrements dans la table
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Compte le nombre d'enregistrement dans la table respectant la condition
     * where.
     *
     * @param db
     * @param where la condition
     * @return le nombre d'enregistrement en fonction de la condition
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
     * Vérifie l'existance d'un enregistrement dans la table.
     *
     * @param db
     * @param id_classe
     * @param id_categorie
     * @return 1 si l'enregistrement existe sinon renvoi 0
     */
    public static boolean exists(Db_sqlite db, int id_classe, int id_categorie) {
        return (count(db, "id_classe = " + id_classe + " AND id_categorie = " + id_categorie + "") == 1);
    }

    /**
     * Retourne la première partie de l'identifiant de cet enregistrement.
     *
     * @return
     */
    public int getIdClasse() {
        return this.id_classe;
    }

    /**
     * Retourne la seconde partie de l'identifiant de cet enregistrement.
     *
     * @return
     */
    public int getIdCategorie() {
        return this.id_categorie;
    }

    /**
     * Valorise l'objet base de données utilisée.
     *
     * @param db
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Retourne l'objet base de données.
     *
     * @return
     */
    public Db_sqlite getDb() {
        return this.db;
    }

    /**
     * Valorise le prix pour cet enregistrement.
     *
     * @param prix
     */
    public void setPrix(float prix) {
        if (prix < 0) {
            prix = 0f;
        }
        this.prix = Verify.toFloat(Float.toString(prix));
    }

    /**
     * Retourne le prix de cet enregistrement.
     *
     * @return
     */
    public float getPrix() {
        return this.prix;
    }

    /**
     * Retourne la requête de création de la table.
     *
     * @return la requête de création au format SQL.
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Permet de tester les valeurs de l'objet courant.
     */
    private void print() {
        System.out.println();
        System.out.println("table tal_prix");
        System.out.println("  id_classe : " + getIdClasse());
        System.out.println("  id_categorie : " + getIdCategorie());
        System.out.println("  prix : " + getPrix());
    }

    /**
     * Méthode de test de classe
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Prix");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/tmp/database.sqlite");
        System.out.println(" OK.");
        db.sqlExec("DROP TABLE IF EXISTS tal_prix");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Prix.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        float prix01 = 14.1f;
        M_Prix p01 = new M_Prix(db, 14, 15, prix01);
        p01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int idclasse02 = p01.getIdClasse() + 2;
        int idcateg02 = p01.getIdCategorie() + 2;
        float prix02 = 15.1f;
        M_Prix p02 = new M_Prix(db, idclasse02, idcateg02, prix02, true);
        p02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int idclasse03 = 9999;
        int idcateg03 = 4848;
        float prix03 = 45.5f;
        M_Prix p03 = new M_Prix(db, idclasse03, idcateg03, prix03, false);
        p03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Prix p04 = new M_Prix(db, p02.getIdClasse(), p02.getIdCategorie());
        p04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", p02.getIdClasse() / 2, M_Prix.count(db, String.format("id_classe < %d", p02.getIdClasse() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Prix.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Prix.exists(db, 9999, 4848)));

        int idclasse01 = p01.getIdClasse();
        int idcateg01 = p01.getIdCategorie();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", idclasse01, M_Prix.exists(db, idclasse01, idcateg01)));
        System.out.print(String.format("Méthode delete() [id= %d]...", idclasse01));
        p01.delete();
        p01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", idclasse01, M_Prix.exists(db, idclasse01, idcateg01)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Prix.count(db)));

        CopyOnWriteArrayList<M_Prix> liste01 = M_Prix.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Prix uneCate : liste01) {
            uneCate.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Prix> liste02 = M_Prix.records(db, String.format("id_classe < %d", p02.getIdClasse() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", p02.getIdClasse() / 2, liste02.size()));
        for (M_Prix uneCate : liste02) {
            uneCate.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        p04.print();

        p04.setPrix(45f);
        System.out.println("\nContenu de l'objet avant UPDATE");
        p04.print();
        p04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        p04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Prix p05 = new M_Prix(db, p04.getIdClasse(), p04.getIdCategorie());
        p05.print();
        System.out.println("\nFIN de la méthode update()");
    }
}
