
/**
 * Write a description of class CourseMaker here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.*;
public class CourseMaker
{
    public static char currentChar;
    public static boolean pipeDraw, cloudDraw, bushDraw;
    
    public static int drawX, drawY;
    public static Screen screen;
    public static JFrame frame;
    static char[][] lines;
    
    public static boolean done;
    public static void main(String[] args)
    {
        screen = new Screen();
        
        screen.enemies = new ArrayList<Enemy>();
        
        screen.backgroundColor = Color.WHITE;
        screen.drawGrid = true;
        screen.editingMode = true;
        
        screen.startLevel();
        
        frame = new JFrame();
        frame.getContentPane().add(screen);
        
        final Scanner reader = new Scanner(System.in);
        
        
        lines = new char[12][120];
        for(int i = 0; i < 12; i++)
        {
            for(int j = 0; j < 120; j++)
            {
                lines[i][j] = ' ';
                if(i == 11)
                    lines[i][j] = 'd';
            }
        }
        lines[9][117] = 'F';
        lines[10][117] = 'c';
        
        currentChar = 'c';
        
        
        
        
        frame.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    screen.cameraX -= Constants.block_rendering_width;
                    if(screen.cameraX < Constants.block_rendering_width * screen.leftMapBuffer)
                        screen.cameraX = Constants.block_rendering_width * screen.leftMapBuffer;
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    screen.cameraX += Constants.block_rendering_width;
                }
                else if(e.getKeyCode() == 80) // P - Print
                {
                    String whole = "";
                    for(int i = 0; i < 12; i++)
                    {
                        for(char c : lines[i])
                        {
                            whole += c;
                        }
                        whole += "\n";
                    }
                    System.out.print("\f");
                    System.out.print(whole);
                }
                else if(e.getKeyCode() == 83) //S - Save
                {
                    try
                    {
                        String line = JOptionPane.showInputDialog(null, "Map Name?");
                        File dir = new File("Maps/" +line +"/");
                        dir.mkdir();
                        
                        File rmf = new File("Maps/" +line +"/rmf.txt");
                        
                        if(!rmf.exists())
                            rmf.createNewFile();
                    
                        FileWriter fw = new FileWriter(rmf.getAbsoluteFile());
                        BufferedWriter out = new BufferedWriter(fw);
                        
                        
                        
                        String temp = "";
                        for(int i = 0; i < 12; i++)
                        {
                            for(char c : lines[i])
                            {
                                temp += c;
                            }
                            out.write(temp);
                            out.newLine();
                            temp = "";
                        }
                        out.close();
                        JOptionPane.showMessageDialog(null,"File Saved");
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null,"Error Saving File");
                    }
                }
                else if(e.getKeyCode() == 82) //R - Run
                {
                    try
                    {
                        File dir = new File("Maps/temp/");
                        dir.mkdir();
                        File rmf = new File("Maps/temp/rmf.txt");
                        
                        if(!rmf.exists())
                            rmf.createNewFile();
                    
                        FileWriter fw = new FileWriter(rmf.getAbsoluteFile());
                        BufferedWriter out = new BufferedWriter(fw);
                        
                        String temp = "";
                        for(int i = 0; i < 12; i++)
                        {
                            for(char c : lines[i])
                            {
                                temp += c;
                            }
                            out.write(temp);
                            out.newLine();
                            temp = "";
                        }
                        out.close();
                        
                        
                        Game.main(new String[] {"temp"});
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null,"Error Saving File");
                    }
                }
                else if(e.getKeyCode() == 79) // O - Open
                {
                    try
                    {
                        String line = JOptionPane.showInputDialog(null, "Open which Map?");
                        File rmf = new File("Maps/" +line +"/rmf.txt");
                        Scanner in = new Scanner(rmf);
                        int counter = 0;
                        while(in.hasNextLine())
                        {
                            String lne = in.nextLine();
                            int c2 = 0;
                            for(char c : lne.toCharArray())
                            {
                                if(counter < lines.length && c2 < lines[counter].length)
                                    lines[counter][c2] = c;
                                c2++;
                            }
                            counter++;
                        }
                        
                        String whole = "";
                        for(int i = 0; i < 12; i++)
                        {
                            for(char c : lines[i])
                            {
                                whole += c;
                            }
                            whole += "\n";
                        }
                        screen.generateMapFromString(whole);
                        
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null,"Error Opening File");
                    }
                }
                else if(e.getKeyCode() == 66) //b
                {
                    if(screen.backgroundColor == Color.BLACK)
                    {
                        screen.backgroundColor = new Color(70,149,250);
                    }
                    else
                    {
                        screen.backgroundColor = Color.BLACK;
                    }
                    
                }
                frame.repaint();
            }
            public void keyReleased(KeyEvent e)
            {
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
        
        screen.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                int x = (e.getX()+screen.cameraX)/Constants.block_rendering_width;//+screen.leftMapBuffer;
                int y = e.getY()/Constants.block_rendering_width;
                
                if(e.getX() > 12 * Constants.block_rendering_width)
                {
                    pipeDraw = false;
                    cloudDraw = false;
                    bushDraw = false;
                    
                    if(e.getX()/Constants.block_rendering_width == 12)
                    {
                        if(Screen.sceneryCharArray.length > y && Screen.sceneryCharArray[y] != '\0')
                        {
                            currentChar = Screen.sceneryCharArray[y];
                            Screen.selectedX = e.getX()/Constants.block_rendering_width;
                            Screen.selectedY = e.getY()/Constants.block_rendering_width;
                        }
                    }
                    else if(e.getX()/Constants.block_rendering_width == 13 || e.getX()/Constants.block_rendering_width == 14)
                    {
                        int loc = y + 12 * (e.getX()/Constants.block_rendering_width - 13);
                        if(Screen.blockCharArray.length > loc && Screen.blockCharArray[loc] != '\0')
                        {
                            currentChar = Screen.blockCharArray[loc];
                            Screen.selectedX = e.getX()/Constants.block_rendering_width;
                            Screen.selectedY = e.getY()/Constants.block_rendering_width;
                        }
                        
                    }
                    else if(e.getX()/Constants.block_rendering_width == 15)
                    {
                        if(y < Screen.enemyCharArray.length)
                        {
                            currentChar = Screen.enemyCharArray[y];
                            Screen.selectedX = e.getX()/Constants.block_rendering_width;
                            Screen.selectedY = e.getY()/Constants.block_rendering_width;
                        }
                    }
                    else if(e.getX()/Constants.block_rendering_width == 16)
                    {
                        if(y < 4)
                        {
                            currentChar = (""+y).charAt(0);
                            Screen.selectedX = e.getX()/Constants.block_rendering_width;
                            Screen.selectedY = e.getY()/Constants.block_rendering_width;
                        }
                        else if(y > 4 && y < 9)
                        {
                            currentChar = (""+y).charAt(0);
                            Screen.selectedX = e.getX()/Constants.block_rendering_width;
                            Screen.selectedY = e.getY()/Constants.block_rendering_width;
                        }
                    }
                    refreshScreen();
                    return;
                }
                
                if(lines[y][x-1] == 'F')
                    return;
                    
                    
                if(pipeDraw || cloudDraw || bushDraw)
                {
                    drawX = x-1;
                    drawY = y;
                    return;
                }
                
                
                if(lines[y][x-1] == ' ')
                    lines[y][x-1] = currentChar;
                else
                    lines[y][x-1] = ' ';
                
                refreshScreen();
            }
            public void mouseReleased(MouseEvent e)
            {
            }
        });
        screen.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int x = (e.getX()+screen.cameraX)/Constants.block_rendering_width;//+screen.leftMapBuffer;
                int y = e.getY()/Constants.block_rendering_width;
                
                if(e.getX() > 12 * Constants.block_rendering_width)
                {
                    return;
                }
                
                if(pipeDraw)
                {
                    for(int i = drawX; i < drawX + 2; i++)
                    {
                        for(int j = drawY; j < y+1; j++)
                        {
                            if(i == drawX && j == drawY)
                                lines[j][i] = '(';
                            else if(i == drawX+1 && j == drawY)
                                lines[j][i] = ')';
                            else if(i == drawX)
                            {
                                lines[j][i] = '[';
                            }
                            else
                            {
                                lines[j][i] = ']';
                            }
                        }
                    }
                }
                else if(cloudDraw)
                {
                    for(int i = Math.min(drawX,x-1); i < Math.max(drawX+1,x); i++)
                    {
                        if(i == Math.min(drawX,x-1))
                            lines[drawY][i] = '{';
                        else if(i == Math.max(drawX+1,x)-1)
                            lines[drawY][i] = '}';
                        else 
                            lines[drawY][i] = '=';
                    }
                }
                else if(bushDraw)
                {
                    for(int i = Math.min(drawX,x-1); i < Math.max(drawX+1,x); i++)
                    {
                        if(i == Math.min(drawX,x-1))
                            lines[drawY][i] = '<';
                        else if(i == Math.max(drawX+1,x)-1)
                            lines[drawY][i] = '>';
                        else 
                            lines[drawY][i] = '.';
                    }
                }
                else
                {
                    if(lines[y][x-1] != 'F')
                        lines[y][x-1] = currentChar;
                }
                
                refreshScreen();
            }
        });
        
        
        
        
        
        
        try
        {
            Thread.sleep(100);
        }
        catch(Exception e)
        {
        }
        
        
        frame.pack();
        frame.setVisible(true);
        frame.setSize(frame.getWidth() + 6 * Constants.block_rendering_width, frame.getHeight());
        
        done = true;
        refreshScreen();
    }
    
    private static void refreshScreen()
    {
        if(!done)
            return;
        
        String whole = "";
        for(int i = 0; i < 12; i++)
        {
            for(char c : lines[i])
            {
                whole += c;
            }
            whole += "\n";
        }
        screen.generateMapFromString(whole);
        frame.repaint();
    }
}
