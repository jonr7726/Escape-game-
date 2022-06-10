import greenfoot.*;

/*
 * SceneManager will manage all Renderers maintaining their loop and calling load on starting their loop
 * SceneManager's background image will be set to the active Renderer's image
 * Allows for changing Renderers
 * begins the Loop thread
 * contains soundhandler to manage sounds between renderers
 */
public class SceneManager extends World {

    private Renderer world; //active renderer
    public Loop loop; //Loop thread
    public SoundHandler sound; //sound manager

    /**
     * constructor initilizes world and sets instance variables above
     */
    public SceneManager() {
        super(800, 600, 1, false);
        loop = new Loop(50, this);
        sound = new SoundHandler();
        world = new Menu(this);
    }
    
    /**
     * called by greenfoot when game is begun, will start the Loop thread and calls world load
     */
    public void started() {
        loop.start();
        world.load();
    }
    
    /**
     *loops active world, returns if game should end
     */
    public boolean loop() {
        world.loop();
        if(world.render()) {
            Greenfoot.stop();
            return true;
        }
        return false;
    }
    
    /**
     * changes renderer and calls it's load method
     */
    public void changeScene(Renderer world) {
        this.world = world;
        world.load();
    }
}