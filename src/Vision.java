import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Vision is an image that will sit over the map constricting the players vision
 * Consists of an image of a transparanet circle fading at the edges and black rectangles created based of the players position to fill the rest of the map
 */
public class Vision extends Displayable {  
    
    private GreenfootImage vision;
    private GreenfootImage image;
    private int height; //dimentions of image
    private int width;
    private int halfHeight; //(only used to save unnecesary calculations)
    private int halfWidth;
    
    /**
     * Constuctor sets instance variables and calles super-class constructor
     */
    public Vision() {
        super(0,0);
        vision = new GreenfootImage("vision.PNG");
        vision.scale(500,500); //originally 300,300 when i made this in photoshop however play-testing showed 500,500 is more appropriate dimentions
        height = vision.getHeight();
        width = vision.getWidth();
        halfHeight = (int)height/2;
        halfWidth = (int)width/2;
    }
    
    /**
     * sets vision; starts with blank image, draws the transparent circle (which is actuly a square as the corners are black)
     * then draws 4 rectangles to fill the rest of the map as black
     */
    public void setVision(int x, int y) {
        image = new GreenfootImage(800,600);
        image.drawImage(vision, x-halfWidth, y-halfHeight);
        image.setColor(Color.BLACK);
        image.fillRect(0, y-halfHeight, x-halfWidth, height); //left
        image.fillRect(x+halfWidth, y-halfHeight, 800-(x+halfWidth), height); //right
        image.fillRect(0, 0, 800, y-halfHeight); //up
        image.fillRect(0, y+halfHeight, 800, 600-(y+halfHeight)); //down
        setImage(image);
    }
}
