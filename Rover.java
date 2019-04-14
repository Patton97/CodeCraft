import java.awt.Rectangle;
import java.awt.Point;

public class Rover 
{
    public static Rover roverSingleton;
    public boolean finished = false;
    public static Rover getRover()
    {
        if(roverSingleton == null) 
        {
            roverSingleton = new Rover();
        }
        return roverSingleton;
    }
    public void clearFrames()
    {
        //frames.clear();
    }
    public void moveUp()
    {
        Level.getLevel().addFrame(new Point(0,-1));        
    }
    public void moveDown()
    {
        Level.getLevel().addFrame(new Point(0,1));
    }
    public void moveRight()
    {
        Level.getLevel().addFrame(new Point(1,0));
    }
    public void moveLeft()
    {
        Level.getLevel().addFrame(new Point(-1,0));
    }
    public void finished()
    {
        finished = true;
    }
}
