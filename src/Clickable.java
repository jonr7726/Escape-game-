import greenfoot.*;

/**
 * Functional interface for something that can be clicked with a mouse and have certain functionality (used only in buttons however could be implemented in toher places in future)
 */
public interface Clickable {
	/**
     * abstract function called by a mouse manager class, takes in current renderer and mouseButton clicked with
     */
    void onClick(Renderer world, int mouseButton);
}

