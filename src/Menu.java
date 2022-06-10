import greenfoot.*; 
import java.util.ArrayList;

/**
 * sub-class of Renderer, will be the first sub-renderer created from sceneManager
 * contains lists of displayables that will be displayed, or not displayed depending on navigation of menu
 */
public class Menu extends Renderer {
    
    private Mouse mouse; //mouse to monitor when buttons are pressed/hovered over
    private ArrayList<Displayable> options; //title screen with options
    private ArrayList<Displayable> help; //how to play screen
    private ArrayList<Displayable> ladder; //leaderboard screen
    
    /**
     * constructor calls superclass constructor
     * stops all currently playing sounds
     * sets instance variables
     * calls createMenu
     */
    public Menu(SceneManager scene) {
        super(800, 600, 1, 100, scene);
        scene.sound.stopAll();
        setBackgroundImage(new GreenfootImage("menu.jpg"));
        mouse = new Mouse(this);
        options = new ArrayList<Displayable>();
        help = new ArrayList<Displayable>();
        ladder = new ArrayList<Displayable>();
        createMenu();
    }
    
    /**
     * called once initialized, starts title screen music
     */
    public void load() {
        scene.sound.play(SoundHandler.title, true, true, 75);
    }
    
    /**
     * creates lists of displaybles for each screen of the menu
     * makes use of functional interfaces with Button class to create custom functionality of buttons
     * manually does this to save creating button-sub-classes for each button function
     * shows options list (title screen options)
     */
    private void createMenu() {
        options.add(new Button(10, 360, "Play",
        (w, b) -> {
            if(b == 1 && w instanceof Menu) { //starts game -loading screen used as otherwise it looks as if the game is not responding to create user feedback
                ((Menu)w).hideOptions();
                ((Menu)w).addActive(new Text(10, 560, "Loading...", 30, Color.GREEN, new Color(0,0,0,0), new Color(0,120,0), 2));
                ((Menu)w).render();
                ((Menu)w).scene.changeScene(new Game(((Menu)w).scene));
            }
        }));
        options.add(new Button(10, 410, "How To Play",
        (w, b) -> {
            if(b == 1 && w instanceof Menu) { //shows how to play page
                ((Menu)w).hideOptions();
                ((Menu)w).showHelp();
            }
        }));
        options.add(new Button(10, 460, "Map Editor",
        (w, b) -> {
            if(b == 1 && w instanceof Menu) { //starts map editor -loading screen used as otherwise it looks as if the game is not responding to create user feedback
                ((Menu)w).hideOptions();
                ((Menu)w).addActive(new Text(10, 560, "Loading...", 30, Color.GREEN, new Color(0,0,0,0), new Color(0,120,0), 2));
                ((Menu)w).render();
                ((Menu)w).scene.changeScene(new MapEditor(((Menu)w).scene));
            }
        }));
        options.add(new Button(10, 510, "Leaderboard",
        (w, b) -> {
            if(b == 1 && w instanceof Menu) { //shows leaderboard page
                ((Menu)w).hideOptions();
                ((Menu)w).showLadder();
            }
        }));
        options.add(new Button(10, 560, "Exit",
        (w, b) -> {
            if(b == 1 && w instanceof Menu) { //ends excecution
                w.end();
            }
        }));
        
        help.add(new Button(730, 560, "back",
        (w2, b2) -> {
            if(b2 == 1 && w2 instanceof Menu) { //returns to titlescreen page
                ((Menu)w2).hideHelp();
                ((Menu)w2).showOptions();
            }
        }));
        help.add(new Text(10,10, "Movement: W A S D", 30, Color.WHITE, Renderer.transparent));
        help.add(new Text(10,60, "Interact: E", 30, Color.WHITE, Renderer.transparent));
        help.add(new Text(10,230, "(Map Editor)", 30, Color.WHITE, Renderer.transparent));
        help.add(new Line(new Point(235,0), new Point(235,340), Color.WHITE));
        help.add(new Line(new Point(0,50), new Point(800,50), Color.WHITE));
        help.add(new Line(new Point(0,220), new Point(800,220), Color.WHITE));
        help.add(new Line(new Point(0,340), new Point(800,340), Color.WHITE));
        help.add(new Passage(240, 10));
        help.add(new PlayerObject(240, 10));
        help.add(new Lever(240, 60,MapEditor.doorConnections, null));
        help.add(new Text(240,90, "adds 15 seconds", 15, Color.WHITE, Renderer.transparent));
        help.add(new Text(240,110, "to timer", 15, Color.WHITE, Renderer.transparent));
        help.add(new Door(350, 60, MapEditor.doorConnections, null));
        help.add(new VentDoor(385, 60, MapEditor.doorConnections, null));
        help.add(new Text(350,90, "opens doors", 15, Color.WHITE, Renderer.transparent));
        help.add(new Terminal(460, 60, MapEditor.terminalConnections, 0, null, null));
        help.add(new Text(460,90, "launches pod", 15, Color.WHITE, Renderer.transparent));
        help.add(new Text(460,110, "(wins game)", 15, Color.WHITE, Renderer.transparent));
        help.add(new Terminal(570, 60, MapEditor.terminalConnections, 1, null, null));
        help.add(new Terminal(605, 60, MapEditor.terminalConnections, 2, null, null));
        help.add(new Text(570,90, "opens matching", 15, Color.WHITE, Renderer.transparent));
        help.add(new Text(570,110, "door", 15, Color.WHITE, Renderer.transparent));
        help.add(new LockedDoor(570, 130, MapEditor.doorConnections, 1, null));
        help.add(new LockedDoor(605, 130, MapEditor.doorConnections, 2, null));
        help.add(new Text(238,170, "Find the pod and activate terminal to escape facility without being spotted by the", 20, Color.WHITE, Renderer.transparent));
        help.add(new Text(238,190, "Alien. Once the timer runs out it will sense your location.", 20, Color.WHITE, Renderer.transparent));
        help.add(new Text(238,230, "Q/E : cycles pallet", 20, Color.WHITE, Renderer.transparent));
        help.add(new Text(238,250, "left click : places object", 20, Color.WHITE, Renderer.transparent));
        help.add(new Text(238,270, "right click : switches pallet to object below", 20, Color.WHITE, Renderer.transparent));
        help.add(new Text(238,290, "middle click : clears object below", 20, Color.WHITE, Renderer.transparent));
        help.add(new Text(238,310, "escape : save and exit; type a file name and press 'enter'", 20, Color.WHITE, Renderer.transparent));
        
        ArrayList<ArrayList<String>> scores = Grid.getScores();
        int leaderSize = scores.size();
        if(leaderSize > 17) {leaderSize = 17;} //prevents leaderboard exceding page size
        for(int i = 0; i < leaderSize; i ++) { //orders leaderboard from highest to lowest score, sets displaybles to leaderboard screen of each score
            int high = 0;
            int highIndex = 0;
            for(int ii = 0; ii < scores.size(); ii ++) {
                if(Integer.parseInt(scores.get(ii).get(1)) > high) {
                    high = Integer.parseInt(scores.get(ii).get(1));
                    highIndex = ii;
                }
            }
            ladder.add(new Text(10,50+i*30, "User: "+scores.get(highIndex).get(0), 20, Color.WHITE, Renderer.transparent));
            ladder.add(new Text(600,50+i*30, scores.get(highIndex).get(2), 20, Color.WHITE, Renderer.transparent));
            ladder.add(new Text(700,50+i*30, "Score: " + scores.get(highIndex).get(1), 20, Color.WHITE, Renderer.transparent));
            ladder.add(new Line(new Point(10, 50+i*30+25), new Point(790, 50+i*30+25), Color.WHITE));
            scores.remove(highIndex);
        }
        ladder.add(new Text(300, 0, "Leaderboard:", 40, Color.GREEN, new Color(0,0,0,0), new Color(0,120,0), 2));
        ladder.add(new Line(new Point(10, 45), new Point(790, 45), Color.WHITE));
        ladder.add(new Button(730, 560, "back",
        (w2, b2) -> {
            if(b2 == 1 && w2 instanceof Menu) { //returns to titlepage screen
                ((Menu)w2).hideLadder();
                ((Menu)w2).showOptions();
            }
        }));
        
        showOptions();
    }
    
    public void showOptions() { //shows title page
        for(Displayable d : options) {
            addActive(d);
        }
    }

    public void hideOptions() { //hides title page
        for(Displayable d : options) {
            removeActive(d);
        }
    }
    
    public void showHelp() { //shows how to play page
        for(Displayable d : help) {
            addActive(d);
        }
    }
    
    public void hideHelp() { //hides how to play page
        for(Displayable d : help) {
            removeActive(d);
        }
    }
    
    public void showLadder() { //shows leaderboard page
        for(Displayable d : ladder) {
            addActive(d);
        }
    }
    
    public void hideLadder() { //hides leaderboard page
        for(Displayable d : ladder) {
            removeActive(d);
        }
    }
    
    /**
     * loop called by sceneManager, calls mouse act method and the player act method (for the player on the help page to move)
     */
    public void loop() {
        mouse.act();
        ((PlayerObject)help.get(9)).act();
    }
}
