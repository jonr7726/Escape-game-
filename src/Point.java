import greenfoot.*;

/**
 * Point contains x and y position
 * extends Displayable so it can be added to world for visualization (used in development)
 */
public class Point extends Displayable {
    public double x; public double y; //x,y position
    private GreenfootImage dot; //point image
    
    /**
     * Constructor passes to other constructor with default image of dot
     */
    public Point(double x, double y) {
        this(x, y, new GreenfootImage("dot.png"));
    }
    
    /**
     * Constructor initilizes instance variables
     */
    public Point(double x, double y, GreenfootImage image) {
        super((int)(x), (int)(y), image);
        this.x = x; this.y = y;
    }
}
