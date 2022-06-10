import greenfoot.*; 
import java.util.ArrayList;

/**
 * sub-renderer fr displaying score after game is ended
 * has animation + sound of scores appearing on screen 
 * will save as file the score of player and uses userInput to generate filename
 */
public class Score extends Renderer {
    
    private Mouse mouse; //mouse to handle buttons
    
    private int loopCount; //timer to offset animations
    private int loopIndex; //position in animation
    private ArrayList<Displayable> ladder; //list of diplayables of the score (to be displayed in increments as animation)
    
    private int total; //total score
    private boolean win; //whether the player won
    
    private int nameTimer; //timer for making name (as otherwise pressing the key once will register several times due to the speed of the loop)
    private boolean makeName; //whether the user is deciding a name
    private Text filename; //text of the username (Text type as it is displayed as displayable)
    
    /**
     * constructor will take in score data from game
     * calls superclass constructor
     * initilizes instance variables above
     * calculates scores and passes them into createScore()
     */
    public Score(SceneManager scene, boolean win, int levers, int doors, int time, int totalLevers) {
        super(800, 600, 1, 100, scene);
        scene.sound.stopAll();
        setBackgroundImage(new GreenfootImage("menu.jpg"));
        mouse = new Mouse(this);
        loopCount = 0;
        ladder = new ArrayList<Displayable>();
        this.win = win;
        filename = new Text(0, 270, "Username: ", 50, Color.WHITE, new Color(30,30,30));
        
        String text;
        int winBonus = 0;
        int timeBonus = 0;
        int doorScore = doors*1000;
        int leverScore = levers*750;
        int timeScore = (levers*15 + 30 -time)*10;
        if(win) {
            winBonus = 3000;
            //time bonus generated with the function f(x) = -5000/(60-15t) * x + 150000/(60-15t)+5000 (where x is the time taken and t is the total levers in map)
            //for this function, it will return: 0 (if time = total time aviable), 5000 (at 30 seconds into game), other values (proportinal to total time avialable in map with levers and how much time you used)
            int totalTime = (totalLevers * 15) + 30;
            double m = -5000/(30-totalTime);
            double b = (m*-30 + 5000);
            timeBonus = (int) (m*(timeScore/10) + b);
        }
        total = leverScore + doorScore + timeScore + winBonus + timeBonus;
        
        createScore(leverScore, doorScore, timeScore, winBonus, timeBonus);
    }
    
    /**
     * creates list of diaplayables to be added in animation sequence
     */
    private void createScore(int levers, int doors, int time, int winBonus, int timeBonus) {
        if(win) {
            addActive(new Text(10,10, "You Survived", 50, Color.GREEN, Renderer.transparent, new Color(0,120,0), 2));
        }
        else {
            addActive(new Text(10,10, "You Died", 50, Color.RED, Renderer.transparent, new Color(120,0,0), 2));
        }
        
        ladder.add(new Text(10,70, "Time Survived:", 30, Color.WHITE, Renderer.transparent));
        ladder.add(new Text(700,70, "" + time, 30, Color.WHITE, Renderer.transparent));
        
        ladder.add(new Text(10,100, "Levers Actived:", 30, Color.WHITE, Renderer.transparent));
        ladder.add(new Text(700,100, "" + levers, 30, Color.WHITE, Renderer.transparent));
        
        ladder.add(new Text(10,130, "Doors Unlocked:", 30, Color.WHITE, Renderer.transparent));
        ladder.add(new Text(700,130, "" + doors, 30, Color.WHITE, Renderer.transparent));
        
        ladder.add(new Text(10,160, "Win Bonus:", 30, Color.WHITE, Renderer.transparent));
        ladder.add(new Text(700,160, "" + winBonus, 30, Color.WHITE, Renderer.transparent));
        
        ladder.add(new Text(10,190, "Completion Time bonus:", 30, Color.WHITE, Renderer.transparent));
        ladder.add(new Text(700,190, "" + timeBonus, 30, Color.WHITE, Renderer.transparent));
        
        ladder.add(new Line(new Point(10,230), new Point(790,230), new Color(120,120,120)));
        ladder.add(new Text(10,230, "Total:", 30, Color.WHITE, Renderer.transparent));
        ladder.add(new Text(700,230, "" + total, 30, Color.WHITE, Renderer.transparent));
        
        addActive(new Button(620, 560, "Return to Menu",
        (w, b) -> { //will begin the process of saving score
            if(b == 1 && w instanceof Score) {
                ((Score)w).submitScore();
            }
        }));
    }
    
    /**
     * triggers create name sequence, displays file name, skips to end of animation if it has not completed
     */
    public void submitScore() {
        makeName = true;
        addActive(filename);
        Greenfoot.getKey(); //(called to reset the last key pressed so the filename will not begin with e)
        for(int i = loopIndex; i < ladder.size(); i ++) {
            addActive(ladder.get(i));
        }
    }
    
    /**
     * checks if the username exisits within score files
     * if will only update that score file if this score is higher
     * if username does not exist will create file
     */
    private void makeText(String winText) {
        boolean userInSystem = false;
        int userScore = 0;
        ArrayList<ArrayList<String>> scores = Grid.getScores();
        for(int i = 0; i < scores.size(); i ++) {
            if(scores.get(i).get(0).equals(filename.getText().substring(10))) {
                userInSystem = true;
                userScore = Integer.parseInt(scores.get(i).get(1));
                break;
            }
        }
        if(!userInSystem || total > userScore) {
            Grid.write(filename.getText().substring(10), "scores", new String[] {total+"", winText});
        }
    }
    
    /**
     * called when choosing username, if enter is pressed write score to file
     * otherwise if key is pressed add it to the username
     * backspace will remove last letter
     */
    public void createName() {
        if(Greenfoot.isKeyDown("enter")) {
            makeText(win ? "Survived" : "Died");
            Menu menu = new Menu(scene);
            menu.showLadder();
            menu.hideOptions();
            scene.changeScene(menu);
        }
        else if(nameTimer == 0) {
            if(Greenfoot.isKeyDown("backspace")) {
                filename.updateText(filename.getText().substring(0, filename.getText().length() - 1));
                nameTimer = 10;
            }
            else {
                String key = Greenfoot.getKey();
                if(key != null) { if(key.length() == 1) {
                    filename.updateText(filename.getText()+key);
                    nameTimer = 10;
                }}
            }
        }
        if(nameTimer != 0) {nameTimer --;}
    }

    /**
     * loop called by sceneManager
     * if creating username it will run createName()
     * otherwise it will continue the animation sequence and run mouse act
     */
    public void loop() {
        if(makeName) {
            createName();
        }
        else {
            if(loopIndex < ladder.size()) { //if animation not finished
                loopCount ++;
                if(loopCount == 100) { //every 100 loops
                    for(int i = 0; i < 2; i ++) { //add the next 2 displayables in list
                        addActive(ladder.get(loopIndex));
                        loopIndex++;
                    }
                    if(loopIndex == ladder.size() - 1) { //has 1 diaplayble left in animation display that (in place as last animation sequence displays 3 rather than 2 displayables)
                        addActive(ladder.get(loopIndex));
                        loopIndex++;
                    }
                    scene.sound.play(SoundHandler.thud, false, false); //play thud sound
                    loopCount = 0;
                }
            }
            mouse.act();
        }
    }
}
