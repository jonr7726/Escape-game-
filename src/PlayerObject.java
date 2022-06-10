import greenfoot.*;

/**
 * Player tile to be used in map editor and for animation in how to play
 * has animation feature with move animation copied from player class
 * has no other function
 */
public class PlayerObject extends Tile {
    
    private String stationary = "         "+"\n"+ 
                                " ,,      "+"\n"+ 
                                "@(##),/(   "+"\n"+ 
                                " .,,,.,,/"+"\n"+ 
                                "         ";
                            
    private String walk1 =      "  ,.   .( "+"\n"+                                           
                                "  ,,    . "+"\n"+        
                                "  ,(##),(   "+"\n"+        
                                " ,.,/,.,  "+"\n"+         
                                "@   ..    "; 
                              
    private String walk2 =      "      ,,  "+"\n"+                                            
                                " @,  ,*.  "+"\n"+        
                                " ,,(##),    "+"\n"+       
                                "  ..,.,,  "+"\n"+    
                                " ...   // ";
                                
    private int walkTimer; //delay in animation                       
    private int imageIndex; //index in immage array
    private GreenfootImage[] imageArr = new GreenfootImage[4]; //walk animation array
    
    /**
     * constructor sets image array and rotates images accordingly
     */
    public PlayerObject(int x, int y) {
        super(x, y, 'o', MapEditor.connections, false, false);
        walkTimer = 0;
        imageIndex = 0;
        GreenfootImage a = Text.createText(stationary, Color.GREEN, new Color(255,255,255,0));
        GreenfootImage b = Text.createText(walk1, Color.GREEN, new Color(255,255,255,0));
        GreenfootImage c = Text.createText(walk2, Color.GREEN, new Color(255,255,255,0));
        a.rotate(90);
        b.rotate(90);
        c.rotate(90);
        imageArr[0] = a; imageArr[1] = b; imageArr[2] = a; imageArr[3] = c;
        setImage(a);
        setRotation(270);
    }
    
    /**
     * will run animation (if not called then animation will not run)
     */
    public void act() {
        walkTimer ++;
        if(walkTimer >= 15 || walkTimer <= -15) {
            imageIndex += (int)(walkTimer/Math.abs(walkTimer));
            if(imageIndex == 4) {
                imageIndex = 0;
            }
            else if(imageIndex == -1) {
                imageIndex = 3;
            }
            walkTimer = 0;
            setImage(imageArr[imageIndex]);
        }
    }
    
    /**
     * interact does noting
     */
    public void interact() {
        return;
    }
}
