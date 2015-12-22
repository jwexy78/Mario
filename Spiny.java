
/**
 * Write a description of class Goomba here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Spiny extends Enemy
{
    public Spiny(int x, int y)
    {
        super(x,y);
        xLocation += 0x03000;
        yLocation += 0x05000;
        width = 0x0A000;
        height = 0x06000;
        graphicsXOffset = 0x03000;
        graphicsYOffset = 0x05000;
        
        floatHeight = 0x05000;
        graphicsSubfolder = "spiny";
        graphicsFile = "step1";
        flipped = true;
        //yAcceleration = 0x01000;
        
        xVelocity = -1 * Constants.enemy_walking_speed;
        
        canBeSteppedOn = false;
        
        reverses = true;
        bounces = true;
        //width = 
    }
    public void tickGraphics()
    {
        if(dead)
            return;
        animCounter++;
        if(animCounter >= 10)
        {
            animCycle++;
            if(animCycle > 1)
                animCycle = 0;
            if(animCycle == 0)
                graphicsFile = "step1";
            else
                graphicsFile = "step2";
            animCounter = 0;
        }
        if(xVelocity > 0)
            flipped = false;
        else
            flipped = true;
    }
    public void getSteppedOn()
    {
        steppedOn = true;
        die();
    }
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
}
