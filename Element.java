/**
 * Element of map layout, used by grid as a segment toward the more complicated Tile class to be made in Game
 */
public class Element  {
    
    private boolean[] connections; //connection in order top,right,bottom,left
    private char type; //type of tiile it is
    
    /**
     * constructor sets type
     */
    public Element(char type) {
        this.type = type;
    }
    
    /**
     * sets connections
     */
    public void setConnections(boolean[] connections) {
        this.connections = connections;
    }
    
    /**
     * returns type
     */
    public char getType() {
        return type;
    }
    
    /**
     * returns connections
     */
    public boolean[] getConnections() {
        return connections;
    }
}
