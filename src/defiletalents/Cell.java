package defiletalents;

/**
 * Classe gerant les cellules des images.
 * @author giraudeaup
 */
public class Cell {
    /**
     * Place contenu dans cette cellule.
     */
    private M_Place place;
    /**
     * Largeur de la cellule.
     */
    private int width;
    /**
     * Hauteur de la cellule.
     */
    private int height;

    /**
     * Constructeur de la cellule.
     * @param place 
     * @param width 
     * @param height 
     */
    public Cell(M_Place place, int width, int height) {
        this.place = place;
        this.width = width;
        this.height = height;
    }

    /**
     * Retourne la place contenu dans cette cellule.
     * @return 
     */
    public M_Place getPlace() {
        return place;
    }
    
    public int getIdPlan() {
        return getPlace().getId_Plan();        
    }

    /**
     * Retourne la coordonnées x du coin haut-gauche de la cellule.
     * @return 
     */
    public int getXPlan() {
        return place.getXplan() - getWidth() / 2;
    }

    /**
     * Retourne la coordonnées y du coin haut-gauche de la cellule.
     * @return 
     */
    public int getYPlan() {
        return place.getYplan() - getHeight() / 2;
    }

    /**
     * Retourne la largeur de la cellule.
     * @return 
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retourne la hauteur de la cellule.
     * @return 
     */
    public int getHeight() {
        return height;
    }
}
