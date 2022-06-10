import greenfoot.*;

/**
 * LockedDoor can only be unlocked with relative terminal
 */
public class LockedDoor extends Doors {
    
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
    private String[][] textTypes = {v0,v1,v2,v3}; //text types explained in Tile class
    
    public int line; //line represpective to terminal
    
    private static char[] c = {'[', '{'};
    
    /**
     * contructor will call super construcotr and set line and images
     */
    public LockedDoor(int x, int y, boolean[] connections, int line, Game world) {
       super(x, y, c[line-1], connections, world);
       this.line = line;
       Color fore;
       if(line == 1) {fore = new Color(100,200,200);}
       else {fore = new Color(100,100,200);}
       super.buildObject(textTypes, fore, new Color(30,30,30));
       doorClosed = image;
       doorOpened = new GreenfootImage(30,30);
       doorOpened.setColor(new Color(30,30,30));
       doorOpened.fill();
    }
    
    /**
     * will prevent opening
     */
    @Override
    public void interact() {
        return;
    }
    
    /**
     * will prevent closing
     */
    @Override
    public void act() {
        return;
    }
}
