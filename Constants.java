
/**
 * Write a description of class Constants here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Constants
{
    public static final int FPS = 60;
    //Mario Movement
    public static final int minimum_walk_speed = 0x00130;
    public static final int walking_acceleration = 0x00098;
    public static final int running_acceleration = 0x000e4;
    public static final int release_deceleration = 0x00d0;
    public static final int skidding_deceleration = 0x01a0;
    public static final int maximum_walk_speed = 0x01900;
    public static final int maximum_running_speed = 0x02900;
    public static final int skid_turnaround_speed = 0x00900;
    //Mario Bouncing
    public static final int mario_bounce_speed = -0x04300;// -0x04000;
    public static final int green_mario_bounce_speed = -0x05300;// -0x04000;
    //Mario Jumping
    public static final int jumping_fast_speed = 0x01900;
    public static final int jumping_slow_acceleration = 0x00098;
    public static final int jumping_fast_acceleration = 0x000E4;
    //Mario Animation
    public static final int run_anim_frame = 2;
    public static final int walk_anim_frame = 4;
    //Block Graphics
    public static final int block_width = 0x10000;
    public static final int block_rendering_width = 50;
    //Enemy Modifiers
    public static final int bullet_speed = 0x01000;
    public static final int piranha_speed = 0x00900;
    public static final int enemy_walking_speed = 0x00A00; //0x00A00;
    public static final int enemy_gravity = 0x02800; //0x02800
    public static final int enemy_max_falling_speed = 0x05000; //0x05000
    public static final int turtle_shell_speed = 0x03000;
}
