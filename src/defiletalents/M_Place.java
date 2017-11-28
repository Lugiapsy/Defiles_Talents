package defiletalents;

import db_sqlite.Db_sqlite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import methodesVerif.Verify;

/**
 * Classe permettant de gérer les places dans la salle
 *
 * @author rousselotv, giraudeaup
 *
 */
public class M_Place {

    /**
     * Nom de la table.
     */
    private static final String TABLE_NAME = "tal_place";
    /**
     * Requête de création de la table.
     */
    private static final String SQL_CREATE = "create table if not exists tal_place (id integer not null, code varchar(10), disponible boolean, commentaire varchar(200), xplan integer, yplan integer, id_zone integer, id_plan integer, id_type integer, id_gauche integer, id_droite integer, constraint pk_tal_place primary key (id));";
    /**
     * Requête d'insertion complète dans la table.
     */
    private static final String SQL_INSERT_ID = "INSERT INTO tal_place (id, code, disponible, commentaire, xplan, yplan, id_zone, id_plan, id_type, id_gauche, id_droite) VALUES (%d, '%s', %d, '%s', %d, %d, %d, %d, %d, %d, %d)";
    /**
     * Requête d'insertion sans ID dans la table.
     */
    private static final String SQL_INSERT = "INSERT INTO tal_place (code, disponible, commentaire, xplan, yplan, id_zone, id_plan, id_type, id_gauche, id_droite) VALUES ('%s', %d, '%s', %d, %d, %d, %d, %d, %d,%d)";
    /**
     * Requête de selection avec ID dans la table.
     */
    private static final String SQL_SELECT_ID = "SELECT * FROM tal_place WHERE id = %d;";
    /**
     * Requête de selection avec condition dans la table.
     */
    private static final String SQL_SELECT_WHERE = "SELECT * FROM tal_place WHERE %s";
    /**
     * Requête de selection count(*) dans la table.
     */
    private static final String SQL_SELECT_COUNT = "SELECT COUNT(*) as nb FROM tal_place WHERE %s";
    /**
     * Requête de mise à jour de la table.
     */
    private static final String SQL_UPDATE = "UPDATE tal_place SET code = '%s', disponible = %d, commentaire = '%s', xplan = %d, yplan = %d, id_zone = %d, id_plan = %d, id_type = %d, id_gauche = %d, id_droite = %d WHERE id = %d";
    /**
     * Requête de suppression de l'enregistrement dans la table.
     */
    private static final String SQL_DELETE = "DELETE FROM tal_place WHERE id = %d";

    /**
     * Base de données SQLite.
     */
    private Db_sqlite db;
    /**
     * Identifiant de l'enregistrement.
     */
    private int id;
    /**
     * Code attribué à cette place.
     */
    private String code;
    /**
     * Disponibilité de cette place.
     */
    private boolean disponible;
    /**
     * Un commentaire pour cette place.
     */
    private String commentaire;
    /**
     * La position x de la place dans la salle.
     */
    private int xplan;
    /**
     * La position y de la place dans la salle.
     */
    private int yplan;
    /**
     * L'identifiant de la zone attachée à cette place.
     */
    private int id_zone;
    /**
     * L'identifiant du plan attaché à cette place.
     */
    private int id_plan;
    /**
     * L'identifiant du type de place attaché à cette place.
     */
    private int id_type;
    
    /**
     * Identifiant de la place sur la gauche.
     */
    private int id_gauche;
    /**
     * Identifiant de la place sur le droite.
     */
    private int id_droite;

    /**
     * Constructeur pour un nouvel enregistrement dans la table et auquel on
     * fournit toutes les valeurs. N'execute pas de requête SQL.
     *
     * @param db
     * @param id
     * @param code
     * @param disponible
     * @param commentaire
     * @param xplan
     * @param yplan
     * @param id_zone
     * @param id_plan
     * @param id_type
     * @param id_gauche
     * @param id_droite
     */
    public M_Place(Db_sqlite db, int id, String code, boolean disponible, String commentaire, int xplan, int yplan, int id_zone, int id_plan, int id_type, int id_gauche, int id_droite) {
        setDb(db);
        this.id = id;
        setCode(code);
        setDisponible(disponible);
        setCommentaire(commentaire);
        setXplan(xplan);
        setYplan(yplan);
        setId_Zone(id_zone);
        setId_Plan(id_plan);
        setId_Type(id_type);
        setId_gauche(id_gauche);
        setId_droite(id_droite);
    }

    /**
     * Constructeur avec accès à la base de données avec une requête INSERT puis
     * un accès à la base de données pour récupérer la valeur de l'identifiant.
     *
     * @param db
     * @param code
     * @param disponible
     * @param commentaire
     * @param xplan
     * @param yplan
     * @param id_zone
     * @param id_plan
     * @param id_type
     * @param id_gauche
     * @param id_droite
     */
    public M_Place(Db_sqlite db, String code, boolean disponible, String commentaire, int xplan, int yplan, int id_zone, int id_plan, int id_type, int id_gauche, int id_droite) {
        ResultSet res;
        setDb(db);
        setCode(code);
        setDisponible(disponible);
        setCommentaire(commentaire);
        setXplan(xplan);
        setYplan(yplan);
        setId_Zone(id_zone);
        setId_Plan(id_plan);
        setId_Type(id_type);
        setId_gauche(id_gauche);
        setId_droite(id_droite);

        try {
            db.sqlExec(String.format(SQL_INSERT, getCode(), getDisponbleAsInt(), getCommentaire(), getXplan(), getYplan(), getId_Zone(), getId_Plan(), getId_Type(), getId_gauche(), getId_droite()));
            res = db.sqlLastId(TABLE_NAME);
            if (res.next()) {
                this.id = res.getInt("id");
            }
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
     * @param db
     * @param id
     * @param code
     * @param disponible
     * @param commentaire
     * @param xplan
     * @param yplan
     * @param id_zone
     * @param id_plan
     * @param id_type
     * @param id_gauche
     * @param id_droite
     * @param inserer
     */
    public M_Place(Db_sqlite db, int id, String code, boolean disponible, String commentaire, int xplan, int yplan, int id_zone, int id_plan, int id_type, int id_gauche, int id_droite, boolean inserer) {
        this(db, id, code, disponible, commentaire, xplan, yplan, id_zone, id_plan, id_type, id_gauche, id_droite);

        if (inserer) {
            try {
                db.sqlExec(String.format(SQL_INSERT_ID, getId(), getCode(), getDisponbleAsInt(), getCommentaire(), getXplan(), getYplan(), getId_Zone(), getId_Plan(), getId_Type(), getId_gauche(), getId_droite()));
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
    public M_Place(Db_sqlite db, int id) {
        ResultSet res;
        setDb(db);
        this.id = id;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_ID, getId()));
            if (res.next()) {
                setCode(res.getString("code"));
                setDisponible(res.getBoolean("disponible"));
                setCommentaire(res.getString("commentaire"));
                setXplan(res.getInt("xplan"));
                setYplan(res.getInt("yplan"));
                setId_Zone(res.getInt("id_zone"));
                setId_Plan(res.getInt("id_plan"));
                setId_Type(res.getInt("id_type"));
                setId_gauche(res.getInt("id_gauche"));
                setId_droite(res.getInt("id_droite"));
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Procédure de mise à jour de la table.
     *
     */
    public void update() {
        try {
            db.sqlExec(String.format(SQL_UPDATE, getCode(), getDisponbleAsInt(), getCommentaire(), getXplan(), getYplan(), getId_Zone(), getId_Plan(), getId_Type(), getId_gauche(), getId_droite(), getId()));
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Procédure de suppression de l'enregistrement.
     *
     * @throws SQLException Si l'intégrité référencielle n'est pas respectée.
     */
    public void delete() throws SQLException {
        if (M_Ligne.count(db, "id_place = " + getId()) != 0) {
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
     * Méthode de classe pour récupérer l'ensemble des enregistrements
     *
     * @param db base de données sqlite
     * @return collection de toutes les catégories
     */
    public static CopyOnWriteArrayList<M_Place> records(Db_sqlite db) {
        return records(db, "1=1");
    }

    /**
     * Méthode de classe pour récupérer un sous-ensemble des enregistrements
     *
     * @param db base de données sqlite
     * @param where clause SQL pour filtrer les categories
     * @return collection de catégories selon la clause SQL
     */
    public static CopyOnWriteArrayList<M_Place> records(Db_sqlite db, String where) {
        CopyOnWriteArrayList<M_Place> places = new CopyOnWriteArrayList();
        ResultSet res;

        try {
            res = db.sqlSelect(String.format(SQL_SELECT_WHERE, where));
            while (res.next()) {
                places.add(new M_Place(db, res.getInt("id"), res.getString("code"), res.getBoolean("disponible"), res.getString("commentaire"), res.getInt("xplan"), res.getInt("yplan"), res.getInt("id_zone"), res.getInt("id_plan"), res.getInt("id_type"), res.getInt("id_gauche"), res.getInt("id_droite")));
            }
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return places;
    }

    /**
     * Méthode de classe pour récupérer le nombre d'enregistrements
     *
     * @param db base de données sqlite
     * @return nombre de catégories au total
     */
    public static int count(Db_sqlite db) {
        return count(db, "1=1");
    }

    /**
     * Méthode de classe pour récupérer un sous-ensemble d'enregistrements
     *
     * @param db base de données sqlite
     * @param where clause SQL pour filtrer les catégories
     * @return nombres de catégories selon la clause
     */
    public static int count(Db_sqlite db, String where) {
        int nb = 0;
        ResultSet res;
        try {
            res = db.sqlSelect(String.format(SQL_SELECT_COUNT, where));
            if (res.next()) nb = res.getInt("nb");

        }
        catch (SQLException e) {
            LogHandler.log().store(e);
        }
        return nb;
    }

    /**
     * Méthode de classe qui permet de savoir, en fonction de son identifiant,
     * si un enregistrement existe ou non dans la table
     *
     * @param db base de données sqlite
     * @param id identifiant de la base de données
     * @return si l'enregistrement est trouvé ou non
     */
    public static boolean exists(Db_sqlite db, int id) {
        return (count(db, "id = " + id) == 1);
    }

    /**
     * Valorise l'obje base de données utilisé par la classe.
     *
     * @param db base de données SQLite
     */
    public void setDb(Db_sqlite db) {
        this.db = db;
    }

    /**
     * Retourne l'objet base de données SQLite.
     *
     * @return
     */
    public Db_sqlite getDb() {
        return db;
    }

    /**
     * Valorise le code de l'enregistrement.
     *
     * @param code
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
     * Valorise la disponibilité de l'enregistrement.
     *
     * @param disponible
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Retourne la disponibilité de l'enregistrement.
     *
     * @return
     */
    public boolean getDisponible() {
        return this.disponible;
    }

    /**
     * Retourne la disponibilité de l'enregistrement au format ENTIER
     *
     * @return 1 pour vrai, 0 pour faux
     */
    public int getDisponbleAsInt() {
        return (getDisponible() ? 1 : 0);
    }

    /**
     * Valorise le commentaire de l'enregistrement.
     *
     * @param commentaire
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = Verify.toString(commentaire, 200);
    }

    /**
     * Retourne le commentaire de l'enregistrement.
     *
     * @return
     */
    public String getCommentaire() {
        return this.commentaire;
    }

    /**
     * Valorise la position x de l'enregistrement
     *
     * @param xplan
     */
    public void setXplan(int xplan) {
        this.xplan = xplan;
    }

    /**
     * Retourne la position x de l'enregistrement.
     *
     * @return
     */
    public int getXplan() {
        return this.xplan;
    }

    /**
     * Valorise la position y de l'enregistrement.
     *
     * @param yplan
     */
    public void setYplan(int yplan) {
        this.yplan = yplan;
    }

    /**
     * Retourne la position y de l'enregistrement.
     *
     * @return
     */
    public int getYplan() {
        return this.yplan;
    }

    /**
     * Valorise l'identifiant de la zone correspondant à l'enregistrement.
     *
     * @param id_zone
     */
    public void setId_Zone(int id_zone) {
        this.id_zone = id_zone;
    }

    /**
     * Retourne l'identifiant de la zone correspondant à l'enregistrement.
     *
     * @return
     */
    public int getId_Zone() {
        return this.id_zone;
    }

    /**
     * Valorise l'idenfiant du plan correspondant à l'enregistrement.
     *
     * @param id_plan
     */
    public void setId_Plan(int id_plan) {
        this.id_plan = id_plan;
    }

    /**
     * Retourne l'idenfiant du plan correspondant à l'enregistrement.
     *
     * @return
     */
    public int getId_Plan() {
        return this.id_plan;
    }

    /**
     * Valorise l'identifiant du type de place correspondant à l'enregistrement.
     *
     * @param id_type
     */
    public void setId_Type(int id_type) {
        this.id_type = id_type;
    }

    /**
     * Retourne l'identifiant du type de place correspondant à l'enregistrement.
     *
     * @return
     */
    public int getId_Type() {
        return this.id_type;
    }

    /**
     * Valorise l'identifiant de la place sur la gauche.
     * @param id_gauche 
     */
    public void setId_gauche(int id_gauche) {
        this.id_gauche = id_gauche;
    }

    /**
     * Retourne l'identifiant de la place sur la gauche.
     * @return 
     */
    public int getId_gauche() {
        return id_gauche;
    }

    /**
     * Valorise l'identifiant de la place sur la droite.
     * @param id_droite 
     */
    public void setId_droite(int id_droite) {
        this.id_droite = id_droite;
    }

    /**
     * Retourne l'identifiant de la palce sur la droite.
     * @return 
     */
    public int getId_droite() {
        return id_droite;
    }    

    /**
     * Retourne l'identifiant de l'enregistrement.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne la requête de création de la table.
     *
     * @return la requête de création de la table au format SQL
     */
    public static String getCreateSqlStatement() {
        return SQL_CREATE;
    }

    /**
     * Affiche à la console les données de l'objet.
     */
    public void print() {
        System.out.println();
        System.out.println(String.format("Table %s, enregistrement numéro %d", TABLE_NAME, getId()));
        System.out.println(String.format("Code: >%s<", getCode()));
        System.out.println(String.format("Disponible: >%b<", getDisponible()));
        System.out.println(String.format("Commentaire: >%s<", getCommentaire()));
        System.out.println(String.format("Xplan: >%d<", getXplan()));
        System.out.println(String.format("Yplan: >%d<", getYplan()));
        System.out.println(String.format("Id_zone >%d<", getId_Zone()));
        System.out.println(String.format("Id_Plan >%d<", getId_Plan()));
        System.out.println(String.format("Id_Type >%d<", getId_Type()));
        System.out.println(String.format("Id_Gauche >%d<", getId_gauche()));
        System.out.println(String.format("Id_Droite >%d<", getId_droite()));
    }

    /**
     * Méthode de test de classe
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        /*Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/tmp/database.sqlite");
        db.sqlExec(M_Place.getCreateSqlStatement());
        CopyOnWriteArrayList<M_Place> places = M_Place.records(db);
        places.add(new M_Place(db, "CODE", true, "Commentaire", 5, 2, 1, 2, 3));
        for (M_Place p : places) {
            p.print();
        }
        */
    }
}
