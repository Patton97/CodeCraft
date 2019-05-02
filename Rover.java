import java.awt.Rectangle;
import java.awt.Point;

public class Rover 
{
    public static Rover roverSingleton;
    public boolean finished = false;
    
    private Point projectedPos = new Point(0,0); //Used for saftey checks, never sent to level.
    
    /**
     * Factory method to get the rover singleton object.
     */
    public static Rover getRover()
    {
        if(roverSingleton == null) 
        {
            roverSingleton = new Rover();
        }
        return roverSingleton;
    }
    
    /* * * * * MOVEMENT FUNCTIONS * * * * */
    /**
     * Add new frame in which rover moves up
     */ 
    protected void moveUp()
    {
        Level.getLevel().addFrame(new Point(0,-1));
        getRover().projectedPos.y -= 1;
    }
    /**
     * Add new frame in which rover moves down
     */ 
    protected void moveDown()
    {
        Level.getLevel().addFrame(new Point(0,1));
        getRover().projectedPos.y += 1;
    }
    /**
     * Add new frame in which rover moves right
     */ 
    protected void moveRight()
    {
        Level.getLevel().addFrame(new Point(1,0));
        getRover().projectedPos.x += 1;
    }     
    /**
     * Add new frame in which rover moves left
     */    
    protected void moveLeft()
    {
        Level.getLevel().addFrame(new Point(-1,0));
        getRover().projectedPos.x -= 1;
    }
    
    /* * * * * SAFETY CHECK * * * * */
    /**
     * Checks if specified tile is safe to move into. 
     * Usually invoked via one of the directional referrer methods
     * 
     * @param x x-coord of tile being evaluated
     * @param y y-coord of tile being evaluated
     * 
     * @return boolean Is the specified tile safe? (True if safe, False if unsafe)
     */
    protected boolean isSafe(int x, int y)
    {
        //Get rover's starting position
        Point roverPos = Level.getLevel().getRoverPos();
        
        roverPos.x += getRover().projectedPos.x;
        roverPos.y += getRover().projectedPos.y;
        
        //Check tile in scanned direction
        try
        {
            switch(Level.getLevel().getTile(roverPos.x + x, roverPos.y + y))
            {
                case 0: //Surface
                case 1: //Rover
                case 4: //Target
                    return true;
                case 2:  //Rock
                case 3:  //Mineral
                case 5: //Warning
                default: //Better to return false for unknown tile
                    return false;
            }
        }
        catch (java.lang.ArrayIndexOutOfBoundsException e)
        {
            //e.printStackTrace();
            return false;
        }
    }
    //Referrer functions
    protected boolean    upIsSafe(){return isSafe( 0,-1);}
    protected boolean  downIsSafe(){return isSafe( 0, 1);}
    protected boolean rightIsSafe(){return isSafe( 1, 0);}
    protected boolean  leftIsSafe(){return isSafe(-1, 0);}
    
    /**
     * Orders the rover to stop its movement. Until reset, no more frames 
     * of the simulation will be run
     */
    public void finished()
    {
        finished = true;
    }
    
    /**
     * Resets the rover, removing it from its finished state if necessary
     * and reinitialising the projected position to (0,0)
     */
    public void reset()
    {
        finished = false;
        projectedPos = new Point(0,0);
    }
}
