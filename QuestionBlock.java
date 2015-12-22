
/**
 * Write a description of class QuestionBlock here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class QuestionBlock extends StaticBlock
{
    private boolean isBouncing;
    private boolean hasBeenHit;
    public boolean invisible;
    public int type;
    /**
     * Creates a block at the given glock coordinates
     */
    public QuestionBlock(int x, int y)
    {
        super(x,y);
        width = 0x10000;
        height = 0x10000;
        
        graphicsFile = "questionBlock1";
        graphicsFolder = "block";
        
        drawPriority = 2;
        
        animCounter = 50;
        type = 0;
    }
    public void tick()
    {
        animCycle = Screen.tickCounter % 50;
        if(!invisible)
        {
            if(!hasBeenHit)
            {
                if(animCycle < 28)    
                    graphicsFile = "questionBlock1";
                else if(animCycle < 34)    
                    graphicsFile = "questionBlock2";
                else if(animCycle < 42)    
                    graphicsFile = "questionBlock3";
                else    
                    graphicsFile = "questionBlock2";
            }
            else
                graphicsFile = "questionBlockHit";
        }
        else
        {
            graphicsFile = "";
            hidden = true;
            interactsWithEnemies = false;
        }
        
        
        
        if(animCounter < 50)
        {
            animCounter++;
        }
        else
        {
            isBouncing = false;
        }
        if(animCounter < 7)
        {
            yLocation -= 0x01000;
        }
        else if(animCounter < 13)
        {
            yLocation += 0x01000;
            hasBeenHit = true;
        }
    }
    public void getHitFromBelow()
    {
        if(hasBeenHit || !canBounce)
            return;
        invisible = false;
        interactsWithEnemies = true;
        animCounter = 0;
        isBouncing = true;
        if(type == 0)
        {
            Coin c = new Coin(xLocation / 0x10000, yLocation / 0x10000 - 1);
            c.makeMini();
            Game.mainGameScreen.addToGame(c);
            gameMario().coins++;
        }
        else if(type == 1 || type == 2)
        {
            Mushroom m = new Mushroom(xLocation / 0x10000, yLocation / 0x10000 - 1);
            m.setType(type);
            Game.mainGameScreen.addToGame(m);
        }
        else if(type == 3)
        {
            Goomba m = new Goomba(xLocation / 0x10000, yLocation / 0x10000 - 1);
            Game.mainGameScreen.addToGame(m);
        }
        
    }
}
