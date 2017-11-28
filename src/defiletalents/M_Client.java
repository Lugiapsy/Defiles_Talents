package defiletalents;

import java.sql.ResultSet;
import java.sql.SQLException;

import db_sqlite.Db_sqlite;
import java.util.concurrent.CopyOnWriteArrayList;
import methodesVerif.Verify;

/**
 *
 * Classe permettant de gérer la table client.
 *
 * @author giraudeaup
 *
 */
public class M_Client {

    /**
     * Nom de la table.
     */
    private static final String TABLE_NAME = "tal_client";

    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "create table if not exists tal_client (id integer not null, nom varchar(50), prenom varchar(30), adresse1 varchar(38), adresse2 varchar(38), lieu_dit varchar(38), code_postal varchar(5), bureau varchar(32), telephone varchar(30), courriel varchar(30), commentaire varchar(200), id_origine integer, constraint pk_tal_client primary key (id));";

    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_client (id, nom, prenom, adresse1, adresse2, lieu_dit, code_postal, bureau, telephone, courriel, commentaire, id_origine) " + "VALUES(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d)";
    /**
     * Requête d'insertion sans ID dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_client (nom, prenom, adresse1, adresse2, lieu_dit, code_postal, bureau, telephone, courriel, commentaire, id_origine) " + "VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d)";
    /**
     * Requête de selection avec ID dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_client WHERE id=%d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_client WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_client WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_client SET nom='%s', prenom='%s', adresse1='%s', adresse2='%s', lieu_dit='%s', code_postal='%s', bureau='%s', telephone='%s', courriel='%s', commentaire='%s', id_origine=%d WHERE id=%d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_client WHERE id=%d";

    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;

    /**
     * Clé primaire de la table.
     */
    private int id;
    /**
     * Nom du client.
     */
    private String nom;
    /**
     * Prénom du client.
     */
    private String prenom;
    /**
     * Adresse du client.
     */
    private String adresse1;
    /**
     * Complément d'adresse du client.
     */
    private String adresse2;
    /**
     * Lieu dit du client.
     */
    private String lieu_dit;
    /**
     * Code postal du client.
     */
    private String code_postal;
    /**
     * Bureau du client ?.
     */
    private String bureau;
    /**
     * Téléphone du client.
     */
    private String telephone;
    /**
     * Adresse electronique du client.
     */
    private String courriel;
    /**
     * Un commentaire pour ce client.
     */
    private String commentaire;
    /**
     * L'identifiant de l'origine attachée à ce client.
     */
    private int id_origine;

    /**
     * Valorise les attributs, ne fait aucune requête SQL.
     *
     * @param db la base de donnée SQLite
     * @param id clé primaire de la table
     * @param nom le nom du client
     * @param prenom le prenom du client
     * @param adresse1 l'adresse du client
     * @param adresse2 le complément d'adresse du client
     * @param lieu_dit le lieu dit
     * @param code_postal le code postal
     * @param bureau
     * @param telephone telephone du client
     * @param courriel son adresse mail
     * @param commentaire un commentaire
     * @param id_origine
     */
    public M_Client(Db_sqlite db, int id, String nom, String prenom, String adresse1, String adresse2, String lieu_dit, String code_postal, String bureau, String telephone, String courriel, String commentaire, int id_origine) {
        setDb(db);
        this.id = id;
        setNom(nom);
        setPrenom(prenom);
        setAdresse1(adresse1);
        setAdresse2(adresse2);
        setLieuDit(lieu_dit);
        setCodePostal(code_postal);
        setBureau(bureau);
        setTelephone(telephone);
        setCourriel(courriel);
        setCommentaire(commentaire);
        setIdOrigine(id_origine);
    }

    /**
     * Valorise les attributs, execute une requête SQL type INSERT.
     *
     * @param db la base de donnée SQLite
     * @param nom le nom du client
     * @param prenom le prenom du client
     * @param adresse1 l'adresse du client
     * @param adresse2 le complément d'adresse du client
     * @param lieu_dit le lieu dit
     * @param code_postal le code postal
     * @param bureau
     * @param telephone telephone du client
     * @param courriel son adresse mail
     * @param commentaire un commentaire
     * @param id_origine
     */
    public M_Client(Db_sqlite db, String nom, String prenom, String adresse1, String adresse2, String lieu_dit, String code_postal, String bureau, String telephone, String courriel, String commentaire, int id_origine) {
        ResultSet res;

        setDb(db);
        setNom(nom);
        setPrenom(prenom);
        setAdresse1(adresse1);
        setAdresse2(adresse2);
        setLieuDit(lieu_dit);
        setCodePostal(code_postal);
        setBureau(bureau);
        setTelephone(telephone);
        setCourriel(courriel);
        setCommentaire(commentaire);
        setIdOrigine(id_origine);

        try {
            db.sqlExec(String.format(SQL_INSERT, getNom(), getPrenom(), getAdresse1(), getAdresse2(), getLieuDit(), getCodePostal(), getBureau(), getTelephone(), getCourriel(), getCommentaire(), getIdOrigine()));
            res = db.sqlLastId(TABLE_NAME);
            this.id = res.getInt("id");
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Valorise les atrtibuts, execute une requête SQL type INSERT si le boolean
     * est vrai
     *
     * @param db la base de donnée SQLite
     * @param id la clé primaire de la table
     * @param nom le nom du client
     * @param prenom le prenom du client
     * @param adresse1 l'adresse du client
     * @param adresse2 le complément d'adresse du client
     * @param lieu_dit le lieu dit
     * @param code_postal le code postal
     * @param bureau
     * @param telephone telephone du client
     * @param courriel son adresse mail
     * @param commentaire un commentaire
     * @param id_origine
     * @param inserer execute une requête INSERT si vrai, rien si faux
     */
    public M_Client(Db_sqlite db, int id, String nom, String prenom, String adresse1, String adresse2, String lieu_dit, String code_postal, String bureau, String telephone, String courriel, String commentaire, int id_origine, boolean inserer) {
        this(db, id, nom, prenom, adresse1, adresse2, lieu_dit, code_postal, bureau, telephone, courriel, commentaire, id_origine);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERT_ID, getId(), getNom(), getPrenom(), getAdresse1(), getAdresse2(), getLieuDit(), getCodePostal(), getBureau(), getTelephone(), getCourriel(), getCommentaire(), getIdOrigine()));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Valorise les attributs, execute une requête SQL type SELECT WHERE
     *
     * @param db la base de donnée SQLite
     * @param id la clé primaire de la table
     */
    public M_Client(Db_sqlite db, int id) {
        ResultSet res;
        setDb(db);
        this.id = id;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, getId()));
            if (res.next()) {
                setNom(res.getString("nom"));
                setPrenom(res.getString("prenom"));
                setAdresse1(res.getString("adresse1"));
                setAdresse2(res.getString("adresse2"));
                setLieuDit(res.getString("lieu_dit"));
                setCodePostal(res.getString("code_postal"));
                setBureau(res.getString("bureau"));
                setTelephone(res.getString("telephone"));
                setCourriel(res.getString("courriel"));
                setCommentaire(res.getString("commentaire"));
                setIdOrigine(res.getInt("id_origine"));
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }

    }

    /**
     * Met à jour la base de donnée avec les attributs de la classe. Execute une
     * requête SQL type UPDATE
     *
     */
    public void update() {
        try {
            db.sqlExec(String.format(SQL_UPDATE, getNom(), getPrenom(), getAdresse1(), getAdresse2(), getLieuDit(), getCodePostal(), getBureau(), getTelephone(), getCourriel(), getCommentaire(), getIdOrigine(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime la ligne correspondante à l'id en attribut. Execute une requête
     * SQL type DELETE FROM
     */
    public void delete() {
        try {
            db.sqlExec(String.format(SQL_DELETE, getId()));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     *
     * @param db la base de donnée SQLite
     * @return retourne une collection de M_Client contenant la table complète
     * dans la base de donnée
     */
    public static CopyOnWriteArrayList<M_Client> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     *
     * @param db la base de donnée SQLite
     * @param where condition de selection
     * @return retourne une collection de M_Client contenant la table complète
     * et respectant la condition
     */
    public static CopyOnWriteArrayList<M_Client> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Client> clients = new CopyOnWriteArrayList<M_Client>();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                clients.add(new M_Client(db, res.getInt("id"), res.getString("nom"), res.getString("prenom"), res.getString("adresse1"), res.getString("adresse2"), res.getString("lieu_dit"), res.getString("code_postal"), res.getString("bureau"), res.getString("telephone"), res.getString("courriel"), res.getString("commentaire"), res.getInt("id_origine")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return clients;
    }

    /**
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
     * @param where condition de selection
     * @return retourne le nombre d'élement dans la table respectant la
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
     * @return retourne vrai si l'id existe dans la table, faux sinon
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id =" + id) == 1);
    }

    /**
     *
     * @return Retourne la requête SQL permettant de créer la table tal_achat
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Valorise l'attribut base de données
     *
     * @param db la base de données SQLite
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Fournit la base de données
     *
     * @return la base de données SQLite
     */
    public Db_sqlite getDb() {
        return this.db;
    }

    /**
     * Valorise l'attribut nom
     *
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = Verify.toString(nom, 50).toUpperCase();
    }

    /**
     * Fournit l'attribut privé nom
     *
     * @return le nom
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Valorise l'attribut prénom
     *
     * @param prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = Verify.toString(prenom, 30);
    }

    /**
     * Fournit l'attribut privé prénom
     *
     * @return le prénom
     */
    public String getPrenom() {
        return this.prenom;
    }

    /**
     * Valorise l'adresse 1
     *
     * @param adresse1
     */
    public void setAdresse1(String adresse1) {
        this.adresse1 = Verify.toString(adresse1, 38);
    }

    /**
     * Fournit l'adresse 1 de la table
     *
     * @return
     */
    public String getAdresse1() {
        return this.adresse1;
    }

    /**
     * Valorise le complément d'adresse
     *
     * @param adresse2
     */
    public void setAdresse2(String adresse2) {
        this.adresse2 = Verify.toString(adresse2, 38);
    }

    /**
     * Fournit le complément d'adresse
     *
     * @return l'adresse 2
     */
    public String getAdresse2() {
        return this.adresse2;
    }

    /**
     * Valorise le lieu dit
     *
     * @param lieu_dit
     */
    public void setLieuDit(String lieu_dit) {
        this.lieu_dit = Verify.toString(lieu_dit, 38).toUpperCase();
    }

    /**
     * Fournit le lieu dit
     *
     * @return
     */
    public String getLieuDit() {
        return this.lieu_dit;
    }

    /**
     * Valorise le code postal
     *
     * @param code_postal
     */
    public void setCodePostal(String code_postal) {
        this.code_postal = Verify.toString(code_postal, 5);

    }

    /**
     * Fournit le code postal
     *
     * @return
     */
    public String getCodePostal() {
        return this.code_postal;
    }

    /**
     * Valorise l'attribut bureau
     *
     * @param bureau
     */
    public void setBureau(String bureau) {
        this.bureau = Verify.toString(bureau, 32).toUpperCase();
    }

    /**
     * Fournit l'attribut bureau
     *
     * @return
     */
    public String getBureau() {
        return this.bureau;
    }

    /**
     * Valorise l'attribut téléphone
     *
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = Verify.toString(telephone, 30);
    }

    /**
     * Fournit l'attribut telephone
     *
     * @return
     */
    public String getTelephone() {
        return this.telephone;
    }

    /**
     * Valorise l'attribut courriel
     *
     * @param courriel
     */
    public void setCourriel(String courriel) {
        this.courriel = Verify.toString(courriel, 30);
    }

    /**
     * Fournit l'attribut courriel
     *
     * @return
     */
    public String getCourriel() {
        return this.courriel;
    }

    /**
     * Valorise un commentaire pour ce client
     *
     * @param commentaire
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = Verify.toString(commentaire, 200);
    }

    /**
     * Fournit le commentaire
     *
     * @return
     */
    public String getCommentaire() {
        return this.commentaire;
    }

    /**
     * Valorise l'identifiant origine de la table
     *
     * @param id_origine
     */
    public void setIdOrigine(int id_origine) {
        this.id_origine = id_origine;
    }

    /**
     * Fournit l'identifiant origine de la table
     *
     * @return
     */
    public int getIdOrigine() {
        return this.id_origine;
    }

    /**
     * Fournit l'identifiant (clé primaire) de la table
     *
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Methode d'affichage des attributs privés.
     */
    public void print() {
        System.out.println();
        System.out.println(String.format("Table %s, enregistrement >%d<", TABLE_NAME, getId()));
        System.out.println(String.format("  Nom [nom] (chaine) : >%s<", getNom()));
        System.out.println(String.format("  Prenom [prenom] : >%s<", getPrenom()));
        System.out.println(String.format("  Adresse 1  [adresse1] (chaine) : >%s<", getAdresse1()));
        System.out.println(String.format("  Adresse 2  [adresse2] : >%s<", getAdresse2()));
        System.out.println(String.format("  Lieu dit [lieu_dit] : >%s<", getLieuDit()));
        System.out.println(String.format("  Code postal [code_postal] : >%s<", getCodePostal()));
        System.out.println(String.format("  Bureau [bureau] : >%s<", getBureau()));
        System.out.println(String.format("  Telephone [telephone] : >%s<", getTelephone()));
        System.out.println(String.format("  Courriel [courriel] : >%s<", getCourriel()));
        System.out.println(String.format("  Commentaire [commentaire] : >%s<", getCommentaire()));
        System.out.println(String.format("  Id Origine [id_origine] : >%d<", getIdOrigine()));
    }

    /**
     * Méthode de test de la classe
     *
     * @param args non utilisé
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Client");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/tmp/database.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Client.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        String nom = "giraudeau";
        String prenom = "Pierre";
        String adresse1 = "1, oaaokofa";
        String adresse2 = "n/a";
        String lieu_dit = "Bressuire";
        String code_postal = "793001";
        String bureau = "Entité";
        String telephone = "10122548455";
        String courriel = "Test88zzfz@fafa";
        String commentaire = "testComm";
        int id_origine = 15;

        M_Client client01 = new M_Client(db, nom, prenom, adresse1, adresse2, lieu_dit, code_postal, bureau, telephone, courriel, commentaire, id_origine);
        client01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = client01.getId() + 2;
        String nom02 = "giraudeau";
        String prenom02 = "Pierre";
        String adresse102 = "1, oaaokofa";
        String adresse202 = "n/a";
        String lieu_dit02 = "Bressuire";
        String code_postal02 = "793001";
        String bureau02 = "Entité";
        String telephone02 = "10122548455";
        String courriel02 = "Test88zzfz@fafa";
        String commentaire02 = "testComm";
        int id_origine02 = 15;
        M_Client client02 = new M_Client(db, id02, nom02, prenom02, adresse102, adresse202, lieu_dit02, code_postal02, bureau02, telephone02, courriel02, commentaire02, id_origine02, true);
        client02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        String nom03 = "giraudeau";
        String prenom03 = "Pierre";
        String adresse103 = "1, oaaokofa";
        String adresse203 = "n/a";
        String lieu_dit03 = "Bressuire";
        String code_postal03 = "793001";
        String bureau03 = "Entité";
        String telephone03 = "10122548455";
        String courriel03 = "Test88zzfz@fafa";
        String commentaire03 = "testComm";
        int id_origine03 = 15;
        M_Client client03 = new M_Client(db, id03, nom03, prenom03, adresse103, adresse203, lieu_dit03, code_postal03, bureau03, telephone03, courriel03, commentaire03, id_origine03, false);
        client03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Client client04 = new M_Client(db, client02.getId());
        client04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id < %d] : %d", client02.getId() / 2, M_Client.count(db, String.format("id < %d", client02.getId() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Client.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Client.exists(db, 9999)));

        int id01 = client01.getId();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", id01, M_Client.exists(db, id01)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        client01.delete();
        client01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Client.exists(db, id01)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Client.count(db)));

        CopyOnWriteArrayList<M_Client> liste01 = M_Client.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Client unClient : liste01) {
            unClient.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Client> liste02 = M_Client.records(db, String.format("id < %d", client02.getId() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", client02.getId() / 2, liste02.size()));
        for (M_Client unClient : liste02) {
            unClient.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        client04.print();
        client04.setNom("NOUVEAU NOM AVEC DES maj");
        System.out.println("\nContenu de l'objet avant UPDATE");
        client04.print();
        client04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        client04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Client client05 = new M_Client(db, client04.getId());
        client05.print();
        System.out.println("\nFIN de la méthode update()");
    }
}
