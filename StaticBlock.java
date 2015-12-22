
/**
 * Write a description of class Static_Block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StaticBlock extends Block
{
    boolean isBouncing;
    
    public boolean isBeingStoodUpon;
    
    public boolean canBounce;
    /**
     * Creates a block at the given glock coordinates
     */
    public StaticBlock(int x, int y)
    {
        super(x,y);
        width = 0x10000;
        height = 0x10000;
        
        graphicsFile = "clay";
        graphicsFolder = "block";
        
        drawPriority = 2;
        
        animCounter = 50;
        
        interactsWithEnemies = true;
    }
    public void tick()
    {
        
        if(animCounter < 14)
        {
            animCounter++;
        }
        else
            isBouncing = false;
        if(animCounter < 7)
            yLocation -= 0x01000;
        else if(animCounter < 13)
            yLocation += 0x01000;
    }
    public void getHitFromBelow()
    {
        if(isBouncing || !canBounce)
            return;
        animCounter = 0;
        isBouncing = true;
    }
}
