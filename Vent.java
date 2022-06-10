import greenfoot.*;

/**
 * Similar to passage (blocks neither visibility nor movement), safer from alien
 */
public class Vent extends Tile {
    
    private String[] v0 = {"┏","━","┓",
                           "┣"," ","┫",
                           "┗","━","┛"};
                        
    private String[] v1 = {"|"," ","|",
                           "|"," ","|",
                           "|"," ","|"};
                        
    private String[] v2 = {"-","-","-",
                           " "," "," ",
                           "-","-","-"};
                        
    private String[] v3 = {"┘"," ","└",
                           " "," "," ",
                           "┐"," ","┌"};
    private String[][] textTypes = {v0,v1,v2,v3}; //text types explained in Tile class
    
    /**
     * constructor creates image from super
     */
    public Vent(int x, int y, boolean[] connections) {
       super(x, y, '=', connections, false, true);
       super.buildObject(textTypes, new Color(200,200,200), new Color(100,100,100));
    }
    
    /**
     * interact does nothing
     */
    public void interact() {
        return;
    }
}
