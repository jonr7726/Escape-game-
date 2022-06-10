import greenfoot.*;

/**
 * Lever creates image from textType method and also prints a lever over it that can move
 * interacting will increase timer and switch lever
 */
public class Lever extends Tile {
    
    private String[] v0 = {"┏","━","┓",
                           "┣","┿","┫",
                           "┗","━","┛"};
                        
    private String[] v1 = {"┣","┿","┫",
                           "┣","┿","┫",
                           "┣","┿","┫"};
                        
    private String[] v2 = {"┳","━","┳",
                           "┿","┿","┿",
                           "┻","━","┻"};
                        
    private String[] v3 = {"╋","━","╋",
                           "┣","┿","┫",
                           "╋","━","╋"};
    private String[][] textTypes = {v0,v1,v2,v3}; //text types explained in Tile class
    
    private String[] l = {" ","┇"," ",
                           " ","╧"," ",
                           " "," "," "};
    
    private Game world; //game
    private boolean activated; //whether lever is activated
    private GreenfootImage lever1; //unswitched image
    private GreenfootImage lever2; //switched image
    
    /**
     * constructor creates image with texxttypes and then draws lever over the top (cutting out sides to sclae correctly)
     */
    public Lever(int x, int y, boolean[] connections, Game world) {
        super(x, y, '!', connections, true, false);
        this.world = world;
        super.buildObject(textTypes, new Color(200,200,200), new Color(30,30,30));
        
        String text = "";
        for(String letter : l) {
           text += letter;
        }
        GreenfootImage lever = Text.createText(buildText(text), new Color(139,69,19), new Color(30,30,30));
        GreenfootImage leverTransition = new GreenfootImage(20,20);
        leverTransition.drawImage(lever,0,0);
        leverTransition.mirrorHorizontally();
        lever1 = new GreenfootImage(10,20);
        lever1.drawImage(leverTransition,0,0);
        lever2 = new GreenfootImage(lever1);
        lever2.rotate(180);
        GreenfootImage imageWithLever = new GreenfootImage(image);
        imageWithLever.drawImage(lever1, 10, 0);
        setImage(imageWithLever);
    }
    
    /**
     * interact will play sound, switch lever and add timer to timer (if not alreaady activated)
     */
    public void interact() {
        if(!activated) {
            GreenfootImage imageWithLever = new GreenfootImage(image);
            imageWithLever.drawImage(lever2, 10, 10);
            setImage(imageWithLever);
            world.timer.addTime(15);
            world.addLever();
            activated = true;
            world.scene.sound.play(SoundHandler.lever, false, true);
        }
    }
}
