package defiletalents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe permettant de gérer et de stocker les exceptions levées dans un
 * fichier texte.
 *
 * @author giraudeaup
 */
public final class LogHandler {

    /**
     * Header du fichier log utilisé par cette classe.
     */
    private static final String HEADER = "LogHandler File";
    /**
     * Fichier par défaut utilisé.
     */
    private static final String LOG_FILE = Globales.APP_LOG;
    /**
     * Mise à disposition statique d'un objet déjà crée avec le fichier par
     * défaut.
     */
    private static final LogHandler LOGGER = new LogHandler();

    /**
     * Fichier utilisé par la classe pour enregistrer les logs.
     */
    private File logFile;
    /**
     * BufferedWriter utilisé par la classe pour écrire dans le fichier.
     */
    private BufferedWriter logWriter;

    /**
     * Retourne l'objet déjà construit de cette classe.
     *
     * @return
     */
    public static LogHandler log() {
        return LOGGER;
    }

    /**
     * Chemin absolu vers le fichier de log.
     *
     * @param logFile
     * @throws IOException si le fichier n'est pas valide ou n'a pas pu être
     * créee.
     */
    public LogHandler(File logFile) throws IOException {
        this.logFile = logFile;
        if (!checkFile()) {
            throw new IOException("Invalid Logfile !");
        }
    }

    /**
     * Chemin absolu vers le fichier de log.
     *
     * @param filename
     * @throws IOException si le fichier n'est pas valide ou n'a pas pu être
     * créee.
     */
    public LogHandler(String filename) throws IOException {
        this(new File(filename));
    }

    /**
     * Crée l'objet et le met à disposition de cette classe.
     */
    private LogHandler() {
        logFile = new File(LOG_FILE);
    }

    /**
     * Enregistre l'Exception passée en paramètre dans le fichier log.
     *
     * @param e
     */
    public void store(Exception e) {
        e.printStackTrace();
        try {
            getLogWriter().write("\n" + getDate());
            getLogWriter().newLine();
            getLogWriter().write(e.toString());
            getLogWriter().newLine();
            for (int i = 0; i < e.getStackTrace().length; i++) {
                String s = "\tat " + e.getStackTrace()[i].toString();
                getLogWriter().write(s);
                getLogWriter().newLine();
            }
            getLogWriter().write("----------------");
            closeWriter();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Ferme le Writer utilisé.
     */
    private void closeWriter() {
        try {
            logWriter.close();
            logWriter = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne la date et l'heure du jour.
     *
     * @return
     */
    private String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }

    /**
     * Vérifie l'intégrité du logFile utilisé par l'objet
     *
     * @return
     */
    private boolean checkFile() {
        boolean valid = true;
        if (!getLogFile().exists()) {
            try {
                getLogFile().getParentFile().mkdirs();
                getLogFile().createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(getLogFile()));
                bw.write(HEADER);
                bw.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                valid = false;
            }
        }
        else {
            try {
                BufferedReader br = new BufferedReader(new FileReader(getLogFile()));
                String line = br.readLine();
                if (!line.equals(HEADER)) valid = false;
                br.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Retourne le fichier utilisé par cet objet.
     *
     * @return
     */
    private File getLogFile() {
        return this.logFile;
    }

    /**
     * Retourne l'objet permettant d'écrire à la fin du fichier log.
     *
     * @return
     */
    private BufferedWriter getLogWriter() {
        if (logWriter == null) {
            try {
                logWriter = new BufferedWriter(new FileWriter(getLogFile(), true));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logWriter;
    }
}
