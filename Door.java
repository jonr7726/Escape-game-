import greenfoot.*;

/**
 * standard door that connects to walls
 */
public class Door extends Doors {
    
    private String[] v0 = {"┏"," ","┓",
                           " ","|"," ",
                           "┗"," ","┛"};
                        
    private String[] v1 = {"╲","┿","╱",
                           " ","┿ "," ",
                           "╱","┿","╲"};
                        
    private String[] v2 = {"╲"," ","╱",
                           "┿","┿","┿",
                           "╱"," ","╲"};
                        
    private String[] v3 = {"╋","━","╋",
                           "┣","┿","┫",
                           "╋","━","╋"};
    private String[][] textTypes = {v0,v1,v2,v3}; //textTypes explained in Tile class
    
    /**
     * Constructor calls super constructor, sets open and closed image
     */
    public Door(int x, int y, boolean[] connections, Game world) {
       super(x, y, '/', connections, world);
       super.buildObject(textTypes, new Color(200,200,200), new Color(30,30,30));
       doorClosed = image;
       doorOpened = new GreenfootImage(30,30);
       doorOpened.setColor(new Color(30,30,30));
       doorOpened.fill();
    }
    
}
