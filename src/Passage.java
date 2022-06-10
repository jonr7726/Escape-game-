import greenfoot.*;

/**
 * empty passage, blocks neither visibility nor movement
 */
public class Passage extends Tile {

    /**
     * calls super constructor, crates image from colour
     */
    public Passage(int x, int y) {
       super(x, y, ' ', null, false, true);
       image = new GreenfootImage(30,30);
       image.setColor(new Color(30,30,30));
       image.fill();
       setImage(image);
    }
    
    /**
     * Interact does nothing
     */
    public void interact() {
        return;
    }
}
