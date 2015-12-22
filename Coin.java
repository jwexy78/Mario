
/**
 * Write a description of class Mushroom here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Coin extends Collectable
{
    public boolean mini;
    public Coin(int x, int y)
    {
        super(x,y);
        xLocation += 0x03000;
        yLocation += 0x02000;
        width = 0x0A000;
        height = 0xC000;
        
        graphicsXOffset = 0x03000;
        graphicsYOffset = 0x02000;
        
        graphicsFile = "coin1";
        flipped = false;
        //yAcceleration = 0x01000;
        
        drawPriority = 3;
        
        animCycle = 1;
        animCounter = 0;
    }
    public void makeMini()
    {
        graphicsFile = "minicoin1";
        mini = true;
        
        xLocation -= 0x03000;
        yLocation -= 0x02000;
        
        width = 0x00000;
        height = 0x00000;
        
        graphicsXOffset = 0;
        graphicsYOffset = 0;
        
        
        
        
        yVelocity = -0x04800;
        yAcceleration = 0x00500;
    }
    public void tick()
    {
        animCounter = Screen.tickCounter / 8;
        animCycle = animCounter % 4 + 1;
        if(mini)
        {
            animCycle = animCounter % 4 + 1;
            graphicsFile = "minicoin" +animCycle;
            yLocation += yVelocity;
            yVelocity += yAcceleration;
            if(yVelocity > 0x03000)
            {
                removeSelfFromGame();
            }
        }
        else
        {
            animCycle = animCounter % 4 + 1;
            graphicsFile = "coin" +animCycle;
        }
    }
}
