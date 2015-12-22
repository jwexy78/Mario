import javax.swing.JComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.geom.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.*;
public class Screen extends JComponent
{
    public static boolean rightIsDown, leftIsDown;
    public static boolean runIsDown;
    public static boolean jumpIsDown;
    public static boolean pressedJump;
    
    public static boolean kill;
    
    
    public static char[] enemyCharArray;
    
    public static char[] sceneryCharArray;
    public static char[] blockCharArray;
    
    public static ArrayList<Game_Object> gameObjects;
    
    public static ArrayList<Enemy> enemies;
    
    public static  HashMap<String, BufferedImage> images;
    
    
    public static StaticBlock[][] staticBlockMap;
    private int mapWidth;
    private int mapHeight;
    
    public static int tickCounter;
    
    public static Mario mario;
    
    public boolean showHitboxes;
    
    public int cameraX, cameraY;
    private int screenWidth, screenHeight;
    private int horizontalBuffer;
    
    public String currentMap;
    
    public static HashMap<Character, String> blockGraphics;
    public static HashMap<Character, String> sceneryGraphics;
    
    public static Flag flag;
    
    public int leftMapBuffer;
    public int rightMapBuffer;
    public int topMapBuffer;
    public int bottomMapBuffer;
    
    private ArrayList<Game_Object> toAdd;
    private ArrayList<Game_Object> toRemove;
    
    
    public boolean drawGrid;
    public Color backgroundColor;
    
    
    public static boolean editingMode;
    
    public static int selectedX, selectedY;
    
    boolean drawImages;
    
    int world, level;
    
    public Screen()
    {
        mapWidth = 0;
        mapHeight = 0;
        
        leftMapBuffer = 1;
        rightMapBuffer = 1;
        topMapBuffer = 10;
        bottomMapBuffer = 2;
        
        screenWidth = 12;
        screenHeight = 12;
        
        horizontalBuffer = 5;
        
        cameraX = Constants.block_rendering_width * leftMapBuffer;
        cameraY = Constants.block_rendering_width * topMapBuffer;
        
        backgroundColor = new Color(70,149,250);
        
        this.setPreferredSize(new Dimension(Constants.block_rendering_width * screenWidth,Constants.block_rendering_width * screenHeight));
        
        toAdd = new ArrayList<Game_Object>();
        toRemove = new ArrayList<Game_Object>();
        
        world = 1;
        level = 1;
        currentMap = "" +world + "-" +level;
        
        try
        {
            loadGraphics();
        }
        catch(Exception e)
        {
            System.out.println("Error Loading Graphics: " +e.getStackTrace());
            kill = true;
        }
        
        enemyCharArray = new char[] {'g','s','p','T', '#'};
        sceneryCharArray = new char[12];
        blockCharArray = new char[24];
        
        
        selectedX = -100;
        
        showHitboxes = false;
        drawImages = true;
        
        //backgroundColor = Color.BLACK;
    }
    
    public void beatLevel()
    {
        level++;
        if(level > 3)
        {
            level = 1;
            world++;
        }
        currentMap = "" +world + "-" +level;
        startLevel();
    }
    
    public void addToGame(Game_Object e)
    {
        toAdd.add(e);
    }
    public void removeFromGame(Game_Object e)
    {
        toRemove.add(e);
    }
    public void startLevel()
    {
        try
        {
            loadMap(currentMap);
        }
        catch(Exception e)
        {
            kill = true;
            e.printStackTrace();
        }
    }
    
    public void build()
    {
        int x = mario.xLocation;
        int y = mario.yLocation;
        startLevel();
        mario.xLocation = x;
        mario.yLocation = y;
    }
    
    public void exterminate()
    {
        for(Enemy e : enemies)
        {
            e.die();
        }
    }
    
    public void generateMapFromString(String str)
    {
        staticBlockMap = new StaticBlock[mapWidth][mapHeight];
        gameObjects = new ArrayList<Game_Object>();
        enemies = new ArrayList<Enemy>();
        
        for(int i = 0; i < mapWidth; i++)
        {
            addStaticBlockToMap("",i,0);
            addStaticBlockToMap("spike",i,mapHeight-1, true);
        }
        
        for(int i = 0; i < mapHeight; i++)
        {
            addStaticBlockToMap("",0,i);
            addStaticBlockToMap("",mapWidth-1,i);
        }
        
        String[] strarr = str.split("\n");
        
        char[][] fullArr = new char[strarr.length][strarr[0].length()];
        
        for(int i = 0; i < strarr.length; i++)
        {
            fullArr[i] = strarr[i].toCharArray();
        }
        
        for(int lineNum = 0; lineNum < strarr.length; lineNum++)
        {
            if(lineNum >= mapHeight)
                break;
            char[] arr = strarr[lineNum].toCharArray();
            for(int i = 0; i < arr.length; i++)
            {
                if(i >= mapWidth-1)
                    break;
                if(blockGraphics.get(arr[i]) != null)
                {
                    StaticBlock block = addStaticBlockToMap(blockGraphics.get(arr[i]),i+leftMapBuffer,lineNum+topMapBuffer);
                    
                    boolean up = lineNum > 0 && fullArr[lineNum-1][i] != fullArr[lineNum][i];
                    boolean down = lineNum < fullArr.length - 1 && fullArr[lineNum+1][i] != fullArr[lineNum][i];
                    boolean left = i > 0 && fullArr[lineNum][i-1] != fullArr[lineNum][i];
                    boolean right = i < fullArr[0].length - 1 && fullArr[lineNum][i+1] != fullArr[lineNum][i];
                    
                    if(up && left && images.get("block_" +blockGraphics.get(arr[i]) + "_ul") != null)
                    {
                        block.graphicsFile += "_ul";
                    }
                    else if(up && right && images.get("block_" +blockGraphics.get(arr[i]) + "_ur") != null)
                    {
                        block.graphicsFile += "_ur";
                    }
                    else if(down && left && images.get("block_" +blockGraphics.get(arr[i]) + "_dl") != null)
                    {
                        block.graphicsFile += "_dl";
                    }
                    else if(down && right && images.get("block_" +blockGraphics.get(arr[i]) + "_dr") != null)
                    {
                        block.graphicsFile += "_dr";
                    }
                    else if(up && images.get("block_" +blockGraphics.get(arr[i]) + "_u") != null)
                    {
                        block.graphicsFile += "_u";
                    }
                    else if(down && images.get("block_" +blockGraphics.get(arr[i]) + "_d") != null)
                    {
                        block.graphicsFile += "_d";
                    }
                    else if(left && images.get("block_" +blockGraphics.get(arr[i]) + "_l") != null)
                    {
                        block.graphicsFile += "_l";
                    }
                    else if(right && images.get("block_" +blockGraphics.get(arr[i]) + "_r") != null)
                    {
                        block.graphicsFile += "_r";
                    }
                    
                    if(arr[i] == 'B')
                    {
                        BillBlaster b = new BillBlaster(i+leftMapBuffer,lineNum+topMapBuffer);
                        block.drawPriority = 4;
                        gameObjects.add(b);
                    }
                    else if(arr[i] == 'b')
                    {
                        block.canBounce = true;
                        gameObjects.add(block);
                    }
                    else if(arr[i] == 'n')
                    {
                        gameObjects.add(block);
                    }
                    else if(arr[i] == '+')
                    {
                        block.injures = true;
                    }
                }
                else if(sceneryGraphics.get(arr[i]) != null)
                {
                    StaticBlock block = addSceneryBlockToMap(sceneryGraphics.get(arr[i]),i+leftMapBuffer,lineNum+topMapBuffer);
                    
                    boolean up = lineNum > 0 && fullArr[lineNum-1][i] != fullArr[lineNum][i];
                    boolean down = lineNum < fullArr.length - 1 && fullArr[lineNum+1][i] != fullArr[lineNum][i];
                    boolean left = i > 0 && fullArr[lineNum][i-1] != fullArr[lineNum][i];
                    boolean right = i < fullArr[0].length - 1 && fullArr[lineNum][i+1] != fullArr[lineNum][i];
                    
                    if(up && left && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_ul") != null)
                    {
                        block.graphicsFile += "_ul";
                    }
                    else if(up && right && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_ur") != null)
                    {
                        block.graphicsFile += "_ur";
                    }
                    else if(down && left && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_dl") != null)
                    {
                        block.graphicsFile += "_dl";
                    }
                    else if(down && right && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_dr") != null)
                    {
                        block.graphicsFile += "_dr";
                    }
                    else if(up && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_u") != null)
                    {
                        block.graphicsFile += "_u";
                    }
                    else if(down && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_d") != null)
                    {
                        block.graphicsFile += "_d";
                    }
                    else if(left && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_l") != null)
                    {
                        if(right && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_r") != null)
                        {
                            if(fullArr[lineNum][i+1] != ' ')
                            {
                                block.graphicsFile += "_l";
                            }
                            else
                            {
                                block.graphicsFile += "_r";
                            }
                        }
                        else
                        {
                            block.graphicsFile += "_l";
                        }
                    }
                    else if(right && images.get("scenery_" +sceneryGraphics.get(arr[i]) + "_r") != null)
                    {
                        block.graphicsFile += "_r";
                    }
                }
                else if(arr[i] == '0' || arr[i] == '1' || arr[i] == '2'|| arr[i] == '3') // Question Block
                {
                    QuestionBlock block = new QuestionBlock(i+leftMapBuffer,lineNum+topMapBuffer);
                    staticBlockMap[i+leftMapBuffer][lineNum+topMapBuffer] = block;
                    block.type = arr[i] - '0';
                    block.canBounce = true;
                    gameObjects.add(block);
                }
                else if(arr[i] == '5' || arr[i] == '6' || arr[i] == '7' || arr[i] == '8') // Invisible Question Block
                {
                    QuestionBlock block = new QuestionBlock(i+leftMapBuffer,lineNum+topMapBuffer);
                    staticBlockMap[i+leftMapBuffer][lineNum+topMapBuffer] = block;
                    block.type = arr[i] - '5';
                    block.invisible = true;
                    block.graphicsFile = "";
                    block.canBounce = true;
                    gameObjects.add(block);
                }
                else if(arr[i] == '$') // Coin
                {
                    Coin block = new Coin(i+leftMapBuffer,lineNum+topMapBuffer);
                    gameObjects.add(block);
                }
                else if(arr[i] == 'g')
                {
                    enemies.add(new Goomba(i+leftMapBuffer,lineNum+topMapBuffer));
                }
                else if(arr[i] == 's')
                {
                    enemies.add(new Spiny(i+leftMapBuffer,lineNum+topMapBuffer));
                }
                else if(arr[i] == 'T')
                {
                    enemies.add(new Turtle(i+leftMapBuffer,lineNum+topMapBuffer));
                }
                else if(arr[i] == 'p')
                {
                    enemies.add(new PiranhaPlant(i+leftMapBuffer,lineNum+topMapBuffer));
                }
                else if(arr[i] == 'P')
                {
                    PiranhaPlant p = new PiranhaPlant(i+leftMapBuffer,lineNum+topMapBuffer);
                    p.moves = false;
                    enemies.add(p);
                }
                else if(arr[i] == '#')
                {
                    Thwomp t = new Thwomp(i+leftMapBuffer,lineNum+topMapBuffer);
                    enemies.add(t);
                }
                else if(arr[i] == 'F')
                {
                    flag = new Flag(i+leftMapBuffer,lineNum+topMapBuffer);
                }
            }
        }
    }
    
    
    public void loadMap(String mapname) throws Exception
    {
        if(mario == null)
            mario = new Mario();
        else
        {
            int power = mario.power;
            mario = new Mario();
            mario.power = power;
        }
        
        
        
        
        try
        {
            
            BufferedReader infoReader = new BufferedReader(new FileReader("maps/" +mapname +"/mif.txt"));
            String liner = "";
            while((liner = infoReader.readLine()) != null)
            {
                if(liner.equals("#width"))
                {
                    mapWidth = Integer.parseInt(infoReader.readLine()) + leftMapBuffer + rightMapBuffer;
                }
                else if(liner.equals("#height"))
                {
                    mapHeight = Integer.parseInt(infoReader.readLine()) + topMapBuffer + bottomMapBuffer;
                }
                else if(liner.equals("#color"))
                {
                    liner = infoReader.readLine();
                    if(liner.equalsIgnoreCase("black"))
                    {
                        backgroundColor = Color.BLACK;
                    }
                    else if(liner.equalsIgnoreCase("blue"))
                    {
                        backgroundColor = new Color(70,149,250);
                    }
                }
            }
            
        }
        catch(Exception e)
        {
            backgroundColor = new Color(70,149,250);
            mapWidth = 120 + leftMapBuffer + rightMapBuffer;
            mapHeight = 12 + topMapBuffer + bottomMapBuffer;
        }
        //String whole = "";
        //String line = "";
        //while((line = infoReader.readLine()) != null)
        //{
        //    whole += line +"\n";
        //}
        
        
        mario.xLocation = 0x40000;
        mario.yLocation = 0x140000;
        
        BufferedReader mapReader = new BufferedReader(new FileReader("maps/" +mapname +"/rmf.txt"));
        String whole = "";
        String line = "";
        while((line = mapReader.readLine()) != null)
        {
            whole += line +"\n";
        }
        
        
        
        generateMapFromString(whole);
    }
    
    public StaticBlock addStaticBlockToMap(String type, int x, int y)
    {
        StaticBlock block = new StaticBlock(x,y);
        block.graphicsFile = type;
        staticBlockMap[x][y] = block;
        return block;
    }
    public StaticBlock addSceneryBlockToMap(String type, int x, int y)
    {
        StaticBlock block = new StaticBlock(x,y);
        block.graphicsFile = type;
        staticBlockMap[x][y] = block;
        block.graphicsFolder = "scenery";
        block.scenery = true;
        return block;
    }
    private void addStaticBlockToMap(String type, int x, int y, boolean ki)
    {
        StaticBlock block = new StaticBlock(x,y);
        block.graphicsFile = type;
        block.kills = ki;
        staticBlockMap[x][y] = block;
    }
    
    
    private void loadGraphics() throws Exception
    {
        images = new HashMap<String, BufferedImage>();
        loadGraphicsFromFolder("mario", true, true);
        loadGraphicsFromFolder("block");
        loadGraphicsFromFolder("scenery");
        loadGraphicsFromFolder("enemy", true, true);
        loadGraphicsFromFolder("piranhaplant");
        loadGraphicsFromFolder("flag");
        loadGraphicsFromFolder("Collectables");
        
        blockGraphics = new HashMap<Character, String>();
        //Pipe keys
        blockGraphics.put('|',"pipe");
        
        //Single, non-related bricks
        blockGraphics.put('d',"dirt");
        blockGraphics.put('c',"clay");
        
        //bricks with _flag markers
        blockGraphics.put('b',"brick");
        blockGraphics.put('t',"tree");
        blockGraphics.put('M',"mushroom");
        blockGraphics.put('W',"whiteTree");
        
        blockGraphics.put('S',"sandblock");
        blockGraphics.put('o',"outside");
        blockGraphics.put('D',"desert");
        
        
        
        blockGraphics.put('B',"blastertop");
        blockGraphics.put('^',"blasterbottom");
        blockGraphics.put('%',"bridge");
        blockGraphics.put('m',"metal");
        blockGraphics.put('+',"spike");
        blockGraphics.put('C',"cloud");
        
        blockGraphics.put('G',"greyblock");
        blockGraphics.put('L',"blueblock");
        sceneryGraphics = new HashMap<Character, String>();
        
        
        
        sceneryGraphics.put('\"',"bush");
        sceneryGraphics.put('=',"cloud");
        sceneryGraphics.put('[',"stem");
        
        sceneryGraphics.put('w',"water");
        sceneryGraphics.put('l',"lava");
        
        sceneryGraphics.put('~',"bridgefence");
        sceneryGraphics.put('f',"fence");
        sceneryGraphics.put(':',"treetrunk");
    }
    private void loadGraphicsFromFolder(String folder) throws Exception
    {
        File dir = new File("Graphics/" +folder +"/");
        File[] listOfFiles = dir.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) 
        {
            if(!listOfFiles[i].getName().contains(".png"))
            {
                listOfFiles[i] = null;
            }
        }
        for(File f : listOfFiles)
        {
            if(f != null)
            {
                String name = folder +"_" +f.getName().substring(0,f.getName().length()-4);
                
                
                BufferedImage img = ImageIO.read(f);
                
                int scale = 1;
                if(name.contains("_x2"))
                {
                   name.replaceAll("_x2","");
                   scale = 2;
                }
                BufferedImage imgScaled = scale(img, Constants.block_rendering_width, Constants.block_rendering_width * img.getHeight() / img.getWidth());
                images.put(name, imgScaled);
            }
        }
    }
    private void loadGraphicsFromFolder(String folder, boolean flip, boolean upside) throws Exception
    {
        File dir = new File("Graphics/" +folder +"/");
        File[] listOfFiles = dir.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) 
        {
            if(!listOfFiles[i].getName().contains(".png"))
            {
                listOfFiles[i] = null;
            }
        }
        for(File f : listOfFiles)
        {
            if(f != null)
            {
                String name = folder +"_" +f.getName().substring(0,f.getName().length()-4);
                BufferedImage img = ImageIO.read(f);
                
                double scale = 1;
                if(name.contains("_x2"))
                {
                   name = name.replaceAll("_x2","");
                   scale = 22 / 16.0;
                }
                
                BufferedImage imgScaled = scale(img, (int)(Constants.block_rendering_width*scale),(int)( Constants.block_rendering_width*scale * img.getHeight() / img.getWidth()));
                images.put(name, imgScaled);
                if(flip)
                    images.put(name +"_f",flip(imgScaled));
                if(upside)
                {
                    images.put(name +"_f_u",flipUpsideDown(flip(imgScaled)));
                    images.put(name +"_u",flipUpsideDown(imgScaled));
                }
            }
        }
    }
    public void tick()
    {
        for(Game_Object obj : gameObjects)
        {
            if(isWithinScreen(obj))
            {
                obj.tick();
                if(obj instanceof StaticBlock)
                    ((StaticBlock)obj).isBeingStoodUpon = false;
            }
        }
        
        if(mario.dead)
        {
            if(tickCounter > 0)
                tickCounter = 0;
            tickCounter--;
            mario.tick();
            if(toPixels(mario.yLocation) > Constants.block_rendering_width * (mapHeight + 4))
                startLevel();
            return;
        }
        
        
        for(Enemy obj : enemies)
        {
            if(isWithinScreen(obj))
                obj.tick();
        }
        mario.tick();
        
        if(toPixels(mario.xLocation + mario.width/2) > cameraX + (screenWidth-horizontalBuffer) * Constants.block_rendering_width)
        {
            cameraX = toPixels(mario.xLocation + mario.width/2) - (screenWidth-horizontalBuffer) * Constants.block_rendering_width;
        }
        else if(cameraX + horizontalBuffer * Constants.block_rendering_width > toPixels(mario.xLocation + mario.width/2))
        {
            cameraX = toPixels(mario.xLocation + mario.width/2) - horizontalBuffer * Constants.block_rendering_width;
        }
        if(cameraX < Constants.block_rendering_width * leftMapBuffer)
        {
            cameraX = Constants.block_rendering_width * leftMapBuffer;
        }
        if(cameraX + screenWidth * Constants.block_rendering_width > (mapWidth-rightMapBuffer) * Constants.block_rendering_width)
        {
            cameraX = (mapWidth - screenWidth - rightMapBuffer) * Constants.block_rendering_width;
        }
        tickCounter ++;
        if(tickCounter >= 60 * 60 * 60)
            kill = true;
            
            
    }
    public void paintComponent(Graphics g)
    {
        g.setColor(backgroundColor);
        g.fillRect(0,0,screenWidth*Constants.block_rendering_width,screenHeight*Constants.block_rendering_width);

        if(drawGrid)
        {
            if(backgroundColor == Color.BLACK)
            {
                g.setColor(Color.WHITE);
            }
            else
                g.setColor(Color.BLACK);
            for(int i = 0; i < mapWidth; i++)
            {
                g.drawLine(i*Constants.block_rendering_width-cameraX, 0,i*Constants.block_rendering_width-cameraX, mapHeight*Constants.block_rendering_width);
            }
            for(int i = 0; i < mapHeight; i++)
            {
                g.drawLine(0,i*Constants.block_rendering_width,mapWidth*Constants.block_rendering_width,i*Constants.block_rendering_width);
            }
        }
        
        for(Game_Object obj : toAdd)
        {
            if(obj instanceof Enemy)
            {
                enemies.add((Enemy)obj);
            }
            else
            {
                gameObjects.add(obj);
            }
        }
        toAdd.clear();
        
        for(Game_Object obj : toRemove)
        {
            if(obj instanceof Enemy)
            {
                enemies.remove((Enemy)obj);
            }
            else
            {
                gameObjects.remove(obj);
            }
        }
        toRemove.clear();
        
        
        
        
        
        
        for(int i = 1; i < 5; i++)
        {
            for(Game_Object obj : gameObjects)
            {
                if(!obj.hidden && obj.drawPriority == i)
                {
                    int xPixel = toPixels(obj.xLocation - obj.graphicsXOffset) - cameraX;
                    int yPixel = toPixels(obj.yLocation - obj.graphicsYOffset) - cameraY;
                    if(drawImages)
                        g.drawImage(images.get(obj.graphicsString()),xPixel, yPixel,null);
                    if(showHitboxes)
                    {
                        g.setColor(Color.RED);
                        g.drawRect(toPixels(obj.xLocation)-cameraX, toPixels(obj.yLocation)-cameraY, toPixels(obj.width), toPixels(obj.height));
                    }
                }
            }
            
            for(Enemy obj : enemies)
            {
                if(!obj.hidden && obj.drawPriority == i)
                {
                    int xPixel = toPixels(obj.xLocation - obj.graphicsXOffset) - cameraX;
                    int yPixel = toPixels(obj.yLocation - obj.graphicsYOffset) - cameraY;
                    if(drawImages)
                        g.drawImage(images.get(obj.graphicsString()),xPixel, yPixel,null);
                    if(showHitboxes)
                    {
                        g.setColor(Color.RED);
                        g.drawRect(toPixels(obj.xLocation)-cameraX, toPixels(obj.yLocation)-cameraY, toPixels(obj.width), toPixels(obj.height));
                    }
                }
            }
            
            for(int x = 0; x < staticBlockMap.length; x++)
            {
                for(int y = 0; y < staticBlockMap[x].length; y++)
                {
                    if(staticBlockMap[x][y] != null && staticBlockMap[x][y].drawPriority == i)
                    {
                        if(drawImages)
                            g.drawImage(images.get(staticBlockMap[x][y].graphicsString()),toPixels(staticBlockMap[x][y].xLocation)-cameraX,toPixels(staticBlockMap[x][y].yLocation)-cameraY,null);
                        if(staticBlockMap[x][y] instanceof QuestionBlock && editingMode)
                        {
                            if(((QuestionBlock)staticBlockMap[x][y]).invisible)
                            {
                                if(drawImages)
                                    g.drawImage(images.get("block_questionBlockClear"),toPixels(staticBlockMap[x][y].xLocation)-cameraX,toPixels(staticBlockMap[x][y].yLocation)-cameraY,null);
                            }
                            if(((QuestionBlock)staticBlockMap[x][y]).type == 1)
                                g.drawImage(images.get("Collectables_greenMushroom"),toPixels(staticBlockMap[x][y].xLocation)-cameraX,toPixels(staticBlockMap[x][y].yLocation)-cameraY,null);
                            else if(((QuestionBlock)staticBlockMap[x][y]).type == 2)
                                g.drawImage(images.get("Collectables_blackMushroom"),toPixels(staticBlockMap[x][y].xLocation)-cameraX,toPixels(staticBlockMap[x][y].yLocation)-cameraY,null);
                            else if(((QuestionBlock)staticBlockMap[x][y]).type == 3)
                                g.drawImage(images.get("enemy_goomba_step"),toPixels(staticBlockMap[x][y].xLocation)-cameraX,toPixels(staticBlockMap[x][y].yLocation)-cameraY,null);
                        
                        }
                        if(showHitboxes)
                        {
                            if(staticBlockMap[x][y].scenery)
                            {
                                g.setColor(Color.WHITE);
                            }
                            else
                                g.setColor(new Color(156, 93, 82));
                            g.drawRect(toPixels(staticBlockMap[x][y].xLocation)-cameraX, toPixels(staticBlockMap[x][y].yLocation)-cameraY, toPixels(staticBlockMap[x][y].width), toPixels(staticBlockMap[x][y].height));
                        }
                    }
                }
            }
        }
        
        int xPixel = toPixels(flag.xLocation - flag.graphicsXOffset) - cameraX;
        int yPixel = toPixels(flag.yLocation - flag.graphicsYOffset) - cameraY;
        g.drawImage(images.get(flag.graphicsString()),xPixel, yPixel,null);
        
        
        
        
        xPixel = toPixels(mario.xLocation - mario.graphicsXOffset) - cameraX;
        yPixel = toPixels(mario.yLocation - mario.graphicsYOffset) - cameraY;
        
        //g.fillRect((int)xPixel,(int)yPixel,(int)width,(int)height);
        g.drawImage(images.get(mario.graphicsString()),xPixel, yPixel,null);
        if(showHitboxes)
        {
            g.setColor(Color.BLUE);
            g.drawRect(toPixels(mario.xLocation)-cameraX, toPixels(mario.yLocation)-cameraY, toPixels(mario.width), toPixels(mario.height));
        }
        
        //if(Math.abs(gameObjects.get(0).xVelocity) > 0x00010)
        //    g.drawString((gameObjects.get(0).xVelocity < 0 ? "-" : " ")+String.format("0x%8s", Integer.toHexString(Math.abs(gameObjects.get(0).xVelocity))).replace(' ', '0'), 300, 300);
        //else
        //    g.drawString("0x00000",300,300);
        
        
        
        if(editingMode)
        {
            g.setColor(backgroundColor);
            g.fillRect(screenWidth*Constants.block_rendering_width,0,(screenWidth+5)*Constants.block_rendering_width,screenHeight*Constants.block_rendering_width);
            
            
            Iterator it = sceneryGraphics.entrySet().iterator();
            int counter = 0;
            while (it.hasNext()) 
            {
                Map.Entry pair = (Map.Entry)it.next();
                if(images.get("scenery_"+pair.getValue() +"_i") != null)
                {
                    sceneryCharArray[counter] = (char)pair.getKey();
                    g.drawImage(images.get("scenery_"+pair.getValue() +"_i"),screenWidth * Constants.block_rendering_width, counter * Constants.block_rendering_width , null);
                    counter ++;
                }
                else if(images.get("scenery_"+pair.getValue()) != null)
                {
                    sceneryCharArray[counter] = (char)pair.getKey();
                    g.drawImage(images.get("scenery_"+pair.getValue()),screenWidth * Constants.block_rendering_width, counter * Constants.block_rendering_width , null);
                    counter ++;
                }
            }
            
            
            it = blockGraphics.entrySet().iterator();
            counter = 0;
            while (it.hasNext()) 
            {
                Map.Entry pair = (Map.Entry)it.next();
                if(images.get("block_"+pair.getValue()) != null)
                {
                        blockCharArray[counter] = (char)pair.getKey();
                        g.drawImage(images.get("block_"+pair.getValue()),(screenWidth+1 +(counter/12)) * Constants.block_rendering_width, (counter%12) * Constants.block_rendering_width , null);
                        counter++;
                }
            }
           
            
            
            g.drawImage(images.get("block_questionBlock1"),(screenWidth+4) * Constants.block_rendering_width,0,null);
            
            g.drawImage(images.get("block_questionBlock1"),(screenWidth+4) * Constants.block_rendering_width,1*Constants.block_rendering_width,null);
            g.drawImage(images.get("Collectables_greenMushroom"),(screenWidth+4) * Constants.block_rendering_width,1*Constants.block_rendering_width,null);
            
            g.drawImage(images.get("block_questionBlock1"),(screenWidth+4) * Constants.block_rendering_width,2*Constants.block_rendering_width,null);
            g.drawImage(images.get("Collectables_blackMushroom"),(screenWidth+4) * Constants.block_rendering_width,2*Constants.block_rendering_width,null);
            
            g.drawImage(images.get("block_questionBlock1"),(screenWidth+4) * Constants.block_rendering_width,3*Constants.block_rendering_width,null);
            g.drawImage(images.get("enemy_goomba_step"),(screenWidth+4) * Constants.block_rendering_width,3*Constants.block_rendering_width,null);
            
            
            g.drawImage(images.get("block_questionBlockClear"),(screenWidth+4) * Constants.block_rendering_width,5*Constants.block_rendering_width,null);
            
            g.drawImage(images.get("block_questionBlockClear"),(screenWidth+4) * Constants.block_rendering_width,6*Constants.block_rendering_width,null);
            g.drawImage(images.get("Collectables_greenMushroom"),(screenWidth+4) * Constants.block_rendering_width,6*Constants.block_rendering_width,null);
            
            g.drawImage(images.get("block_questionBlockClear"),(screenWidth+4) * Constants.block_rendering_width,7*Constants.block_rendering_width,null);
            g.drawImage(images.get("Collectables_blackMushroom"),(screenWidth+4) * Constants.block_rendering_width,7*Constants.block_rendering_width,null);
            
            g.drawImage(images.get("block_questionBlockClear"),(screenWidth+4) * Constants.block_rendering_width,8*Constants.block_rendering_width,null);
            g.drawImage(images.get("enemy_goomba_step"),(screenWidth+4) * Constants.block_rendering_width,8*Constants.block_rendering_width,null);
            
            
            
            //g.drawImage(images.get("block_pipe"),(screenWidth+2) * Constants.block_rendering_width,0,null);
            //g.drawImage(images.get("scenery_cloudfull"),(screenWidth+2) * Constants.block_rendering_width,Constants.block_rendering_width*1,null);
            //g.drawImage(images.get("scenery_bushfull"),(screenWidth+2) * Constants.block_rendering_width,Constants.block_rendering_width*2,null);
            
            g.drawImage(images.get("enemy_goomba_step"),(screenWidth+3) * Constants.block_rendering_width,0,null);
            g.drawImage(images.get("enemy_spiny_step1_f"),(screenWidth+3) * Constants.block_rendering_width,Constants.block_rendering_width * 1,null);
            g.drawImage(images.get("enemy_piranhaplant_icon"),(screenWidth+3) * Constants.block_rendering_width,Constants.block_rendering_width * 2,null);
            g.drawImage(images.get("enemy_turtle_icon"),(screenWidth+3) * Constants.block_rendering_width,Constants.block_rendering_width * 3,null);
            g.drawImage(images.get("enemy_thwomp_icon"),(screenWidth+3) * Constants.block_rendering_width,Constants.block_rendering_width * 4,null);
            
            g.setColor(Color.YELLOW);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(6));
            g2.drawRect(selectedX * Constants.block_rendering_width,selectedY * Constants.block_rendering_width,Constants.block_rendering_width,Constants.block_rendering_width);
        }
    }
    public boolean isWithinScreen(Game_Object obj)
    {
        return cameraX < toPixels(obj.centerX() + obj.bufferDistance) && cameraX + screenWidth * Constants.block_rendering_width > toPixels(obj.centerX() - obj.bufferDistance);
    }
    public static BufferedImage scale(BufferedImage in, int width, int height) 
    {
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < out.getWidth(); i++)
        {
            for(int j = 0; j < out.getHeight(); j++)
            {
                int scX = i * in.getWidth() / width;
                int scY = j * in.getHeight() / height;
                out.setRGB(i,j, in.getRGB(scX,scY));
            }
        }
        return out;
    }
    public static BufferedImage flip(BufferedImage in)
    {
        BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < out.getWidth(); i++)
        {
            for(int j = 0; j < out.getHeight(); j++)
            {
                out.setRGB(i,j, in.getRGB(in.getWidth() - i - 1,j));
            }
        }
        return out;
    }
    public static BufferedImage flipUpsideDown(BufferedImage in)
    {
        BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < out.getWidth(); i++)
        {
            for(int j = 0; j < out.getHeight(); j++)
            {
                out.setRGB(i,j, in.getRGB(i,in.getHeight() - j - 1));
            }
        }
        return out;
    }
    public int toPixels(int hexi)
    {
        return hexi * Constants.block_rendering_width / Constants.block_width;
    }
}
