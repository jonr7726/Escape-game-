import greenfoot.*;

/**
 * Interface that incorporates functions to be implemented to have ability for change of state when mouse hovers over
 */
public interface Hoverable {
    
    void onHover(); //called when mouse hovers over
    void onMove(); //called once mouse moved off
    boolean hover(); //will return if currently being hoverered over
}

