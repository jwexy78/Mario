
/**
 * Write a description of class Object here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Game_Object
{
    public int xLocation, yLocation;
    public int xVelocity, yVelocity;
    public int xAcceleration, yAcceleration;
    public int width, height;
    public String graphicsFolder;
    public String graphicsFile;
    int graphicsXOffset;
    int graphicsYOffset;
    
    public boolean flipped;
    public boolean scenery;
    
    public boolean hidden;
    
    public int animCounter;
    public int animCycle;
    
    public int drawPriority;
    
    public int bufferDistance;
    public Game_Object()
    {
        
    }
    public Game_Object(int x, int y)
    {
        xLocation = x * 0x10000;
        yLocation = y * 0x10000;
        drawPriority = 1;
        bufferDistance = 0x100000;
    }
    public void applyPhysics()
    {
        xLocation += xVelocity;
        yLocation += yVelocity;
        xVelocity += xAcceleration;
        yVelocity += yAcceleration;
    }
    public abstract void tick();
    public String hexPrint(int n)
    {
        return (n < 0 ? "-" : "")+String.format("0x%8s", Integer.toHexString(Math.abs(n))).replace(' ', '0');
    }
    public String graphicsString()
    {
        return graphicsFolder +"_" +graphicsFile + (flipped ? "_f" : "");
    }
    public boolean intersects(Game_Object other)
    {
        if(other == null || other.scenery)
            return false;
        if(other.xLocation > xLocation + width)
            return false;
        if(other.xLocation + other.width < xLocation)
            return false;
        if(other.yLocation + other.height < yLocation)
            return false;
        if(yLocation + height < other.yLocation)
            return false;
        return true;
    }
    public int centerX()
    {
        return xLocation + width/2;
    }
    public Mario gameMario()
    {
        return Game.mainGameScreen.mario;
    }
    public void addSelfToGame()
    {
        Game.mainGameScreen.addToGame(this);
    }
    public void removeSelfFromGame()
    {
        Game.mainGameScreen.removeFromGame(this);
    }
}
