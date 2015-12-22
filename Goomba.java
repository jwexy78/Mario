
/**
 * Write a description of class Goomba here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Goomba extends Enemy
{
    public Goomba(int x, int y)
    {
        super(x,y);
        xLocation += 0x03000;
        yLocation += 0x05000;
        width = 0x0A000;
        height = 0x06000;
        
        graphicsXOffset = 0x03000;
        graphicsYOffset = 0x05000;
        
        floatHeight = 0x05000;
        
        graphicsSubfolder = "goomba";
        graphicsFile = "step";
        flipped = false;
        
        
        
        xVelocity = -1 * Constants.enemy_walking_speed;
        
        canBeSteppedOn = true;
        
        reverses = true;
        bounces = true;
        //width = 
        
        patrols = false;
    }
    public void tickGraphics()
    {
        if(dead)
            return;
        animCounter++;
        if(animCounter >= 10)
        {
            flipped = !flipped;
            animCounter = 0;
        }
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
        if(steppedOn)
            graphicsFile = "squish";
    }
}
