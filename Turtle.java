
/**
 * Write a description of class Goomba here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Turtle extends Enemy
{
    public boolean isShell;
    public boolean wasJustHit;
    public Turtle(int x, int y)
    {
        super(x,y);
        xLocation += 0x01000;
        yLocation += 0x01000;
        width = 0x0E000;
        height = 0x0E000;
        
        graphicsXOffset = 0x01000;
        graphicsYOffset = 0x09000;
        
        floatHeight = 0x01000;
        
        
        
        graphicsSubfolder = "turtle";
        graphicsFile = "step1";
        flipped = false;
        //yAcceleration = 0x01000;
        
        xVelocity = -1 * Constants.enemy_walking_speed;
        yAcceleration = Constants.enemy_gravity;
        
        canBeSteppedOn = true;
        
        reverses = true;
        bounces = true;
        
        patrols = true;
        //width = 
    }
    public void tickGraphics()
    {
        if(dead)
            return;
        if(!isShell)
        {
            animCounter++;
            if(animCounter >= 10)
            {
                animCycle = (animCycle + 1) % 2;
                graphicsFile = "step" +(animCycle + 1);
                animCounter = 0;
            }
            if(xVelocity < 0)
                flipped = false;
            else
                flipped = true;
        }
        else
        {
            animCounter++;
            if(animCounter > 20)
                wasJustHit = false;
        }
    }
    public void getHit(Mario m)
    {
        if(!isShell)
            return;
        if(m.xLocation + m.width / 2 < xLocation + width / 2)
        {
            xVelocity = Constants.turtle_shell_speed;
        }
        else
        {
            xVelocity = -1 * Constants.turtle_shell_speed;
        }
        wasJustHit = true;
        killsEnemies = true;
        animCounter = 0;
    }
    public void getSteppedOn()
    {
        if(wasJustHit)
            return;
        if(!isShell)
        {
            isShell = true;
            graphicsFile = "shell";
            xVelocity = 0;
            patrols = false;
            wasJustHit = true;
            animCounter = 0;
            killsEnemies = false;
            reverses = true;
            bounces = false;
            bufferDistance = 0x100000;
        }
        else
        {
            if(xVelocity != 0)
            {
                xVelocity = 0;
                wasJustHit = true;
                animCounter = 0;
                killsEnemies = false;
                reverses = true;
            }
            else
            {
                reverses = false;
                Mario m = Game.mainGameScreen.mario;
                if(m.xLocation + m.width / 2 < xLocation + width / 2)
                {
                    xVelocity = Constants.turtle_shell_speed;
                }
                else
                {
                    xVelocity = -1 * Constants.turtle_shell_speed;
                }
                killsEnemies = true;
                wasJustHit = true;
                animCounter = 0;
            }
        }
    }
}
