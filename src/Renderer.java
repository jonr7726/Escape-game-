import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Renderer will replace world
 * Renderer contains a list of active and static Displayables;
 * active are displayables that will move and thus must be rendered each frame
 * wheras static displayabes will not be moved and will only be rendered once to reduce loop time
 */
public abstract class Renderer {
    
    public static Color transparent = new Color(0,0,0,0);
    
    public SceneManager scene; //scenemanager stored so that world can be changed
    private boolean end; //set to true will end the loop cycle
    
    private int width; //screen width
    private int height; //screen height
    
    private ArrayList<Displayable> staticDisplayables; //static displayables that do not need to be updated
    private ArrayList<Displayable> activeDisplayables; //displayables that need to be rendered each frame
    private GreenfootImage staticDisplayablesImage; //image of all statics
    private GreenfootImage backgroundImage; //background image
    private GreenfootImage staticImage; //image of background and statics
    
    /**
     * constructor initialises all class variables above
     */
    public Renderer(int width, int height, int scale, int fps, SceneManager scene) { 
        this.width = width;
        this.height = height;
        this.scene = scene;
        scene.loop.setFPS(fps);
        staticDisplayables = new ArrayList<Displayable>();
        activeDisplayables = new ArrayList<Displayable>();
        staticDisplayablesImage = new GreenfootImage(width,height);
        setBackgroundImage(new GreenfootImage(width,height));
        end = false;
    }
    
    /**
     * will end excecution in render method
     */
    public final void end() {
        end = true;
    }
    
    /**
     * will be called each loop by this in render
     */
    public abstract void loop();
    
    /**
     * will be called once by scenemanager when renderer is initilized
     * not abstract as it is not always a necessary method
     */
    public void load() {};
    
    /**
     * set background image
     */
    public final void setBackgroundImage(GreenfootImage image) {
        backgroundImage = image;
        setStaticImage();
    }
    
    /**
     * sets static image with staticDisplayablesImage and backgroundImage
     */
    public final void setStaticImage() {
        GreenfootImage image = backgroundImage;
        image.drawImage(staticDisplayablesImage, 0, 0);
        staticImage = image;
    }
    
    /**
     * sets priorities (int order of least to most important)
     * will change the paint order of active displayables as to move certain displayables forward in display
     */
    public final void setOrder(java.lang.Class... priorities) {
        ArrayList<Displayable> priorityList = new ArrayList<Displayable>();
        for(java.lang.Class<Displayable> priority : priorities) {
            for(Displayable active : activeDisplayables) {
                if(active.getClass() == priority) {
                    priorityList.add(active);
                }
            }
        }
        for(Displayable active : priorityList) {
            activeDisplayables.remove(active);
            activeDisplayables.add(active);
        }
    }
    
    /**
     * called by sceneManager, will render image by painting all active displayables over staticImage
     * sets sceneManager image that greenfoot's thread will render
     * if end() has been called it will return true ending the Loops excecution
     */
    public final boolean render() { //itterate over each active display, return true if ending game
        GreenfootImage image = new GreenfootImage(width, height);
        image.drawImage(staticImage,0,0);
        for(Displayable activeDisplayable : activeDisplayables) {
            image.drawImage(activeDisplayable.getImage(), activeDisplayable.getX(), activeDisplayable.getY());
        }
        scene.setBackground(image);
        if(end) { //if escape key pressed end game
            return true;
        }
        return false;
    }
    
    /**
     * adds static displayable to list and calls addedToWorld method
     * updates staticdisplayablesimage and then staticimage 
     */
    public final void addStatic(Displayable staticDisplayable) {
        staticDisplayables.add(staticDisplayable);
        staticDisplayablesImage.drawImage(staticDisplayable.getImage(),staticDisplayable.getX(), staticDisplayable.getY());
        setStaticImage();
        staticDisplayable.addedToWorld();
    }
    
    /**
     * adds active displayable to list and calls addedToWorld method
     */
    public final void addActive(Displayable activeDisplayable) {
        activeDisplayables.add(activeDisplayable);
        activeDisplayable.addedToWorld();
    }
    
    /**
     * removes active displayble from list
     */
    public final void removeActive(Displayable activeDisplayable) {
        activeDisplayables.remove(activeDisplayable);
    }
    
    /**
     * returns list of active displayables
     */
    public final ArrayList<Displayable> getActive() {
        return activeDisplayables;
    }
    
    /**
     * returns list of static displaybles
     */
    public final ArrayList<Displayable> getStatic() {
        return staticDisplayables;
    }
    
    /**
     * returns all active displyables that are an instance of a certain class
     */
    public final <D> List<D> getActive(java.lang.Class<D> cls) {
        List displayable = new ArrayList<D>();
        for(Displayable active : activeDisplayables) {
            if(cls.isInstance(active)) {
                displayable.add(active);
            }
        }
        return displayable;
    }
    
    /**
     * returns getActiveAt with default class of no limitations
     */
    public final List<Displayable> getActiveAt(int x, int y) {
        return getActiveAt(x, y, Displayable.class);
    }
    
    /**
     * returns all active displayables that intersect a certain position and are an instace of a certain class
     */
    public final <D> List<D> getActiveAt(int x, int y, java.lang.Class<D> cls) {
        List displayables = new ArrayList<D>();
        for(Displayable active : activeDisplayables) {
            if(cls.isInstance(active) && (x > active.getX() && x < active.getX()+active.getImage().getWidth() && y > active.getY() && y < active.getY()+active.getImage().getHeight())) {
                displayables.add(active);
            }
        }
        return displayables;
    }
}
