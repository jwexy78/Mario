
/**
 * Write a description of class BulletBill here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BulletBill extends Enemy
{
    public BulletBill(int x, int y, boolean movingRight)
    {
        super(x,y);
        
        xLocation += 0x03000;
        yLocation += 0x05000;
        width = 0x0A000;
        height = 0x06000;
        
        graphicsXOffset = 0x03000;
        graphicsYOffset = 0x05000;
        
        graphicsSubfolder = "bullet";
        graphicsFile = "bullet";
        
        
        flipped = movingRight;
        //yAcceleration = 0x01000;
        
        xVelocity = (movingRight ? Constants.bullet_speed : -1 * Constants.bullet_speed);
        yAcceleration = 0;
        
        doesntHitTiles = true;
        
        canBeSteppedOn = true;
        
        reverses = false;
        bounces = false;
        
        //width = 
    }
    public void getSteppedOn()
    {
        die();
    }
    public void die()
    {
        dead = true;
        animCounter = 0;
        yAcceleration = 0x00400;
        xVelocity = 0;
        graphicsFile += "_u";
    }
}
