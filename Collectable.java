
/**
 * Write a description of class Collectable here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Collectable extends Game_Object
{
    public static final int COIN = 1;
    public Collectable(int x, int y)
    {
        super(x,y);
        
        graphicsFolder = "Collectables";
    }
    public void tickPhysics()
    {
        //Add that acceleration to the velocity
        xVelocity += xAcceleration;
        yVelocity += yAcceleration;
        
        if(yVelocity > 0x04800)
        {
            yVelocity = 0x04000;
        }
        
        //Check for horizontal block intersections
        
        int gridX = (xLocation+width/2) / 0x10000;
        int gridY = (yLocation+height/2) / 0x10000;
        
        StaticBlock up = Screen.staticBlockMap[gridX][gridY-1];
        StaticBlock down = Screen.staticBlockMap[gridX][gridY+1];
        StaticBlock left = Screen.staticBlockMap[gridX-1][gridY];
        StaticBlock right = Screen.staticBlockMap[gridX+1][gridY];
        
        StaticBlock upRight = Screen.staticBlockMap[gridX+1][gridY-1];
        StaticBlock downRight = Screen.staticBlockMap[gridX+1][gridY+1];
        StaticBlock upLeft = Screen.staticBlockMap[gridX-1][gridY-1];
        StaticBlock downLeft = Screen.staticBlockMap[gridX-1][gridY+1];
        
        int oldX = xLocation;
        int oldY = yLocation;
        
        //If the velocity is not insignificant, add it to the location
        if(Math.abs(xVelocity) > Constants.minimum_walk_speed)
        {
            xLocation += xVelocity;
        }
        yLocation += yVelocity;
        
        int newX = xLocation;
        int newY = yLocation;
        
        if(intersects(right) && xVelocity >= 0)
        {
            xLocation = right.xLocation - width - 0x00100;
            xVelocity = -1 * xVelocity;
        }
        else if(intersects(left) && xVelocity <= 0)
        {
            xLocation = left.xLocation + left.width + 0x00100;
            xVelocity = -1 * xVelocity;
        }
        
        if(intersects(up) && yVelocity <= 0)
        {
            yLocation = up.yLocation + up.height;
            yVelocity = 0;
        }
        else if(intersects(upRight) && yVelocity <= 0)
        {
            yLocation = upRight.yLocation + upRight.height;
            yVelocity = 0;
        }
        else if(intersects(upLeft) && yVelocity <= 0)
        {
            yLocation = upLeft.yLocation + upLeft.height;
            yVelocity = 0;
        }
        else if(intersects(down) && yVelocity >= 0)
        {
            yLocation = down.yLocation - height;
            yVelocity = 0;
            if(down.isBouncing)
            {
                yVelocity = 0x10000;
            }
        }
        else if(intersects(downRight) && yVelocity >= 0)
        {
            yLocation = downRight.yLocation - height;
            yVelocity = 0;
            if(downRight.isBouncing)
            {
                yVelocity = 0x10000;
            }
        }
        else if(intersects(downLeft) && yVelocity >= 0)
        {
            yLocation = downLeft.yLocation - height;
            yVelocity = 0;
            if(downLeft.isBouncing)
            {
                yVelocity = 0x10000;
            }
        }
    }
}
