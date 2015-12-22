
/**
 * Write a description of class Enemy_Spawner here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Enemy_Spawner extends Game_Object
{
    public int shotDelay;
    public boolean shootRight;
    public Enemy_Spawner(int x, int y)
    {
        super(x,y);
        bufferDistance = 0x40000;
        animCounter = -2;
    }
    public void tick()
    {
        animCounter++;
        if(animCounter == shotDelay || animCounter == -1)
        {
            Enemy e = enemyToShoot();
            addEnemyToGame(e);
            animCounter = 0;
        }
    }
    public abstract Enemy enemyToShoot();
    public void addEnemyToGame(Enemy e)
    {
        Game.mainGameScreen.addToGame(e);
    }
}
