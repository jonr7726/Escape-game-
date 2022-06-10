import greenfoot.*;

/**
 * Player is the user's avatar, has animation for walking, can run, has collision
 */
public class Player extends Displayable {
    
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
                                
    private Game world;
    private int walkTimer; //will delay animation
    private long interactTimer; //prevents revieving multiple buttons
    private int imageIndex; //position in image array
    private GreenfootImage[] imageArr = new GreenfootImage[4]; //image array for walk animation
    
    /**
     * constructor cretes image array for animation
     */
    public Player(int x, int y, Game world) {
        super(x, y);
        this.world = world;
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
        interactTimer = 50;
        setRotation(180);
        world.vision.setVision(x, y);
    }
    
    /**
     * act will update animation if necesary, get user input, increment interact timer
     */
    public void act() {
        getInput();
        interactTimer ++;
        if(walkTimer >= 9 || walkTimer <= -9) {
            imageIndex += (int)(walkTimer/Math.abs(walkTimer));
            imageIndex = imageIndex % 4;
                if(imageIndex == -1) {
                imageIndex = 3;
            }
            walkTimer = 0;
            setImage(imageArr[imageIndex]);
        }
    }
    
    /**
     * will handle user controls, e will interact with Tile infront of player, w,a,s,d move and hold shirft runs
     */
    private void getInput() {
        int num = 1;
        boolean move = false;
        if(Greenfoot.isKeyDown("shift")) {num = 2;}
        
        if(Greenfoot.isKeyDown("w")) {
            move = true;
            if(getRotation() != 270) {changeRotation(270);}
        }
        else if(Greenfoot.isKeyDown("s")) {
            move = true;
            if(getRotation() != 90) {changeRotation(90);}
        }
        else if(Greenfoot.isKeyDown("a")) {
            move = true;
            if(getRotation() != 180) {changeRotation(180);}
        }
        else if(Greenfoot.isKeyDown("d")) {
            move = true;
            if(getRotation() != 0) {changeRotation(0);}
        }
        
        if(move) {movePlayer(num);}
        
        
        if(interactTimer >= 50) {
            if(Greenfoot.isKeyDown("e")) {
                switch(getRotation()) {
                    case 0:
                        if((getX()+5) % 30 < 20) {
                            getGridObject(1, 0).interact();
                        }
                        break;
                    case 90:
                        if((getY()+15) % 30 < 20) {
                            getGridObject(0, 1).interact();
                        }
                        break;
                    case 180:
                        if((getX()+5) % 30 > 10) {
                            getGridObject(-1, 0).interact();
                        }
                        break;
                    case 270 :
                        if((getY()+15) % 30 > 10) {
                            getGridObject(0, -1).interact();
                        }
                        break;
                }
                interactTimer = 0;
            }
        }
    }
    
    /**
     * rotates player, resets walk timer adn index
     */
    private void changeRotation(int num) {
        setRotation(num);
        walkTimer = 0;
        imageIndex = 0;
        setImage(imageArr[imageIndex]);
    }
    
    /**
     * moves player forward given speed
     */
    private void movePlayer(int num) {
        switch(getRotation()) {
            case 0:
                if(checkSafe(getX()+num, getY())) {
                    setLocation(getX()+num, getY());
                }
                break;
            case 90:
                if(checkSafe(getX(), getY()+num)) {
                    setLocation(getX(), getY()+num);
                }
                break;
            case 180:
                if(checkSafe(getX()-num, getY())) {
                    setLocation(getX()-num, getY());
                }
                break;
            case 270 :
                if(checkSafe(getX(), getY()-num)) {
                    setLocation(getX(), getY()-num);
                }
                break;
        }
        walkTimer += num/Math.abs(num);
    }   
    
    /**
     * returns grid Tile below player
     */
    public Tile getGridObject() {
        int xIndex = (int)(getX()+5) / 30;
        int yIndex = (int)(getY()+15) / 30;
        return world.grid[xIndex][yIndex];
    }
    
    /**
     * returns grid Tile below position
     */
    private Tile getGridObject(int x, int y) {
        int xIndex = (int)(getX()+5) / 30;
        int yIndex = (int)(getY()+15) / 30;
        return world.grid[xIndex+x][yIndex+y];
    }
    
    /**
     * checks if given location is safe to be in
     */
    private boolean checkSafe(int x, int y) {
        x += 15; y += 15;
        int xIndex = (int)(x-10) / 30;
        int yIndex = (int)y / 30;
        if((((x-10) % 30 > 10) || world.grid[xIndex-1][yIndex].isPassable) &&
           (((x-10) % 30 < 20) || world.grid[xIndex+1][yIndex].isPassable) &&
           ((y % 30 > 10) || world.grid[xIndex][yIndex-1].isPassable) &&
           ((y % 30 < 20) || world.grid[xIndex][yIndex+1].isPassable)) {
               if(world.grid[xIndex][yIndex] != getGridObject()) {
                   //world.vision.changeCandidates(xIndex, yIndex);
               }
               world.vision.setVision(x, y);
               return true;
        }
        else {
            return false;
        }
    }
}
