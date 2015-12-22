import java.io.*;
/**
 * Write a description of class Mario here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Mario extends Game_Object
{
    private boolean falling;
    
    int clock;
    int animCycle;
    
    int initialXVelocity;
    
    boolean dead;
    boolean slidingDownFlag;
    
    boolean walkingTowardsCastle;
    
    boolean canWallJump;
    
    public int power;
    
    public boolean invincible;
    public int invincibilityCounter;
    
    private String oldGraphics;
    
    
    public boolean upIsUp;
    
    public int coins;
    
    private int walkingSprites;
    
    private static BufferedReader in;
    private static BufferedWriter out;
    
    public static boolean isReading = false;
    public static boolean isWriting = false;
    
    
    public Mario()
    {
        super();
        
            
        xLocation = 0x40000;
        yLocation = 0x80000;
        
        width = 0x0C000;
        height = 0x0E000;
        
        graphicsXOffset = 0x02000;
        graphicsYOffset = 0x02000;
        
        yAcceleration = 0x00700;
        
        falling = false;
        
        graphicsFolder = "mario";
        graphicsFile = "standing";
        
        flipped = false;
        
        animCycle = 1;
        
        power = 0;
        
        upIsUp = true;
        
        walkingSprites = 4;
        try
        {
            if(isWriting)
            {
                File file = new File("MoveList.txt");
                file.createNewFile();
                out = new BufferedWriter(new FileWriter(file));
            }
            else if(isReading)
            {
                in = new BufferedReader(new FileReader("MoveList.txt"));
            }
        }
        catch(Exception e)
        {
            System.out.println("File Error");
        }
    }
    public void jump()
    {
        if(falling)
        {
            return;
        }
    }
    public void die()
    {
        dead = true;
        graphicsFile = "dead";
        animCounter = 0;
        yVelocity = -0x04000;
        yAcceleration = 0x00200;
        
        isReading = false;
        isWriting = false;
        if(out != null)
        {
            try
            {
                out.close();
                
            }catch(Exception e){}
        }
    }
    public void tick()
    {
        boolean[] arr = new boolean[4];
        arr[0] = Screen.runIsDown;
        arr[1] = Screen.jumpIsDown;
        arr[2] = Screen.leftIsDown;
        arr[3] = Screen.rightIsDown;
        
        String str;
        try
        {
            if(isReading)
            {
                if((str = in.readLine()) != null)
                {
                    arr[0] = str.charAt(0) == '1';
                    arr[1] = str.charAt(1) == '1';
                    arr[2] = str.charAt(2) == '1';
                    arr[3] = str.charAt(3) == '1';
                }
            }
            else if(isWriting)
            {
                String outString = "";
                outString += (Screen.runIsDown ? "1" : "0");
                outString += (Screen.jumpIsDown ? "1" : "0");
                outString += (Screen.leftIsDown ? "1" : "0");
                outString += (Screen.rightIsDown ? "1" : "0");
                out.write(outString);
                out.newLine();
            }
        }
        catch(Exception e)
        {
            
        }
        
        
        arr[0] = true;
        tick(arr);
    }
    public void tick(boolean[] arr)
    {
        if(arr[1] && !Screen.jumpIsDown)
            Screen.pressedJump = true;
        
        Screen.runIsDown = true;//arr[0];
        Screen.jumpIsDown = true;//arr[1];
        Screen.leftIsDown = false;//arr[2];
        Screen.rightIsDown = true;//arr[3];
        
        if(dead)
        {
            animCounter ++;
            if(animCounter > 30)
            {
                yLocation += yVelocity;
                yVelocity += yAcceleration;
            }
            return;
        }
        
        if(slidingDownFlag)
        {
            animCounter ++;
            if(animCounter > 30)
            {
                if(yLocation < Screen.flag.yLocation)
                {
                    yLocation += 0x01000;
                }
                else
                {
                    slidingDownFlag = false;
                    walkingTowardsCastle = true;
                    animCounter = 0;
                }
            }
            return;
        }
        
        if(walkingTowardsCastle)
        {
            animCounter++;
            if(animCounter == 20)
            {
                flipped = true;
                xLocation += width + 0x04000;
                return;
            }
            else if(animCounter == 40)
            {
                Game.mainGameScreen.beatLevel();
            }
            return;
        }
        
        
        if(centerX() > Screen.flag.centerX() && !walkingTowardsCastle)
        {
            xLocation = Screen.flag.xLocation - width/2;
            graphicsFile = "sliding";
            slidingDownFlag = true;
            flipped = false;
            upIsUp = true;
            animCounter = 0;
            isReading = false;
            isWriting = false;
            if(out != null)
        {
            try
            {
                out.close();
            }catch(Exception e){}
        }
            
            return;
        }
        
        if(power == 2)
        {
            upIsUp = false;
            graphicsYOffset = 0;
        }
        else
        {
            upIsUp = true;
            graphicsYOffset = 0x02000;
        }
        
        
        
        if(!Screen.jumpIsDown)
        {
            Screen.pressedJump = false;
        }
        if(!falling && Screen.pressedJump)
        {
            if(upIsUp)
            {
                if(Math.abs(xVelocity) >= 0x02500)
                    yVelocity = -0x05000;
                else
                    yVelocity = -0x04000;
            }
            else
            {
                if(Math.abs(xVelocity) >= 0x02500)
                    yVelocity = 0x05000;
                else
                    yVelocity = 0x04000;
            }
            initialXVelocity = Math.abs(xVelocity);
            graphicsFile = "jumping";
            falling = true;
            Screen.pressedJump = false;
        }
        else if(canWallJump && power == 1 && Screen.pressedJump)
        {
            yVelocity = -0x04000;
            initialXVelocity = Math.abs(xVelocity);
            graphicsFile = "jumping";
            falling = true;
            Screen.pressedJump = false;
            flipped = !flipped;
            xVelocity = (flipped ? -0x01000 : 0x01000);
        }
        
        boolean skidding = false;
        
        //Set the proper acceleration
        if(!falling)
        {
            //If we are walking on "ground"
            if(Screen.rightIsDown)
            {
                //If we are pressing right
                if(Screen.runIsDown)
                {
                    //If we are holding down the run button
                    if(xVelocity < 0 && !Screen.leftIsDown)
                    {
                        //If we are moving left but holding right only
                        xAcceleration = Constants.skidding_deceleration;
                        skidding = true;
                        
                        graphicsFile = "skidding";
                        flipped = false;
                        
                        clock = 0;
                        animCycle = 1;
                    }
                    else
                    {
                        xAcceleration = Constants.running_acceleration;
                        flipped = true;
                        
                        clock++;
                        if(clock > Constants.run_anim_frame)
                        {
                            clock = 0;
                            animCycle ++;
                            if(animCycle >= walkingSprites)
                                animCycle = 1;
                        }
                        
                        graphicsFile = "walk" +animCycle;
                        flipped = false;
                    }
                }
                else
                {
                    if(xVelocity < 0 && !Screen.leftIsDown)
                    {
                        //If we are moving left but holding right only
                        xAcceleration = Constants.skidding_deceleration;
                        skidding = true;
                        
                        graphicsFile = "skidding";
                        flipped = false;
                        
                        clock = 0;
                        animCycle = 1;
                        
                    }
                    else
                    {
                        xAcceleration = Constants.walking_acceleration;
                        flipped = true;
                        
                        clock++;
                        if(clock > Constants.walk_anim_frame)
                        {
                            clock = 0;
                            animCycle ++;
                            if(animCycle >= walkingSprites)
                                animCycle = 1;
                        }
                        
                        graphicsFile = "walk" +animCycle;
                        flipped = false;
                    }
                }
            }
            else if(Screen.leftIsDown)
            {
                if(Screen.runIsDown)
                {
                    if(xVelocity > 0 && !Screen.rightIsDown)
                    {
                        xAcceleration = -1 * Constants.skidding_deceleration;
                        skidding = true;
                        
                        graphicsFile = "skidding";
                        flipped = true;
                    }
                    else
                    {
                        xAcceleration = -1 * Constants.running_acceleration;
                        
                        clock++;
                        if(clock > Constants.run_anim_frame)
                        {
                            clock = 0;
                            animCycle ++;
                            if(animCycle >= walkingSprites)
                                animCycle = 1;
                        }
                        
                        graphicsFile = "walk" +animCycle;
                        flipped = true;
                    }
                }
                else
                {
                    if(xVelocity > 0 && !Screen.rightIsDown)
                    {
                        xAcceleration = -1 * Constants.skidding_deceleration;
                        skidding = true;
                        
                        graphicsFile = "skidding";
                        flipped = true;
                    }
                    else
                    {
                        xAcceleration = -1 * Constants.walking_acceleration;
                        
                        clock++;
                        if(clock > Constants.walk_anim_frame)
                        {
                            clock = 0;
                            animCycle ++;
                            if(animCycle >= walkingSprites)
                                animCycle = 1;
                        }
                        
                        graphicsFile = "walk" +animCycle;
                        flipped = true;
                    }
                }
            }
            else if(Math.abs(xVelocity) > Constants.minimum_walk_speed)
            {
                //"friction" if we are moving but not pressing any keys
                xAcceleration = (xVelocity > 0 ?  -1 * 0x000D0 : 0x000D0);
                clock++;
                if(clock > Constants.walk_anim_frame)
                {
                    clock = 0;
                    animCycle ++;
                    if(animCycle == 4)
                        animCycle = 1;
                }
                
                graphicsFile = "walk" +animCycle;
            }
            else
            {
                xAcceleration = 0;
                graphicsFile = "standing";
            }
        }
        else
        {
            if(upIsUp)
            {
                //Midair Physics with Y
                if(yVelocity < 0 && Screen.jumpIsDown)
                {
                    if(initialXVelocity < 0x01000)
                    {
                       yAcceleration = 0x00200; 
                    }
                    else if(initialXVelocity < 0x024ff)
                    {
                       yAcceleration = 0x001e0; 
                    }
                    else
                    {
                        yAcceleration = 0x00280;
                    }
                }
                else
                {
                    if(initialXVelocity < 0x01000)
                    {
                       yAcceleration = 0x00700; 
                    }
                    else if(initialXVelocity < 0x024ff)
                    {
                       yAcceleration = 0x00600; 
                    }
                    else
                    {
                        yAcceleration = 0x0900;
                    }
                }
                
                //Midair physics with X
                if(Screen.rightIsDown)
                {
                    //If we are pressing right
                    if(xVelocity >= 0x01900)
                    {
                        //xAcceleration = 0x00098;
                        xAcceleration = 0x00150;
                    }
                    else if(xVelocity >= 0)
                    {
                        //xAcceleration = 0x000E4;
                        xAcceleration = 0x00150;
                    }
                    else if(xVelocity <= -0x01900)
                    {
                        //xAcceleration = 0x000E4;
                        xAcceleration = 0x00150;
                    }
                    else if(initialXVelocity >= 0x01D00)
                    {
                        //xAcceleration = 0x000D0;
                        xAcceleration = 0x00150;
                    }
                    else
                    {
                        //xAcceleration = 0x00098;
                        xAcceleration = 0x00150;
                    }
                }
                else if(Screen.leftIsDown)
                {
                    if(xVelocity <= 0x01900)
                    {
                        //xAcceleration = -0x00098;
                        xAcceleration = -0x00150;
                    }
                    else if(xVelocity <= 0)
                    {
                        //xAcceleration = -0x000E4;
                        xAcceleration = -0x00150;
                    }
                    else if(xVelocity <= 0x01900)
                    {
                        //xAcceleration = -0x000E4;
                        xAcceleration = -0x00150;
                    }
                    else if(initialXVelocity <= 0x01D00)
                    {
                        //xAcceleration = -0x000D0;
                        xAcceleration = -0x00150;
                    }
                    else
                    {
                        //xAcceleration = -0x00098;
                        xAcceleration = -0x00150;
                    }
                }
                else
                {
                    xAcceleration = 0;
                }
            }
            else
            {
                //Midair Physics with Y UPSIDE DOWN
                if(yVelocity > 0 && Screen.jumpIsDown)
                {
                    if(initialXVelocity < 0x01000)
                    {
                       yAcceleration = -0x00200; 
                    }
                    else if(initialXVelocity < 0x024ff)
                    {
                       yAcceleration = -0x001e0; 
                    }
                    else
                    {
                        yAcceleration = -0x00280;
                    }
                }
                else
                {
                    if(initialXVelocity < 0x01000)
                    {
                       yAcceleration = -0x00700; 
                    }
                    else if(initialXVelocity < 0x024ff)
                    {
                       yAcceleration = -0x00600; 
                    }
                    else
                    {
                        yAcceleration = -0x0900;
                    }
                }
                
                //Midair physics with X
                if(Screen.rightIsDown)
                {
                    //If we are pressing right
                    if(xVelocity >= 0x01900)
                    {
                        //xAcceleration = 0x00098;
                        xAcceleration = 0x00150;
                    }
                    else if(xVelocity >= 0)
                    {
                        //xAcceleration = 0x000E4;
                        xAcceleration = 0x00150;
                    }
                    else if(xVelocity <= -0x01900)
                    {
                        //xAcceleration = 0x000E4;
                        xAcceleration = 0x00150;
                    }
                    else if(initialXVelocity >= 0x01D00)
                    {
                        //xAcceleration = 0x000D0;
                        xAcceleration = 0x00150;
                    }
                    else
                    {
                        //xAcceleration = 0x00098;
                        xAcceleration = 0x00150;
                    }
                }
                else if(Screen.leftIsDown)
                {
                    if(xVelocity <= 0x01900)
                    {
                        //xAcceleration = -0x00098;
                        xAcceleration = -0x00150;
                    }
                    else if(xVelocity <= 0)
                    {
                        //xAcceleration = -0x000E4;
                        xAcceleration = -0x00150;
                    }
                    else if(xVelocity <= 0x01900)
                    {
                        //xAcceleration = -0x000E4;
                        xAcceleration = -0x00150;
                    }
                    else if(initialXVelocity <= 0x01D00)
                    {
                        //xAcceleration = -0x000D0;
                        xAcceleration = -0x00150;
                    }
                    else
                    {
                        //xAcceleration = -0x00098;
                        xAcceleration = -0x00150;
                    }
                }
                else
                {
                    xAcceleration = 0;
                }
            }
            
            
        }
        //Add that acceleration to the velocity
        xVelocity += xAcceleration;
        yVelocity += yAcceleration;
        
        if(skidding)
        {
            if(Math.abs(xVelocity) < Constants.skid_turnaround_speed)
            {
                xVelocity = (xVelocity < 0 ? Constants.skid_turnaround_speed: -1 * Constants.skid_turnaround_speed);
                
            }
        }
        
        //Bound the velocity by the game maximums
        if(Screen.runIsDown)
        {
            if(Math.abs(xVelocity) > Constants.maximum_running_speed)
            {
                xVelocity = (xVelocity > 0 ?  Constants.maximum_running_speed : -1 * Constants.maximum_running_speed);
            }
        }
        else
        {
            if(Math.abs(xVelocity) > Constants.maximum_walk_speed)
            {
                xVelocity = (xVelocity > 0 ?  Constants.maximum_walk_speed : -1 * Constants.maximum_walk_speed);
            }
        }
        
        if(upIsUp && yVelocity > 0x04800)
        {
            yVelocity = 0x04000;
        }
        if(!upIsUp && yVelocity < -0x04800)
        {
            yVelocity = -0x04000;
        }
        
        //Check for horizontal block intersections
        
        int gridX = (xLocation+width/2) / 0x10000;
        int gridY = (yLocation+height/2) / 0x10000;
        
        StaticBlock up = Screen.staticBlockMap[gridX][gridY-1];
        StaticBlock down = Screen.staticBlockMap[gridX][gridY+1];
        StaticBlock left = Screen.staticBlockMap[gridX-1][gridY];
        StaticBlock right = Screen.staticBlockMap[gridX+1][gridY];
        
        StaticBlock upRight = Screen.staticBlockMap[gridX+1][gridY-1];
        StaticBlock downRight = Screen.staticBlockMap[gridX+1][gridY+1];
        StaticBlock upLeft = Screen.staticBlockMap[gridX-1][gridY-1];
        StaticBlock downLeft = Screen.staticBlockMap[gridX-1][gridY+1];
        
        int oldX = xLocation;
        int oldY = yLocation;
        
        //If the velocity is not insignificant, add it to the location
        if(Math.abs(xVelocity) > Constants.minimum_walk_speed)
        {
            xLocation += xVelocity;
        }
        yLocation += yVelocity;
        
        int newX = xLocation;
        int newY = yLocation;
        
        falling = true;
        canWallJump = false;
        if(intersects(right) && xVelocity >= 0)
        {
            if(!(right instanceof QuestionBlock && ((QuestionBlock)right).invisible))
            {
                if(Screen.rightIsDown && !flipped)
                    canWallJump = true;
                xLocation = right.xLocation - width - 0x00100;
                xVelocity = 0;
            }
            if(right.kills)
            {
                getHit();
                die();
            }
            else if(right.injures && !invincible)
            {
                getHit();
            }
        }
        else if(intersects(left) && xVelocity <= 0)
        {
            if(! (left instanceof QuestionBlock) ||  !((QuestionBlock)left).invisible)
            {
                if(Screen.leftIsDown && flipped)
                    canWallJump = true;
                xLocation = left.xLocation + left.width + 0x00100;
                xVelocity = 0;
            }
            if(left.kills)
            {
                getHit();
                die();
            }
            else if(left.injures && !invincible)
            {
                getHit();
            }
        }
        
        if(intersects(up) && yVelocity <= 0)
        {
            yLocation = up.yLocation + up.height;
            yVelocity = 0;
            if(upIsUp)
                up.getHitFromBelow();
            if(!upIsUp)
                falling = false;
            if(up.kills)
            {
                getHit();
                die();
            }
            else if(up.injures && !invincible)
            {
                getHit();
            }
        }
        else if(intersects(upRight) && yVelocity <= 0)
        {
            yLocation = upRight.yLocation + upRight.height;
            yVelocity = 0;
            if(upIsUp)
                upRight.getHitFromBelow();
            if(!upIsUp)
                falling = false;
            if(upRight.kills)
            {
                getHit();
                die();
            }
            else if(upRight.injures && !invincible)
            {
                getHit();
            }
        }
        else if(intersects(upLeft) && yVelocity <= 0)
        {
            yLocation = upLeft.yLocation + upLeft.height;
            yVelocity = 0;
            if(upIsUp)
                upLeft.getHitFromBelow();
            if(!upIsUp)
                falling = false;
            if(upLeft.kills)
            {
                getHit();
                die();
            }
            else if(upLeft.injures && !invincible)
            {
                getHit();
            }
        }
        else if(intersects(down) && yVelocity >= 0 && ((!( down instanceof QuestionBlock)) ||  !((QuestionBlock)down).invisible))
        {
            yLocation = down.yLocation - height;
            yVelocity = 0;
            if(upIsUp)
                falling = false;
            down.isBeingStoodUpon = true;
            if(down.kills)
            {
                getHit();
                die();
            }
            else if(down.injures && !invincible)
            {
                getHit();
            }
        }
        else if(intersects(downRight) && yVelocity >= 0 && (!(downRight instanceof QuestionBlock) ||  !((QuestionBlock)downRight).invisible))
        {
            yLocation = downRight.yLocation - height;
            yVelocity = 0;
            if(upIsUp)
                falling = false;
            downRight.isBeingStoodUpon = true;
            if(downRight.kills)
            {
                getHit();
                die();
            }
            else if(downRight.injures && !invincible)
            {
                getHit();
            }
        }
        else if(intersects(downLeft) && yVelocity >= 0 && (!(downLeft instanceof QuestionBlock) ||  !((QuestionBlock)downLeft).invisible))
    {
            yLocation = downLeft.yLocation - height;
            yVelocity = 0;
            if(upIsUp)
                falling = false;
            downLeft.isBeingStoodUpon = true;
            if(downLeft.kills)
            {
                getHit();
                die();
            }
            else if(downLeft.injures && !invincible)
            {
                getHit();
            }
        }
        
        //xLocation = newX;
        checkForEnemyIntersections();
        
        for(Game_Object obj : Screen.gameObjects)
        {
            if(obj instanceof Collectable && intersects(obj))
                collect((Collectable)obj);
        }
        
        if(invincible)
        {
            invincibilityCounter++;
            if(invincibilityCounter > 90)
            {
                invincible = false;
            }
            if(!graphicsFile.equals(""))
                oldGraphics = graphicsFile;
            if(invincibilityCounter % 4 == 0)
                graphicsFile = "";
            else
                graphicsFile = oldGraphics;
        }
    }
    public void checkForEnemyIntersections()
    {
        boolean stepped = false;
        for(Enemy e : Screen.enemies)
        {
            if(intersects(e) && !e.dead)
            {
                if(e.canBeSteppedOn && (yVelocity > 0x00100 || stepped))
                {
                    e.getSteppedOn();
                    stepped = true;
                    if(power == 1)
                        yVelocity = Constants.green_mario_bounce_speed;
                    else 
                        yVelocity = Constants.mario_bounce_speed;
                        
                    falling = true;
                
                }
                else if(!invincible)
                {
                    if((e instanceof Turtle) && ((Turtle)e).isShell)
                    {
                        if(e.xVelocity != 0)
                        {
                            if(!((Turtle)e).wasJustHit)
                            {
                                getHit();
                            }
                        }
                        else
                        {
                            ((Turtle)e).getHit(this);
                        }
                    }
                    else
                    {
                        getHit();
                    }
                }
            }
        }
    }
    public void getHit()
    {
        if(power != 0)
        {
            power = 0;
            graphicsXOffset = 0x02000;
            graphicsYOffset = 0x02000;  
            upIsUp = true;
            invincible = true;
            invincibilityCounter = 0;
        }
        else
            die();
    }
    public void collect(Collectable c)
    {
        if(c instanceof Mushroom)
        {
            power = ((Mushroom)c).type;
            if(power == 2)
            {
                upIsUp = false;
                graphicsYOffset = 0x00000;
            }
            else 
                upIsUp = true;
            c.removeSelfFromGame();
        }
        else if(c instanceof Coin)
        {
            c.removeSelfFromGame();
            coins++;
            System.out.println("Coins: " +coins);
        }
    }
    public String graphicsString()
    {
        if(power == 0)
            return graphicsFolder +"_" +graphicsFile +(flipped ? "_f" : "");
        else if(power == 1)
            return graphicsFolder +"_" +graphicsFile + "_g" +(flipped ? "_f" : "");
        else if(power == 2)
        {
            if(!upIsUp)
                return graphicsFolder +"_" +graphicsFile + "_b" +(flipped ? "_f" : "") +"_u";
            else
                return graphicsFolder +"_" +graphicsFile + "_b" +(flipped ? "_f" : "");
        }
        else
            return "";
    }
}
