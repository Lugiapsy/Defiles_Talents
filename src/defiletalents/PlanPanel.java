package defiletalents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Classe permettant de gérer les images de plan
 *
 * @author giraudeaup
 */
public class PlanPanel extends JPanel {

    /**
     * Largeur du panel.
     */
    private static final int WIDTH = 1169;
    /**
     * Hauteur du panel.
     */
    private static final int HEIGHT = 826;

    /**
     * Controleur de l'appli.
     */
    private Controller controller;
    /**
     * Tableau d'images contenant les plans.
     */
    private ArrayList<BufferedImage> images;
    /**
     * Choix de l'utilisateur.
     */
    private int currentChoice;

    /**
     * Collection des cecllules de la grille.
     */
    private ArrayList<Cell> places;

    /**
     * Constructeur du JPanel.
     *
     * @param controller
     */
    public PlanPanel(Controller controller) {
        this.controller = controller;
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        currentChoice = 0;

        init();
        addListener();
    }

    /**
     * Initialise les tableaux.
     */
    private void init() {
        try {
            CopyOnWriteArrayList<M_Place> allPlaces = controller.getPlaces();
            places = new ArrayList();

            for (int i = 0; i < allPlaces.size(); i++) {
                int width = controller.getPlan(allPlaces.get(i).getId_Plan()).getLargeur_place();
                int height = controller.getPlan(allPlaces.get(i).getId_Plan()).getHauteur_place();
                places.add(new Cell(allPlaces.get(i), width, height));
            }
        }
        catch (Exception e) {
            LogHandler.log().store(e);
        }
    }

    public void incrementChoice() {
        currentChoice++;
        if (currentChoice > nbImages() - 1) currentChoice = nbImages() - 1;
    }

    public void decrementChoice() {
        currentChoice--;
        if (currentChoice < 0) currentChoice = 0;
    }

    public int getCurrentChoice() {
        return currentChoice;
    }

    /**
     * Ajoute un mouseListener sur les images afin de determiner les coordonées.
     */
    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Coordonées du pointeur de la souris
                int x = e.getX();
                int y = e.getY();
                for (Cell c : places) {
                    // Coordonées du plan en haut à gauche de la cellule
                    int xo = c.getXPlan();
                    int yo = c.getYPlan();
                    boolean dispo = controller.estDispoAlavente(c.getPlace().getId());
                    // Filtre les places avec le choix actuel, et vérifie que le pointeur de la souris est bien dans une cellule de l'image
                    if (currentChoice == c.getIdPlan() - 1 && x <= xo + c.getWidth() && y <= yo + c.getHeight() && x >= xo && y >= yo && dispo) {
                        // Refresh l'image d'abord, paint la cellule selectionnée ensuite
                        render();
                        Graphics2D g = (Graphics2D) getGraphics();
                        // Couleur Verte transparente a 40% (0,255,0)
                        g.setColor(new Color(0, 1, 0, 0.4f));
                        g.fillRect(c.getXPlan(), c.getYPlan(), c.getWidth(), c.getHeight());
                        g.dispose();
                        System.out.println(c.getPlace().getCode());
                        break;
                    }
                }
            }
        });
    }

    /**
     * Charge les images des plans.
     *
     * @param paths
     */
    public void loadImages(String... paths) {
        if (images != null && paths.length == images.size()) return;
        images = new ArrayList();
        InputStream in = null;
        for (String path : paths) {
            try {
                in = new FileInputStream(new File(path));
                images.add(ImageIO.read(in));
            }
            catch (IOException e) {
                LogHandler.log().store(e);
            }
        }
    }

    /**
     * Retourne l'image contenu au choix de l'utilisateur.
     *
     * @return
     */
    private BufferedImage getCurrentImage() {
        return images.get(currentChoice);
    }

    /**
     * Nombres d'imags dans la collection.
     *
     * @return
     */
    public int nbImages() {
        return images.size();
    }

    /**
     * Methode pour afficher les images.
     */
    public void render() {
        paint(getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(getCurrentImage(), 0, 0, WIDTH, HEIGHT, null);

        // Couleur Rouge transparente a 40% (255,0,0)
        g.setColor(new Color(1, 0, 0, 0.4f));
        for (Cell c : places) {
            if (!controller.estDispoAlavente(c.getPlace().getId())) {
                if (c.getIdPlan() - 1 == currentChoice) {
                    // Paint les cellules contenant des places déjà vendues ou non disponible.
                    g.fillRect(c.getXPlan(), c.getYPlan(), c.getWidth(), c.getHeight());
                }
            }
        }
        // Dessine une bordure noire sur l'image
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, HEIGHT - 1);
        g.dispose();
    }
}
