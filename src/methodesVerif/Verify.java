package methodesVerif;

import db_sqlite.Db_sqlite;
import defiletalents.LogHandler;
import defiletalents.M_Client;
import defiletalents.M_Place;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

/**
 * Classe contenant les diverses méthodes utilisées pour vérifier les valeurs.
 *
 * @author giraudeaup, henrij
 * @version 1.0
 *
 */
public class Verify {

    /**
     * Pattern utilisé pour vérifié une adresse mail.
     */
    public static final String PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{1,}.";

    /**
     * Verifie la chaine de caractères. Coupe les espaces en début et en fin de
     * chaine.
     *
     * @param input la valeur d'entrée
     * @param length la longueur max autorisé
     * @return une chaine dont la longueur est inférieur ou égale à la longueur
     * passée en paramètre. Coupe les caractères en trop si necessaire.
     */
    public static String toString(String input, int length) {
        // si la chaine input n'est pas initialisée, elle est fixée à vide
        if (input == null || input.equals("")) {
            input = "";
        }
        else {
            // on enlève les espaces et tabulations de début et fin
            input = input.trim();
        }

        // on ajuste le paramètre length dans l'intervalle [1..input.length()]
        int nb = Math.min(Math.max(1, length), input.length());

        return (nb > 0 ? input.substring(0, nb) : "");
    }

    /**
     * Transforme une chaine de caractères en Entier. Retire toutes les lettres
     * s'il y'en a.
     *
     * @param input La chaine à transformer.
     * @return Un entier représentant la chaine sans les caractères
     * alphabétiques.
     */
    public static int toInt(String input) {
        int result = 0;
        if (input == null || input.equals("")) {
            input = "0";
        }
        // On essaye de parse directement la valeur d'entree et si on ne peut pas, on verifie la valeur
        try {
            result = Integer.parseInt(input);
        }
        catch (Exception e) {
            String s = "0";
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                    s += input.charAt(i);
                }
            }
            result = Integer.parseInt(s);
        }
        return result;
    }

    /**
     * Transforme une chaine de caractères en Réel. Retire toutes les lettres
     * s'il y'en a.
     *
     * @param input la chaine à transformer
     * @return Un nombre réel représentant la chaine sans les caractères
     * alphabétiques.
     */
    public static float toFloat(String input) {
        float result = 0f;
        if (input == null || input.equals("")) {
            input = "0";
        }
        if (input.contains(",")) {
            input = input.replace(',', '.');
        }

        try {
            result = Float.parseFloat(input);
        }
        catch (Exception e) {
            String s = "0";
            for (int i = 0; i < input.length(); i++) {
                if ((input.charAt(i) >= '0' && input.charAt(i) <= '9') || (input.charAt(i) == '.')) {
                    s += input.charAt(i);
                }
            }
            result = Float.parseFloat(s);
        }
        return result;
    }

    /**
     * Genere une chaine aléatoire de caractères
     *
     * @param length la taille voulue
     * @return une chaine aléatoire de taille 'length'
     */
    public static String generateRandomString(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
        String result = "";
        Random r = new Random();

        while (result.length() != length) {
            result += alphabet.charAt(r.nextInt(alphabet.length()));
        }

        return result;
    }

    /**
     * Vérifie l'adresse mail passée en paramètre.
     *
     * @param input l'addresse mail à vérifier
     * @return retourne vrai si l'adresse mail est conforme, faux sinon
     */
    public static boolean toMail(String input) {
        // On compile le pattern voulu, puis on compare à la valeur d'entree et on retourne si oui ou non ils correspondent.
        return Pattern.compile(PATTERN).matcher(input).matches();
    }

    /**
     * Permet de trier une collection de client
     *
     * @param clients la collection à trier
     * @return la collection triée par nom de client
     */
    public static CopyOnWriteArrayList<M_Client> trierClients(CopyOnWriteArrayList<M_Client> clients) {
        Comparator<M_Client> comparator = new CompClient();

        Collections.sort(clients, comparator);

        return clients;
    }

    /**
     * Permet de trier une collection de place.
     *
     * @param places la collection à trier
     * @return la collection triée par code de place.
     */
    public static CopyOnWriteArrayList<M_Place> trierPlaces(CopyOnWriteArrayList<M_Place> places) {
        Comparator<M_Place> comparator = new CompPlace();

        Collections.sort(places, comparator);

        return places;
    }

    private static class CompClient implements Comparator<M_Client> {

        @Override
        public int compare(M_Client client1, M_Client client2) {
            return client1.getNom().compareTo(client2.getNom());
        }
    }

    private static class CompPlace implements Comparator<M_Place> {

        @Override
        public int compare(M_Place p1, M_Place p2) {
            return p1.getCode().compareTo(p2.getCode());
        }
    }

    /**
     * Permet de détecter le type du fichier passé en paramètre. Ne fonctionne
     * que sur les systèmes basés sur linux.
     *
     * @param file Le fichier à tester
     * @return Le type de fichier detecté ou null.
     */
    public static String fileDetect(File file) {
        String type = "null";
        try {
            type = Files.probeContentType(file.toPath());
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
        return type;
    }

    /**
     * Variation de la méthode fileDetec pour fonctionner sous windows
     *
     * @param file
     * @return vrai si le fichier est une base de données SQLite 3, faux sinon
     */
    public static boolean isValidDb(File file) {
        boolean result;
        if (!file.exists()) {
            result = false;
        }
        else {
            try {
                // Lecture de la première ligne contenant le header qui nous interesse
                result = (new BufferedReader(new FileReader(file)).readLine().contains("SQLite format 3"));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
                result = false;
            }
        }
        return result;
    }

    /**
     * Méthode de test de classe.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        try {
            /*int length = 1005;
            System.out.println(String.format("Taille de la chaine : %d", length));
            System.out.println(generateRandomString(length) + " ... Length : " + generateRandomString(length).length());*/

            System.out.println("\nTest de la classe Verify");
            System.out.println("========================\n");

            System.out.println(String.format("Création d'une chaine aléatoire de 5 caractères: >%s<", generateRandomString(5)));
            System.out.println(String.format("Création d'une chaine aléatoire de 0 caractères: >%s<", generateRandomString(0)));

            String uneChaine = "Bonjour";
            int nbCar = 0;
            System.out.println(String.format("\nNettoyage de la chaine de %d car >%s<", uneChaine.length(), uneChaine));
            String uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = "Bonjour";
            nbCar = 1;
            System.out.println(String.format("\nNettoyage de la chaine de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = "Bonjour";
            nbCar = 2;
            System.out.println(String.format("\nNettoyage de la chaine de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = "Bonjour";
            nbCar = uneChaine.length();
            System.out.println(String.format("\nNettoyage de la chaine de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = "Bonjour";
            nbCar = uneChaine.length() + 2;
            System.out.println(String.format("\nNettoyage de la chaine de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = null;
            nbCar = 1;
            System.out.println(String.format("\nNettoyage de la chaine null >%s<", uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = "";
            nbCar = 1;
            System.out.println(String.format("\nNettoyage de la chaine vide >%s<", uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = " Bonjour ";
            nbCar = 2;
            System.out.println(String.format("\nNettoyage de la chaine de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = " Bonjour ";
            nbCar = 7;
            System.out.println(String.format("\nNettoyage de la chaine de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = "  Bonjour  ";
            nbCar = 7;
            System.out.println(String.format("\nNettoyage de la chaine de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = "\tBonjour \t ";
            nbCar = 7;
            System.out.println(String.format("\nNettoyage d'une chaine avec tabulation de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            uneChaine = "\tBonjour \t ";
            nbCar = 10;
            System.out.println(String.format("\nNettoyage d'une chaine avec tabulation de %d car >%s<", uneChaine.length(), uneChaine));
            uneAutreChaine = toString(uneChaine, nbCar);
            System.out.println(String.format("et limite d'une chaine à %d caractères (%d): >%s<", nbCar, uneAutreChaine.length(), uneAutreChaine));

            System.out.println("\nVerification de la méthode toMail()");
            System.out.println("========================\n");
            String mail = "giraudeaup@saintjo.org";
            System.out.printf("Adresse mail %s est conforme ? %b\n", mail, toMail(mail));
            String mail2 = "giraudeaup@saintjoorg";
            System.out.printf("Adresse mail %s est conforme ? %b\n", mail2, toMail(mail2));
            String mail3 = "giraudeaupsaintjo.org";
            System.out.printf("Adresse mail %s est conforme ? %b\n", mail3, toMail(mail3));
            String mail4 = "giraudeaupsaintjoorg";
            System.out.printf("Adresse mail %s est conforme ? %b\n", mail4, toMail(mail4));
            String mail5 = "g.r@s.org";
            System.out.printf("Adresse mail %s est conforme ? %b\n", mail5, toMail(mail5));
            String mail6 = "g.r@s.orgeeeeeeeeeeeeeeeeeeeeee";
            System.out.printf("Adresse mail %s est conforme ? %b\n", mail6, toMail(mail6));
            String mail7 = "g.r@s.ee";
            System.out.printf("Adresse mail %s est conforme ? %b\n", mail7, toMail(mail7));

            System.out.println("\nVerification de la méthode trierClient()");
            System.out.println("========================\n");

            Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/defileTalents_files/database2.sqlite");

            CopyOnWriteArrayList<M_Client> clients = M_Client.records(db);

            System.out.println("Collection non triée:");
            System.out.println("=====================");
            for (M_Client c : clients) {
                System.out.printf("Nom: %s", c.getNom() + "\n");
            }
            clients = trierClients(clients);

            System.out.println("Collection triée:");
            System.out.println("=====================");
            for (M_Client c : clients) {
                System.out.printf("Nom: %s", c.getNom() + "\n");
            }

            System.out.println("\nVerification de la méthode toInt()");
            System.out.println("========================\n");

            String s1 = "5555";
            System.out.printf("Chaine '%s' toInt() : %d\n", s1, toInt(s1));
            String s2 = "4f5f4z8";
            System.out.printf("Chaine '%s' toInt() : %d\n", s2, toInt(s2));
            String s3 = "";
            System.out.printf("Chaine '%s' toInt() : %d\n", s3, toInt(s3));
            String s4 = "ffazfe";
            System.out.printf("Chaine '%s' toInt() : %d\n", s4, toInt(s4));
            String s5 = null;
            System.out.printf("Chaine '%s' toInt() : %d\n", s5, toInt(s5));

            System.out.println("\nVerification de la méthode toFloat()");
            System.out.println("========================\n");

            String f1 = "14,5€";
            System.out.printf("Chaine '%s' toFloat() : %f\n", f1, toFloat(f1));
            String f2 = "";
            System.out.printf("Chaine '%s' toFloat() : %f\n", f2, toFloat(f2));
            String f3 = "ga44ha51,5az5h1";
            System.out.printf("Chaine '%s' toFloat() : %f\n", f3, toFloat(f3));
            String f4 = "only letters";
            System.out.printf("Chaine '%s' toFloat() : %f\n", f4, toFloat(f4));
            String f5 = null;
            System.out.printf("Chaine '%s' toFloat() : %f\n", f5, toFloat(f5));

            System.out.println("\nVerification de la méthode fileDetect()");
            System.out.println("========================\n");

            File file1 = new File(System.getProperty("user.home") + "/defileTalents_files/database.sqlite");
            System.out.printf("Fichier %s de type <%s>(Fichier Texte avec extension sqlite)\n", file1.getName(), fileDetect(file1));
            File file2 = new File(System.getProperty("user.home") + "/tmp/config.xml");
            System.out.printf("Fichier %s de type <%s>(Fichier XML)\n", file2.getName(), fileDetect(file2));
            File file3 = new File(System.getProperty("user.home") + "/defileTalents_files/dataBase.sqlite");
            System.out.printf("Fichier %s de type <%s>(Fichier SQLITE valide)\n", file3.getName(), fileDetect(file3));

        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }

    }
}
