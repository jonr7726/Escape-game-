import greenfoot.*;

/**
 * Alien tile to be used with map editor -has no function other than visual
 */
public class AlienObject extends Tile {
    
    private String stationary = "              "+"\n"+ 
                                "      @@      "+"\n"+ 
                                "   &@ @& @    "+"\n"+ 
                                "   %#&&%%%    "+"\n"+ 
                                "    &&@&@     "+"\n"+ 
                                "     @%@&&    "+"\n"+
                                "   @&&&&%&@   "+"\n"+
                                "    #    /@   "+"\n"+
                                "              ";

    /**
     * constrcutor sets image and calls super class constructor
     */
    public AlienObject(int x, int y) {
        super(x, y, 'a', MapEditor.connections, false, false);
        GreenfootImage a = Text.createText(stationary, Color.RED, new Color(255,255,255,0));
        a.rotate(90);
        setImage(a);
        setRotation(180);
    }
    
    /**
     * no function
     */
    public void interact() {
        return;
    }
}
