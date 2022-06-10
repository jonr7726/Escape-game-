import java.io.*;
import java.util.*;

/*
 * wall: "#"
 * door: "/"
 * passage: " "
 * vent: "="
 * vent door: "+"
 * player: "o"
 * alien: "a"
 * nothing: "_"
 * lever: "!"
 * doorTerminal: "]"/"}"
 * podTerminal: "|"
 * podDoor: "["/"{"
 */

/**
 * Grid will decompile map data files and be used to create the layout of the map
 * will choose random file from map folder list and create list of elements based off that
 * Game will then create displayables from this
 */
public class Grid {
    
    private Element[][] grid; //map layout

    /**
     * Constructor finds random file within map folder and calles createGrid with it
     */
    public Grid(){
        final File folder = new File(System.getProperty("user.dir")+"\\data\\maps");
        File[] files = folder.listFiles();
        File file = files[new Random().nextInt(files.length)];
        grid = new Element[26][20];
        
        createGrid(read(file));
    }
    
    /**
     * takes in 2D array of chars and creates grid from that
     */
    private void createGrid(char[][] data) {
        for(int i = 0; i < 26; i++) {
            for(int ii = 0; ii < 20; ii++) {
                grid[i][ii] = new Element(data[i][ii]);
            }
        }
        for(int i = 0; i < 26; i++) {
            for(int ii = 0; ii < 20; ii++) {
                grid[i][ii].setConnections(getConnections(data,i,ii));
            }
        }
    }
    
    /**
     * will return element at specified index
     */
    public Element getElement(int index) {
        return grid[(int)(index/26)][(index%26)];
    }
    
    /**
     * will return element at specified index
     */
    public Element getElement(int x, int y) {
        return grid[x][y];
    }
    
    /**
     * returns array of booleans as to where the tile has connections
     * wall and door and lever all connect
     * vents and vent doors connect
     */
    public static boolean[] getConnections(char[][] data, int x, int y) {
        boolean[] connections = {false, false, false, false};
        char type = data[x][y];
        if(type == ' ' || type == 'o') {
            return connections;
        }
        char dat = data[x][y];
        if(dat == '+') {
            dat = '=';
        }
        else if(dat == '!') {
            dat = '#';
        }
        if(y<19){
            if(data[x][y+1] == dat || ((data[x][y+1] == '#' || data[x][y+1] == '!') && data[x][y] != '=') || (data[x][y+1] == '+' && data[x][y] == '=')) {
                connections[2] = true;
            }
        }
        if(y>0){
            if(data[x][y-1] == dat || ((data[x][y-1] == '#' || data[x][y-1] == '!') && data[x][y] != '=') || (data[x][y-1] == '+' && data[x][y] == '=')) {
                connections[0] = true;
            }
        }
        if(x<25){
            if(data[x+1][y] == dat || ((data[x+1][y] == '#' || data[x+1][y] == '!') && data[x][y] != '=') || (data[x+1][y] == '+' && data[x][y] == '=')) {
                connections[1] = true;
            }
        }
        if(x>0){
            if(data[x-1][y] == dat || ((data[x-1][y] == '#' || data[x-1][y] == '!') && data[x][y] != '=') || (data[x-1][y] == '+' && data[x][y] == '=')) {
                connections[3] = true;
            }
        }
        return connections;
    }
    
    /**
     * reads map file, returning a 2D array of chars of its contents
     */
    public static char[][] read(File file) {
        ArrayList<String> ret = new ArrayList<String>(); 
        try {       
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                ret.add(data);
            }
            scan.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        char[][] arrayret = new char[26][20]; 
        for(int i = 0; i < 26; i++) {
            for(int ii = 0; ii < 20; ii++) {
                arrayret[i][ii] = ret.get(ii).charAt(i);
            }
        }
        return arrayret;
    }
    
    /**
     * reads all score files returning a 2D arraylist of the score data
     */
    public static ArrayList<ArrayList<String>> getScores() { //{{name,score,won},}
        File scoreFolder = new File(System.getProperty("user.dir")+"\\data\\scores");
        File[] scoreFiles = scoreFolder.listFiles();
        ArrayList<ArrayList<String>> scores = new ArrayList<ArrayList<String>>();
        
        for(int f = 0; f < scoreFiles.length; f++) {
            File file = scoreFiles[f];
            ArrayList<String> ret = new ArrayList<String>(); 
            ret.add(file.getName().substring(0, file.getName().length()-4));
            try {       
                Scanner scan = new Scanner(file);
                while (scan.hasNextLine()) {
                    String data = scan.nextLine();
                    ret.add(data);
                }
                scan.close();
            } catch (IOException e) {
                System.out.println(e);
            }
            scores.add(ret);
        }
        return scores;
    }
    
    /**
     * generic write to file algorithm
     */
    public static void write(String filename, String location, String[] data) {
        try {
            FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\data\\"+location+"\\"+filename+".txt");
            for(String d : data) {
                writer.write(d + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
