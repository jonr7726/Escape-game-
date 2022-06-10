import greenfoot.*; 

/**
 * Button is a text that implements hoverable and clickable
 * therefore it will change its image when it is hovered over to be white instead of green
 * and it will have a function that is activated once clicked
 * function is given with functional interface
 */
public class Button extends Text implements Hoverable, Clickable {
    
    private GreenfootImage image; //image when not hovered over
    private GreenfootImage hoverImage; //image when hovered over
    private boolean hovered; //whether it is hovered over
    private Clickable click; //holds fucntion for when clicked
    
    /**
     * constructor creates custom button given a position, string, size, colors for text and background for its hover, non hover and border, offset of border and a fucntion once clicked
     */
    public Button(int x, int y, String string, int size, Color fore, Color back, Color foreHover, Color backHover, Color border, int borderSize, Clickable click) {
        super(x, y, string, size, fore, back, border, borderSize);
        image = getImage();
        hoverImage = new GreenfootImage(string, size, foreHover, backHover);
        hovered = false;
        this.click = click;
    }
    /**
     * calls constructor above with inputted text position and function
     * default button is: gren with dark green border and is white when hovered; border offset is 2 pixels
     */
    public Button(int x, int y, String string, Clickable click) {
        this(x, y, string, 30, Color.GREEN, Renderer.transparent, Color.WHITE, Renderer.transparent, new Color(0,120,0), 2, click);
    }
    
    /**
     * called when hovered over
     * sets image to hoverImage
     */
    public void onHover() {
        setImage(hoverImage);
        hovered = true;
    }
    
    /**
     * called when mouse moved off
     * sets image to regular image
     */
    public void onMove() {
        setImage(image);
        hovered = false;
    }
    
    /**
     * returns if currently hovered over
     */
    public boolean hover() {
        return hovered;
    }
    
    /**
     * called when clicked
     * runs function
     */
    public void onClick(Renderer w, int b) {
        click.onClick(w, b);
    }
}
