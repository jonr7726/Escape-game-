import greenfoot.*;

/**
 * Terminal has a function when interacted with and a line (for opening locked doors)
 */
public class Terminal extends Tile {
        
    private String[] v0 = {"╔","╦","╗",
                           "╠","╬","╣",
                           "╙","⌨","╜"};
                                                 
    private boolean doorOpen = false;
    
    public int line; //line to correspoinding locked door (0 for none)
    private Function f; //function when interacted
    private Game world;
    private static char[] c = {'|', ']', '}'};
    
    /**
     * constuctor creates image in alternative way (assuming it will be surrounded by 3 walls adn will simply rotate)
     * sets instance variables and calls super constructor
     */
    public Terminal(int x, int y, boolean[] connections, int line, Game world, Function f) {
        super(x, y, c[line], connections, false, false);
        this.line = line;
        this.f = f;
        this.world = world;
        
        Color back;
        if(line == 1) {back=new Color(100,200,200);}
        else if(line == 2) {back=new Color(75,0,130);}
        else {back=new Color(100,160,100);}
        
        String text = "";
        for(String letter : v0) {
           text += letter;
        }
        image = Text.createText(buildText(text), Color.WHITE, back);
        if(!connections[0]) {
           image.rotate(180);
        }
        else if(!connections[1]) {
           image.rotate(-90);
        }
        else if(!connections[3]) {
           image.rotate(90);
        }
        else if(connections[2]) {
           image.rotate(-90);
        }
        setImage(image);
    }
    
    /**
     * will call function
     */
    public void interact() {
        if(!doorOpen) {
            f.function(world, line);
            doorOpen = true;
        }
    }
}
