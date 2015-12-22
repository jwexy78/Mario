
/**
 * Abstract class Block - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Block extends Game_Object
{
    public boolean hasAnimation;
    public boolean kills;
    public boolean injures;
    public boolean interactsWithEnemies;
    public Block(int x, int y)
    {
        super(x,y);
    }
    public abstract void tick();
    public abstract void getHitFromBelow();
}
