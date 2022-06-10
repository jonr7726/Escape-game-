import greenfoot.*; 

/**
 * Timer keeps track of time left until Alien finds player
 * works off intended delta time
 * time can be added with levers
 */
public class Timer extends Text {
    
    private double time; //time left
    private double deltaTime; //loop fps
    private boolean end; //if timer has ended
    private int wholeTime; //whole time to decide if to render image
    
    /**
     * Creates text, initilizes instance variables above
     */
    public Timer(int x, int y, int fps) {
        super(x, y, "30", 30, Color.GREEN, Renderer.transparent, new Color(0,120,0), 2);
        deltaTime = 1.0/fps;
        time = 30;
        wholeTime = 30;
        end = false;
    }
    
    /**
     * called each loop, removes delta time from time
     * if no time left; play both timer end sounds and begin perminant spotted from alien
     */
    public void act(Game world) {
        if(time > 0) {
            time -= deltaTime;
            if((int)time != wholeTime) {
                wholeTime = (int)time;
                updateText(wholeTime + "");
            }
            if(time <= 0) {
                world.alien.timerEnd();
                world.scene.sound.play(SoundHandler.timer, false,  false);
                world.scene.sound.play(SoundHandler.timer2, false,  true);
                end = true;
            }
        }
    }
    
    /**
     * Adds time from lever only if the timer has not already ended
     */
    public void addTime(int time) {
        if(!end) {
            this.time += time;
        }
    }
    
    /**
     * returns time
     */
    public int getTime() {
        return (int)time;
    }
}