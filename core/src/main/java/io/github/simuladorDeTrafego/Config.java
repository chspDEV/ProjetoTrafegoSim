package io.github.simuladorDeTrafego;

public class Config {
    // Tela
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    
    // Veículos
    public static final float VEHICLE_WIDTH = 24f;
    public static final float VEHICLE_HEIGHT = 48f;
    
    // Tamanho dos semáforos no desenho
    public static final float LIGHT_WIDTH = 25f;
    public static final float LIGHT_HEIGHT = 70f;
    
    // Posições de spawn e faixas dos veículos (Ajuste fino aqui)
    // Coordenadas X para quem anda na vertical
    public static final float LANE_NORTH_INNER_X = 660f;
    public static final float LANE_NORTH_OUTER_X = 710f;
    
    public static final float LANE_SOUTH_INNER_X = 610f;
    public static final float LANE_SOUTH_OUTER_X = 560f;
    
    // Coordenadas Y para quem anda na horizontal
    public static final float LANE_EAST_INNER_Y = 320f;
    public static final float LANE_EAST_OUTER_Y = 270f;
    
    public static final float LANE_WEST_INNER_Y = 380f;
    public static final float LANE_WEST_OUTER_Y = 430f;
    
    // Limites do Cruzamento (Onde os carros param e onde eles liberam o Mutex)
    // Linhas horizontais (Eixo Y)
    public static final float INTERSECTION_BOTTOM_Y = 250f;
    public static final float INTERSECTION_TOP_Y = 450f;
    
    // Linhas verticais (Eixo X)
    public static final float INTERSECTION_LEFT_X = 530f;
    public static final float INTERSECTION_RIGHT_X = 740f;
    
    // Posições dos Semáforos no desenho
    public static final float LIGHT_NORTH_SUL_X = 500f; // Semáforo de quem vem do norte descendo pro sul
    public static final float LIGHT_NORTH_SUL_Y = 460f;
    
    public static final float LIGHT_SOUTH_NORTE_X = 750f; // Semáforo de quem vem do sul subindo pro norte
    public static final float LIGHT_SOUTH_NORTE_Y = 190f;
    
    public static final float LIGHT_WEST_LESTE_X = 500f; // Semáforo de quem vem do oeste indo pro leste
    public static final float LIGHT_WEST_LESTE_Y = 190f;
    
    public static final float LIGHT_EAST_OESTE_X = 750f; // Semáforo de quem vem do leste indo pro oeste
    public static final float LIGHT_EAST_OESTE_Y = 460f;
}
