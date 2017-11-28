package testPackage;

import db_sqlite.Db_sqlite;
import defiletalents.M_Place;
import java.util.concurrent.CopyOnWriteArrayList;

public final class TestClassPlace {

    private static final TestClassPlace t = new TestClassPlace();
    private CopyOnWriteArrayList<M_Place> places;

    private static TestClassPlace t() {
        return t;
    }

    private TestClassPlace() {
        try {
            Db_sqlite db = new Db_sqlite(System.getProperty("user.home") + "/defileTalents_files/database2.sqlite");
            places = M_Place.records(db);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Retourne la place se trouvant à coté de la place passée en paramètre. 
     * @param droite vrai pour la place à droite, faux pour la place à gauche.
     * @param place la place à vérifier.
     * @return la place se trouvant à coté ou <i>null</i> s'il n'y a pas de place.
     */
    private M_Place placeNextTo(boolean droite, M_Place place) {
        return (droite ? onTheRight(place) : onTheLeft(place));
    }

    /**
     * Retourne la place se trouvant à droite de la place passée en paramètre.
     * @param place la place à vérifier.
     * @return la place se trouvant à droite ou <i>null</i> s'il n'y a pas de place.
     */
    private M_Place onTheRight(M_Place place) {
        M_Place result = null;
        int x = place.getXplan();
        int y = place.getYplan();

        for (M_Place p : places) {
            if (p.getYplan() == y) {
                if (x != 2) {
                    if (x % 2 == 0) {
                        if (p.getXplan() == x - 2) {
                            result = p;
                            break;
                        }
                    }
                    else if (p.getXplan() == x + 2) {
                        result = p;
                        break;
                    }
                }
                else if (p.getXplan() == x - 1) {
                    result = p;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Retourne la place se trouvant à gauche de la place passée en paramètre.
     * @param place la place à vérifier.
     * @return la place se trouvant à gauche ou <i>null</i> s'il n'y a pas de place.
     */
    private M_Place onTheLeft(M_Place place) {
        M_Place result = null;
        int x = place.getXplan();
        int y = place.getYplan();

        for (M_Place p : places) {
            if (p.getYplan() == y) {
                if (x == 1) {
                    if (p.getXplan() == x + 1) {
                        result = p;
                        break;
                    }
                }
                else if (x % 2 == 0) {
                    if (p.getXplan() == x + 2) {
                        result = p;
                        break;
                    }
                }
                else if (p.getXplan() == x - 2) {
                    result = p;
                    break;
                }
            }
        }
        return result;
    }

    private M_Place getPlace(int id) {
        M_Place result = null;
        for (M_Place p : places) {
            if (p.getId() == id) {
                result = p;
                break;
            }
        }
        return result;
    }

    public static void main(String... args) {
        M_Place placeCheked = TestClassPlace.t().getPlace(1);
        System.out.printf("x: %d, y: %d\n", placeCheked.getXplan(), placeCheked.getYplan());

        M_Place placeOnTheRight = TestClassPlace.t().placeNextTo(true, placeCheked);
        System.out.printf("[RIGHTSIDE] x: %d, y: %d\n", placeOnTheRight.getXplan(), placeOnTheRight.getYplan());
        
        M_Place placeOnTheLeft = TestClassPlace.t().placeNextTo(false, placeCheked);
        System.out.printf("[LEFTSIDE]x: %d, y: %d\n", placeOnTheLeft.getXplan(), placeOnTheLeft.getYplan());

    }
}
