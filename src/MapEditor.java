import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Map editor class indended mostly for development of maps
 */
public class MapEditor extends Renderer {
    
    public Tile[][] grid; //map layout
    public char[][] gridString; //string of map
    public MouseEditor mouse; //mouse monitor
    public Tile selected; //Tile holding
    
    public Text filename; //filename
    public String[] data; //data for file
    public static boolean[] connections = {false, false, false, false}; //static connections for best looking tiles
    public static boolean[] doorConnections = {false, true, false, true};
    public static boolean[] terminalConnections = {true, true, false, true};
    
    /**
     * constructor sets blank grid, calls create world, creates filename to be added later and sets instance variables
     */
    public MapEditor(SceneManager scene) {    
        super(800, 600, 1, 100, scene);

        GreenfootImage background = new GreenfootImage(800,600);
        background.setColor(Color.BLACK);
        background.fill();
        setBackgroundImage(background);
        mouse = new MouseEditor(this);
        filename = new Text(0, 0, "Filename: ", 50, Color.WHITE, new Color(30,30,30));
        
        data = new String[20];
        grid = new Tile[26][20];
        gridString = new char[26][20];
        for(int i = 0; i < 26; i ++) {
            for(int ii = 0; ii < 20; ii ++) {
                gridString[i][ii] = '_';
            }
        }
        selected = null;
        createWorld();
    }
    
    /**
     * loop will monitor mouse
     */
    public void loop() {
        mouse.act();
    }
    
    /**
     * will change tile at index relative to symbol
     */
    public void updateWorld(char c, int i, int ii) {
        gridString[i][ii] = c;
        switch(c) {
            case '#':
                changeTile(new Wall(i*30+10,ii*30,connections),i,ii);
                break;
            case ' ':
                changeTile(new Passage(i*30+10,ii*30),i,ii);
                break;
            case '_':
                changeTile(new Blank(i*30+10,ii*30),i,ii);
                break;
            case '=':
                changeTile(new Vent(i*30+10,ii*30,connections),i,ii);
                break;
            case '+':
                changeTile(new VentDoor(i*30+10,ii*30,doorConnections, null),i,ii);
                break;
            case '/':
                changeTile(new Door(i*30+10,ii*30,doorConnections, null),i,ii);
                break;
            case '!':
                changeTile(new Lever(i*30+10,ii*30,connections, null),i,ii);
                break;
            case '[':
                changeTile(new LockedDoor(i*30+10,ii*30,doorConnections, 1, null),i,ii);
                addActive(grid[i][ii]);
                break;
            case '{':
                changeTile(new LockedDoor(i*30+10,ii*30,doorConnections, 2, null),i,ii);
                break;
            case ']':
                changeTile(new Terminal(i*30+10,ii*30,terminalConnections, 1, null, (Game game, int line) -> {return;}),i,ii);
                break;
            case '}':
                changeTile(new Terminal(i*30+10,ii*30,terminalConnections, 2, null, (Game game, int line) -> {return;}),i,ii);
                break;
            case '|':
                changeTile(new Terminal(i*30+10,ii*30,terminalConnections, 0, null, (Game game, int line) -> {return;}),i,ii);
                break;
            case 'o':
                changeTile(new PlayerObject(i*30+10,ii*30),i,ii);
                break;
            case 'a':
                changeTile(new AlienObject(i*30+10,ii*30),i,ii);
                break;
            default :
                changeTile(new Blank(i*30+10,ii*30),i,ii);
        }
    }
    
    /**
     * creates ;ines for grid
     */
    public void createWorld() {
       for(int i = 0; i < 26; i++) {
           addStatic(new Line(new Point(i*30+10,0), new Point(i*30+10, 600), Color.WHITE));
            for(int ii = 0; ii < 20; ii++) {
                updateWorld(gridString[i][ii], i, ii);
                if(i == 0) {
                    addStatic(new Line(new Point(10,ii*30), new Point(790,ii*30), Color.WHITE));
                }
            }
        }
       addStatic(new Line(new Point(790,0), new Point(790, 600), Color.WHITE));
    }
    
    /**
     * will compile data for file
     */
    public void compileData() {
        data = new String[20];
        for(int i = 0; i < 20; i ++) {
            data[i] = "";
            for(int ii = 0; ii < 26; ii ++) {
                data[i] += gridString[ii][i];
            }
        }
    }
    
    /**
     * change tile at a specifiic location
     */
    private void changeTile(Tile o, int i, int ii) {
        removeActive(grid[i][ii]);
        grid[i][ii] = o;
        addActive(grid[i][ii]);
    }
    
    /**
     * compiles data and adds filename to be visible
     */
    public void saveAndExit() {
        compileData();
        addActive(filename);
    }
}
