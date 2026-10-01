package io.github.simuladorDeTrafego;

public class Config {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final float FONT_SIZE = 1.5f;
    
    public static volatile float globalSpeedMultiplier = 1.0f;
    public static volatile int accidentCount = 0;
    
    public static final int MAX_VEHICLES = 300; 
    public static final float VEHICLE_WIDTH = 24f;
    public static final float VEHICLE_HEIGHT = 48f;
    
    public static final float LIGHT_WIDTH = 25f;
    public static final float LIGHT_HEIGHT = 70f;
    
    public static final float LANE_NORTH_INNER_X = 660f;
    public static final float LANE_NORTH_OUTER_X = 710f;
    
    public static final float LANE_SOUTH_INNER_X = 610f;
    public static final float LANE_SOUTH_OUTER_X = 560f;
    
    public static final float LANE_EAST_INNER_Y = 320f;
    public static final float LANE_EAST_OUTER_Y = 270f;
    
    public static final float LANE_WEST_INNER_Y = 380f;
    public static final float LANE_WEST_OUTER_Y = 430f;
    
    public static final float INTERSECTION_BOTTOM_Y = 250f;
    public static final float INTERSECTION_TOP_Y = 450f;
    
    public static final float INTERSECTION_LEFT_X = 530f;
    public static final float INTERSECTION_RIGHT_X = 740f;
    
    public static final float LIGHT_NORTH_SUL_X = 500f;
    public static final float LIGHT_NORTH_SUL_Y = 460f;
    
    public static final float LIGHT_SOUTH_NORTE_X = 750f; 
    public static final float LIGHT_SOUTH_NORTE_Y = 190f;
    
    public static final float LIGHT_WEST_LESTE_X = 500f;
    public static final float LIGHT_WEST_LESTE_Y = 190f;
    
    public static final float LIGHT_EAST_OESTE_X = 750f;
    public static final float LIGHT_EAST_OESTE_Y = 460f;
}
