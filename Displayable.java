import greenfoot.*;

/**
 * a replacement of Actor that is compatable with Renderer
 * does not include act method as to reduce unecesary loop time
 * instead, for subclasses that require act it will be called from the sub-renderer class
 */
public class Displayable {
    
    private int x; //x and y position (note the point x,y represents the top left or the image, whereas in Actor it is the centre of the image)
    private int y;
    private int rotation; //rotation (int degrees) (note 0' is North, wheras in Actor it is East)
    private GreenfootImage image; //image
    
    /**
     * calls constructor with blank image and 0,0 position
     */
    public Displayable() {
        this(0, 0, new GreenfootImage(1,1));
    }
    
    /**
     * calls constructor with blank image
     */
    public Displayable(int x, int y) {
        this(x, y, new GreenfootImage(1,1));
    }
    
    /**
     * sets location to given co-ordinates
     * sets image to given image
     * sets rotation to 0
     */
    public Displayable(int x, int y, GreenfootImage image) {
        setLocation(x, y);
        setImage(image);
        setRotation(0);
    }
    
    /**
     * called by Renderer once initilized
     */
    public void addedToWorld() {}
    
    /**
     * sets location of displayble
     */
    public void setLocation(int x, int y) {
        this.x = x; this.y = y;
    }
    
    /**
     * sets rotation of image
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
    
    /**
     * sets image of displayable
     */
    public void setImage(GreenfootImage image) {
        this.image = image;
    }
    
    /**
     * returns x position
     */
    public int getX() {
        return x;
    }
    
    /**
     * returns y position
     */
    public int getY() {
        return y;
    }
    
    /**
     * returns rotation
     */
    public int getRotation() {
        return rotation;
    }
    
    /**
     * rotates image to rotation set earlier (done as otherwise rotation is relative to displayables current rotation, not North)
     * returns rotated image
     */
    public GreenfootImage getImage() {
        GreenfootImage rotated = new GreenfootImage(image.getWidth(),image.getHeight());
        rotated.drawImage(image, 0, 0);
        rotated.rotate(rotation);
        return rotated;
    }
}
