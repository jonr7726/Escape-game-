import java.util.ArrayList;
import java.util.Arrays;

/**
 * pathfinding ability for alien
 * creates a path for alien to follow and stores directions to get there
 * uses breadth first search algorithm to calculate path
 */
public class Pathfinding {

    private ArrayList<Tile> path = new ArrayList<Tile>(); //path of Tile
    private ArrayList<int[]> direction = new ArrayList<int[]>(); //directions between Tile
    private int index; //index in path list
    private Tile[][] grid; //map from game class
    private boolean vents; //whether the alien can cross vents (if it has spotted player)
    
    /**
     * constructor initilizes instance vairables
     * creates path
     */
    public Pathfinding(Tile start, Tile finish, Tile[][] grid, boolean vents) {
        this.grid = grid;
        this.vents = vents;
        pathfind(start, finish);
    }
    
    /**
     * returns the direction from one cell to an adjecent cell
     */
    private int[] findDirection(Tile objectFrom, Tile objectTo) {
        int[] objectDirection = {0,0};
        if(objectFrom.getY() < objectTo.getY()) {
            objectDirection[1] = 1;
        }
        else if(objectFrom.getY() > objectTo.getY()) {
            objectDirection[1] = -1;
        }
        if(objectFrom.getX() < objectTo.getX()) {
            objectDirection[0] = 1;
        }
        else if(objectFrom.getX() > objectTo.getX()) {
            objectDirection[0] = -1;
        }
        return objectDirection;
    }
    
    /**
     * returns if the cell at a position is valid for alien to cross
     * ie if it is a passage, door, locked door that is unlocked or vent (only if the alien has spotted player)
     */
    private Tile isValid(int x, int y) {
        if(x < grid.length && x >= 0 && y < grid[0].length && y >= 0) {
            if(grid[x][y] instanceof Passage || (grid[x][y] instanceof Doors && !(grid[x][y] instanceof LockedDoor)) || (grid[x][y] instanceof Vent && vents)) {
                return grid[x][y];
            }
        }
        return null;
    }
    
    /**
     * finds adjacent Tile -including diagonals -only if they can be reached (ie the 2 adjacent squares also being passed in are also clear)
     */
    private ArrayList<Tile> findNeighbours(Tile object) {
        ArrayList<Tile> neighbours = new ArrayList<Tile>();
        int[] oIndex  = getIndex(grid, object);
        ArrayList<Tile> adjacent = new ArrayList<Tile>();
        for(int p = -1; p <= 1; p +=2) { //adds each valid adjacent cell
            adjacent.add(isValid(oIndex[0] + p, oIndex[1]));
            adjacent.add(isValid(oIndex[0], oIndex[1] + p));
        }
        int ii = 0;
        int x = 1; int y = -1; //x and y represent offset (in Tiles) from object
        for(int i = 0; i <= 3; i++) {
            /**
             * in next lines i represents adjacent cell and moves clockwise around object
             * ii represents another adjacent cell which is offset 1 behind i (and also moves clockwise)
             * x and y represents the x/y offset of the cell diagonal from the object and between the 2 adjacent cells
             */
            ii++;
            x *= -1;
            if(i == 2) {y = 1; x*= -1;}
            else if(ii == 4) {ii = 0;}
            
            if(adjacent.get(i) != null) { //if adjacent i is valid
                neighbours.add(adjacent.get(i)); //add i to valid neighbours (note ii is not being added as i will cycle around all adjacents, adding ii will create duplicate neighbours)
                if(adjacent.get(ii) != null){ //if ii is also valid (and therefore diagonal is can be reached)
                    Tile diagonal = (isValid(oIndex[0] + x, oIndex[1] + y)); //diagonal
                    if(diagonal != null) { //if diagonal is valid
                        neighbours.add(diagonal); //add diagonal to valid neighbours
                    }
                }
            }
        }
        return neighbours;
    }
    
    /**
     * algorithm finds index of element within a 2D array
     */
    public static <T>int[] getIndex(T[][] array, T element) {
        for(int x = 0; x < array.length; x ++) {
            for(int y = 0; y < array[0].length; y ++) {
                if(array[x][y] == element) {
                    return new int[] {x,y};
                }
            }
        }
        return null;
    }
    
    /**
     * algorithm prints 2D array to console in ledgible way (used in development)
     */
    public static <T>void printArray(T[][] array) {
        for(int x = 0; x < array.length; x ++) {
            String s = "";
            for(int y = 0; y < array[0].length; y ++) {
                s += array[x][y] + " | ";
            }
            System.out.println(s);
        }
    }
    
    /**
     * algorithm prints ArrayList of intergers to console in ledgible way (used in development)
     */
    public static void printArray(ArrayList<int[]> array) {
        for(int x = 0; x < array.size(); x ++) {
            String s = "";
            for(int y = 0; y < 2; y ++) {
                s += array.get(x)[y] + " | ";
            }
            System.out.println(s);
        }
    }
 
    
    /**
     * creates path using breadth first search
     * (starting at one location and moving to every adjacent location until exit is reached then backtracking to find quickest path)
     */
    public void pathfind(Tile start, Tile finish) {
        Tile[][] previous = new Tile[grid.length][grid[0].length]; //refrence to parent tile (to backtrack)
        boolean[][] visited = new boolean[grid.length][grid[0].length]; //if each tile has been visited
        for(int x = 0; x < grid.length; x ++) {
            for(int y = 0; y < grid[0].length; y ++) {
                if(grid[x][y] == start) {
                    visited[x][y] = true;
                }
                else {
                    visited[x][y] = false;
                }
            }
        }
        Queue<Tile> q = new Queue<Tile>(start);
        
        while(!q.isEmpty()) { //while the grid is not fully sweaped
            Tile object = q.dequeue();
            ArrayList<Tile> neighbours = findNeighbours(object);
            
            for(Tile neighbour : neighbours) { //for each neighbouring Tile that has not been visited, add it to the queue, mark it as visited add its previous Tile to be the current Tile
                int[] nIndex = getIndex(grid, neighbour);
                if(!visited[nIndex[0]][nIndex[1]]) {
                    q.enqueue(neighbour);
                    visited[nIndex[0]][nIndex[1]] = true;
                    previous[nIndex[0]][nIndex[1]] = object;
                    if(neighbour == finish) { //if tile is destination, backtrack in the list to create a list of directions and tiles of how to get from the starting Tile to this Tile 
                        Tile backtrack = finish;
                        Tile prev = backtrack;
                        while(backtrack != start) { //backtrack and add to path
                            path.add(backtrack);
                            prev = backtrack;
                            int[] backIndex = getIndex(grid, backtrack);
                            backtrack = previous[backIndex[0]][backIndex[1]];
                            direction.add(findDirection(backtrack, prev));
                        }
                        index = path.size() - 1;
                        return;
                    }
                }
            }
        }
    }
    
    /**
     * returns path at the current index
     */
    public Tile getPath() {
        return path.get(index);
    }
    
    /**
     * returns direction at current index
     */
    public int[] getDirection() {
        return direction.get(index);
    }
    
    /**
     * increments index (negativly because we backtracked in the path algorithm)
     */
    public void incrementIndex() {
        index --;
    }
    
    /**
     * returns true if path is complete
     */
    public boolean indexReached() {
        return (index < 0 || !(path.size() > 0)); 
    }
}
