package defiletalents;

import db_sqlite.Db_sqlite;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFrame;
import methodesVerif.Verify;
import org.joda.time.DateTime;

/**
 * Lanceur de l'application DefileTalents
 *
 * @author giraudeaup
 */
public final class Controller {

    /**
     * Le fichier de config Xml géré par la classe XmlHandler.
     */
    private XmlHandler configFile = null;
    /**
     * La base de données SQLite.
     */
    private Db_sqlite db;

    // -- Collections correspondantes aux tables de la base de données -- //
    private CopyOnWriteArrayList<M_Achat> achats;
    private CopyOnWriteArrayList<M_Categorie_Prix> categoriesPrix;
    private CopyOnWriteArrayList<M_Classe_Prix> classesPrix;
    private CopyOnWriteArrayList<M_Client> clients;
    private CopyOnWriteArrayList<M_Ligne> lignes;
    private CopyOnWriteArrayList<M_Mode_Paiement> modesPaiement;
    private CopyOnWriteArrayList<M_Origine> origines;
    private CopyOnWriteArrayList<M_Paiement> paiements;
    private CopyOnWriteArrayList<M_Plan> plans;
    private CopyOnWriteArrayList<M_Place> places;
    private CopyOnWriteArrayList<M_Prix> lesPrix;
    private CopyOnWriteArrayList<M_Type_Place> typesPlace;
    private CopyOnWriteArrayList<M_Zone> zones;

    // -- Vue principale -- //
    private V_Main mainFrame;

    // -- Vues diverses -- //
    private V_ErrMessage errMessageFrame;
    private V_DeleteConfirm deleteConfirmFrame;
    private V_Recherche rechercheFrame;
    private V_Calendrier calendarFrame;
    private V_About aboutFrame;
    private V_Plans plansFrame;
    private V_InitDb initFrame;

    // -- Vues relatives aux paramètres -- //
    private V_Origine_All originesFrame;
    private V_Origine_Edit originesEditFrame;
    private V_ModePaiement_All modesPaiementFrame;
    private V_ModePaiement_Edit modesPaiementEditFrame;
    private V_CategoriePrix_All categoriesPrixFrame;
    private V_CategoriePrix_Edit categoriesPrixEditFrame;
    private V_ClassePrix_All classesPrixFrame;
    private V_ClassePrix_Edit classesPrixEditFrame;
    private V_Prix_All prixFrame;
    private V_Prix_Edit prixEditFrame;
    private V_TypePlace_All typesPlaceFrame;
    private V_TypePlace_Edit typesPlaceEditFrame;
    private V_Zone_All zonesFrame;
    private V_Zone_Edit zonesEditFrame;
    private V_Plan_All planFrame;
    private V_Plan_Edit planEditFrame;
    private V_Place_All placesFrame;
    private V_Place_Edit placesEditFrame;

    // -- Fenêtres relatives à la vente -- //
    private V_Client_All clientsFrame;
    private V_Client_Edit clientsEditFrame;
    private V_Client_Select clientsSelectFrame;
    private V_Achat_All achatsFrame;
    private V_Achat_Edit achatsEditFrame;
    private V_Ligne_All ligneFrame;
    private V_Ligne_Edit ligneEditFrame;
    private V_Paiement_All paiementFrame;
    private V_Paiement_Edit paiementEditFrame;

    /**
     * Constructeur et lanceur du projet DefileTalents.
     */
    private Controller() {
        long startTime = System.nanoTime();
        initFiles();
        initDb();
        initData();
        initFrames();
        System.out.printf("Chargé en %f secondes.\n", (System.nanoTime() - startTime) / 1e9);
    }

    /**
     * Initialise le fichier de config (config.xml) et charge la base de
     * données. Dans le cas d'une première exécution, crée les fichiers
     * necessaires.
     */
    private void initFiles() {
        configFile = new XmlHandler(Globales.APP_CONFIG);
        if (!getConfigFile().elementExists("db_path")) {
            getConfigFile().addElement("db_path");
            getConfigFile().setValue("db_path", "null");
        }
    }

    /**
     * Initialise les collections avec les enregistrements de la base de
     * données.
     */
    private void initData() {
        achats = M_Achat.records(db);
        categoriesPrix = M_Categorie_Prix.records(db);
        classesPrix = M_Classe_Prix.records(db);
        clients = M_Client.records(db);
        lignes = M_Ligne.records(db);
        modesPaiement = M_Mode_Paiement.records(db);
        origines = M_Origine.records(db);
        paiements = M_Paiement.records(db);
        places = M_Place.records(db);
        plans = M_Plan.records(db);
        lesPrix = M_Prix.records(db);
        typesPlace = M_Type_Place.records(db);
        zones = M_Zone.records(db);
    }

    /**
     * Initialise les vues et rend visible la vue principale.
     */
    private void initFrames() {
        mainFrame = new V_Main(this);

        errMessageFrame = new V_ErrMessage();
        rechercheFrame = new V_Recherche(this);
        deleteConfirmFrame = new V_DeleteConfirm(this);
        calendarFrame = new V_Calendrier();
        aboutFrame = new V_About(this);
        plansFrame = new V_Plans(this);

        categoriesPrixFrame = new V_CategoriePrix_All(this);
        categoriesPrixEditFrame = new V_CategoriePrix_Edit(this);
        classesPrixFrame = new V_ClassePrix_All(this);
        classesPrixEditFrame = new V_ClassePrix_Edit(this);
        modesPaiementFrame = new V_ModePaiement_All(this);
        modesPaiementEditFrame = new V_ModePaiement_Edit(this);
        originesFrame = new V_Origine_All(this);
        originesEditFrame = new V_Origine_Edit(this);
        planFrame = new V_Plan_All(this);
        planEditFrame = new V_Plan_Edit(this);
        prixFrame = new V_Prix_All(this);
        prixEditFrame = new V_Prix_Edit(this);
        typesPlaceFrame = new V_TypePlace_All(this);
        typesPlaceEditFrame = new V_TypePlace_Edit(this);
        zonesFrame = new V_Zone_All(this);
        zonesEditFrame = new V_Zone_Edit(this);
        placesFrame = new V_Place_All(this);
        placesEditFrame = new V_Place_Edit(this);

        clientsFrame = new V_Client_All(this);
        clientsEditFrame = new V_Client_Edit(this);
        clientsSelectFrame = new V_Client_Select(this);
        achatsFrame = new V_Achat_All(this);
        achatsEditFrame = new V_Achat_Edit(this);
        ligneFrame = new V_Ligne_All(this);
        ligneEditFrame = new V_Ligne_Edit(this);
        paiementFrame = new V_Paiement_All(this);
        paiementEditFrame = new V_Paiement_Edit(this);

        setMainStats();
        mainFrame.setVisible(true);
    }

    /**
     * Initialise la base de données utilisée par l'application.
     */
    private void initDb() {
        File database = new File(getConfigFile().getValue("db_path"));
        try {
            if (!Verify.isValidDb(database)) {
                initFrame = new V_InitDb(this);
                initFrame.setVisible(true);
            }
            else {
                db = new Db_sqlite(database.getAbsolutePath());
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Initialise la création des tables dans la base de données.
     */
    private void initTables() {
        try {
            db.sqlExec(M_Achat.getCreateSqlStatement());
            db.sqlExec(M_Categorie_Prix.getCreateSqlStatement());
            db.sqlExec(M_Classe_Prix.getCreateSqlStatement());
            db.sqlExec(M_Client.getCreateSqlStatement());
            db.sqlExec(M_Ligne.getCreateSqlStatement());
            db.sqlExec(M_Mode_Paiement.getCreateSqlStatement());
            db.sqlExec(M_Origine.getCreateSqlStatement());
            db.sqlExec(M_Paiement.getCreateSqlStatement());
            db.sqlExec(M_Place.getCreateSqlStatement());
            db.sqlExec(M_Plan.getCreateSqlStatement());
            db.sqlExec(M_Prix.getCreateSqlStatement());
            db.sqlExec(M_Type_Place.getCreateSqlStatement());
            db.sqlExec(M_Zone.getCreateSqlStatement());
        }
        catch (SQLException e) {
            LogHandler.log().store(e);
            errWindow("Erreur lors de la création des tables");
        }
    }

    /**
     * Crée une nouvelle base de données SQLite à l'emplacement indiqué en
     * paramètre.
     *
     * @param filepath
     */
    public void newDb(String filepath) {
        if (!new File(filepath).exists()) {
            try {
                db = new Db_sqlite(filepath);
                configFile.setValue("db_path", filepath);
                initTables();
                initData();

            }
            catch (Exception e) {
                LogHandler.log().store(e);
                errWindow("Erreur lors de la création du fichier.");
            }
        }
        else {
            errWindow("Erreur: Le fichier existe déjà.");
        }
    }

    /**
     * Ouvre la base de données passé en paramètre
     *
     * @param file (doit être un fichier .sqlite ou .sq3)
     */
    public void openDb(File file) {
        if (Verify.isValidDb(file)) {
            try {
                db = new Db_sqlite(file.getAbsolutePath());
                getConfigFile().setValue("db_path", file.getAbsolutePath());
                initData();
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
        else {
            errWindow("Erreur: Le fichier n'est pas une base de données sqlite valide.");
        }
    }

    /**
     * Methode permettant de creer une nouvelle base de données à partir d'une
     * déjà existante.
     *
     * @param oldDb La base de données d'origne
     * @param newDb La nouvelle
     */
    public void importDb(File oldDb, File newDb) {
        if (!newDb.exists() && Verify.isValidDb(oldDb)) {
            try {
                Files.copy(oldDb.toPath(), newDb.toPath());
                openDb(newDb);
                errWindow("Base de données importée avec succès.");
            }
            catch (Exception e) {
                LogHandler.log().store(e);
                errWindow("Erreur lors de la creation du fichier.");
            }
        }
        else {
            errWindow("Erreur: L'ancienne base de données n'est pas valide ou le nouveau fichier existe déjà.");
        }
    }

    /**
     * Méthode pour importer un script SQL.
     *
     * @param file
     */
    public void importSql(File file) {
        if (!file.isAbsolute()) {
            throw new IllegalArgumentException("Filepath is not absolute!");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                db.sqlExec(line);
            }
            br.close();
            initData();
        }
        catch (IOException | SQLException e) {
            LogHandler.log().store(e);
        }

    }

    /**
     * Afficher une fenêtre d'erreur pour prévenir l'utilisateur.
     *
     * @param message le message destiné à l'utilisateur
     */
    private void errWindow(String message) {
        errMessageFrame.setMessage(message);
        errMessageFrame.setVisible(true);
    }

    /**
     * Retourne le fichier de config.xml utilisé par l'application
     *
     * @return le fichier XML
     */
    private XmlHandler getConfigFile() {
        return this.configFile;
    }

    /**
     * Procédure exécutée à la fermeture de l'application. Sauvegarde le fichier
     * XML et quitte.
     */
    public void exit() {
        getConfigFile().save();
        System.exit(0);
    }

    /**
     * Effectue une recherche sur le nom de client ou le code de la place.
     * Affiche ensuite la vue correspondante, dans le cas d'une recherche ne
     * retournant qu'un seul résultat, affiche la fenetre d'édition directement.
     *
     * @param recherche la chaine de caractères à rechercher dans la base de
     * données
     * @param b vrai pour une recherche de client, faux pour une recherche de
     * place
     */
    public void rechercher(String recherche, boolean b) {
        int resultFound = 0;
        CopyOnWriteArrayList<M_Client> resultClient = new CopyOnWriteArrayList();
        CopyOnWriteArrayList<M_Place> resultPlace = new CopyOnWriteArrayList();

        if (b) {
            for (M_Client c : getClients()) {
                if (c.getNom().toLowerCase().contains(recherche.toLowerCase())) {
                    resultClient.add(c);
                    resultFound++;
                }
            }

            switch (resultFound) {
                case 0:
                    errWindow("Aucun élément trouvé.");
                    break;
                case 1:
                    getClientEditFrame(resultClient.get(0).getId(), V_Main.MODE_CONSULT, null).setVisible(true);
                    break;
                default:
                    getClientAllFrame().rechercheMode(true, resultClient);
                    getClientAllFrame().setVisible(true);
                    break;
            }
        }
        else {
            for (M_Place p : getPlaces()) {
                if (p.getCode().toLowerCase().contains(recherche.toLowerCase())) {
                    resultPlace.add(p);
                    resultFound++;
                }
            }

            switch (resultFound) {
                case 0:
                    errWindow("Aucun élément trouvé.");
                    break;
                case 1:
                    getPlaceEditFrame(resultPlace.get(0).getId(), V_Main.MODE_CONSULT).setVisible(true);
                    break;
                default:
                    getPlaceAllFrame().rechercheMode(true, resultPlace);
                    getPlaceAllFrame().setVisible(true);
                    break;
            }
        }
    }

    // -- Toutes les fenêtres -- //
    /**
     * Retourne la vue principale
     *
     * @return
     */
    public V_Main getMainFrame() {
        return this.mainFrame;
    }

    /**
     * Retourne la vue destinée à la recherche par mot clé.
     *
     * @return
     */
    public V_Recherche getRechercheFrame() {
        return this.rechercheFrame;
    }

    /**
     * Retourne la vue permettant d'afficher un calendrier.
     *
     * @param parent
     * @return
     */
    public V_Calendrier getCalendarFrame(JFrame parent) {
        calendarFrame.setValues(parent);
        return calendarFrame;
    }

    /**
     * Retourne la vue de suppression d'un enregistrement.
     *
     * @param table la table contenant l'enregistrement à supprimer
     * @param id l'identifiant de l'enregistrement à supprimer.
     * @return
     */
    public V_DeleteConfirm getDeleteConfirmFrame(String table, String id) {
        deleteConfirmFrame.setValues(table, id);
        return this.deleteConfirmFrame;
    }

    /**
     * Retourne la vue correspondante à la fenêtre 'A propos'.
     *
     * @return
     */
    public V_About getAboutFrame() {
        return this.aboutFrame;
    }

    /**
     * Retourne la vue correspondante à l'affichage des plans.
     *
     * @return
     */
    public V_Plans getPlansFrame() {
        if (getPlans().isEmpty()) {
            errWindow("La table plan n'a pas d'image.");
            throw new IllegalArgumentException("tal_plan doesn't have any images");
        }
        int errors = 0;
        String[] paths = new String[getPlans().size()];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = getPlans().get(i).getImage();
            if (!new File(getPlans().get(i).getImage()).exists()) errors++;
        }

        if (errors != 0) {
            errWindow("Une ou plusieurs images n'ont pas pu être chargée.");
            LogHandler.log().store(new Exception("Couldn't load image(s)."));
        }
        plansFrame.setImages(paths);

        return this.plansFrame;
    }

    /**
     * Retourne la vue correspondante aux catégories de prix.
     *
     * @return
     */
    public V_CategoriePrix_All getCategoriePrixAllFrame() {
        return this.categoriesPrixFrame;
    }

    /**
     * Retourne la vue categoriePrixEditFrame en mode Ajout
     *
     * @param mode
     * @return
     */
    public V_CategoriePrix_Edit getCategoriePrixEditFrame(int mode) {
        return getCategoriePrixEditFrame(-1, mode);
    }

    /**
     * Retourne la vue categoriePrixEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_CategoriePrix_Edit getCategoriePrixEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            categoriesPrixEditFrame.setValues(id, null, mode);
        }
        else {
            categoriesPrixEditFrame.setValues(id, getCategoriePrix(id).getLibelle(), mode);
        }
        return this.categoriesPrixEditFrame;
    }

    /**
     * Retourne la vue correspondante aux classes de prix.
     *
     * @return
     */
    public V_ClassePrix_All getClassePrixAllFrame() {
        return this.classesPrixFrame;
    }

    /**
     * Retourne la vue classePrixEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_ClassePrix_Edit getClassePrixEditFrame(int mode) {
        return getClassePrixEditFrame(-1, mode);
    }

    /**
     * Retourne la vue classePrixEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_ClassePrix_Edit getClassePrixEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            classesPrixEditFrame.setValues(id, null, mode);
        }
        else {
            classesPrixEditFrame.setValues(id, getClassePrix(id).getLibelle(), mode);
        }
        return this.classesPrixEditFrame;
    }

    /**
     * Retourne la vue correspondante aux clients.
     *
     * @return
     */
    public V_Client_All getClientAllFrame() {
        return this.clientsFrame;
    }

    /**
     * Retourne la vue clientEditFrame dans en mode Ajout.
     *
     * @param mode
     * @param parent
     * @return
     */
    public V_Client_Edit getClientEditFrame(int mode, JFrame parent) {
        return getClientEditFrame(-1, mode, parent);
    }

    /**
     * Retourne la vue clientEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @param parent
     * @return
     */
    public V_Client_Edit getClientEditFrame(int id, int mode, JFrame parent) {
        if (mode == V_Main.MODE_AJOUT) {
            clientsEditFrame.setValues(id, mode, parent);
        }
        else {
            clientsEditFrame.setValues(id, getClient(id).getNom(), getClient(id).getPrenom(), getClient(id).getAdresse1(), getClient(id).getAdresse2(), getClient(id).getLieuDit(), getClient(id).getCodePostal(), getClient(id).getBureau(), getClient(id).getTelephone(), getClient(id).getCourriel(), getClient(id).getCommentaire(), getClient(id).getIdOrigine(), mode, parent);
        }
        return clientsEditFrame;
    }

    /**
     * Retourne la vue de selection d'un client pour une nouvelle vente.
     *
     * @param parent
     * @return
     */
    public V_Client_Select getClientSelectFrame(JFrame parent) {
        clientsSelectFrame.setValues(parent);
        return clientsSelectFrame;
    }

    /**
     * Retourne la vue correspondante aux modes de paiements.
     *
     * @return
     */
    public V_ModePaiement_All getModePaiementAllFrame() {
        return this.modesPaiementFrame;
    }

    /**
     * Retourne la vue modePaiementEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_ModePaiement_Edit getModePaiementEditFrame(int mode) {
        return getModePaiementEditFrame(-1, mode);
    }

    /**
     * Retourne la vue modePaiementEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_ModePaiement_Edit getModePaiementEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            modesPaiementEditFrame.setValues(id, null, null, mode);
        }
        else {
            modesPaiementEditFrame.setValues(id, getModePaiement(id).getCode(), getModePaiement(id).getLibelle(), mode);
        }
        return this.modesPaiementEditFrame;
    }

    /**
     * Retourne la vue correspondante aux origines.
     *
     * @return
     */
    public V_Origine_All getOrigineAllFrame() {
        return this.originesFrame;
    }

    /**
     * Retourne la vue originesEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_Origine_Edit getOriginesEditFrame(int mode) {
        return getOriginesEditFrame(-1, mode);
    }

    /**
     * Retourne la vue originesEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_Origine_Edit getOriginesEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            originesEditFrame.setValues(id, null, null, mode);
        }
        else {
            originesEditFrame.setValues(id, getOrigine(id).getCode(), getOrigine(id).getLibelle(), mode);
        }
        return originesEditFrame;
    }

    /**
     * Retourne la vue correspondante au plans.
     *
     * @return
     */
    public V_Plan_All getPlanAllFrame() {
        return this.planFrame;
    }

    /**
     * Retourne la vue planEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_Plan_Edit getPlanEditFrame(int mode) {
        return getPlanEditFrame(-1, mode);
    }

    /**
     * Retourne la vue planEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_Plan_Edit getPlanEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            planEditFrame.setValues(id, null, null, 0, 0, mode);
        }
        else {
            planEditFrame.setValues(id, getPlan(id).getLibelle(), getPlan(id).getImage(), getPlan(id).getLargeur_place(), getPlan(id).getHauteur_place(), mode);
        }
        return this.planEditFrame;
    }

    /**
     * Retourne la vue correspondante aux prix.
     *
     * @return
     */
    public V_Prix_All getPrixAllFrame() {
        return this.prixFrame;
    }

    /**
     * Retourne la vue getPrixEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_Prix_Edit getPrixEditFrame(int mode) {
        return getPrixEditFrame(-1, -1, mode);
    }

    /**
     * Retourne la vue prixEditFrame dans le mode passé en paramètre.
     *
     * @param id_classe
     * @param id_categorie
     * @param mode
     * @return
     */
    public V_Prix_Edit getPrixEditFrame(int id_classe, int id_categorie, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            prixEditFrame.setValues(id_classe, id_categorie, 0, mode);
        }
        else {
            prixEditFrame.setValues(id_classe, id_categorie, getUnPrix(id_classe, id_categorie).getPrix(), mode);
        }
        return this.prixEditFrame;
    }

    /**
     * Retourne la vue correspondante aux types de place.
     *
     * @return
     */
    public V_TypePlace_All getTypePlaceAllFrame() {
        return this.typesPlaceFrame;
    }

    /**
     * Retourne la vue typePlaceEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_TypePlace_Edit getTypePlaceEditFrame(int mode) {
        return getTypePlaceEditFrame(-1, mode);
    }

    /**
     * Retourne la vue typePlaceEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_TypePlace_Edit getTypePlaceEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            typesPlaceEditFrame.setValues(id, null, mode);
        }
        else {
            typesPlaceEditFrame.setValues(id, getTypePlace(id).getLibelle(), mode);
        }
        return this.typesPlaceEditFrame;
    }

    /**
     * Retourne la vue correspondante aux zones.
     *
     * @return
     */
    public V_Zone_All getZoneAllFrame() {
        return this.zonesFrame;
    }

    /**
     * Retourne la vue zoneEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_Zone_Edit getZoneEditFrame(int mode) {
        return getZoneEditFrame(-1, mode);
    }

    /**
     * Retourne la vue zoneEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_Zone_Edit getZoneEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            zonesEditFrame.setValues(id, null, 0, mode);
        }
        else {
            zonesEditFrame.setValues(id, getZone(id).getLibelle(), getZone(id).getIdClasse(), mode);
        }
        return this.zonesEditFrame;
    }

    /**
     * Retourne la vue correspondante aux achats.
     *
     * @return
     */
    public V_Achat_All getAchatsAllFrame() {
        return this.achatsFrame;
    }

    /**
     * Retourne la vue achatEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_Achat_Edit getAchatsEditFrame(int mode) {
        return getAchatsEditFrame(-1, mode);
    }

    /**
     * Retourne la vue achatEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_Achat_Edit getAchatsEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            achatsEditFrame.setValues(id, null, null, true, null, 0, mode);
        }
        else {
            String date_resa = getAchat(id).getDate_resa().toString("yyyy-MM-dd");
            achatsEditFrame.setValues(id, date_resa, getAchat(id).getRef_resa(), getAchat(id).getValide(), getAchat(id).getCommentaire(), getAchat(id).getId_client(), mode);
        }
        return this.achatsEditFrame;
    }

    /**
     * Retourne la vue correspondante aux paiements pour l'id_achat passé en
     * paramètre.
     *
     * @param id_achat
     * @return
     */
    public V_Paiement_All getPaiementAllFrame(int id_achat) {
        if (paiementFrame.getId_Achat() != id_achat) {
            paiementFrame.setValues(id_achat);
        }
        return this.paiementFrame;
    }

    /**
     * Retourne la vue paiementEditFrame en mode Ajout.
     *
     * @param id_achat
     * @param mode
     * @return
     */
    public V_Paiement_Edit getPaiementEditFrame(int id_achat, int mode) {
        return getPaiementEditFrame(id_achat, -1, mode);
    }

    /**
     * Retourne la vue paiementEditFrame dans le mode passé en paramètre.
     *
     * @param id_achat
     * @param id
     * @param mode
     * @return
     */
    public V_Paiement_Edit getPaiementEditFrame(int id_achat, int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            paiementEditFrame.setValues(id_achat, id, null, 0, null, 0, mode);
        }
        else {
            String date_paiement = getPaiement(id_achat, id).getDate_Paiement().toString("yyyy-MM-dd");
            paiementEditFrame.setValues(id_achat, id, date_paiement, getPaiement(id_achat, id).getMontant(), getPaiement(id_achat, id).getCommentaire(), getPaiement(id_achat, id).getId_Mode(), mode);
        }
        return this.paiementEditFrame;
    }

    /**
     * Retoune la vue correspondante aux lignes pour l'id_achat passé en
     * paramètre.
     *
     * @param id_achat
     * @return
     */
    public V_Ligne_All getLigneAllFrame(int id_achat) {
        if (ligneFrame.getId_Achat() != id_achat) {
            ligneFrame.setValues(id_achat);
        }
        return this.ligneFrame;
    }

    /**
     * Retourne la vu ligneEditFrame en mode Ajout.
     *
     * @param id_achat
     * @param mode
     * @return
     */
    public V_Ligne_Edit getLigneEditFrame(int id_achat, int mode) {
        return getLigneEditFrame(id_achat, -1, mode);
    }

    /**
     * Retourne la vue ligneEditFrame dans le mode passé en paramètre.
     *
     * @param id_achat
     * @param id
     * @param mode
     * @return
     */
    public V_Ligne_Edit getLigneEditFrame(int id_achat, int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            ligneEditFrame.setValues(id_achat, id, false, 0, 0, mode);
        }
        else {
            ligneEditFrame.setValues(id_achat, id, getLigne(id_achat, id).getImprime(), getLigne(id_achat, id).getIdPlace(), getLigne(id_achat, id).getIdCategorie(), mode);
        }
        return this.ligneEditFrame;
    }

    /**
     * Retourne la vue correspondante aux places.
     *
     * @return
     */
    public V_Place_All getPlaceAllFrame() {
        return this.placesFrame;
    }

    /**
     * Retourne la vue placeEditFrame en mode Ajout.
     *
     * @param mode
     * @return
     */
    public V_Place_Edit getPlaceEditFrame(int mode) {
        return getPlaceEditFrame(-1, mode);
    }

    /**
     * Retourne la vue placeEditFrame dans le mode passé en paramètre.
     *
     * @param id
     * @param mode
     * @return
     */
    public V_Place_Edit getPlaceEditFrame(int id, int mode) {
        if (mode == V_Main.MODE_AJOUT) {
            placesEditFrame.setValues(id, "", "", 0, 0, false, 0, 0, 0, 0, 0, mode);
        }
        else {
            placesEditFrame.setValues(id, getPlace(id).getCode(), getPlace(id).getCommentaire(), getPlace(id).getXplan(), getPlace(id).getYplan(), getPlace(id).getDisponible(), getPlace(id).getId_Zone(), getPlace(id).getId_Plan(), getPlace(id).getId_Type(), getPlace(id).getId_gauche(), getPlace(id).getId_droite(), mode);
        }
        return this.placesEditFrame;
    }

    // -- Collections des tables -- //
    /**
     * Retourne la collection représentant les achats.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Achat> getAchats() {
        return this.achats;
    }

    /**
     * Retourne la collection représentant les clients.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Client> getClients() {
        return this.clients;
    }

    /**
     * Retourne la collection représentant les lignes.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Ligne> getLignes() {
        return this.lignes;
    }

    /**
     * Retourne la collection représentant les origines.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Origine> getOrigines() {
        return this.origines;
    }

    /**
     * Retourne la collection représentant les catégories de prix.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Categorie_Prix> getCategoriesPrix() {
        return this.categoriesPrix;
    }

    /**
     * Retourne la collection représentant les classes de prix.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Classe_Prix> getClassesPrix() {
        return this.classesPrix;
    }

    /**
     * Retourne la collection représentant les modes de paiement.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Mode_Paiement> getModesPaiement() {
        return this.modesPaiement;
    }

    /**
     * Retourne la collection représentant les paiements.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Paiement> getPaiements() {
        return this.paiements;
    }

    /**
     * Retourne la collection représentant les places.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Place> getPlaces() {
        return this.places;
    }

    /**
     * Retourne la collection représentant les plans.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Plan> getPlans() {
        return this.plans;
    }

    /**
     * Retourne la collection représentant les prix.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Prix> getPrix() {
        return this.lesPrix;
    }

    /**
     * Retourne la collection représentant les types de place.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Type_Place> getTypesPlace() {
        return this.typesPlace;
    }

    /**
     * Retourne la collection représentant les zones.
     *
     * @return
     */
    public CopyOnWriteArrayList<M_Zone> getZones() {
        return this.zones;
    }

    //  -- Selection, update et suppression des enregistrements dans les collections -- //
    /**
     * Retourne le client correspondant à l'id passée en paramètre.
     *
     * @param id id du client
     * @return le client correspondant ou null s'il n'est pas trouvé
     */
    public M_Client getClient(int id) {
        M_Client result = null;
        for (M_Client c : getClients()) {
            if (c.getId() == id) {
                result = c;
                break;
            }
        }
        return result;
    }

    /**
     * Si id != -1: Met à jour l'enregistrement correspondant à l'id passé en
     * paramètre. Sinon: Ajoute un nouveau client dans la table
     *
     * @param id
     * @param nom
     * @param prenom
     * @param adresse1
     * @param adresse2
     * @param lieu_dit
     * @param code_postal
     * @param bureau
     * @param telephone
     * @param courriel
     * @param commentaire
     * @param id_origine
     * @return
     */
    public int updateClient(int id, String nom, String prenom, String adresse1, String adresse2, String lieu_dit, String code_postal, String bureau, String telephone, String courriel, String commentaire, int id_origine) {
        int result = 0;
        M_Client newClient = null;
        if (id != -1) {
            getClient(id).setNom(nom);
            getClient(id).setPrenom(prenom);
            getClient(id).setAdresse1(adresse1);
            getClient(id).setAdresse2(adresse2);
            getClient(id).setLieuDit(lieu_dit);
            getClient(id).setCodePostal(code_postal);
            getClient(id).setBureau(bureau);
            getClient(id).setTelephone(telephone);
            getClient(id).setCourriel(courriel);
            getClient(id).setCommentaire(commentaire);
            getClient(id).setIdOrigine(id_origine);
            getClient(id).update();
            result = id;
        }
        else {
            newClient = new M_Client(db, nom, prenom, adresse1, adresse2, lieu_dit, code_postal, bureau, telephone, courriel, commentaire, id_origine);
            result = newClient.getId();
            getClients().add(newClient);
        }
        return result;
    }

    /**
     * Supprime le client ayant l'id passé en paramètre. Supprime également
     * l'objet de la collection.
     *
     * @param id
     */
    public void delClient(int id) {
        for (M_Achat a : getAchats()) {
            if (a.getId_client() == id) {
                delAchat(a.getId());
            }
        }
        getClient(id).delete();
        getClients().remove(getClient(id));
    }

    /**
     * Retourne l'origine correspondant à l'id passée en paramètre.
     *
     * @param id
     * @return L'origine ou null si l'origine n'a pas été trouvée
     */
    public M_Origine getOrigine(int id) {
        M_Origine result = null;
        for (M_Origine o : getOrigines()) {
            if (o.getId() == id) {
                result = o;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour l'origine si l'id != de -1, en ajoute une nouvelle sinon.
     *
     * @param id
     * @param code
     * @param libelle
     */
    public void updateOrigine(int id, String code, String libelle) {
        if (id != -1) {
            getOrigine(id).setCode(code);
            getOrigine(id).setLibelle(libelle);
            getOrigine(id).update();
        }
        else {
            getOrigines().add(new M_Origine(db, code, libelle));
        }
    }

    /**
     * Supprime l'origine comportant l'id passée en paramètre. Supprime
     * également l'objet de la collection d'origines
     *
     * @param id
     */
    public void delOrigine(int id) {
        try {
            getOrigine(id).delete();
            getOrigines().remove(getOrigine(id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCette origine est utilisée par la table cliente.");
        }
    }

    /**
     * Retourne l'objet correspondant à l'id de l'achat passée en paramètre.
     *
     * @param id
     * @return
     */
    public M_Achat getAchat(int id) {
        M_Achat result = null;
        for (M_Achat a : getAchats()) {
            if (a.getId() == id) {
                result = a;
                break;
            }
        }
        return result;
    }

    /**
     * Si id != -1: Met à jour l'enregistrement correspondant à l'id passé en
     * paramètre. Sinon: Ajoute un nouvel achat dans la table
     *
     * @param id
     * @param date
     * @param ref_resa
     * @param valide
     * @param commentaire
     * @param id_client
     */
    public int updateAchat(int id, DateTime date, String ref_resa, boolean valide, String commentaire, int id_client) {
        int id_achat = id;
        if (id != -1) {
            getAchat(id).setDate_resa(date);
            getAchat(id).setRef_resa(ref_resa);
            getAchat(id).setValide(valide);
            getAchat(id).setCommentaire(commentaire);
            getAchat(id).setId_client(id_client);
            getAchat(id).update();
        }
        else {
            M_Achat newAchat = new M_Achat(db, date, ref_resa, valide, commentaire, id_client);
            getAchats().add(newAchat);
            id_achat = newAchat.getId();
        }
        return id_achat;
    }

    /**
     * Supprime l'achat ayant l'id passé en paramètre. Supprime également
     * l'objet de la collection.
     *
     * @param id
     */
    public void delAchat(int id) {
        for (M_Ligne l : getLignes()) {
            if (l.getIdAchat() == id) {
                delLigne(l.getIdAchat(), l.getId());
            }
        }
        for (M_Paiement p : getPaiements()) {
            if (p.getId_Achat() == id) {
                delPaiement(p.getId_Achat(), p.getId());
            }
        }
        getAchat(id).delete();
        getAchats().remove(getAchat(id));
    }

    /**
     * Retourne l'objet correspondant à l'id du mode de paiement passée en
     * paramètre.
     *
     * @param id
     * @return
     */
    public M_Mode_Paiement getModePaiement(int id) {
        M_Mode_Paiement result = null;
        for (M_Mode_Paiement m : getModesPaiement()) {
            if (m.getId() == id) {
                result = m;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour le mode de paiement si l'id != de -1, en ajoute un nouvelle
     * sinon.
     *
     * @param id
     * @param code
     * @param libelle
     */
    public void updateModePaiement(int id, String code, String libelle) {
        if (id != -1) {
            getModePaiement(id).setCode(code);
            getModePaiement(id).setLibelle(libelle);
            getModePaiement(id).update();
        }
        else {
            getModesPaiement().add(new M_Mode_Paiement(db, code, libelle));
        }
    }

    /**
     * Supprime le mode de paiement comportant l'id passée en paramètre.
     * Supprime également l'objet de la collection.
     *
     * @param id
     */
    public void delModePaiement(int id) {
        try {
            getModePaiement(id).delete();
            getModesPaiement().remove(getModePaiement(id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCe mode de paiement est utilisé par la table paiement.");
        }
    }

    /**
     * Retourne l'objet correspondant à l'id de la catégorie de prix passée en
     * paramètre.
     *
     * @param id
     * @return
     */
    public M_Categorie_Prix getCategoriePrix(int id) {
        M_Categorie_Prix result = null;
        for (M_Categorie_Prix m : getCategoriesPrix()) {
            if (m.getId() == id) {
                result = m;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour la catégorie de prix si l'id != de -1, en ajoute une nouvelle
     * sinon.
     *
     * @param id
     * @param libelle
     */
    public void updateCategoriePrix(int id, String libelle) {
        if (id != -1) {
            getCategoriePrix(id).setLibelle(libelle);
            getCategoriePrix(id).update();
        }
        else {
            getCategoriesPrix().add(new M_Categorie_Prix(db, libelle));
        }
    }

    /**
     * Supprime la catégorie de prix comportant l'id passée en paramètre.
     * Supprime également l'objet de la collection.
     *
     * @param id
     */
    public void delCategoriePrix(int id) {
        try {
            getCategoriePrix(id).delete();
            getCategoriesPrix().remove(getCategoriePrix(id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCette catégorie de prix est utilisée par la table Prix ou Ligne.");
        }
    }

    /**
     * Retourne l'objet correspondant à l'id du prix passée en paramètre.
     *
     * @param id_classe
     * @param id_categorie
     * @return
     */
    public M_Prix getUnPrix(int id_classe, int id_categorie) {
        M_Prix result = null;
        for (M_Prix p : getPrix()) {
            if (p.getIdClasse() == id_classe && p.getIdCategorie() == id_categorie) {
                result = p;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour le prix si l'id != de -1, en ajoute un nouveau sinon.
     *
     * @param id_classe
     * @param id_categorie
     * @param prix
     * @param mode
     */
    public void updateUnPrix(int id_classe, int id_categorie, float prix, int mode) {
        switch (mode) {
            case V_Main.MODE_AJOUT:
                getPrix().add(new M_Prix(db, id_classe, id_categorie, prix, true));
                break;
            default:
                getUnPrix(id_classe, id_categorie).setPrix(prix);
                getUnPrix(id_classe, id_categorie).update();
                break;
        }
    }

    /**
     * Supprime le prix comportant l'id passée en paramètre. Supprime également
     * l'objet de la collection.
     *
     * @param id_classe
     * @param id_categorie
     */
    public void delUnPrix(int id_classe, int id_categorie) {
        getUnPrix(id_classe, id_categorie).delete();
        getPrix().remove(getUnPrix(id_classe, id_categorie));
    }

    /**
     * Retourne l'objet correspondant à l'id de la classe de prix passée en
     * paramètre.
     *
     * @param id
     * @return
     */
    public M_Classe_Prix getClassePrix(int id) {
        M_Classe_Prix result = null;
        for (M_Classe_Prix m : getClassesPrix()) {
            if (m.getId() == id) {
                result = m;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour la classe de prix si l'id != de -1, en ajoute une nouvelle
     * sinon.
     *
     * @param id
     * @param libelle
     */
    public void updateClassePrix(int id, String libelle) {
        if (id != -1) {
            getClassePrix(id).setLibelle(libelle);
            getClassePrix(id).update();
        }
        else {
            getClassesPrix().add(new M_Classe_Prix(db, libelle));
        }
    }

    /**
     * Supprime la classe de prix comportant l'id passée en paramètre. Supprime
     * également l'objet de la collection.
     *
     * @param id
     */
    public void delClassePrix(int id) {
        try {
            getClassePrix(id).delete();
            getClassesPrix().remove(getClassePrix(id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCette classe de prix est utilisée par la table Prix ou Zone.");
        }
    }

    /**
     * Retourne l'objet correspondant à l'id du paiement passée en paramètre.
     *
     * @param id_achat
     * @param id
     * @return
     */
    public M_Paiement getPaiement(int id_achat, int id) {
        M_Paiement result = null;
        for (M_Paiement p : getPaiements()) {
            if (p.getId_Achat() == id_achat && p.getId() == id) {
                result = p;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour le paiement si l'id != de -1, en ajoute un nouveau sinon.
     *
     * @param id_achat
     * @param id
     * @param date_paiement
     * @param montant
     * @param commentaire
     * @param id_mode
     */
    public void updatePaiement(int id_achat, int id, DateTime date_paiement, float montant, String commentaire, int id_mode) {
        if (id != -1) {
            getPaiement(id_achat, id).setDate_Paiement(date_paiement);
            getPaiement(id_achat, id).setMontant(montant);
            getPaiement(id_achat, id).setCommentaire(commentaire);
            getPaiement(id_achat, id).setId_Mode(id_mode);
            getPaiement(id_achat, id).update();
        }
        else {
            getPaiements().add(new M_Paiement(db, id_achat, date_paiement, montant, commentaire, id_mode));
        }
    }

    /**
     * Supprime le paiement comportant l'id passée en paramètre. Supprime
     * également l'objet de la collection.
     *
     * @param id_achat
     * @param id
     */
    public void delPaiement(int id_achat, int id) {
        getPaiement(id_achat, id).delete();
        getPaiements().remove(getPaiement(id_achat, id));
    }

    /**
     * Retourne l'objet correspondant à l'id de la zone passée en paramètre.
     *
     * @param id
     * @return
     */
    public M_Zone getZone(int id) {
        M_Zone result = null;
        for (M_Zone z : getZones()) {
            if (z.getId() == id) {
                result = z;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour la zone si l'id != de -1, en ajoute une nouvelle sinon.
     *
     * @param id
     * @param libelle
     * @param id_classe
     */
    public void updateZone(int id, String libelle, int id_classe) {
        if (id != -1) {
            getZone(id).setLibelle(libelle);
            getZone(id).setIdClasse(id_classe);
            getZone(id).update();
        }
        else {
            getZones().add(new M_Zone(db, libelle, id_classe));
        }
    }

    /**
     * Supprime la zone comportant l'id passée en paramètre. Supprime également
     * l'objet de la collection
     *
     * @param id
     */
    public void delZone(int id) {
        try {
            getZone(id).delete();
            getZones().remove(getZone(id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCette zone est utilisée par la table Place.");
        }
    }

    /**
     * Retourne l'objet correspondant à l'id du type de place passée en
     * paramètre.
     *
     * @param id
     * @return
     */
    public M_Type_Place getTypePlace(int id) {
        M_Type_Place result = null;
        for (M_Type_Place t : getTypesPlace()) {
            if (t.getId() == id) {
                result = t;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour le type de place si l'id != de -1, en ajoute un nouveau sinon.
     *
     * @param id
     * @param libelle
     */
    public void updateTypePlace(int id, String libelle) {
        if (id != -1) {
            getTypePlace(id).setLibelle(libelle);
            getTypePlace(id).update();
        }
        else {
            getTypesPlace().add(new M_Type_Place(db, libelle));
        }
    }

    /**
     * Supprime le type de place comportant l'id passée en paramètre. Supprime
     * également l'objet de la collection.
     *
     * @param id
     */
    public void delTypePlace(int id) {
        try {
            getTypePlace(id).delete();
            getTypesPlace().remove(getTypePlace(id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCe type de place est utilisé par la table Place.");
        }
    }

    /**
     * Retourne l'objet correspondant à l'id du plan passée en paramètre.
     *
     * @param id
     * @return
     */
    public M_Plan getPlan(int id) {
        M_Plan result = null;
        for (M_Plan p : getPlans()) {
            if (p.getId() == id) {
                result = p;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour le plan si l'id != de -1, en ajoute un nouveau sinon.
     *
     * @param id
     * @param libelle
     * @param image
     * @param width
     * @param height
     */
    public void updatePlan(int id, String libelle, String image, int width, int height) {
        if (id != -1) {
            getPlan(id).setLibelle(libelle);
            getPlan(id).setImage(image);
            getPlan(id).setLargeur_place(width);
            getPlan(id).setHauteur_place(height);
            getPlan(id).update();
        }
        else {
            getPlans().add(new M_Plan(db, libelle, image, width, height));
        }
    }

    /**
     * Supprime le plan comportant l'id passée en paramètre. Supprime également
     * l'objet de la collection.
     *
     * @param id
     */
    public void delPlan(int id) {
        try {
            getPlan(id).delete();
            getPlans().remove(getPlan(id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCe plan est utilisé par la table Place.");
        }
    }

    /**
     * Retourne l'objet correspondant à l'id de la place passée en paramètre.
     *
     * @param id
     * @return
     */
    public M_Place getPlace(int id) {
        M_Place result = null;
        for (M_Place p : getPlaces()) {
            if (p.getId() == id) {
                result = p;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour la place si l'id != de -1, en ajoute une nouvelle sinon.
     *
     * @param id
     * @param code
     * @param disponible
     * @param commentaire
     * @param xplan
     * @param yplan
     * @param id_zone
     * @param id_plan
     * @param id_type
     */
    public void updatePlace(int id, String code, boolean disponible, String commentaire, int xplan, int yplan, int id_zone, int id_plan, int id_type, int id_gauche, int id_droite) {
        if (id != -1) {
            getPlace(id).setCode(code);
            getPlace(id).setDisponible(disponible);
            getPlace(id).setCommentaire(commentaire);
            getPlace(id).setXplan(xplan);
            getPlace(id).setYplan(yplan);
            getPlace(id).setId_Zone(id_zone);
            getPlace(id).setId_Plan(id_plan);
            getPlace(id).setId_Type(id_type);
            getPlace(id).setId_gauche(id_gauche);
            getPlace(id).setId_droite(id_droite);
            getPlace(id).update();
        }
        else {
            getPlaces().add(new M_Place(db, code, disponible, commentaire, xplan, yplan, id_zone, id_plan, id_type, id_gauche, id_droite));
        }
    }

    /**
     * Supprime la place comportant l'id passée en paramètre. Supprime également
     * l'objet de la collection.
     *
     * @param id
     */
    public void delPlace(int id) {
        try {
            getPlace(id).delete();
            getPlaces().remove(getPlace(id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCette place est utilisée par la table Ligne.");
        }
    }

    /**
     * Vérifie la disponibilité à la vente d'une place.
     *
     * @param id id de la place
     * @return
     */
    public boolean estDispoAlavente(int id) {
        boolean result = true;
        if (!getPlace(id).getDisponible()) {
            result = false;
        }
        else {
            for (M_Ligne l : getLignes()) {
                if (l.getIdPlace() == id) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Retourne l'objet correspondant à l'id de la ligne passée en paramètre.
     *
     * @param id_achat
     * @param id
     * @return
     */
    public M_Ligne getLigne(int id_achat, int id) {
        M_Ligne result = null;
        for (M_Ligne l : getLignes()) {
            if (l.getId() == id && l.getIdAchat() == id_achat) {
                result = l;
                break;
            }
        }
        return result;
    }

    /**
     * Met à jour la ligne si l'id != de -1, en ajoute une nouvelle sinon.
     *
     * @param id_achat
     * @param id
     * @param imprime
     * @param id_place
     * @param id_categorie
     */
    public void updateLigne(int id_achat, int id, boolean imprime, int id_place, int id_categorie) {
        if (id != -1) {
            getLigne(id_achat, id).setImprime(imprime);
            getLigne(id_achat, id).setIdPlace(id_place);
            getLigne(id_achat, id).setIdCategorie(id_categorie);
            getLigne(id_achat, id).update();
        }
        else {
            getLignes().add(new M_Ligne(db, id_achat, imprime, id_place, id_categorie));
        }
    }

    /**
     * Supprime la ligne comportant l'id passée en paramètre. Supprime également
     * l'objet de la collection.
     *
     * @param id_achat
     * @param id
     */
    public void delLigne(int id_achat, int id) {
        try {
            getLigne(id_achat, id).delete();
            getLignes().remove(getLigne(id_achat, id));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
            errWindow("Impossible de supprimer l'enregistrement.\nCette place est utilisée par la table Ligne.");
        }
    }

    /**
     * Retourne le montant dû pour l'achat comportant l'id_achat fourni en
     * paramètre.
     *
     * @param id_achat
     * @return
     */
    public float getMontant(int id_achat) {
        float result = 0f;
        try {
            int id_classe = 0;
            int id_categorie = 0;

            for (M_Ligne l : getLignes()) {
                if (l.getIdAchat() == id_achat) {
                    id_categorie = l.getIdCategorie();
                    id_classe = getZone(getPlace(l.getIdPlace()).getId_Zone()).getIdClasse();
                    result += getUnPrix(id_classe, id_categorie).getPrix();
                }
            }
        }
        catch (NullPointerException e) {
            LogHandler.log().store(e);
            result = -1;
        }
        return result;
    }

    /**
     * Envoi à la vue principale les stats à afficher.
     */
    public void setMainStats() {
        int nbVentes = getAchats().size();
        int placesVendues = getLignes().size();
        int nonDispo = 0;
        for (M_Place p : getPlaces()) {
            if (!p.getDisponible()) nonDispo++;
        }
        int placesRestantes = getPlaces().size() - (placesVendues + nonDispo);
        getMainFrame().refreshData(nbVentes, placesVendues, placesRestantes);
    }

    /**
     * Methode principale, utilisé pour creer l'objet Controller
     *
     * @param args unused
     */
    public static void main(String... args) {
        new Controller();
    }
}
