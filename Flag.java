
/**
 * Write a description of class Flag here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Flag extends Game_Object
{
    public int flagLoc;
    public Flag(int x, int y)
    {
        xLocation = x * 0x10000;
        yLocation = y * 0x10000;
        
        graphicsYOffset = 0x80000;
        
        graphicsFolder = "flag";
        graphicsFile = "flag";
    }
    public void tick(){}
}
