import greenfoot.*;  
import java.util.ArrayList;

/**
 * Super class for all door classes
 * features opend and close functionality, interact will open door, act will run a timer to close the door if nothing is on it
 */
public class Doors extends Tile {
    
    private boolean doorOpen; //if door is open
    protected GreenfootImage doorOpened; //image for door opened
    protected GreenfootImage doorClosed; //image for door closed
    private int openTimer; //timer for door to close
    private Game world; //game
    private static int soundRadius = 7; //sound of opening radius
    
    /**
     * constructor will call super constructor
     */
    public Doors(int x, int y, char c, boolean[] connections, Game world) {
        super(x, y, c, connections, true, false);
        this.world = world;
        doorOpen = false;
        openTimer = 0;
    }
    
    /**
     * opens door, plays sound
     */
    public void open() {
        doorOpen = true;
        image = doorOpened;
        setImage(image);
        isPassable = true;
        visibility = false;
        openTimer = 100;
        world.scene.sound.play(SoundHandler.door, false, true, new Point(getX() + 15, getY() + 15), new Point(world.player.getX() + 15, world.player.getY() + 15), soundRadius);
    }
    
    /**
     * closes door
     */
    public void close() {
        doorOpen = false;
        image = doorClosed;
        setImage(image);
        isPassable = false;
        visibility = true;
    }
    
    /**
     * if door is closed open door
     */
    public void interact() {
        if(!doorOpen) {
            open();
        }
    }
    
    /**
     * if door is open run timer to close door if nothing is blocking it
     */
    public void act() {
        if(doorOpen) {
            if(checkBlocked()) {
                openTimer --;
                if(openTimer <= 0) {
                    close();
                }
            }
        }
    }
    
    /**
     * returns if player is in the way of the door
     */
    private boolean checkBlocked() {
        int x = world.player.getX()+15; int y = world.player.getY()+15;
        int xIndex = (int)(x-10) / 30;
        int yIndex = (int)y / 30;
        boolean bool = ((((x-10) % 30 > 10) || world.grid[xIndex-1][yIndex] != this) &&
               (((x-10) % 30 < 20) || world.grid[xIndex+1][yIndex] != this) &&
               ((y % 30 > 10) || world.grid[xIndex][yIndex-1] != this) &&
               ((y % 30 < 20) || world.grid[xIndex][yIndex+1] != this) &&
               world.player.getGridObject() != this);
        return bool;
    }
}
