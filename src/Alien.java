import greenfoot.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Alien based of state machine model; can be either walking to a random position, idleing in that position, or hunting player once spotted
 * Will ray cast to player to check if it is spotted
 * will use Pathfinding to move to random position in map (walk state) or player position (spotted state)
 */
public class Alien extends Displayable {
                           
    private String stationary = "              "+"\n"+ 
                                "      @@      "+"\n"+ 
                                "   &@ @& @    "+"\n"+ 
                                "   %#&&%%%    "+"\n"+ 
                                "    &&@&@     "+"\n"+ 
                                "     @%@&&    "+"\n"+
                                "   @&&&&%&@   "+"\n"+
                                "    #    /@   "+"\n"+
                                "              "; //ascii image
    
    private String walk1 =      "         &,   "+"\n"+ 
                                "      @@ &    "+"\n"+ 
                                "   &  @& @    "+"\n"+ 
                                "   %#&&%%%    "+"\n"+ 
                                "    &&@&@     "+"\n"+ 
                                "     @%@&&@   "+"\n"+
                                "   @&&&&%&    "+"\n"+
                                "   @&         "+"\n"+
                                "    @         "; //ascii image for walk animation
    
    private String walk2 =      "   ,&         "+"\n"+ 
                                "    & @@      "+"\n"+ 
                                "   /@ @& #&   "+"\n"+ 
                                "   %#&&%%%    "+"\n"+ 
                                "    &&@&@     "+"\n"+ 
                                "  ,#@&@%@     "+"\n"+
                                "   @&%&&%&]   "+"\n"+
                                "        %@/   "+"\n"+
                                "         @    "; //ascii image for walk animation
    private Game world;
    
    public enum State { //states as mention above
        IDLE,
        WALK,
        SPOTTED,
    }
    private State state; //current state
    private Pathfinding path; //path to destination
    public ArrayList<Tile> destinations; //all possible random destinations (when in walk state)
    
    private int idleTimer; //timer for when in idle state
    private int moveTimer; //will move every 2 loops (this way it can run at 2.5 pixels per second as 3 was too fast and 2 was too slow relative to player speed)
    private int walkTimer; //will increment walk animation
    private int imageIndex; //current index of walk animation
    private final int walkSpeed; //speed it will walk at
    private final int runSpeed; //speed it will run at
    private int currentSpeed; //either walk or run speed based on state
    private boolean timer; //if timer has ended (permenant spotted state)
    private boolean spotted; //queue spotted state (otherwise causes errors as it could be half way betwen 2 Tile)
    private GreenfootImage[] imageArr = new GreenfootImage[4]; //array of animations
    
    /**
     * constructor initilises instance variables and calls superclass constructor
     * begins in idle state
     */
    public Alien(int x, int y, Game world) {
        super(x,y);
        this.world = world;
        state = State.IDLE;
        walkSpeed = 2;
        runSpeed = 5;
        idleTimer = 100;
        moveTimer = 0;
        walkTimer = 0;
        imageIndex = 0;
        destinations = new ArrayList<Tile>();
        GreenfootImage a = Text.createText(stationary, Color.RED, new Color(255,255,255,0));
        GreenfootImage b = Text.createText(walk1, Color.RED, new Color(255,255,255,0));
        GreenfootImage c = Text.createText(walk2, Color.RED, new Color(255,255,255,0));
        imageArr[0] = a; imageArr[1] = b; imageArr[2] = a; imageArr[3] = c; //order for animation
        setImage(a);
        timer = false;
        spotted = false;
    }
    
    /**
     * returns tile under alien
     */
    private Tile getGridObject() {
        int xIndex = (int)(getX()+5) / 30;
        int yIndex = (int)(getY()+15) / 30;
        return world.grid[xIndex][yIndex];
    }
    
    /**
     * returns shortest path to destination -vents deternimes whether the alien will move through vents (only if in spotted state) as to make vents safer
     */
    public void findPath(Tile destination, boolean vents) {
        path = new Pathfinding(getGridObject(), destination, world.grid, vents);
    }
    
    /**
     * handles movement
     * moves position, increments walk animation
     * if touching player ends game
     * if in next cell in path increment path and change state if spotted is queued
     * if at end of queue change state to idel unless perminent spotted is active (from no time in timer left)
     */
    private void move(int x, int y) {
        setLocation(getX() + x*currentSpeed, getY() + y*currentSpeed);
        walkTimer++;
        if(walkTimer >= 9) {
            imageIndex ++;
            if(imageIndex == 4) {
                imageIndex = 0;
            }
            walkTimer = 0;
            setImage(imageArr[imageIndex]);
        }
        
        Tile cell = getGridObject();
        if(cell == world.player.getGridObject()) {
            world.death();
            return;
        }
        if(cell == path.getPath() && Math.abs((getX()+5)%30 - 15) < 3 && Math.abs((getY()+15)%30 - 15) < 3) { //if cell is next in path and Alien is within 3 pixels x and y of the centre of the tile (otherwise alien enters the next cell but does not move to centre of it)
            path.incrementIndex();
            setLocation(cell.getX(), cell.getY()); //done to stop errors when Alien will move off diagonally but miss the centre of the next cell and thus move in one direction indeffinately
            if(cell instanceof Doors && cell.isPassable == false) {
                cell.interact();
            }
            if(spotted) {
                changeState(State.SPOTTED);
                return;
            }
            if(path.indexReached()) {
                if(timer) {
                    changeState(State.SPOTTED);
                }
                else {
                    changeState(State.IDLE);
                }
            }
        }
    }
    
    /**
     * called once to set all possible destinations for when in walk state -note vents are not included as to make them safer for player
     */
    public void setDestinations() {
        for(Tile[] row : world.grid) {
           for(Tile o : row) {
               if(o instanceof Passage) {
                   destinations.add(o);
                }
            } 
        }
    }
    
    /**
     * returns random destinations from destinations
     */
    private Tile findRandomDestination() {
        return destinations.get(new Random().nextInt(destinations.size()));
    }
    
    /**
     * called from game
     * idles if in idle state, moves if in spotted or walk state, checks if can see player if not already in spotted state
     */      
    public void act() {
        if(state == State.IDLE) { //if in idle state, check if spotted state queued, if so change state, othewise if idle timer is finished change state to walk
            idleTimer --;
            if(spotted) {
                changeState(State.SPOTTED);
            }
            else if(idleTimer == 0) {
                changeState(State.WALK);
            }
        }
        else { //every second loop call move if in spotted or walk state
            if(moveTimer == 0) {
                if(!path.indexReached()) {
                    move(path.getDirection()[0], path.getDirection()[1]);
                    moveTimer = 2;
                }
                else {
                    changeState(State.WALK);
                }
            }
        }
        if(state != State.SPOTTED) { //if not in spotted state ray cast to spot player, if it can play shriek sound and queue spotted
            if(checkSpotted()) {
                world.scene.sound.play(SoundHandler.spotted, false, false);
                spotted = true;
                currentSpeed = runSpeed;
                //changeState(State.SPOTTED);
            }
        }
        if(moveTimer != 0) {
            moveTimer --;
        }
    }
    
    /**
     * called from timer once finished; queues spotted and triggers perminant spotted
     */
    public void timerEnd() {
        timer = true;
        spotted = true;
        currentSpeed = runSpeed;
    }
    
    /**
     * will change current state, update speed based on state, create path to player if spotted, create path to random destination if walk, set idle timer if idle
     */
    public void changeState(State state) {
        this.state = state;
        switch(state) {
            case IDLE:
                idleTimer = 100;
                break;
            case WALK:
                currentSpeed = walkSpeed;
                //set pathfind to random position
                path = new Pathfinding(getGridObject(), findRandomDestination(), world.grid, false);
                while(path.indexReached()) { //loop used as sometimes the destination can be behind a locked door, thus we will create a new destination until a valid one comes
                    path = new Pathfinding(getGridObject(), findRandomDestination(), world.grid, false);
                }
                break;
            case SPOTTED:
                currentSpeed = runSpeed;
                path = new Pathfinding(getGridObject(), world.player.getGridObject(), world.grid, true);
                //if(path.indexReached()) {changeState(State.IDLE);}
                spotted = false;
                break;
        }
    }
    
    /**
     * will use ray casting to get the closest point of intercept of line of sight between the Alien and Player
     * If the distance to the point is closer than the distance to the Player then the Alien can see the player
     */
    public boolean checkSpotted() {
        Point a = new Point(getX()+15, getY()+15);
        Point p = new Point(world.player.getX()+15, world.player.getY()+15);
        RayCast ray = new RayCast(a, p, world.grid);
        Point intercept = ray.getIntercept();
        return RayCast.getDistance(a, intercept) > RayCast.getDistance(a, p);
    }
}
