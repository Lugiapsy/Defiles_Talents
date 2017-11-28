package defiletalents;

import java.io.File;
import java.io.FileOutputStream;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Classe permettant de gérer et d'utiliser un fichier au format xml avec un
 * seul niveau d'élément.
 *
 * @author giraudeaup
 */
public class XmlHandler {

    private Document document = null;
    private Element racine = null;
    private File file = null;

    /**
     * Creer un objet avec le fichier passé en paramètre (Doit être un chemin
     * absolu)
     *
     * @param file le fichier à charger
     */
    public XmlHandler(String file) {
        this(new File(file));
    }

    /**
     * Creer un objet avec le fichier passé en paramètre (Doit être un chemin
     * absolu)
     *
     * @param file le fichier à charger
     */
    public XmlHandler(File file) {
        this.file = file;
        load();
    }

    /**
     * Sauvegarde le fichier xml chargé.
     */
    public void save() {
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(getDocument(), new FileOutputStream(getFile()));
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Vérifie que l'élement passé en paramètre existe dans le fichier.
     *
     * @param element élément à vérifier
     * @return vrai si l'élément existe, faux sinon
     */
    public boolean elementExists(String element) {
        return (getRoot().getChild(element) != null);
    }

    /**
     * Ajoute un élément à l'arborescence
     *
     * @param element
     */
    public void addElement(String element) {
        if (elementExists(element)) return;
        getRoot().addContent(new Element(element));
    }

    /**
     * Valorise l'élement
     *
     * @param element l'élement à valoriser
     * @param value la valeur
     */
    public void setValue(String element, String value) {
        if (!elementExists(element)) return;
        getRoot().getChild(element).setAttribute("value", value);
        save();
    }

    /**
     * Retourne la valeur de l'élément passé en paramètre.
     *
     * @param element
     * @return La valeur de l'élément passé en paramètre
     */
    public String getValue(String element) {
        if (!elementExists(element)) return "null";
        return getRoot().getChild(element).getAttributeValue("value");
    }

    /**
     * Charge le fichier xml de l'objet.
     */
    private void load() {
        if (!getFile().exists()) {
            getFile().getParentFile().mkdirs();
            racine = new Element("config");
            document = new Document(racine);
            save();
        }
        else {
            SAXBuilder sxb = new SAXBuilder();
            try {
                document = sxb.build(getFile());
                racine = getDocument().getRootElement();
            }
            catch (Exception e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Affichage console du fichier chargé.
     */
    private void print() {
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(getDocument(), System.out);
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    /**
     * Retourne le document représentant le fichier XML
     *
     * @return
     */
    private Document getDocument() {
        return this.document;
    }

    /**
     * Retourne la racine du fichier XML.
     *
     * @return
     */
    private Element getRoot() {
        return this.racine;
    }

    /**
     * Retourne le fichier utilisé par l'objet.
     *
     * @return
     */
    private File getFile() {
        return this.file;
    }

    /**
     * Tests de classe
     *
     * @param args unused
     */
    public static void main(String[] args) {
        System.out.println("Chargement du fichier config.xml, création s'il n'existe pas.");
        XmlHandler configFile = new XmlHandler(System.getProperty("user.home") + "/tmp/config.xml");

        System.out.println("----------------");
        System.out.println("Element option1 existe (ajout si faux)? " + configFile.elementExists("option1"));
        configFile.addElement("option1");
        configFile.setValue("option1", "true");
        System.out.println("\tValeur de option1: " + configFile.getValue("option1"));

        System.out.println("----------------");
        System.out.println("Element option2 existe (ajout si faux)? " + configFile.elementExists("option2"));
        configFile.addElement("option2");
        configFile.setValue("option2", "valeur2");
        System.out.println("\tValeur de option2: " + configFile.getValue("option2"));

        System.out.println("----------------");
        System.out.println("Modification de option2");
        configFile.setValue("option2", "une autre valeur");
        System.out.println("\tValeur de option2: " + configFile.getValue("option2"));

        System.out.println("----------------");
        System.out.println("Affichage d'une valeur qui n'existe pas (element n'existe pas):");
        System.out.println("\tValeur de option8: " + configFile.getValue("option8"));

        System.out.println("----------------");
        System.out.println("Affichage d'une valeur qui n'existe pas (element existe):");
        configFile.addElement("option3");
        System.out.println("\tValeur de option3: " + configFile.getValue("option3"));

        System.out.println("----------------");
        System.out.print("Sauvegarde");
        configFile.save();
        System.out.println(" .. Done.");

        System.out.println("----------------");
        System.out.print("Chargement");
        configFile = null;
        configFile = new XmlHandler(System.getProperty("user.home") + "/tmp/config.xml");
        System.out.println(" .. Done.");

        System.out.println("----------------");
        System.out.println("Methode print(): ");
        configFile.print();
    }
}
