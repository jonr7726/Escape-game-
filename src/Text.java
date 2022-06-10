import greenfoot.*; 

/**
 * Text can have a border, colour, background colour
 * includes static functions for creating ascii images
 */
public class Text extends Displayable {
    
    public static int textSize = 20;
    
    private boolean hasBorder;
    private int size;
    private int borderSize;
    private Color fore;
    private Color back;
    private Color border;
    private String string;

    /**
     * creates text with string, size, 2 colours
     */
    public Text(int x, int y, String string, int size, Color fore, Color back) {
        super(x, y);
        hasBorder = false;
        this.string = string;
        this.size = size;
        this.fore = fore;
        this.back = back;
        createImage();
    }
    
    /**
     * creates text but with second text attached with offset borderSize to make a show effect
     */
    public Text(int x, int y, String string, int size, Color fore, Color back, Color border, int borderSize) {
        super(x, y);
        hasBorder = true;
        this.string = string;
        this.size = size;
        this.fore = fore;
        this.back = back;
        this.border = border;
        this.borderSize = borderSize;
        createImage();
    }
    
    /**
     * creates a new image for the class with instance variables
     * border makes 2 texts and merges them together with offset
     */
    private void createImage() {
        if(hasBorder) {
            GreenfootImage text = new GreenfootImage(string, size, fore, back);
            GreenfootImage image = new GreenfootImage(text.getWidth()+borderSize, text.getHeight()+borderSize);
            image.drawImage(new GreenfootImage(string, size, border, back), borderSize, borderSize);
            image.drawImage(text, 0,0);
            setImage(image);
        }
        else {
            setImage(new GreenfootImage(string, size, fore, back));
        }
    }
    
    /**
     * update text changes the string and recreates text
     */
    public void updateText(String string) {
        this.string = string;
        createImage();
    }
    
    /**
     * returns string
     */
    public String getText() {
        return string;
    }
    
    /**
     * creaets alein/player images given string, will scale down to 30,30
     */
    public static GreenfootImage createText(String string, Color fore, Color back) {
        GreenfootImage size = new GreenfootImage(string, 100, fore, back);
        GreenfootImage image = new GreenfootImage(size.getWidth(), size.getHeight());
        image.setFont(new Font("monoscape", 100));
        image.setColor(back);
        image.fill();
        image.setColor(fore);
        image.drawString(string, 0, 0);
        image.scale(30,30);
        return image;
    }
    
    /**
     * Creates ascii image with 3 sections (so that it spaces evenly)
     */
    public static GreenfootImage createText(String[] string, Color fore, Color back) {
        GreenfootImage image = new GreenfootImage(30,30);
        GreenfootImage section1 = new GreenfootImage(string[0], 100, fore, back);
        section1.scale(30,10);
        GreenfootImage section2 = new GreenfootImage(string[1], 100, fore, back);
        section2.scale(30,10);
        GreenfootImage section3 = new GreenfootImage(string[2], 100, fore, back);
        section3.scale(30,10);
        image.drawImage(section1, 0,0);
        image.drawImage(section2, 0,10);
        image.drawImage(section3, 0,20);
        return image;
    }
}
