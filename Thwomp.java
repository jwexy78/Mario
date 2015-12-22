
/**
 * Write a description of class Goomba here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Thwomp extends Enemy
{
    private int fallStage;
    public Thwomp(int x, int y)
    {
        super(x,y);
        xLocation += 0x05000;
        yLocation += 0x00000;
        width = 0x16000;
        height = 0x1D000;
        
        graphicsXOffset = 0x00000;
        graphicsYOffset = 0x00000;
        
        floatHeight = 0x00000;
        
        graphicsSubfolder = "thwomp";
        graphicsFile = "thwomp";
        
        flipped = false;
        
        
        
        xVelocity = 0;
        yAcceleration = 0;
        
        canBeSteppedOn = false;
        
        reverses = true;
        bounces = true;
        killsEnemies = true;
        //width = 
        
        patrols = false;
        
        fallStage = 0;
    }
    public void tickGraphics()
    {
        
    }
    public void tick()
    {
        if(fallStage == 0)
        {
            if(Math.abs(gameMario().centerX()- centerX()) < 0x26000)
            {
                yAcceleration = Constants.enemy_gravity;
                fallStage = 1;
            }
        }
        else if(fallStage == 1)
        {
            if(!falling)
                fallStage = 2;
            animCounter = 0;
            killsEnemies = true;
        }
        else if(fallStage == 2)
        {
            animCounter ++;
            killsEnemies = false;
            if(animCounter == 20)
            {
                fallStage = 3;
                yAcceleration = 0;
                yVelocity = -0x01000;
                animCounter = 0;
            }
        }
        else
        {
            if(yVelocity == 0)
                fallStage = 0;
        }
        super.tick();
    }
    public void getSteppedOn()
    {
        steppedOn = true;
        die();
    }
    public void die()
    {
        if(dead)
            return;
        super.die();
    }
}
