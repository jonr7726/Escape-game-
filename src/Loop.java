import greenfoot.*;
import java.util.List;

/**
 * Loop thread will call SceneManager loop() and thus Renderer loop()
 * needed so that greenfoot can update MouseInfo
 */
public class Loop extends Thread {
    
    private SceneManager world;
    private int fps;
    private int deltaTime;
    
    /**
     * constructor initilizes instance variables 
     */
    public Loop(int fps, SceneManager world) {
        this.world = world;
        setFPS(fps);
    }
    

    /**
     * sets intended frames per second
     * update intended deltaTime
     */
    public void setFPS(int fps) {
        this.fps = fps;
        deltaTime = (int)1000/fps; //(in milli-seconds)
    }
    
    /**
     * returns fps
     */
    public int getFPS() {
        return fps;
    }
    
    /**
     * called by Thread class once when start() called
     * will call scenes loop and if it returns true ends the excecution
     * if tab key pressed will end game (used in development to stop thread running when errors occur)
     * regulates loop time to intended delta time to achieve constant fps
     */
    public void run() {
        long time;
        //System.out.println("started");
        
        while(true) {
            time = System.currentTimeMillis();
            if(world.loop()) {
                break;
            }
            if(Greenfoot.isKeyDown("tab")) {
                Greenfoot.stop();
                break;
            }
            try {
                long t = deltaTime - (System.currentTimeMillis() - time);
                if(t > 0) {
                    Thread.sleep(t);
                }
                else {
                    //System.out.println("fps: " + 1000/(System.currentTimeMillis() - time));
                }
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("finished"); 
    }
    
    /**
     * handles pauses in excecution
     * (used in development)
     */
    public static void sleep(int t) {
        try {
            Thread.sleep(t);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
