package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import methodesVerif.Verify;

/**
 * Classe permettant de gérer la table type_place.
 *
 * @author giraudeaup
 */
public class M_Type_Place {

    /**
     * Nom de la table.
     */
    private static final String TABLE_NAME = "tal_zone";

    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS tal_type_place (id INTEGER NOT NULL, libelle varchar(100), CONSTRAINT pk_tal_zone PRIMARY KEY (id));";
    /**
     * Requête d'insertion sans id dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_type_place (libelle) VALUES('%s')";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERTID = "INSERT INTO tal_type_place (id, libelle) VALUES(%d, '%s')";
    /**
     * Requête de selection de l'id dans la table.
     */
    private static final String SQL_SELECTID = "SELECT * FROM tal_type_place WHERE id = %d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_type_place WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_COUNT = "SELECT COUNT(*) AS nb FROM tal_type_place WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_type_place SET libelle= '%s' WHERE id = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_type_place WHERE id = %d";

    /**
     * Base de données SQLite.
     */
    private Db_sqlite db;
    /**
     * Identifiant de l'enregistrement.
     */
    private int id;
    /**
     * Le libellé de l'enregistrement.
     */
    private String libelle;

    /**
     * Constructeur N°1 Valorise les attributs, ne fait aucune requête SQL.
     * L'enregistrement est supposé exister dans la base.
     *
     * @param db  nom de la base de données
     * @param id  l'id dans la table
     * @param libelle  nom de la zone
     */
    public M_Type_Place(Db_sqlite db, int id, String libelle) {
        setDb(db);
        this.id = id;
        setLibelle(libelle);
    }

    /**
     * Constructeur N°2 identifiant automatique.
     *
     * @param db  nom de la base de données
     * @param libelle  nom de la zone
     */
    public M_Type_Place(Db_sqlite db, String libelle) {
        ResultSet res;

        setDb(db);
        setLibelle(libelle);

        try {
            db.sqlExec(String.format(SQL_INSERT, libelle));
            res = db.sqlLastId(TABLE_NAME);
            this.id = res.getInt("id");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructeur N°3 Valorise les attributs, execute une requête SQL type
     * INSERT si le paramètre 'inserer' est vrai
     *
     * @param db
     * @param id
     * @param libelle
     * @param inserer
     */
    public M_Type_Place(Db_sqlite db, int id, String libelle, boolean inserer) {
        this(db, id, libelle);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERTID, getId(), getLibelle()));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Constructeur N°4 Valorise les attributs, execute une requête SQL type
     * SELECT WHERE.
     *
     * @param db
     * @param id
     */
    public M_Type_Place(Db_sqlite db, int id) {
        ResultSet res;

        setDb(db);
        this.id = id;

        try {
            res = db.sqlSelect(String.format(SQL_SELECTID, getId()));
            if (res.next()) {
                setLibelle(res.getString("libelle"));
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
            db.sqlExec(String.format(SQL_UPDATE, libelle, getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime l'enregistrement correspondant à l'id en attribut. Exécute une
     * requêtede type DELETE
     *
     * @throws SQLException si l'intégrité référencielle n'est pas respectée.
     */
    public void delete() throws SQLException {
        if (M_Place.count(db, "id_type = " + getId()) != 0) {
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
     * Fournit le contenu de la table M_Type_Place
     *
     * @param db
     * @return
     */
    public static CopyOnWriteArrayList<M_Type_Place> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Fournit une partie de la table M_Type_Place
     *
     * @param db
     * @param where
     * @return
     */
    public static CopyOnWriteArrayList<M_Type_Place> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Type_Place> typePlace = new CopyOnWriteArrayList<M_Type_Place>();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                typePlace.add(new M_Type_Place(db, res.getInt("id"), res.getString("libelle")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return typePlace;
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_Type_Place
     *
     * @param db
     * @return
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_Type_Place
     * correspondant au critère demandé.
     *
     * @param db
     * @param where
     * @return
     */
    public static int count(Db_sqlite db, String where) {
        String sql = String.format(SQL_COUNT, where);
        int nb = 0;
        ResultSet res;
        try {
            res = db.sqlSelect(sql);
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
     * @param db
     * @param id
     * @return vrai si l'id existe sinon retourne faux
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id =" + id) == 1);
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
     * Valorise la base de données.
     *
     * @param db
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Retourne la base de données utilisée.
     *
     * @return
     */
    public Db_sqlite getDb() {
        return this.db;
    }

    /**
     * Valorise le libellé de cet enregistrement.
     *
     * @param libelle
     */
    public void setLibelle(String libelle) {

        this.libelle = Verify.toString(libelle, 100);
    }

    /**
     * Retourne le libellé de cet enregistrement.
     *
     * @return
     */
    public String getLibelle() {
        return this.libelle;
    }

    /**
     * Retourne l'identifiant de l'enregistrement.
     *
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Affichage console des informations de l'enregistrement.
     */
    private void print() {
        System.out.println();
        System.out.println(String.format("Table %s, enregistrement >%d<", TABLE_NAME, getId()));
        System.out.println(String.format(" Libelle [libelle] (%d): >%s<", getLibelle().length(), getLibelle()));
        //	System.out.println(String.format(" Libelle [libelle] : >%s<", getLibelle()));
    }

    /**
     * Méthode de test de classe.
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Type_Place");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite("dataBase.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Type_Place.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String libelle01 = "[23456789012345678901234567890123456789012345678901[123456789";
        M_Type_Place Place01 = new M_Type_Place(db, libelle01);
        Place01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = Place01.getId() + 2;
        String commentaire02 = "  [2 espaces en debut et en fin[23456789012345678901234567890123456789012345678901[123456789  ";
        M_Type_Place Place02 = new M_Type_Place(db, id02, commentaire02, true);
        Place02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String commentaire03 = "  [2 espaces en debut et en fin[23456789012345678901234567890123456789012345678901[123456789  ";
        M_Type_Place Place03 = new M_Type_Place(db, id03, commentaire03, false);
        Place03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Type_Place Place04 = new M_Type_Place(db, Place02.getId());
        Place04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", Place02.getId() / 2,
                M_Type_Place.count(db, String.format("id < %d", Place02.getId() / 2))));

        System.out.println(String.format("Méthode count() [all] : %d", M_Type_Place.count(db)));

        System.out.println(String.format("\nMéthode exist() [id= 9999] : %b", M_Type_Place.exists(db,
                9999)));
        int id01 = Place01.getId();

        System.out.println(String.format("\nMéthode exist() [id= %d] : %b", id01, M_Type_Place.exists(db,
                id01)));

        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        Place01.delete();
        Place01 = null;
        System.out.println(" OK.");

        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Type_Place.exists(db,
                id01)));

        System.out.println(String.format("Méthode count() [all] : %d", M_Type_Place.count(db)));

        CopyOnWriteArrayList<M_Type_Place> liste01 = M_Type_Place.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Type_Place unePlace : liste01) {
            unePlace.print();
        }
        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Type_Place> liste02 = M_Type_Place.records(db, String.format("id < %d", Place02.getId()
                / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)",
                Place02.getId() / 2, liste02.size()));
        for (M_Type_Place unePlace : liste02) {
            unePlace.print();
        }
        System.out.println("FIN de la liste des enregistrements");
    }
}
