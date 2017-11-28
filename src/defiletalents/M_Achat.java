package defiletalents;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.joda.time.DateTime;
import db_sqlite.Db_sqlite;
import java.util.concurrent.CopyOnWriteArrayList;
import methodesVerif.Verify;

/**
 *
 * Classe permettant de gérer la table achat
 *
 * @author giraudeaup
 */
public class M_Achat {

    /**
     * Le nom de la table.
     */
    private static final String TABLE_NAME = "tal_achat";

    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS tal_achat (id INTEGER NOT NULL, ref_resa VARCHAR(20), date_resa DATE, valide BOOLEAN, commentaire VARCHAR(200), id_client INTEGER, CONSTRAINT pk_tal_achat PRIMARY KEY (id));";

    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_achat (id, ref_resa, date_resa, valide, commentaire, id_client) VALUES(%d, '%s', '%s', %d, '%s', %d)";

    /**
     * Requête d'insertion sans ID dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_achat (ref_resa, date_resa, valide, commentaire, id_client) VALUES('%s', '%s', %d, '%s', %d)";

    /**
     * Requête de selection de l'id dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_achat WHERE id=%d";

    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_achat WHERE %s";

    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_achat WHERE %s";

    /**
     * Reqûete de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_achat SET ref_resa = '%s', date_resa='%s', valide=%d, commentaire='%s', id_client=%d WHERE id=%d";

    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_achat WHERE id=%d";

    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;

    /**
     * La clé primaire de la table.
     */
    private int id;
    /**
     * La date de réservation.
     */
    private DateTime date_resa;
    /**
     * La référence de réservation.
     */
    private String ref_resa;
    /**
     * Valide ?.
     */
    private boolean valide;
    /**
     * Un commentaire pour cette réservation.
     */
    private String commentaire;
    /**
     * L'identifiant du client attaché à cette reservation.
     */
    private int id_client;

    /**
     * Valorise les attributs, ne fait aucune requête SQL. L'enregistrement est
     * supposé exister dans la base.
     *
     * @param db Base de donnée SQLite
     * @param id Identifiant de la reservation
     * @param ref_resa Référence de reservation
     * @param date_resa Date de reservation
     * @param valide Validation de la reservation par le gestionnaire
     * @param commentaire Commentaire pour la reservation
     * @param id_client Identifiant du client effectuant la reservation
     */
    public M_Achat(Db_sqlite db, int id, DateTime date_resa, String ref_resa, boolean valide, String commentaire, int id_client) {
        setDb(db);
        this.id = id;
        setDate_resa(date_resa);
        setRef_resa(ref_resa);
        setValide(valide);
        setCommentaire(commentaire);
        setId_client(id_client);
    }

    /**
     * Valorise les attributs, execute une requête SQL de type INSERT avec
     * identifiant automatique.
     *
     * @param db Base de donnée SQLite
     * @param ref_resa Référence de reservation
     * @param date_resa Date de reservation
     * @param valide Validation de la reservation par le gestionnaire
     * @param commentaire Commentaire pour la reservation
     * @param id_client Identifiant du client effectuant la reservation
     */
    public M_Achat(Db_sqlite db, DateTime date_resa, String ref_resa, boolean valide, String commentaire, int id_client) {
        ResultSet res;

        setDb(db);
        setDate_resa(date_resa);
        setRef_resa(ref_resa);
        setValide(valide);
        setCommentaire(commentaire);
        setId_client(id_client);

        try {
            db.sqlExec(String.format(SQL_INSERT, getRef_resa(), getDate_resaAsString(), getValideAsInt(), getCommentaire(), getId_client()));
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
     * @param id Identifiant de la reservation
     * @param ref_resa Référence de reservation
     * @param date_resa Date de la reservation
     * @param valide Validation de la reservation par le gestionnaire
     * @param commentaire Commentaire pour la reservation
     * @param id_client Identifiant du client effectuant la reservation
     * @param inserer VRAI: l'enregistrement est ajouté dans la base de données
     */
    public M_Achat(Db_sqlite db, int id, DateTime date_resa, String ref_resa, boolean valide, String commentaire, int id_client, boolean inserer) {
        this(db, id, date_resa, ref_resa, valide, commentaire, id_client);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERT_ID, id, getRef_resa(), getDate_resaAsString(), getValideAsInt(), getCommentaire(), getId_client()));
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
    public M_Achat(Db_sqlite db, int id) {
        ResultSet res;

        setDb(db);
        this.id = id;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, id));
            if (res.next()) {
                setRef_resa(res.getString("ref_resa"));
                setDate_resa(new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(res.getString("date_resa"))));
                setValide(res.getInt("valide"));
                setCommentaire(res.getString("commentaire"));
                setId_client(res.getInt("id_client"));
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
            db.sqlExec(String.format(SQL_UPDATE, getRef_resa(), getDate_resaAsString(), getValideAsInt(), getCommentaire(), getId_client(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime l'enregistrement correspondant à l'id en attribut. Exécute une
     * requête de type DELETE
     */
    public void delete() {
        try {
            db.sqlExec(String.format(SQL_DELETE, getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Fournit le contenu de la table M_Achat
     *
     * @param db Base de donnée SQLite
     * @return Collection de M_Achat correspondant à la table complète dans la
     * base de donnée
     */
    public static CopyOnWriteArrayList<M_Achat> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Fournit une partie de la table M_Achat
     *
     * @param db Base de donnée SQLite
     * @param where Condition de selection
     * @return Collection de M_Achat correspondant au critère 'where'
     */
    public static CopyOnWriteArrayList<M_Achat> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Achat> achats = new CopyOnWriteArrayList();
        ResultSet res;
        M_Achat thisAchat;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                thisAchat = new M_Achat(db, res.getInt("id"), new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(res.getString("date_resa"))), res.getString("ref_resa"), res.getBoolean("valide"), res.getString("commentaire"), res.getInt("id_client"));
                achats.add(thisAchat);
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
        return achats;
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_Achat
     *
     * @param db Base de donnée SQLite
     * @return Retourne le nombre d'éléments dans la table
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_achat correspondant
     * au critère demandé.
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
     * @return Requête SQL permettant de créer la table tal_achat
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Valorise la propriété db de l'objet M_achat
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
     * Valorise la propriété correspondant à la date de reservation
     *
     * @param date_resa Date de reservation
     *
     */
    public void setDate_resa(DateTime date_resa) {
        this.date_resa = date_resa;
    }

    /**
     * Fournit la date de reservation
     *
     * @return Date de reservation
     */
    public DateTime getDate_resa() {
        return this.date_resa;
    }

    /**
     * Fournit la date de reservation sous forme d'une chaine de caractères
     *
     * @return Date de reservation
     */
    public String getDate_resaAsString() {
        return date_resa.toString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Valorise la propriété correspondant à la référence de reservation.
     *
     * @param ref_resa Référence de reservation
     */
    public void setRef_resa(String ref_resa) {
        this.ref_resa = Verify.toString(ref_resa, 20);
    }

    /**
     * Fournit la référence de reservation sous forme d'une chaine de caractères
     *
     * @return Référénce de reservation
     */
    public String getRef_resa() {
        return this.ref_resa;
    }

    /**
     * Valorise la propriété correspondant à la validation de l'achat par le
     * gestionnaire
     *
     * @param valide VRAI pour un achat validé
     */
    public void setValide(boolean valide) {
        this.valide = valide;
    }

    /**
     * Valorise la propriété correspondant à la validation de l'achat par le
     * gestionnaire
     *
     * @param valide valeur numérique 1 pour un achat validé
     */
    private void setValide(int valide) {
        setValide(valide == 1);
    }

    /**
     * Fournit l'état de la reservation
     *
     * @return VRAI si la reservation a été validée
     */
    public Boolean getValide() {
        return this.valide;
    }

    /**
     * Fournit l'état de la reservation sous forme d'un entier
     *
     * @return 1: la reservation est validée, 0 sinon
     */
    private int getValideAsInt() {
        return (getValide() ? 1 : 0);
    }

    /**
     * Valorise le commentaire associé à la reservation. varchar(200)
     *
     * @param commentaire Commentaire associé à la reservation
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = Verify.toString(commentaire, 200);
    }

    /**
     * Fournit le commentaire associé à la reservation
     *
     * @return Commentaire associé à la reservation
     */
    public String getCommentaire() {
        return this.commentaire;
    }

    /**
     * Valorise l'identifiant du client qui réalise la reservation
     *
     * @param id_client Identifiant du client
     */
    public void setId_client(int id_client) {
        this.id_client = (id_client < 0 ? 0 : id_client);
    }

    /**
     * Fournit l'identifiant du client qui réalise la reservation
     *
     * @return Identifiant du client
     */
    public int getId_client() {
        return this.id_client;
    }

    /**
     * Fournit l'identifiant de la reservation
     *
     * @return Identifiant de la reservation
     */
    public int getId() {
        return this.id;
    }

    /**
     * Affiche sur la console le contenu de l'objet (mise au point)
     */
    private void print() {
        System.out.println();
        System.out.println(String.format("Table %s, enregistrement >%d<", TABLE_NAME, getId()));
        System.out.println(String.format("  Référence de reservation [ref_resa] (chaine) : >%s<", getRef_resa()));
        System.out.println(String.format("  Date de reservation [date_resa] (date) : >%s<", getDate_resa()));
        System.out.println(String.format("  Date de reservation [date_resa] (chaine) : >%s<", getDate_resa().toString("E dd MMM YYYY")));
        System.out.println(String.format("  Date de reservation [date_resa] (chaine sqlite) : >%s<", getDate_resaAsString()));
        System.out.println(String.format("  Validation de la reservation [valide] (booléen) : >%b<", getValide()));
        System.out.println(String.format("  Validation de la reservation [valide] (entier) : >%d<", getValideAsInt()));
        System.out.println(String.format("  Commentaire [commentaire] (%d): >%s<", getCommentaire().length(), getCommentaire()));
        System.out.println(String.format("  Identifiant du client [id_client] : >%d<", getId_client()));
    }

    /**
     * Procédure de test de la classe
     *
     * @param args Paramètres à l'exécution de la classe (non utilisé)
     * @throws java.lang.Exception Exception en cas de problème.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Achat");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/tmp/database.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Achat.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String ref_resa01 = "[545454841655111111]8888888888";
        DateTime date01 = DateTime.now();
        Boolean valide01 = true;
        String commentaire01 = "[234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789]1234567890";
        int idClient01 = 1;
        M_Achat achat01 = new M_Achat(db, date01, ref_resa01, valide01, commentaire01, idClient01);
        achat01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = achat01.getId() + 2;
        String ref_resa02 = "[545454841655111111]";
        DateTime date02 = DateTime.now();
        Boolean valide02 = false;
        String commentaire02 = "  [2 espaces en début, 2 espaces en fin890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789]12345678  ";
        int idClient02 = 2;
        M_Achat achat02 = new M_Achat(db, id02, date02, ref_resa02, valide02, commentaire02, idClient02, true);
        achat02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String ref_resa03 = "   [545454841655111111]   ";
        DateTime date03 = DateTime.now();
        Boolean valide03 = true;
        String commentaire03 = "  [2 espaces en début, 2 espaces en fin890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789]12345678  ";
        int idClient03 = 3;
        M_Achat achat03 = new M_Achat(db, id03, date03, ref_resa03, valide03, commentaire03, idClient03, false);
        achat03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Achat achat04 = new M_Achat(db, achat02.getId());
        achat04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", achat02.getId() / 2, M_Achat.count(db, String.format("id < %d", achat02.getId() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Achat.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Achat.exists(db, 9999)));

        int id01 = achat01.getId();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", id01, M_Achat.exists(db, id01)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        achat01.delete();
        achat01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Achat.exists(db, id01)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Achat.count(db)));

        CopyOnWriteArrayList<M_Achat> liste01 = M_Achat.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Achat unAchat : liste01) {
            unAchat.print();
        }
        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Achat> liste02 = M_Achat.records(db, String.format("id < %d", achat02.getId() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", achat02.getId() / 2, liste02.size()));
        for (M_Achat unAchat : liste02) {
            unAchat.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        achat04.print();
        achat04.setRef_resa("Une nouvelle référence qui est bien trop longue");
        achat04.setDate_resa(DateTime.now());
        achat04.setValide(!achat04.getValide());
        achat04.setCommentaire("nouveau Commentaire");
        achat04.setId_client(achat04.getId_client() * 2);
        System.out.println("\nContenu de l'objet avant UPDATE");
        achat04.print();
        achat04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        achat04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Achat achat05 = new M_Achat(db, achat04.getId());
        achat05.print();
        System.out.println("\nFIN de la méthode update()");
    }
}
