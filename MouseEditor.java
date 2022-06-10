import greenfoot.*; 
import java.util.List;

/**
 * Special mouse monitor class for map editor
 * used as map editor is more dependent on the mouse than anywhere else in the code
 */
public class MouseEditor {
    
    private MapEditor world;
    private static char[] cycle = {'#', ' ', '=', '/', '+', '[', '{', ']', '}', '|', '!', 'a', 'o'}; //cycle of all tiles
    private int cycleIndex; //index in cycle
    private int cycleTimer; //prevents registaring multiple button presses for cycling tile selected
    private int nameTimer; //prevents registaring multiple button presses for creating name
    private boolean makeName; //whether choosing filename
    
    /**
     * sets instance variables
     */
    public MouseEditor(MapEditor world) {
        this.world = world;
        cycleIndex = 0;
        cycleTimer = 0;
        nameTimer = 10;
        makeName = false;
    }
    
    /**
     * if making name call that method, otherwise check set selected tile to mouse position,
     * if mouse ledt clicked set grid under to selected tile
     * if right click set selected tile to grid under
     * if middle click clear tile under
     * q and e cycle selected object
     * escape calls making file name
     */
    public void act() {
        if(makeName) {
            createName();
        }
        else {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if(mouse != null) {
                if(world.selected != null) {
                    world.selected.setLocation(mouse.getX()-15, mouse.getY()-15);
                }
                
                if(mouse.getButton() == 1 && world.selected != null) {
                    int x = (int)(mouse.getX()-10) / 30;
                    int y = (int)mouse.getY() / 30;
                    if(x < 26 && y < 20) {
                        world.updateWorld(world.selected.character(), x, y);
                    }
                }
                else if(mouse.getButton() == 3 && mouse.getX() >= 10) {
                    int x = (int)(mouse.getX()-10) / 30;
                    int y = (int)mouse.getY() / 30;
                    findSelected(world.gridString[x][y], mouse.getX()-15, mouse.getY()-15);
                }
                else if(mouse.getButton() == 2) {
                    int x = (int)(mouse.getX()-10) / 30;
                    int y = (int)mouse.getY() / 30;
                    world.updateWorld('_', x, y);
                }
                if(Greenfoot.isKeyDown("e") && cycleTimer == 0) {
                    cycleIndex++;
                    if(cycleIndex == 13) {cycleIndex = 0;}
                    int x = (int)(mouse.getX()-10) / 30;
                    int y = (int)mouse.getY() / 30;
                    findSelected(cycle[cycleIndex], mouse.getX()-15, mouse.getY()-15);
                    cycleTimer = 10;
                }
                else if(Greenfoot.isKeyDown("q") && cycleTimer == 0) {
                    cycleIndex--;
                    if(cycleIndex == -1) {cycleIndex = 12;}
                    int x = (int)(mouse.getX()-10) / 30;
                    int y = (int)mouse.getY() / 30;
                    findSelected(cycle[cycleIndex], mouse.getX()-15, mouse.getY()-15);
                    cycleTimer = 10;
                }
                else if(Greenfoot.isKeyDown("escape")) {
                    world.saveAndExit();
                    makeName = true;
                }
                if(cycleTimer != 0) {cycleTimer --;}
            }
        }
    }
    
    /**
     * changes seleced given symbol and mouse position
     */
    private void findSelected(char c, int mouseX, int mouseY) {
        switch(c) {
            case '#':
                changeSelected(new Wall(mouseX, mouseY, MapEditor.connections));
                break;
            case ' ':
                changeSelected(new Passage(mouseX, mouseY));
                break;
            case '_':
                changeSelected(new Blank(mouseX, mouseY));
                break;
            case '=':
                changeSelected(new Vent(mouseX, mouseY, MapEditor.connections));
                break;
            case '+':
                changeSelected(new VentDoor(mouseX, mouseY, MapEditor.doorConnections, null));
                break;
            case '/':
                changeSelected(new Door(mouseX, mouseY, MapEditor.doorConnections, null));
                break;
            case '!':
                changeSelected(new Lever(mouseX, mouseY, MapEditor.connections, null));
                break;
            case '[':
                changeSelected(new LockedDoor(mouseX, mouseY, MapEditor.doorConnections, 1, null));
                break;
            case '{':
                changeSelected(new LockedDoor(mouseX, mouseY, MapEditor.doorConnections, 2, null));
                break;
            case ']':
                changeSelected(new Terminal(mouseX, mouseY, MapEditor.terminalConnections, 1, null, (Game game, int line) -> {return;}));
                break;
            case '}':
                changeSelected(new Terminal(mouseX, mouseY, MapEditor.terminalConnections, 2, null, (Game game, int line) -> {return;}));
                break;
            case '|':
                changeSelected(new Terminal(mouseX, mouseY, MapEditor.terminalConnections, 0, null, (Game game, int line) -> {return;}));
                break;
            case 'o':
                changeSelected(new PlayerObject(mouseX, mouseY));
                break;
            case 'a':
                changeSelected(new AlienObject(mouseX, mouseY));
                break;
            default :
                changeSelected(new Blank(mouseX, mouseY));
        }
    }
    
    /**
     * changes selected given a tile
     */
    private void changeSelected(Tile selected) {
        world.removeActive(world.selected);
        world.selected = selected;
        world.addActive(world.selected);
    }
    
    /**
     * enter send to file,
     * letter will add to name
     * backspace will remove from name
     * excape returns to making map
     */
    public void createName() {
        if(Greenfoot.isKeyDown("enter")) {
            makeName = false;
            Grid.write(world.filename.getText().substring(10), "maps", world.data);
            world.scene.changeScene(new Menu(world.scene));
        }
        else if(nameTimer == 0) {
            if(Greenfoot.isKeyDown("backspace")) {
                world.filename.updateText(world.filename.getText().substring(0, world.filename.getText().length() - 1));
                nameTimer = 10;
            }
            else if(Greenfoot.isKeyDown("escape")) {
                makeName = false;
                nameTimer = 10;
                world.removeActive(world.filename);
                world.render();
                Loop.sleep(100);
                return;
            }
            else {
                String key = Greenfoot.getKey();
                if(key != null) { if(key.length() == 1) {
                    world.filename.updateText(world.filename.getText()+key);
                    nameTimer = 10;
                }}
            }
        }
        if(nameTimer != 0) {nameTimer --;}
    }
}
