package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import methodesVerif.Verify;

/**
 * Classe pour la gestion de la table Zone
 *
 * @author parisk, giraudeaup
 *
 */
public class M_Zone {

    /**
     * Nom de la table.
     */
    private static final String TABLE_NAME = "tal_zone";
    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS tal_zone (id INTEGER NOT NULL, libelle VARCHAR(100), id_classe INTEGER, CONSTRAINT pk_tal_zone PRIMARY KEY (id));";
    /**
     * Requête d'insertion sans id dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_zone (libelle, id_classe) VALUES('%s', %d)";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERTID = "INSERT INTO tal_zone (id,libelle, id_classe) VALUES(%d, '%s', %d)";
    /**
     * Requête de selection de l'id dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_zone WHERE id = %d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_zone WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_COUNT = "SELECT COUNT(*) AS nb FROM tal_zone WHERE %s";
    /**
     * Rquête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_zone SET libelle= '%s', id_classe = %d WHERE id = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_zone WHERE id = %d";

    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;

    /**
     * L'identifiant de cet enregistrement.
     */
    private int id;
    /**
     * Le libellé de cet enregistrement.
     */
    private String libelle;
    /**
     * La classe de prix attachée à cet enregistrement.
     */
    private int id_classe;

    /**
     * Valorise les attributs, ne fait aucune requête SQL. L'enregistrement est
     * supposé exister dans la base.
     *
     * @param db  nom de la base de données
     * @param id  l'id dans la table
     * @param libelle  nom de la zone
     * @param id_classe  id de la table classe
     */
    public M_Zone(Db_sqlite db, int id, String libelle, int id_classe) {
        setDb(db);
        this.id = id;
        setLibelle(libelle);
        setIdClasse(id_classe);
    }

    /**
     *
     * @param db le nom de la base de données
     * @param libelle le nom de la zone
     * @param id_classe l'id de la table classe
     */
    public M_Zone(Db_sqlite db, String libelle, int id_classe) {

        ResultSet res;

        setDb(db);
        setLibelle(libelle);
        setIdClasse(id_classe);

        try {
            db.sqlExec(String.format(SQL_INSERT, getLibelle(), getIdClasse()));
            res = db.sqlLastId(TABLE_NAME);
            this.id = res.getInt("id");
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Valorise les attributs, execute une requête INSERT si inserer = vrai.
     *
     * @param db
     * @param id
     * @param libelle
     * @param inserer
     */
    public M_Zone(Db_sqlite db, int id, String libelle, int id_classe, boolean inserer) {
        this(db, id, libelle, id_classe);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERTID, id, getLibelle(), getIdClasse()));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Valorise les attributs, execute une requête SELECT pour valoriser le
     * reste des attributs.
     *
     * @param db
     * @param id
     */
    public M_Zone(Db_sqlite db, int id) {
        ResultSet res;

        setDb(db);
        this.id = id;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, id));
            if (res.next()) {
                setLibelle(res.getString("libelle"));
                setIdClasse(res.getInt("id_classe"));
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
            db.sqlExec(String.format(SQL_UPDATE, getLibelle(), getIdClasse(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime l'enregistrement dans la table.
     *
     * @throws SQLException si l'intégrité référencielle n'est pas respectée.
     */
    public void delete() throws SQLException {
        if (M_Place.count(db, "id_zone = " + getId()) != 0) {
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
     * Retourne une collection de M_Zone correspondant à l'intégralité de la
     * table tal_zone.
     *
     * @param db
     * @return
     */
    public static CopyOnWriteArrayList<M_Zone> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Retourne une collection de M_Zone respectant la condition where de la
     * table tal_zone.
     *
     * @param db
     * @param where
     * @return
     */
    public static CopyOnWriteArrayList<M_Zone> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Zone> zones = new CopyOnWriteArrayList<M_Zone>();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                zones.add(new M_Zone(db, res.getInt("id"), res.getString("libelle"), res.getInt("id_classe")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return zones;
    }

    /**
     * Compte le nombre d'enregistrement dans la table.
     *
     * @param db
     * @return
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Compte le nombre d'enregistrement dans la table respectant la condition
     * where.
     *
     * @param db
     * @param where
     * @return
     */
    public static int count(Db_sqlite db, String where) {
        int nb = 0;
        ResultSet res;
        try {
            res = db.sqlSelect(String.format(SQL_COUNT, where));
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
     * Retourne la requête de création de la table.
     *
     * @return la reqûete de création au format SQL.
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Vérifie l'existance d'un enregistrement dans la table.
     *
     * @param db
     * @param id
     * @return vrai si l'id existe sinon retourne faux
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id =" + id) == 1);
    }

    /**
     * Valorise l'objet base de données.
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
     * Valorise le libellé pour cet enregistrement.
     *
     * @param libelle
     */
    public void setLibelle(String libelle) {

        this.libelle = Verify.toString(libelle, 100);
    }

    /**
     * Retourne le libellé pour cet enregistrement.
     *
     * @return
     */
    public String getLibelle() {
        return this.libelle;
    }

    /**
     * Retourne l'identifiant pour cet enregistrement.
     *
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Valorise l'identifiant de la classe de prix attaché à cet enregistrment.
     *
     * @param id_classe
     */
    public void setIdClasse(int id_classe) {
        this.id_classe = id_classe;
    }

    /**
     * Retourne l'identifiant de la classe de prix attaché à cet enregistrement.
     *
     * @return id_classe dans la table Zone
     */
    public int getIdClasse() {
        return id_classe;
    }

    /**
     * Affichage console des informations de l'enregistrement.
     */
    public void print() {
        System.out.println();
        System.out.println(String.format("Table %s, enregistrement >%d<", TABLE_NAME, getId()));
        System.out.println(String.format(" Libelle [libelle] (%d): >%s<", getLibelle().length(), getLibelle()));
        System.out.println(String.format(" Identifiant du client [id_client] : >%d<", getIdClasse()));

    }

    /**
     * Méthode de test de classe.
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Zone");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite("dataBase.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Zone.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String libelle01 = "[234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890[123456789";
        int idClasse01 = 1;
        M_Zone Zone01 = new M_Zone(db, libelle01, idClasse01);
        Zone01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = Zone01.getId() + 2;
        String commentaire02 = "  [2 espaces en debut et en fin234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890[123456789  ";
        int idClasse02 = 2;
        M_Zone Zone02 = new M_Zone(db, id02, commentaire02, idClasse02, true);
        Zone02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String commentaire03 = "  [2 espaces en debut et en fin234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890[123456789  ";
        int idClasse03 = 3;
        M_Zone Zone03 = new M_Zone(db, id03, commentaire03, idClasse03, false);
        Zone03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Zone Zone04 = new M_Zone(db, Zone02.getId());
        Zone04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", Zone02.getId()
                / 2, M_Zone.count(db, String.format("id < %d", Zone02.getId() / 2))));

        System.out.println(String.format("Méthode count() [all] : %d", M_Zone.count(db)));

        System.out.println(String.format("\nMéthode exist() [id= 9999] : %b", M_Zone.exists(db,
                9999)));
        int id01 = Zone01.getId();

        System.out.println(String.format("\nMéthode exist() [id= %d] : %b", id01, M_Zone.exists(db,
                id01)));

        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        Zone01.delete();
        Zone01 = null;
        System.out.println(" OK.");

        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Zone.exists(db,
                id01)));

        System.out.println(String.format("Méthode count() [all] : %d", M_Zone.count(db)));

        CopyOnWriteArrayList<M_Zone> liste01 = M_Zone.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Zone uneZone : liste01) {
            uneZone.print();
        }
        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Zone> liste02 = M_Zone.records(db, String.format("id < %d", Zone02.getId()
                / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)",
                Zone02.getId() / 2, liste02.size()));
        for (M_Zone uneZone : liste02) {
            uneZone.print();
        }
        System.out.println("FIN de la liste des enregistrements");
    }

}
