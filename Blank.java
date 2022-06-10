import greenfoot.*;

/**
 * Blank tile with nothing on it
 * symbol is '_'
 */
public class Blank extends Tile {

	/**
	 * calls superclass constructor
	 */
    public Blank(int x, int y) {
       super(x, y, '_', null, false, false);
    }
    
    /**
	 * interact does nothing
	 */
    public void interact() {
        return;
    }
}
