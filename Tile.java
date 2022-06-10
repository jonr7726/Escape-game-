import java.util.ArrayList;
import greenfoot.*;

/**
 * cell in the grid of the map
 * super class to all cells
 * cub-classes must define interact method (for when player interacts)
 * conatins methods to assist in building ascii images
 * does this with use of 'text types': 3 arrays of strings
 * array 0 represents (for each individual letter) what it is with no connections
 * array 1 """ with top/bottom connection
 * array 2 """ with left/right connection
 * array 3 """ with top/bottom and left/right connection
 */
public abstract class Tile extends Displayable {
    
    private static GreenfootImage blank = new GreenfootImage(30,30); //static blank tile image
    public GreenfootImage image; //current image
    public boolean isPassable; //if blocks movement
    public boolean visibility; //if blocks visibility
    
    private boolean[] connections; //connections to adjacent tiles
    private String text; //ascii text
    private char c; //symbol (used in map editor)
    
    public abstract void interact(); //called by player when pressed e on
    
    /**
     * constuctor calls super constructor and sets instance variables
     */
    public Tile(int x, int y, char c, boolean[] connections, boolean visibility, boolean isPassable) {
        super(x,y);
        this.c = c;
        this.connections = connections;
        this.visibility = visibility;
        this.isPassable = isPassable;
    }
    
    /**
     * build object creates ascii image given textTypes and foreground/bakcground colours
     */
    public void buildObject(String[][] textTypes, Color fore, Color back) {
        text = buildConnections(textTypes);
        image = Text.createText(buildText(text), fore, back);
        setImage(image);
    }
    
    /**
     * method to help creating textTypes
     */
    protected static String[] buildText(String text) {
        String[] text2 = {text.substring(0,3), text.substring(3,6), text.substring(6,9)};
        return text2;
    }
    
    /**
     * alternate to buildObeject used in terminals
     */
    protected String selectImage(String[][] textTypes) {
        int result = 0;
        String ret = "";
        if(!connections[1]) {
            result = 1;
        }
        else if(!connections[2]) {
            result = 2;
        }
        else if(!connections[3]) {
            result = 3;
        }
        for(int i = 0; i < 9; i++) {
            ret += textTypes[result][i];
        }
        return ret;
    }
    
    /**
     * builds image given connections and textTypes
     * notice for any one letter it can either have value 0 (no connections), 1 (top/bottom connection), 2 (left/right connection)
     * or 3 -with addition of 1 and 2 meaning 2 connections total (only if it is the corner letter)
     */
    private String buildConnections(String[][] textTypes) {
        int[] array = {0,0,0,
                       0,0,0,
                       0,0,0};
        String ret = "";
        if(connections[0]) {
            array[0] += 1;
            array[1] += 1;
            array[2] += 1;
        }
        if(connections[2]) {
            array[6] += 1;
            array[7] += 1;
            array[8] += 1;
        }
        if(connections[1]) {
            array[2] += 2;
            array[5] += 2;
            array[8] += 2;
        }
        if(connections[3]) {
            array[0] += 2;
            array[3] += 2;
            array[6] += 2;
        }
        for(int i = 0; i < 9; i++) {
            ret += textTypes[array[i]][i];
        }
        return ret;
    }
    
    /**
     * returns connections
     */
    public boolean[] getConnections() {
        return connections;
    }
    
    /**
     * returns character (used in map editor)
     */
    public char character() {
        return c;
    }
}
