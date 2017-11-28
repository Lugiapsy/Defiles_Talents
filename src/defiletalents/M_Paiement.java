package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import methodesVerif.Verify;
import org.joda.time.DateTime;

/**
 * Classe permettant de gérer la table Paiement.
 *
 * @author giraudeaup
 */
public class M_Paiement {

    /**
     * Nom de la table.
     */
    private final static String TABLENAME = "tal_paiement";
    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "create table if not exists tal_paiement (id_achat integer not null, id integer not null, date_paiement date, montant numeric(10,3), commentaire varchar(200), id_mode integer, constraint pk_tal_paiement primary key (id_achat,id));";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_paiement (id_achat, id, date_paiement, montant, commentaire, id_mode) VALUES(%d, %d, '%s', %f, '%s', %d)";
    /**
     * Requête de selection de l'identifiant max de la table (gestion de
     * l'autoincrement manuel).
     */
    private static final String SQL_SELECT_MAX_ID = "SELECT max(id) AS maxId FROM tal_paiement WHERE id_achat = %d";
    /**
     * Requête de selection de l'id dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_paiement WHERE id = %d AND id_achat = %d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_paiement WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_paiement WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_paiement SET date_paiement = '%s', montant = %f, commentaire = '%s', id_mode = %d WHERE id_achat = %d AND id = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_paiement WHERE id_achat = %d AND id = %d";

    /**
     * Identifiant de la table, correspond à l'id achat du paiement.
     */
    private int id_achat;
    /**
     * Identifiant de la table, correspond à un auto-inrcement manuel.
     */
    private int id;
    /**
     * Date du paiement.
     */
    private DateTime date_paiement;
    /**
     * Montant du paiement.
     */
    private Float montant;
    /**
     * Un commentaire pour ce paiement.
     */
    private String commentaire;
    /**
     * Correspond à l'identifiant du mode de paiement utilisé.
     */
    private int id_mode;
    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;

    /**
     *
     * Constructeur de la classe M_Paiement.
     *
     * @param db
     * @param id_achat
     * @param id
     * @param date_paiement
     * @param montant
     * @param commentaire
     * @param id_mode
     *
     */
    public M_Paiement(Db_sqlite db, int id_achat, int id, DateTime date_paiement, Float montant, String commentaire, int id_mode) {
        setDb(db);
        this.id_achat = id_achat;
        this.id = id;
        setDate_Paiement(date_paiement);
        setMontant(montant);
        setCommentaire(commentaire);
        setId_Mode(id_mode);
    }

    /**
     * Méthode permettant d'enregistrer un nouveau paiement dans la table.
     *
     * @param db
     * @param id_achat
     * @param date_paiement
     * @param montant
     * @param commentaire
     * @param id_mode
     *
     */
    public M_Paiement(Db_sqlite db, int id_achat, DateTime date_paiement, Float montant, String commentaire, int id_mode) {
        ResultSet res;
        int selectedId = 1;
        setDb(db);
        this.id_achat = id_achat;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_MAX_ID, getId_Achat()));

            selectedId = (res.getInt("maxId") + 1);
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }

        this.id = selectedId;

        setDate_Paiement(date_paiement);
        setMontant(montant);
        setCommentaire(commentaire);
        setId_Mode(id_mode);

        try {
            db.sqlExec(String.format(SQL_INSERT_ID, getId_Achat(), getId(), getDate_PaiementAsString(), getMontant(), getCommentaire(), getId_Mode()));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Valorise les attributs, execute une requête INSERT si le boolean inserer
     * est sur vrai.
     *
     * @param db
     * @param id_achat
     * @param id
     * @param date_paiement
     * @param montant
     * @param commentaire
     * @param id_mode
     * @param inserer
     */
    public M_Paiement(Db_sqlite db, int id_achat, int id, DateTime date_paiement, Float montant, String commentaire, int id_mode, boolean inserer) {
        this(db, id_achat, id, date_paiement, montant, commentaire, id_mode);

        if (inserer) {
            try {
                db.sqlExec(String.format(Locale.ENGLISH, SQL_INSERT_ID, getId_Achat(), getId(), getDate_PaiementAsString(), getMontant(), getCommentaire(), getId_Mode()));
            }
            catch (SQLException ex) {
                Logger.getLogger(M_Paiement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Méthode permettant de chercher dans la table un paiement correspondant
     * aux ID fournis en parametres.
     *
     * @param db
     * @param id_achat
     * @param id
     *
     */
    public M_Paiement(Db_sqlite db, int id_achat, int id) {
        ResultSet res;
        setDb(db);
        this.id_achat = id_achat;
        this.id = id;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, getId(), getId_Achat()));
            if (res.next()) {
                setDate_Paiement(new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(res.getString("date_paiement"))));
                setMontant(res.getFloat("montant"));
                setCommentaire(res.getString("commentaire"));
                setId_Mode(res.getInt("id_mode"));
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }

    }

    /**
     * Methode permettant de mettre à jour la table.
     */
    public void update() {
        try {
            db.sqlExec(String.format(Locale.ENGLISH, SQL_UPDATE, getDate_PaiementAsString(), getMontant(), getCommentaire(), getId_Mode(), getId_Achat(), getId()));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Methode permettant de supprimer un paiement de la table.
     */
    public void delete() {
        try {
            db.sqlExec(String.format(SQL_DELETE, getId_Achat(), getId()));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Créer une collection de toute la table.
     *
     * @param db
     * @return
     *
     */
    public static CopyOnWriteArrayList<M_Paiement> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Methode permettant la création d'une collection en fonction des critères
     * choisis pour la clause "where" qui doit être fournie en parametres.
     *
     * @param db
     * @param where
     * @return
     *
     */
    public static CopyOnWriteArrayList<M_Paiement> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Paiement> result = new CopyOnWriteArrayList();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                result.add(new M_Paiement(db, res.getInt("id_achat"), res.getInt("id"), new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(res.getString("date_paiement"))), res.getFloat("montant"), res.getString("commentaire"), res.getInt("id_mode")));
            }
        }
        catch (Exception e) {

            LogHandler.log().store(e);
        }
        return result;
    }

    /**
     * Compte le nombre de paiement contenus dans la table.
     *
     * @param db
     * @return
     *
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Compte un nombre de paiement en fonction des critères choisis dans la
     * clause "where" en parametres.
     *
     * @param db
     * @param where
     * @return
     *
     *
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
     * Vérifie l'existance d'un enregistrement dans la table.
     *
     * @param db
     * @param id_achat
     * @param id
     * @return
     */
    public static boolean exists(Db_sqlite db, int id_achat, int id) {
        return (count(db, "id =" + id + " AND id_achat=" + id_achat) == 1);
    }

    /**
     * Valorise la base de données utilisée par la classe.
     *
     * @param db
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Retourne la base de données.
     *
     * @return
     */
    public Db_sqlite getDb() {
        return db;
    }

    /**
     * Valorise la date du paiement pour cet enregistrement.
     *
     * @param date_paiement
     */
    public void setDate_Paiement(DateTime date_paiement) {
        this.date_paiement = date_paiement;
    }

    /**
     * Retourne la date du paiement.
     *
     * @return
     */
    public DateTime getDate_Paiement() {
        return date_paiement;
    }

    /**
     * Retourne la date du paiement sous forme de chaine de caractères.
     *
     * @return String sous forme 'yyyy-MM-dd HH:mm:ss'
     */
    public String getDate_PaiementAsString() {
        return date_paiement.toString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Valorise le montant pour cet enregistrement.
     *
     * @param montant
     */
    public void setMontant(Float montant) {
        if (montant < 0f) montant = 0f;
        this.montant = montant;
    }

    /**
     * Retourne le montant pour cet enregistrement.
     *
     * @return
     */
    public Float getMontant() {
        return montant;
    }

    /**
     * Valorise le commentaire pour cet enregistrement.
     *
     * @param commentaire
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = Verify.toString(commentaire, 200);
    }

    /**
     * Retourne le commentaire pour cet enregistrement.
     *
     * @return
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Retourne l'identifiant de l'achat correspondant à cet enregistrement.
     *
     * @return
     */
    public int getId_Achat() {
        return id_achat;
    }

    /**
     * Retourne l'identifiant auto-increment de cet enregistrement.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Valorise l'identifiant correspondant au mode de paiement.
     *
     * @param id_mode
     */
    public void setId_Mode(int id_mode) {
        this.id_mode = id_mode;
    }

    /**
     * Retourne l'identifiant correspondant au mode de paiement.
     *
     * @return
     */
    public int getId_Mode() {
        return id_mode;
    }

    /**
     * Retourne la requête de création de la table.
     *
     * @return la requête de création de la table au format SQL.
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Méthode d'affichage console des champs de l'enregistrement.
     */
    private void print() {
        System.out.println("");
        System.out.println(String.format("Table %s, enregistrement >%d", TABLENAME, getId()));
        System.out.println(String.format("id_achat [id_achat] (int) : >%d<", getId_Achat()));
        System.out.println(String.format("id [id] (int) : >%d<", getId()));
        System.out.println(String.format("Date de paiement [date_paiement] (date) : >%s<", getDate_PaiementAsString()));
        System.out.println(String.format("Montant [montant] (Réel) : >%f<", getMontant()));
        System.out.println(String.format("commentaire [commentaire] (chaine) : >%s<", getCommentaire()));
        System.out.println(String.format("id_mode [id_mode] (int)  : >%d<", getId_Mode()));
    }

    /**
     * Méthode de test de classe
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Paiement");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/tmp/database.sqlite");
        System.out.println(" OK.");

        db.sqlExec("DROP TABLE IF EXISTS tal_paiement");
        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Paiement.getCreateSqlStatement());
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        int id_achat01 = 5;
        DateTime date01 = DateTime.now();
        float montant01 = 14.48f;
        String commentaire01 = "UN COMMENTAIRE";
        int id_mode01 = 6;
        M_Paiement p01 = new M_Paiement(db, id_achat01, date01, montant01, commentaire01, id_mode01);
        p01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = p01.getId() + 2;
        int id_achat02 = p01.getId_Achat() + 2;
        DateTime date02 = DateTime.now();
        float montant02 = 14.48f;
        String commentaire02 = "UN COMMENTAIRE";
        int id_mode02 = 6;

        M_Paiement p02 = new M_Paiement(db, id_achat02, id02, date02, montant02, commentaire02, id_mode02, true);
        p02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        int id_achat03 = 4555;
        DateTime date03 = DateTime.now();
        float montant03 = 14.48f;
        String commentaire03 = "UN COMMENTAIRE";
        int id_mode03 = 6;

        M_Paiement p03 = new M_Paiement(db, id_achat03, id03, date03, montant03, commentaire03, id_mode03, false);
        p03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Paiement p04 = new M_Paiement(db, p02.getId_Achat(), p02.getId());
        p04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", p02.getId_Achat() / 2, M_Paiement.count(db, String.format("id_achat < %d", p02.getId_Achat() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Paiement.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Paiement.exists(db, 1, 5)));

        int id01 = p01.getId();
        int id_achat00 = p01.getId_Achat();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", id01, M_Paiement.exists(db, id01, id_achat00)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        p01.delete();
        p01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Paiement.exists(db, id01, id_achat00)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Paiement.count(db)));

        CopyOnWriteArrayList<M_Paiement> liste01 = M_Paiement.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Paiement uneLigne : liste01) {
            uneLigne.print();
        }
        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Paiement> liste02 = M_Paiement.records(db, String.format("id < %d", p02.getId() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", p02.getId() / 2, liste02.size()));
        for (M_Paiement uneLigne : liste02) {
            uneLigne.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        p04.print();
        p04.setMontant(5f);
        p04.setCommentaire("le nouveau commentaire");
        p04.setDate_Paiement(DateTime.now());
        p04.setId_Mode(5);
        System.out.println("\nContenu de l'objet avant UPDATE");
        p04.print();
        p04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        p04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Paiement p05 = new M_Paiement(db, p04.getId_Achat(), p04.getId());
        p05.print();
        System.out.println("\nFIN de la méthode update()");
    }
}
