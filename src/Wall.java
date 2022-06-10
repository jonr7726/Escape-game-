import greenfoot.*;

/**
 * Wall in map, blocks visibility and movement
 */
public class Wall extends Tile {
    
    private String[] v0 = {"┏","━","┓",
                           "┣","┿","┫",
                           "┗","━","┛"};
                        
    private String[] v1 = {"┣","┿","┫",
                           "┣","┿","┫",
                           "┣","┿","┫"};
                        
    private String[] v2 = {"┳","━","┳",
                           "┿","┿","┿",
                           "┻","━","┻"};
                        
    private String[] v3 = {"╋","━","╋",
                           "┣","┿","┫",
                           "╋","━","╋"};
    private String[][] textTypes = {v0,v1,v2,v3}; //text types explained in Tile class
    
    /**
     * constructor creates image from super using textTypes
     */
    public Wall(int x, int y, boolean[] connections) {
       super(x, y, '#', connections, true, false);
       super.buildObject(textTypes, new Color(200,200,200), new Color(30,30,30));
    }
    
    /**
     * Does nothing
     */
    public void interact() {
        return;
    }
}
