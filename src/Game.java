import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

/**
 * sub-class of renderer, main game renderer
 */
public class Game extends Renderer {
    
    private Grid gridElements; //Grid instance to load map
    
    public Tile[][] grid; //2d array of map
    public Player player; //player
    public Alien alien; //alien
    public Vision vision; //player's vision
    public Timer timer; //timer
    
    private ArrayList<Doors> doors; //all doors (used for calling their act methods)
    private boolean paused; //if game is paused
    private int pausedTimer; //timer for pausing (as otherwise pressing the key once will register several times due to the speed of the loop)
    private Displayable[] pauseInterface; //list of displayables required for pause screen
    private Mouse mouse; //mouse to interact with buttons on pause screen
    
    //used for generating score:
    private int levers; //levers flicked
    private int totalLevers; //total levers in map
    private int unlockedDoors; //locked doors unlocked
    
    /**
     * constructor calls superclass constructor
     * stops all currently playing sounds
     * sets background
     * sets instance variables above
     * sets paint order -prioritizing timer, vision, alien and then player
     * plays abient music
     */
    public Game(SceneManager scene) {    
        super(800, 600, 1, 50, scene);
        scene.sound.stopAll();
        GreenfootImage background = new GreenfootImage(800,600);
        background.setColor(Color.BLACK);
        background.fill();
        setBackgroundImage(background);
        vision = new Vision();
        addActive(vision);
        createWorld();
        alien.setDestinations();
        timer = new Timer(0, 0, 50);
        addActive(timer);
        setOrder(Player.class, Alien.class, Vision.class, Timer.class);
        doors = new ArrayList<Doors>();
        for(int i = 0; i < 26; i++) {
            for(int ii = 0; ii < 20; ii++) {
                if(grid[i][ii] instanceof Doors) {
                    doors.add((Doors)grid[i][ii]);
                }
            }
        }
        paused = false;
        mouse = new Mouse(this);
        pausedTimer = 0;
        pauseInterface = new Displayable[3];
        GreenfootImage i = new GreenfootImage(800,600);
        i.setColor(new Color(0,0,0,50));
        i.fill();
        pauseInterface[0] = new Displayable(0,0,i);
        pauseInterface[1] = new Button(10, 10, "Return to Game",
        (w, b) -> {
            if(b == 1 && w instanceof Game) {
                ((Game)w).hidePause();
            }
        });
        pauseInterface[2] = new Button(10, 60, "Exit",
        (w, b) -> {
            if(b == 1 && w instanceof Game) {
                ((Game)w).scene.changeScene(new Menu(((Game)w).scene));
            }
        });
        scene.sound.play(SoundHandler.ambience, true, false);
    }
    
    /**
     * main game loop; called by sceneManager
     * calls player, alien, timer, and each doors act method if not paused
     * otherwise calls mouse act method
     * if escape is pressed toggle paused and diaplay of pause interface
     */
    public void loop() {
        //long time = System.currentTimeMillis();
        if(paused) {
            mouse.act();
        }
        else { 
            player.act();
            //System.out.println("Player: " + (System.currentTimeMillis() - time));
            //time = System.currentTimeMillis();
            alien.act();
            //System.out.println("Alien: " + (System.currentTimeMillis() - time));
            //time = System.currentTimeMillis();
            timer.act(this);
            //System.out.println("Timer: " + (System.currentTimeMillis() - time));
            //time = System.currentTimeMillis();
            for(Doors d : doors) {
                d.act();
            }
            //System.out.println("Doors: " + (System.currentTimeMillis() - time));
        }
        if(Greenfoot.isKeyDown("escape") && pausedTimer == 0) {
            pausedTimer = 5;
            if(paused) {
                hidePause();
            }
            else {showPause();}
        }
        else if(pausedTimer != 0) {
            pausedTimer --;
        }
    }
    
    private void showPause() { //displays pause interface and pauses game
        for(Displayable d : pauseInterface) {
            addActive(d);
        }
        removeActive(timer);
        paused = true;
    }
    private void hidePause() { //hides pause interface and resumes game
        for(Displayable d : pauseInterface) {
            removeActive(d);
        }
        addActive(timer);
        paused = false;
    }
    
    /**
     * creates world from Grid map
     */
    private void createWorld() {
        gridElements = new Grid();
        grid = new Tile[26][20];
        
        Function openDoor = (Game g, int line) -> { //function for terminals to open doors; incremnts doors game class, opens relative door and plays door opening sound; (made here to avoid repeated code)
            List<LockedDoor> doors = g.getActive(LockedDoor.class);
            for(LockedDoor d : doors) {
                if(d.line == line) {d.open();}
            }
            g.scene.sound.play(SoundHandler.lockedDoor, false, true);
            g.addDoor();
        };
        
        for(int i = 0; i < 26; i++) {
            for(int ii = 0; ii < 20; ii++) {
                switch(gridElements.getElement(i,ii).getType()) { //switch for each symbol of map and creates aporpirate Tile class
                    case '#':
                        grid[i][ii] = new Wall(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections());
                        addStatic(grid[i][ii]);
                        break;
                    case ' ':
                        grid[i][ii] = new Passage(i*30+10,ii*30);
                        addStatic(grid[i][ii]);
                        break;
                    case '_':
                        grid[i][ii] = new Blank(i*30+10,ii*30);
                        addStatic(grid[i][ii]);
                        break;
                    case '=':
                        grid[i][ii] = new Vent(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections());
                        addStatic(grid[i][ii]);
                        break;
                    case '+':
                        grid[i][ii] = new VentDoor(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections(), this);
                        addActive(grid[i][ii]);
                        break;
                    case '/':
                        grid[i][ii] = new Door(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections(), this);
                        addActive(grid[i][ii]);
                        break;
                    case '!':
                        grid[i][ii] = new Lever(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections(), this);
                        addActive(grid[i][ii]);
                        totalLevers ++;
                        break;
                    case '[':
                        grid[i][ii] = new LockedDoor(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections(), 1, this);
                        addActive(grid[i][ii]);
                        break;
                    case '{':
                        grid[i][ii] = new LockedDoor(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections(), 2, this);
                        addActive(grid[i][ii]);
                        break;
                    case ']':
                        grid[i][ii] = new Terminal(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections(), 1, this, openDoor);
                        addStatic(grid[i][ii]);
                        break;
                    case '}':
                        grid[i][ii] = new Terminal(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections(), 2, this, openDoor);
                        addStatic(grid[i][ii]);
                        break;
                    case '|':
                        grid[i][ii] = new Terminal(i*30+10,ii*30,gridElements.getElement(i,ii).getConnections(), 0, this, (Game g, int line) -> {g.won();});
                        addStatic(grid[i][ii]);
                        break;
                    case 'o':
                        grid[i][ii] = new Passage(i*30+10,ii*30);
                        addStatic(grid[i][ii]);
                        player = new Player(i*30+10, ii*30, this);
                        addActive(player);
                        break;
                    case 'a':
                        grid[i][ii] = new Passage(i*30+10,ii*30);
                        addStatic(grid[i][ii]);
                        alien = new Alien(i*30+10, ii*30, this);
                        addActive(alien);
                        break;
                    
                    default :
                        grid[i][ii] = new Blank(i*30+10,ii*30);
                        addStatic(grid[i][ii]);
                }
            }
        }
    }
    
    /**
     * for development only
     * removes all points
     */
    public void removePoints() {
        for(Point p : getActive(Point.class)) {
            removeActive(p);
        }
    }
    
    public void addLever() { //increments levers flicked
        levers++;
    }
    

    public void addDoor() { //increments unlocked doors opened
        unlockedDoors++;
    }
    
    /**
     * called once player has died; changes renderer to score
     */
    public void death() {
        scene.changeScene(new Score(scene, false, levers, unlockedDoors, timer.getTime(), totalLevers));
    }
    
    /**
     * called once player has escaped; changes renderer to score
     */
    public void won() {
        scene.changeScene(new Score(scene, true, levers, unlockedDoors, timer.getTime(), totalLevers));
    }
}
