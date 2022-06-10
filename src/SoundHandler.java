import greenfoot.*;
import java.util.ArrayList;

/**
 * manages all sound in game
 */
public class SoundHandler  {
    
    public static GreenfootSound ambience = new GreenfootSound("Ambience.mp3"); //background sound played in game
    public static GreenfootSound title = new GreenfootSound("Title.mp3"); //title screen music
    
    public static GreenfootSound door = new GreenfootSound("Door.mp3"); //door opening sound
    public static GreenfootSound lockedDoor = new GreenfootSound("LockedDoor.mp3"); //locked door opening sound
    public static GreenfootSound lever = new GreenfootSound("Lever.mp3"); //lever activation sound
    public static GreenfootSound thud = new GreenfootSound("Thud.mp3"); //score animation sound

    public static GreenfootSound spotted = new GreenfootSound("Shriek.mp3"); //alien spotted player sound
    public static GreenfootSound timer = new GreenfootSound("Timer.mp3"); //timer 'braam' sound
    public static GreenfootSound timer2 = new GreenfootSound("Timer2.mp3"); //timer hissing sound
    
    private ArrayList<GreenfootSound> playing; //list of all currently playing sounds
    
    
    /**
     * constructor, initilizes instance variables
     */
    public SoundHandler() {
        playing = new ArrayList<GreenfootSound>();
    }
    
    /**
     * stops all currently playing sounds in list and clears list
     */
    public void stopAll() {
        for(GreenfootSound s : playing) {
            if(s.isPlaying()) {
                s.stop();
            }
        }
        playing.clear();
    }
    
    /**
     * stop a specified sound in the list
     */
    public void stop(GreenfootSound sound) {
        for(GreenfootSound s : playing) {
            if(s.isPlaying() && s == sound) {
                s.stop();
            }
        }
    }
    
    /**
     * play a sound, can specify playing in a loop and/or cutting other of the same sounds before playing
     * sound is the sound to play, loop is whether to loop the sound forever, cut is whether to stop all other sounds of that type to play and volume is the loudness
     */
    public void play(GreenfootSound sound, boolean loop, boolean cut, int volume) {
        playing.add(sound);
        sound.setVolume(volume);
        if(cut) {
            stop(sound);
        }
        if(loop) {
            sound.playLoop();
        }
        else {
            sound.play();
        }
    }
        
    /**
     * default variables for play method: ie volume at 100
     */
    public void play(GreenfootSound sound, boolean loop, boolean cut) {
        play(sound, loop, cut, 100);
    }
    
    
    /**
     * alternate play method that has the sound cut off based on distance from the player
     * will call original play method with generated volume
     */
    public void play(GreenfootSound sound, boolean loop, boolean cut, Point emmiter, Point listener, int radius) {
        /**
         * next lines calculate volume with this function f(x) = -(0.1104*r^-2)x^2 + 100
         * where x is the distance between the emmitter and listner and r is the radius of the sound (in tiles not pixels)
         */
        double distance = Math.sqrt(RayCast.getDistance(emmiter, listener));
        double r = 1000/(radius*radius);
        double k = 0.0001104*r;
        double volume = 100 - k*(distance*distance);
        play(sound, loop, cut, (int)volume);
    }
}
