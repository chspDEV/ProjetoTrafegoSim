package io.github.simuladorDeTrafego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Vehicle extends Thread {
    private volatile float x, y;
    private volatile float rotation;
    
    private float velocidade;
    private Texture textura;
    private boolean rodando = true;
    
    private Direction direcao;
    private IntersectionController intersection;
    
    private boolean insideIntersection = false;
    private boolean crossedIntersection = false;

    public Vehicle(Texture textura, Direction direcao, boolean isInnerLane, IntersectionController intersection) {
        this.textura = textura;
        this.direcao = direcao;
        this.intersection = intersection;
        this.velocidade = MathUtils.random(80f, 160f); // Pouco mais rápido para o tamanho da tela maior
        
        setupPosition(isInnerLane);
    }

    private void setupPosition(boolean isInnerLane) {
        switch (direcao) {
            case NORTH:
                x = isInnerLane ? Config.LANE_NORTH_INNER_X : Config.LANE_NORTH_OUTER_X;
                y = -100;
                rotation = 0; // Aponta pra cima
                break;
            case SOUTH:
                x = isInnerLane ? Config.LANE_SOUTH_INNER_X : Config.LANE_SOUTH_OUTER_X;
                y = Config.SCREEN_HEIGHT + 100;
                rotation = 180; // Aponta pra baixo
                break;
            case EAST:
                x = -100;
                y = isInnerLane ? Config.LANE_EAST_INNER_Y : Config.LANE_EAST_OUTER_Y;
                rotation = -90; // Aponta pra direita
                break;
            case WEST:
                x = Config.SCREEN_WIDTH + 100;
                y = isInnerLane ? Config.LANE_WEST_INNER_Y : Config.LANE_WEST_OUTER_Y;
                rotation = 90; // Aponta pra esquerda
                break;
        }
    }

    @Override
    public void run() {
        while (rodando) {
            try {
                if (!crossedIntersection) {
                    checkIntersectionLogic();
                }
                
                // Movimento
                switch (direcao) {
                    case NORTH: y += velocidade * 0.016f; break;
                    case SOUTH: y -= velocidade * 0.016f; break;
                    case EAST:  x += velocidade * 0.016f; break;
                    case WEST:  x -= velocidade * 0.016f; break;
                }

                // Verifica se já passou completamente do cruzamento para liberar o Mutex
                if (insideIntersection && hasExitedIntersection()) {
                    intersection.exitIntersection();
                    insideIntersection = false;
                    crossedIntersection = true;
                }

                // Destrói thread se saiu da tela
                if (isOutOfBounds()) {
                    rodando = false;
                }

                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIntersectionLogic() throws InterruptedException {
        boolean atStopLine = false;
        
        // Define as linhas de parada com base no Config
        if (direcao == Direction.NORTH && (y + Config.VEHICLE_HEIGHT >= Config.INTERSECTION_BOTTOM_Y - 5) && (y < Config.INTERSECTION_BOTTOM_Y)) atStopLine = true;
        if (direcao == Direction.SOUTH && y <= Config.INTERSECTION_TOP_Y + 5 && y > Config.INTERSECTION_TOP_Y) atStopLine = true;
        if (direcao == Direction.EAST && (x + Config.VEHICLE_HEIGHT >= Config.INTERSECTION_LEFT_X - 5) && (x < Config.INTERSECTION_LEFT_X)) atStopLine = true;
        if (direcao == Direction.WEST && x <= Config.INTERSECTION_RIGHT_X + 5 && x > Config.INTERSECTION_RIGHT_X) atStopLine = true;

        if (atStopLine && !insideIntersection) {
            // Se o sinal não estiver verde, fica travado aqui num loop dormindo
            while (intersection.getLightState(direcao) != IntersectionController.LightState.GREEN) {
                Thread.sleep(50); 
            }
            // Quando ficar verde, tenta pegar o Mutex para entrar no cruzamento
            intersection.enterIntersection();
            insideIntersection = true;
        }
    }

    private boolean hasExitedIntersection() {
        if (direcao == Direction.NORTH && y > Config.INTERSECTION_TOP_Y) return true;
        if (direcao == Direction.SOUTH && y + Config.VEHICLE_HEIGHT < Config.INTERSECTION_BOTTOM_Y) return true;
        if (direcao == Direction.EAST && x > Config.INTERSECTION_RIGHT_X) return true;
        if (direcao == Direction.WEST && x + Config.VEHICLE_HEIGHT < Config.INTERSECTION_LEFT_X) return true;
        return false;
    }

    private boolean isOutOfBounds() {
        return (x < -200 || x > Config.SCREEN_WIDTH + 200 || y < -200 || y > Config.SCREEN_HEIGHT + 200);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getRotation() { return rotation; }
    public Texture getTextura() { return textura; }
    public boolean isRodando() { return rodando; }
    public void setRodando(boolean rodando) { this.rodando = rodando; }
}
