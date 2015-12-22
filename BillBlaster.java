
/**
 * Write a description of class BillBlaster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BillBlaster extends Enemy_Spawner
{
    public BillBlaster(int x, int y)
    {
        super(x, y);
        shotDelay = 120;
        //shotDelay = 5;
    }
    public Enemy enemyToShoot()
    {
        BulletBill b = new BulletBill(xLocation/0x10000,yLocation/0x10000,false);
        return b;
    }
}
