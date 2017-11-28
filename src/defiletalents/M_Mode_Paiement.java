package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import methodesVerif.Verify;

/**
 * Classe permettant de gérer la table Mode_paiement.
 *
 * @author fortinm, giraudeaup
 */
public class M_Mode_Paiement {

    /**
     * Nom de la table.
     */
    private final String TABLE_NAME = "tal_mode_paiement";
    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS tal_mode_paiement (id INTEGER NOT NULL, code VARCHAR(10), libelle VARCHAR(100), CONSTRAINT pk_tal_achat PRIMARY KEY (id));";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_mode_paiement (id, code, libelle) VALUES(%d, '%s', '%s')";
    /**
     * Requête d'insertion sans ID dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_mode_paiement (code, libelle) VALUES('%s', '%s')";
    /**
     * Requête de selection de l'ID dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_mode_paiement WHERE id = %d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_mode_paiement WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_mode_paiement WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_mode_paiement SET code = '%s', libelle = '%s' WHERE id = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_mode_paiement WHERE id = %d";

    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;
    /**
     * L'identifiant du Mode de paiement.
     */
    private int id;
    /**
     * Le code correspondant à ce mode de paiement.
     */
    private String code;
    /**
     * Le libellé correspondant à ce mode de paiement.
     */
    private String libelle;

    /**
     * Valorise les attributs. Ne fait aucune requête SQL.
     *
     * @param db base de données utilisé
     * @param id identifiant de la table
     * @param code code de paiement utilisé
     * @param libelle libellé du mode de paiement
     */
    public M_Mode_Paiement(Db_sqlite db, int id, String code, String libelle) {
        ResultSet res;
        this.db = db;
        this.id = id;
        setCode(code);
        setLibelle(libelle);
    }

    /**
     * Valorise les attributs. Execute une requête SQL type INSERT INTO.
     *
     * @param db la base de données SQLite
     * @param code
     * @param libelle
     */
    public M_Mode_Paiement(Db_sqlite db, String code, String libelle) {
        ResultSet res;
        this.db = db;
        setCode(code);
        setLibelle(libelle);

        try {
            db.sqlExec(String.format(SQL_INSERT, code, libelle));
            res = db.sqlLastId(TABLE_NAME);
            res.next();
            this.id = res.getInt("id");
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }

    }

    /**
     * booléen à "vrai" si on veux faire un ajout sans clés primaire auto dans
     * la table. Ne fais que valoriser les attributs si Faux.
     *
     * @param db
     * @param id
     * @param code
     * @param libelle
     * @param inserer
     */
    public M_Mode_Paiement(Db_sqlite db, int id, String code, String libelle, boolean inserer) {
        this(db, id, code, libelle);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERT_ID, getId(), getCode(), getLibelle()));
            }
            catch (SQLException e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Valorise les attributs, execute une requete SELECT pour récupérer les
     * infos
     *
     * @param db
     * @param id
     */
    public M_Mode_Paiement(Db_sqlite db, int id) {
        ResultSet res;
        this.db = db;
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
     * Met à jour la table.
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
     * Suppression de l'enregistrement dans la table.
     *
     * @throws SQLException si l'intégrité référencielle n'est pas respectée.
     */
    public void delete() throws SQLException {
        if (M_Paiement.count(db, "id_mode = " + getId()) != 0) {
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
     * Retourne la requête de création de la table
     *
     * @return la reqûete de creation de la table au format SQL
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Collection de toute la table
     *
     * @param db
     * @return
     */
    public static CopyOnWriteArrayList<M_Mode_Paiement> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Collection contenant le resultat d'une requête SELECT avec un where
     *
     * @param db
     * @param where
     * @return
     */
    public static CopyOnWriteArrayList<M_Mode_Paiement> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Mode_Paiement> mode = new CopyOnWriteArrayList<M_Mode_Paiement>();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                mode.add(new M_Mode_Paiement(db, res.getInt("id"), res.getString("code"), res.getString("libelle")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return mode;
    }

    /**
     * Compte tous les champs de la table
     *
     * @param db
     * @return
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Compte les champs restriction du where
     *
     * @param db
     * @param where
     * @return
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
     * Indique si l'id passée en paramètre existe ou non
     *
     * @param db
     * @param id
     * @return booléen
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id =" + id) == 1);
    }

    /**
     * Affichage console de l'enregistrement.
     */
    private void print() {
        System.out.println("Id : " + getId());
        System.out.println("Code : " + getCode());
        System.out.println("Libelle : " + getLibelle());
    }

    /**
     * Retourne l'identifiant de cet enregistrement.
     *
     * @return
     */
    public int getId() {
        return this.id;
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
     * Valorise l'attribut.
     *
     * @param code le code pour l'enregistrement
     */
    public void setCode(String code) {
        this.code = Verify.toString(code, 10);
    }

    /**
     * Retourne le code de l'enregistrement.
     *
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Valorise l'attribut libelle.
     *
     * @param libelle un libellé pour l'enregistrement
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
     * Methode de test de classe.
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Mode_paiement");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/defileTalents_files/database.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Mode_Paiement.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String code01 = "[545454841655111111]8888888888";
        String libelle01 = "libelle";

        M_Mode_Paiement modePaie01 = new M_Mode_Paiement(db, code01, libelle01);
        modePaie01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = modePaie01.getId() + 2;
        String code02 = "[545454841655111111]";
        String libelle02 = "autre libelle";
        M_Mode_Paiement modePaie02 = new M_Mode_Paiement(db, id02, code02, libelle02, true);
        modePaie02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String code03 = "   [545454841655111111]   ";
        String libelle03 = "  [2 espaces en début, 2 espaces en fin890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789]12345678  ";
        M_Mode_Paiement modePaie03 = new M_Mode_Paiement(db, id03, code03, libelle03, false);
        modePaie03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Mode_Paiement modePaie04 = new M_Mode_Paiement(db, modePaie02.getId());
        modePaie04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", modePaie02.getId() / 2, M_Mode_Paiement.count(db, String.format("id < %d", modePaie02.getId() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Mode_Paiement.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Mode_Paiement.exists(db, 9999)));

        int id01 = modePaie01.getId();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", id01, M_Mode_Paiement.exists(db, id01)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        modePaie01.delete();
        modePaie01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Mode_Paiement.exists(db, id01)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Mode_Paiement.count(db)));

        CopyOnWriteArrayList<M_Mode_Paiement> liste01 = M_Mode_Paiement.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Mode_Paiement unModePaie : liste01) {
            unModePaie.print();
        }
        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Mode_Paiement> liste02 = M_Mode_Paiement.records(db, String.format("id < %d", modePaie02.getId() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", modePaie02.getId() / 2, liste02.size()));
        for (M_Mode_Paiement unModePaie : liste02) {
            unModePaie.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        modePaie04.print();
        modePaie04.setCode("Une nouvelle référence qui est bien trop longue");
        modePaie04.setLibelle("Modifié");
        System.out.println("\nContenu de l'objet avant UPDATE");
        modePaie04.print();
        modePaie04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        modePaie04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Mode_Paiement modePaie05 = new M_Mode_Paiement(db, modePaie04.getId());
        modePaie05.print();
        System.out.println("\nFIN de la méthode update()");
    }

}
