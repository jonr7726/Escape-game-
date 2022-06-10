/**
 * fucntional interface to pass custom interact methods into terminals (rather than making terminal sub-classes)
 */
public interface Function {

	/**
	 * abstract function that will take in the line the terminal is on and the world
	 */
    void function(Game game, int line);
}