package defiletalents;

import java.sql.ResultSet;
import java.sql.SQLException;

import db_sqlite.Db_sqlite;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe permettant de gérer la table tal_ligne
 *
 * @author giraudeaup
 *
 */
public class M_Ligne {

    /**
     * Nom de la table.
     */
    private static final String TABLE_NAME = "tal_ligne";
    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "create table if not exists tal_ligne (id_achat integer not null, id integer not null, imprime boolean, id_place integer, id_categorie integer, constraint pk_tal_ligne primary key (id_achat,id));";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_ligne (id_achat, id, imprime, id_place, id_categorie) " + "VALUES(%d, %d, %d, %d, %d)";
    /**
     * Requête de selection de l'ID max correspondant à l'id_achat spécifié.
     */
    private static final String SQL_SELECT_MAX_ID = "SELECT max(id) AS maxId FROM tal_ligne WHERE id_achat = %d";
    /**
     * Requête de selection de l'id dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_ligne WHERE id=%d AND id_achat = %d";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_ligne WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) AS nb FROM tal_ligne WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_ligne SET imprime=%d, id_place=%d, id_categorie=%d WHERE id_achat=%d AND id=%d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_ligne WHERE id_achat=%d AND id=%d";

    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;

    /**
     * Clé primaire de la table. Correspond à la clé primaire de la table Achat.
     */
    private int id_achat;
    /**
     * Clé primaire de la table. Correspond à un Auto-Increment manuel.
     */
    private int id;
    /**
     * Imprimé ?.
     */
    private boolean imprime;
    /**
     * Correspond l'identifiant de la place.
     */
    private int id_place;
    /**
     * Correspond à l'identifiant de la catégorie de prix.
     */
    private int id_categorie;

    /**
     * Valorise les attributs privés. Ne fait aucune requête SQL
     *
     * @param db la base de données SQLite
     * @param id_achat l'id achat (clé primaire double)
     * @param id l'id de la table (clé primaire double)
     * @param imprime
     * @param id_place
     * @param id_categorie
     */
    public M_Ligne(Db_sqlite db, int id_achat, int id, boolean imprime, int id_place, int id_categorie) {

        setDb(db);
        this.id = id;
        this.id_achat = id_achat;
        setImprime(imprime);
        setIdPlace(id_place);
        setIdCategorie(id_categorie);
    }

    /**
     *
     * Valorise les attributs privés. Execute une requête SQL type INSERT avec
     * identifiant automatique.
     *
     * @param db la base de données SQLite
     * @param id_achat l'id achat (clé primaire double)
     * @param imprime
     * @param id_place
     * @param id_categorie
     */
    public M_Ligne(Db_sqlite db, int id_achat, boolean imprime, int id_place, int id_categorie) {
        ResultSet res;
        int selectedId = 1;
        setDb(db);
        this.id_achat = id_achat;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_MAX_ID, getIdAchat()));

            selectedId = (res.getInt("maxId") + 1);
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }

        this.id = selectedId;

        setImprime(imprime);
        setIdPlace(id_place);
        setIdCategorie(id_categorie);

        try {
            db.sqlExec(String.format(SQL_INSERT_ID, getIdAchat(), getId(), getImprimeAsInt(), getIdPlace(), getIdCategorie()));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Valorise les attributs privés. Execute une requêt eSQL type INSERT si le
     * paramètre 'inserer' est VRAI.
     *
     * @param db
     * @param id_achat
     * @param id
     * @param imprime
     * @param id_place
     * @param id_categorie
     * @param inserer VRAI: l'enregistrement est ajouté dans la base de données
     */
    public M_Ligne(Db_sqlite db, int id_achat, int id, boolean imprime, int id_place, int id_categorie, boolean inserer) {
        this(db, id_achat, id, imprime, id_place, id_categorie);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERT_ID, getIdAchat(), getId(), getImprimeAsInt(), getIdPlace(), getIdCategorie()));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Valorise les attributs privés. Execute une requête SQL type SELECT WHERE
     *
     * @param db la base de données SQLite
     * @param id_achat (clé primaire)
     * @param id (clé primaire)
     */
    public M_Ligne(Db_sqlite db, int id_achat, int id) {
        ResultSet res;
        setDb(db);
        this.id = id;
        this.id_achat = id_achat;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, getId(), getIdAchat()));
            if (res.next()) {
                setImprime(res.getBoolean("imprime"));
                setIdPlace(res.getInt("id_place"));
                setIdCategorie(res.getInt("id_categorie"));
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
            db.sqlExec(String.format(SQL_UPDATE, getImprimeAsInt(), getIdPlace(), getIdCategorie(), getIdAchat(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Supprime le champs de la table. Execute une requête type DELETE FROM
     */
    public void delete() {
        try {
            db.sqlExec(String.format(SQL_DELETE, getIdAchat(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }

    }

    /**
     * Fournit le contenu de la table M_Ligne
     *
     * @param db la base de donnée SQLite
     * @return retourne une collection de M_Ligne contenant la table complète
     * dans la base de donnée
     */
    public static CopyOnWriteArrayList<M_Ligne> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Fournit le contenu de la table M_Ligne
     *
     * @param db la base de donnée SQLite
     * @param where la condition de selection
     * @return retourne une collection de M_Ligne contenant la table complète
     * respectant la condition de selection
     */
    public static CopyOnWriteArrayList<M_Ligne> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Ligne> lignes = new CopyOnWriteArrayList();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                lignes.add(new M_Ligne(db, res.getInt("id_achat"), res.getInt("id"), res.getBoolean("imprime"), res.getInt("id_place"), res.getInt("id_categorie")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return lignes;
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_Ligne
     *
     * @param db la base de donnée SQLite
     * @return retourne le nombre d'élément dans la table
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Fournit le nombre d'enregistrements dans la table M_Ligne
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
     * Verifie que l'enregistrement existe dans la table M_Ligne
     *
     * @param db la base de donnée SQLite
     * @param id l'id à vérifier
     * @return retourne vrai si l'id existe dans la table, faux si non
     */
    public static boolean exists(Db_sqlite db, int id, int id_achat) {
        return (count(db, "id =" + id + " AND id_achat=" + id_achat) == 1);
    }

    /**
     *
     * @return la requête de création de la table
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Valorise l'attribut privé
     *
     * @param db la base de données
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Fournit la base de données SQLite
     *
     * @return la base de données SQLite
     */
    public Db_sqlite getDb() {
        return this.db;
    }

    /**
     * Valorise l'attribut privé
     *
     * @param imprime le boolean imprime
     */
    public void setImprime(boolean imprime) {
        this.imprime = imprime;
    }

    /**
     * Fournit imprime au format boolean
     *
     * @return imprime
     */
    public boolean getImprime() {
        return this.imprime;
    }

    /**
     * Forunit imprime au format entier
     *
     * @return imprime
     */
    private int getImprimeAsInt() {
        return (getImprime() ? 1 : 0);
    }

    /**
     * Valorise l'attribut privé
     *
     * @param id_place
     */
    public void setIdPlace(int id_place) {
        this.id_place = (id_place < 0 ? 0 : id_place);
    }

    /**
     * Fournit l'identifiant de la place
     *
     * @return l'identifiant de la place
     */
    public int getIdPlace() {
        return this.id_place;
    }

    /**
     * Valorise l'identifiant de la catégorie
     *
     * @param id_categorie
     */
    public void setIdCategorie(int id_categorie) {
        this.id_categorie = (id_categorie < 0 ? 0 : id_categorie);
    }

    /**
     * Fournit l'identifiant de la catégorie
     *
     * @return l'identifiant
     */
    public int getIdCategorie() {
        return this.id_categorie;
    }

    /**
     * Fournit l'identifiant (clé primaire) de la table
     *
     * @return la clé primaire
     */
    public int getId() {
        return this.id;
    }

    /**
     * Fournit l'identifiant achat (clé primaire) de la table
     *
     * @return la clé primaire
     */
    public int getIdAchat() {
        return this.id_achat;
    }

    /**
     * Console d'affichage des attributs privés
     */
    public void print() {
        System.out.println();
        System.out.println(String.format("Table %s", TABLE_NAME));
        System.out.println(String.format("  id achat (clé primaire #1) [id_achat] (Integer) : >%s<", getIdAchat()));
        System.out.println(String.format("  id [id] (clé primaire) [id] : >%s<", getId()));
        System.out.println(String.format("  Imprime [imprime] (boolean) : >%b<", getImprime()));
        System.out.println(String.format("  id place [id_place] (Integer) : >%d<", getIdPlace()));
        System.out.println(String.format("  id categorie [id_categorie] (Integer) : >%d<", getIdCategorie()));
    }

    /**
     * Méthode de test de la classe
     *
     * @param args non utilisé
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nTest de la classe M_Ligne");
        System.out.println("=========================\n");

        System.out.print("création d'un objet base de données...");
        Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/tmp/database.sqlite");
        System.out.println(" OK.");

        System.out.print("création de la table si elle n'existe pas déjà...");
        db.sqlExec(M_Ligne.SQL_CREATE);
        System.out.println(" OK.");

        System.out.println("\nConstructeur avec insertion et numéro automatique...");
        int id_achat01 = 5;
        boolean imprime01 = true;
        int id_place01 = 1;
        int id_categorie01 = 6;
        M_Ligne ligne01 = new M_Ligne(db, id_achat01, imprime01, id_place01, id_categorie01);
        ligne01.print();

        System.out.println("\nConstructeur avec insertion SANS numéro automatique...");
        int id02 = ligne01.getId() + 2;
        int id_achat02 = ligne01.getIdAchat() + 2;
        boolean imprime02 = false;
        int id_place02 = 45;
        int id_categorie02 = 87;

        M_Ligne ligne02 = new M_Ligne(db, id_achat02, id02, imprime02, id_place02, id_categorie02, true);
        ligne02.print();

        System.out.println("\nConstructeur SANS insertion SANS numéro automatique...");
        int id03 = 9999;
        int id_achat03 = 4555;
        boolean imprime03 = false;
        int id_place03 = 45;
        int id_categorie03 = 87;
        M_Ligne ligne03 = new M_Ligne(db, id_achat03, id03, imprime03, id_place03, id_categorie03, false);
        ligne03.print();

        System.out.println("\nConstructeur avec SELECT de l'enreg sans numéro automatique...");
        M_Ligne ligne04 = new M_Ligne(db, ligne02.getIdAchat(), ligne02.getId());
        ligne04.print();

        System.out.println(String.format("\nMéthode count() [WHERE id_achat < %d] : %d", ligne02.getIdAchat() / 2, M_Ligne.count(db, String.format("id_achat < %d", ligne02.getIdAchat() / 2))));
        System.out.println(String.format("Méthode count() [all] : %d", M_Ligne.count(db)));

        System.out.println(String.format("\nMéthode exists() [id= 9999] : %b", M_Ligne.exists(db, 1, 5)));

        int id01 = ligne01.getId();
        int id_achat00 = ligne01.getIdAchat();
        System.out.println(String.format("\nMéthode exists() [id= %d] : %b", id01, M_Ligne.exists(db, id01, id_achat00)));
        System.out.print(String.format("Méthode delete() [id= %d]...", id01));
        ligne01.delete();
        ligne01 = null;
        System.out.println(" OK.");
        System.out.println(String.format("Méthode exist() [id= %d] : %b", id01, M_Ligne.exists(db, id01, id_achat00)));
        System.out.println(String.format("Méthode count() [all] : %d", M_Ligne.count(db)));

        CopyOnWriteArrayList<M_Ligne> liste01 = M_Ligne.records(db);
        System.out.println(String.format("\n Liste de tous les enregistrements (%d)", liste01.size()));
        for (M_Ligne uneLigne : liste01) {
            uneLigne.print();
        }
        System.out.println("FIN de la liste des enregistrements");

        CopyOnWriteArrayList<M_Ligne> liste02 = M_Ligne.records(db, String.format("id < %d", ligne02.getId() / 2));
        System.out.println(String.format("\n Liste des enregistrements WHERE id < %d (%d)", ligne02.getId() / 2, liste02.size()));
        for (M_Ligne uneLigne : liste02) {
            uneLigne.print();
        }

        System.out.println("FIN de la liste des enregistrements");

        System.out.println("\nMéthode update()...");
        System.out.println("\nContenu de l'objet avant modification");
        ligne04.print();
        ligne04.setIdCategorie(5);
        ligne04.setIdPlace(6);
        ligne04.setImprime(false);
        System.out.println("\nContenu de l'objet avant UPDATE");
        ligne04.print();
        ligne04.update();
        System.out.println("\nContenu de l'objet après UPDATE");
        ligne04.print();
        System.out.println("\nVérification de l'enregistrement mis à jour");
        M_Ligne ligne05 = new M_Ligne(db, ligne04.getIdAchat(), ligne04.getId());
        ligne05.print();
        System.out.println("\nFIN de la méthode update()");
    }
}
