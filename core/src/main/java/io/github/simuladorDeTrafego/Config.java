package io.github.simuladorDeTrafego;

public class Config {
	
	//TELA
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final float FONT_SIZE = 1.5f;
    
    public static volatile float globalSpeedMultiplier = 1.0f;
    public static volatile int accidentCount = 0;
    
    //VEICULOS
    public static final int MAX_VEHICLES = 300; 
    public static final float VEHICLE_WIDTH = 24f;
    public static final float VEHICLE_HEIGHT = 48f;
    
    //TAMANHO SEM
    public static final float LIGHT_WIDTH = 70f;
    public static final float LIGHT_HEIGHT = 40f;
    
    //RUAS
    
    
    public static final float LANE_NORTH_INNER_X = 660f; //baixo esquerda
    public static final float LANE_NORTH_OUTER_X = 700f; //baixo direita
    
    public static final float LANE_SOUTH_INNER_X = 600f; // cima direita
    public static final float LANE_SOUTH_OUTER_X = 550f; // cima esquerda
    
    public static final float LANE_EAST_INNER_Y = 310f; // esq cima
    public static final float LANE_EAST_OUTER_Y = 270f; // esq baixo
    
    public static final float LANE_WEST_INNER_Y = 365f; // dir  baixo
    public static final float LANE_WEST_OUTER_Y = 410f; // dir cima
    
    public static final float INTERSECTION_BOTTOM_Y = 250f;
    public static final float INTERSECTION_TOP_Y = 450f;
    
    public static final float INTERSECTION_LEFT_X = 530f;
    public static final float INTERSECTION_RIGHT_X = 740f;
    
    // LINHAS DE PARADA
    public static final float STOP_LINE_NORTH = 200f; // Carros subindo (norte) param aqui
    public static final float STOP_LINE_SOUTH = 525f; // Carros descendo (sul) param aqui
    public static final float STOP_LINE_EAST = 465f;  // Carros indo pra direita (leste) param aqui
    public static final float STOP_LINE_WEST = 825f;  // Carros indo pra esquerda (oeste) param aqui
    
    //SEMAFAROS
    
    //esquerda cima
    public static final float LIGHT_NORTH_SUL_X = 455f;
    public static final float LIGHT_NORTH_SUL_Y = 460f;
    
    //direita cima
    public static final float LIGHT_EAST_OESTE_X = 755f;
    public static final float LIGHT_EAST_OESTE_Y = 460f;
    
    //direita baixo
    public static final float LIGHT_SOUTH_NORTE_X = 755f; 
    public static final float LIGHT_SOUTH_NORTE_Y = 220f;
    
    //esquerda baixo
    public static final float LIGHT_WEST_LESTE_X = 455f;
    public static final float LIGHT_WEST_LESTE_Y = 220f;
    
    
}
