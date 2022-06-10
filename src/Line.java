import greenfoot.*;

/**
 * line of 2 points and a colour that can be drawn to screen
 */
public class Line extends Displayable {

    private Point a; //2 points that make the line
    private Point b;
    
    /**
     * constructor takes 2 points and a colour and makes a line to be drawn to screen
     */
    public Line(Point a, Point b, Color c) {
        super(0, 0);
        this.a = a; this.b = b;
        GreenfootImage img = new GreenfootImage(800, 600);   
        img.setColor(c);
        img.drawLine((int)a.x,(int)a.y,(int)b.x,(int)b.y);
        setImage(img);
    }
}

