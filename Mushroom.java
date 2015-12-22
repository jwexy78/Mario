
/**
 * Write a description of class Mushroom here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Mushroom extends Collectable
{
    public int type;
    public Mushroom(int x, int y)
    {
        super(x,y);
        xLocation += 0x03000;
        yLocation += 0x05000;
        width = 0x0A000;
        height = 0x06000;
        
        graphicsXOffset = 0x03000;
        graphicsYOffset = 0x05000;
        
        graphicsFile = "greenMushroom";
        flipped = false;
        //yAcceleration = 0x01000;
        
        type = 1;
        
        drawPriority = 3;
        
        xVelocity = 0x00A00;
    }
    public void setType(int t)
    {
        type = t;
        if(type == 1)
        {
            graphicsFile = "greenMushroom";
        }
        else if(type == 2)
        {
            graphicsFile = "blackMushroom";
        }
    }
    public void tick()
    {
        height += 0x05000;
        xAcceleration = 0;
        yAcceleration = 0x02800;
        tickPhysics();
        height -= 0x05000;
    }
}
