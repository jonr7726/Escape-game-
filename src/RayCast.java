/**
 * RayCast is used by alien to find the closest intercept of the line of sight between the alien and the player (to know if the alien can see the player)
 */
public class RayCast {
    
    private Tile[][] grid; //map layout
    private Point pointFrom; //point casting from
    private Point pointTo; //point casting to
    private Point intercept; //first intercept of obstical blocking line of sight between pointFrom to pointTo
    
    public RayCast(Point pointFrom, Point pointTo, Tile[][] grid) {
        this.pointFrom = pointFrom;
        this.pointTo = pointTo;
        this.grid = grid;
        intercept = cast();
    }
    
    /**
     * returns intercept
     */
    public Point getIntercept() {
        return intercept;
    }
    
    /**
     * returns ratio of distance between 2 points (ratio as i do not square root to save time, however this does not matter as i only need to compare distances)
     */
    public static double getDistance(Point a, Point b) {
        return (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y);
    }
    
    /**
     * returns gradient given 2 points (gradient is point x represents run, y represent rise)
     */
    public static Point getGradient(Point a, Point b) {
        return new Point((a.x - b.x),(a.y - b.y));
    }
    
    /**
     * searches 1 pixel in each direction of given point to see if the tile prevents visibility
     * returns true if line of sight is therefore blocked
     * (search in each direction as the point is a x or y intercept meaning it intersects either 2 or 4 Tile and each of these must be checked for accuracy)
     */
    public boolean findSquareBelow(int x, int y) {
        Tile[] squares = new Tile[4];
        if(x > 778 || y > 598 || x < 2 || y < 2) {return true;}
        for(int i = 0; i < 2; i ++) {
            squares[i*2] = grid[(int)((x+(2*i-1))-10) / 30][(int)y / 30];
            squares[(i*2)+1] = grid[(int)(x-10) / 30][(int)(y+(2*i-1)) / 30];
        }
        
        boolean wall = false;
        for(Tile o : squares) {
            if(o.visibility) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * ray cast to from one point to another
     * does this by finding each x and y intercept (being the intersection between 2 Tile) if this intercept is adjacent to a object that blocks visibility then save that point as the furthest x/y intercept
     * will then return whichever is closer (fartherst x intercept or fartherst y intercept)
     */
    public Point cast() {
        Point gradient = getGradient(pointFrom, pointTo);

        Point xInt = pointTo; //x intercept point
        Point yInt = pointTo; //y intercept point
        double dy = (double)(pointFrom.y)%30; //y distance from top left of current tile pointFrom is on
        double dx = (double)(pointFrom.x-10)%30; //x distance from top left of current tile pointFrom is on
        gradient.x = -gradient.x;
        double gradientVal = gradient.y/gradient.x;
        double yInterceptX; //x value of y intercept
        double xInterceptY; //y value of x intercept
        int xInterceptX = (int)pointFrom.x; //x value of x intercept
        int yInterceptY = (int)pointFrom.y; //y value of y intercept
        int tileStepX; //value that yInterceptX will increment by each intercept
        int tileStepY; //value that xInterceptY will increment by each intercept
        double xStep; //value that xInterceptX will increment by each intercept
        double yStep; //value that yInterceptY will increment by each intercept
        /**
         * following code sets values defined above based on what quadrant the ray is casting in
         * this is based on triganometry where in each case we will know one side and the ratio of the angle (which is equal to tan(θ))
         */
        if(gradient.x < 0) { //if ray is moving left
            tileStepX = -30;
            if(gradient.y > 0) { //Quadrant 2
                gradientVal *= -1;
                
                tileStepY = -30;
                xStep = -30/gradientVal;
                xInterceptX -= dy/gradientVal;
                xInterceptY = pointFrom.y - dy;
                
                tileStepX = -30;
                yStep = -(30*gradientVal);
                yInterceptY += -(dx*gradientVal);
                yInterceptX = pointFrom.x - dx;
            }
            else { //Quadrant 3
                dy = 30 - dy;
                
                tileStepY = 30;
                xStep = -30/gradientVal;
                xInterceptX -= dy/gradientVal;
                xInterceptY = pointFrom.y + dy;
                
                tileStepX = -30;
                yStep = 30*gradientVal;
                yInterceptY += dx*gradientVal;
                yInterceptX = pointFrom.x - dx;
            }
        }
        else { //if ray is moving right
            tileStepX = 30;
            if(gradient.y < 0) { //Quadrant 4
                dy = 30 - dy;
                dx = 30 - dx;
                gradientVal *= -1;
                
                tileStepY = 30;
                xStep = 30/gradientVal;
                xInterceptX += dy/gradientVal;
                xInterceptY = pointFrom.y + dy;
                
                tileStepX = 30;
                yStep = 30*gradientVal;
                yInterceptY += dx*gradientVal;
                yInterceptX = pointFrom.x + dx;
            }
            else { //Quadrant 1
                dx = 30 - dx;
                
                tileStepY = -30;
                xStep = 30/gradientVal;
                xInterceptX += dy/gradientVal;
                xInterceptY = pointFrom.y - dy;
                
                tileStepX = 30;
                yStep = -(30*gradientVal);
                yInterceptY += -(dx*gradientVal);
                yInterceptX = pointFrom.x + dx;
            }
        }
        //loops will now find farthest x/y intercept within line of sight
        while(true) { //x int
            if(findSquareBelow(xInterceptX, (int)Math.round(xInterceptY))) {
                xInt = new Point(xInterceptX, Math.round(xInterceptY));
                break;
            }
            xInterceptX += xStep;
            xInterceptY += tileStepY;
        }
        while(true) { //y int
            if(findSquareBelow((int)Math.round(yInterceptX), yInterceptY)) {
                yInt = new Point(Math.round(yInterceptX), yInterceptY);
                break;
            }
            yInterceptY += yStep;
            yInterceptX += tileStepX;
        }
        double xDistance = getDistance(pointFrom, xInt);
        double yDistance = getDistance(pointFrom, yInt);
        return xDistance < yDistance ? xInt : yInt; //return closest intercept
    }
}
