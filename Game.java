import javax.swing.JFrame;
import java.awt.event.*;
public class Game
{
    public static Screen mainGameScreen;
    public static boolean controlsDisabled;
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        
        mainGameScreen = new Screen();
        if(args.length > -2)
        {
            mainGameScreen.currentMap = "run";
        }
        mainGameScreen.startLevel();
        
        frame.getContentPane().add(mainGameScreen);
        frame.pack();
        
        frame.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if(controlsDisabled)
                    return;
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    Screen.leftIsDown = true;
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    Screen.rightIsDown = true;
                }
                else if(e.getKeyCode() == 90)
                {
                    Screen.runIsDown = true;
                }
                else if(e.getKeyCode() == 88)
                {
                    Screen.jumpIsDown = true;
                    Screen.pressedJump = true;
                }
                else if(e.getKeyCode() == 66)
                {
                    mainGameScreen.build();
                }
                else if(e.getKeyCode() == 69)
                {
                    mainGameScreen.exterminate();
                }
                else if(e.getKeyCode() == 72)
                {
                    mainGameScreen.showHitboxes = !mainGameScreen.showHitboxes;
                }
            }
            public void keyReleased(KeyEvent e)
            {
                if(controlsDisabled)
                    return;
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    Screen.leftIsDown = false;
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    Screen.rightIsDown = false;
                }
                else if(e.getKeyCode() == 90)
                {
                    Screen.runIsDown = false;
                }
                else if(e.getKeyCode() == 88)
                {
                    Screen.jumpIsDown = false;
                }
            }
        });
        
        frame.show();
        
        while(!Screen.kill)
        {
            try
            {
                Thread.sleep(1000 / Constants.FPS);
            }
            catch(Exception wtf){}
            mainGameScreen.tick();
            frame.repaint();
        }
    }
}
