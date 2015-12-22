
/**
 * Write a description of class PiranhaPlant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PiranhaPlant extends Enemy
{
    boolean moves;
    boolean down;
    public PiranhaPlant(int x, int y)
    {
        super(x,y);
        xLocation += 0x0B000;
        yLocation += 0x03000;
        width = 0x0A000;
        height = 0x0A000;
        
        graphicsXOffset = 0x03000;
        graphicsYOffset = 0x09000;
        
        graphicsSubfolder = "piranhaplant";
        graphicsFile = "open";
        flipped = false;
        //yAcceleration = 0x01000;
        moves = true;
        
        drawPriority = 1;
        
        xVelocity = 0x00000;
        yAcceleration = 0;
        
        reverses = false;
        bounces = false;
        
        doesntHitTiles = true;
        
        
        canBeSteppedOn = false;
    }
    public void tick()
    {
        animCycle++;
        if(animCycle % 12 == 0)
        {
            if(graphicsFile.equals("open"))
            {
                graphicsFile = "closed";
            }
            else
            {
                graphicsFile = "open";
            }
        }
        
        if(down && Math.abs(Screen.mario.centerX() - centerX()) < 0x10000)
            return;
        
        animCounter++;
        
        if(moves)
        {
            if(animCounter < (0x18000 / Constants.piranha_speed) )
            {
                yVelocity = Constants.piranha_speed;
                applyPhysics();
            }
            else if(animCounter < 2 * (0x18000 / Constants.piranha_speed))
            {
                yVelocity = 0;
                applyPhysics();
                down = true;
            }
            else if(animCounter <3 *  (0x18000 / Constants.piranha_speed))
            {
                yVelocity = -1 * Constants.piranha_speed;
                applyPhysics();
                down = false;
            }
            else if(animCounter < 4 * (0x18000 / Constants.piranha_speed))
            {
                yVelocity = 0;
                applyPhysics();
            }
            else
            {
                animCounter = -1;
            }
        }
        
    }
    public void getSteppedOn(){}
    public void die()
    {
        Game.mainGameScreen.removeFromGame(this);
    }
}
