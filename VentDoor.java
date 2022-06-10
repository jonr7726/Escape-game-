import greenfoot.*;

/**
 * Similar to door, connects to vent
 */
public class VentDoor extends Doors {
    
    private String[] v0 = {"╱"," ","╲",
                           "╣"," ","╠",
                           "┐"," ","┌"};
                        
    private String[] v1 = {"┘","╩","╲",
                           " "," "," ",
                           "┐","╦","╱"};
                        
    private String[] v2 = {"┘"," ","└",
                           "╣"," ","╠",
                           "╲"," ","╱"};
                        
    private String[] v3 = {"╱","╩","└",
                           " "," "," ",
                           "╲","╦","┌"};
    private String[][] textTypes = {v0,v1,v2,v3}; //text types explained in Tile class
    
    /**
     * constructor sets image using text types method
     */
    public VentDoor(int x, int y, boolean[] connections, Game world) {
       super(x, y, '+', connections, world);
       String text = selectImage(textTypes);
       image = Text.createText(buildText(text), new Color(200,200,200), new Color(100,100,100));
       setImage(image);
       doorClosed = image;
       doorOpened = new GreenfootImage(30,30);
       doorOpened.setColor(new Color(100,100,100));
       doorOpened.fill();
    }
}
