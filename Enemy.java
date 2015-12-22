
/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Enemy extends Game_Object
{
    public boolean canBeSteppedOn;
    
    public String graphicsSubfolder;
    
    
    public boolean falling;
    public boolean dead;
    
    public boolean patrols; //Will it fall off of a cliff
    
    public boolean reverses; //Should enemies "bounce" off of it
    public boolean bounces; //Should I bounce off of enemies
    
    public boolean killsEnemies;
    
    public boolean steppedOn;
    
    public boolean doesntHitTiles;
    
    public int floatHeight;
    public Enemy(int x, int y)
    {
        super(x,y);
        drawPriority = 3;
        graphicsFolder = "enemy";
        
        reverses = true;
        bounces = true;
        
        yAcceleration = Constants.enemy_gravity;
        
        bufferDistance = 0x60000;
    }
    public void tick()
    {
        if(dead)
        {
            tickDeadAnimation();
            return;
        }
        yLocation += floatHeight;
        tickPhysics();
        yLocation -= floatHeight;
        tickGraphics();
        Enemy e = checkEnemyIntersections();
        if(e != null)
        {
            contact(e);
        }
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
        
        int oldX = xLocation;
        int oldY = yLocation;
        
        //If the velocity is not insignificant, add it to the location
        if(Math.abs(xVelocity) > Constants.minimum_walk_speed)
        {
            xLocation += xVelocity;
        }
        if(yVelocity > Constants.enemy_max_falling_speed)
            yVelocity = Constants.enemy_max_falling_speed;
        yLocation += yVelocity;
        
        int newX = xLocation;
        int newY = yLocation;
        
        
        if(doesntHitTiles)
            return;
        
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
        
        
        boolean isMovingShell = (this instanceof Turtle) && ((Turtle)this).isShell && xVelocity != 0;
        
        
        falling = true;
        
        if(intersects(right) && xVelocity >= 0 && right.interactsWithEnemies)
        {
            xLocation = right.xLocation - width - 0x00100;
            xVelocity = -1 * xVelocity;
            if(isMovingShell)
                right.getHitFromBelow();
        }
        else if(intersects(left) && xVelocity <= 0 && left.interactsWithEnemies)
        {
            xLocation = left.xLocation + left.width + 0x00100;
            xVelocity = -1 * xVelocity;
            if(isMovingShell)
                left.getHitFromBelow();
        }
        
        boolean intU = intersects(up) && yVelocity <= 0 && up.interactsWithEnemies;
        boolean intUR = intersects(upRight) && yVelocity <= 0 && upRight.interactsWithEnemies;
        boolean intUL = intersects(upLeft) && yVelocity <= 0 && upLeft.interactsWithEnemies;
        
        boolean intD = intersects(down) && yVelocity >= 0 && down.interactsWithEnemies;
        boolean intDL = intersects(downLeft) && yVelocity >= 0 && downLeft.interactsWithEnemies;
        boolean intDR = intersects(downRight) && yVelocity >= 0 && downRight.interactsWithEnemies;
        
        if(intU)
        {
            yLocation = up.yLocation + up.height;
            yVelocity = 0;
        }
        else if(intUR)
        {
            yLocation = upRight.yLocation + upRight.height;
            yVelocity = 0;
        }
        else if(intUL)
        {
            yLocation = upLeft.yLocation + upLeft.height;
            yVelocity = 0;
        }
        else if(intD)
        {
            yLocation = down.yLocation - height;
            yVelocity = 0;
            falling = false;
            if(down.isBouncing)
            {
                yVelocity = 0x10000;
                die();
            }
        }
        else if(intDR)
        {
            yLocation = downRight.yLocation - height;
            yVelocity = 0;
            falling = false;
            if(downRight.isBouncing)
            {
                yVelocity = 0x10000;
                die();
            }
        }
        else if(intDL)
        {
            yLocation = downLeft.yLocation - height;
            yVelocity = 0;
            falling = false;
            if(downLeft.isBouncing)
            {
                yVelocity = 0x10000;
                die();
            }
        }
        
        if(patrols)
        {
            if(intD)
            {
                if(xVelocity > 0 && !intDR && xLocation + width > down.xLocation + down.width)
                {
                    xVelocity *= -1;
                }
                else if(xVelocity < 0 && !intDL && xLocation < down.xLocation)
                {
                    xVelocity *= -1;
                }
            }
        }
    }
    public abstract void getSteppedOn();
    
    
    
    public void die()
    {
        dead = true;
        animCounter = 0;
        if(!steppedOn)
        {
            flipped = false;
            graphicsFile = graphicsFile +"_u";
            xVelocity = 0x00400;
            yVelocity = -0x03000;
            yAcceleration = 0x00500;
        }
    }
    
    
    public void tickGraphics()
    {
        
    }
    public Enemy checkEnemyIntersections()
    {
        for(Enemy e : Screen.enemies)
        {
            if(e != this && e.intersects(this))
            {
                return e;
            }
        }
        return null;
    }
    public void contact(Enemy e)
    {
        if(!e.dead)
        {
            if(killsEnemies)
                e.die();
            if(e.killsEnemies)
                die();
        }
        if(e.reverses && bounces)
        {
            if(xLocation >= e.xLocation)
                xVelocity = Math.abs(xVelocity);
            else 
                xVelocity = -1 * Math.abs(xVelocity);
        }
    }
    public void tickDeadAnimation()
    {
        if(dead)
        {
            animCounter++;
            if(steppedOn)
            {
                if(animCounter > 30)
                {
                    hidden = true;
                    reverses = false;
                    Game.mainGameScreen.removeFromGame(this);
                }
            }
            else
            {
                xLocation += xAcceleration;
                yLocation += yVelocity;
                yVelocity += yAcceleration;
                if(animCounter > 100)
                    Game.mainGameScreen.removeFromGame(this);
            }
            return;
        }
    }
    
    
    public String graphicsString()
    {
        return graphicsFolder +"_" +graphicsSubfolder +"_" +graphicsFile + (flipped ? "_f" : "");
    }
}
