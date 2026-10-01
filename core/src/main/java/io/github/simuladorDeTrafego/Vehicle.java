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

    public enum TurnIntent {
        STRAIGHT, LEFT, RIGHT
    }

    private TurnIntent turnIntent;
    private boolean hasTurned = false;
    
    private IntersectionController.LaneId laneId;
    private boolean dequeued = false;

    public Vehicle(Texture textura, Direction direcao, boolean isInnerLane, IntersectionController intersection) {
        this.textura = textura;
        this.direcao = direcao;
        this.intersection = intersection;
        this.velocidade = MathUtils.random(80f, 160f);
        
        if (isInnerLane) {
            this.turnIntent = MathUtils.randomBoolean() ? TurnIntent.LEFT : TurnIntent.STRAIGHT;
        } else {
            this.turnIntent = MathUtils.randomBoolean() ? TurnIntent.RIGHT : TurnIntent.STRAIGHT;
        }
        
        setupLaneId(isInnerLane);
        setupPosition(isInnerLane);
        
        intersection.enqueueVehicle(laneId, this);
    }
    
    private void setupLaneId(boolean isInnerLane) {
        switch (direcao) {
            case NORTH: laneId = isInnerLane ? IntersectionController.LaneId.NORTH_INNER : IntersectionController.LaneId.NORTH_OUTER; break;
            case SOUTH: laneId = isInnerLane ? IntersectionController.LaneId.SOUTH_INNER : IntersectionController.LaneId.SOUTH_OUTER; break;
            case EAST:  laneId = isInnerLane ? IntersectionController.LaneId.EAST_INNER : IntersectionController.LaneId.EAST_OUTER; break;
            case WEST:  laneId = isInnerLane ? IntersectionController.LaneId.WEST_INNER : IntersectionController.LaneId.WEST_OUTER; break;
        }
    }

    private void setupPosition(boolean isInnerLane) {
        switch (direcao) {
            case NORTH:
                x = isInnerLane ? Config.LANE_NORTH_INNER_X : Config.LANE_NORTH_OUTER_X;
                y = -100;
                rotation = 0;
                break;
            case SOUTH:
                x = isInnerLane ? Config.LANE_SOUTH_INNER_X : Config.LANE_SOUTH_OUTER_X;
                y = Config.SCREEN_HEIGHT + 100;
                rotation = 180;
                break;
            case EAST:
                x = -100;
                y = isInnerLane ? Config.LANE_EAST_INNER_Y : Config.LANE_EAST_OUTER_Y;
                rotation = -90;
                break;
            case WEST:
                x = Config.SCREEN_WIDTH + 100;
                y = isInnerLane ? Config.LANE_WEST_INNER_Y : Config.LANE_WEST_OUTER_Y;
                rotation = 90;
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
                
                if (insideIntersection && !hasTurned && turnIntent != TurnIntent.STRAIGHT) {
                    processTurnLogic();
                }
                
                switch (direcao) {
                    case NORTH: y += (velocidade * Config.globalSpeedMultiplier) * 0.016f; break;
                    case SOUTH: y -= (velocidade * Config.globalSpeedMultiplier) * 0.016f; break;
                    case EAST:  x += (velocidade * Config.globalSpeedMultiplier) * 0.016f; break;
                    case WEST:  x -= (velocidade * Config.globalSpeedMultiplier) * 0.016f; break;
                }

                if (insideIntersection && hasExitedIntersection()) {
                    intersection.exitIntersection();
                    insideIntersection = false;
                    crossedIntersection = true;
                }

                if (isOutOfBounds()) {
                    rodando = false;
                }

                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        if (!dequeued) {
            intersection.dequeueVehicle(laneId, this);
        }
    }

    private void processTurnLogic() {
        float margin = 5f;
        switch (direcao) {
            case NORTH:
                if (turnIntent == TurnIntent.LEFT && y >= Config.LANE_WEST_INNER_Y - margin) {
                    direcao = Direction.WEST; rotation = 90; y = Config.LANE_WEST_INNER_Y; hasTurned = true;
                } else if (turnIntent == TurnIntent.RIGHT && y >= Config.LANE_EAST_OUTER_Y - margin) {
                    direcao = Direction.EAST; rotation = -90; y = Config.LANE_EAST_OUTER_Y; hasTurned = true;
                }
                break;
            case SOUTH:
                if (turnIntent == TurnIntent.LEFT && y <= Config.LANE_EAST_INNER_Y + margin) {
                    direcao = Direction.EAST; rotation = -90; y = Config.LANE_EAST_INNER_Y; hasTurned = true;
                } else if (turnIntent == TurnIntent.RIGHT && y <= Config.LANE_WEST_OUTER_Y + margin) {
                    direcao = Direction.WEST; rotation = 90; y = Config.LANE_WEST_OUTER_Y; hasTurned = true;
                }
                break;
            case EAST:
                if (turnIntent == TurnIntent.LEFT && x >= Config.LANE_NORTH_INNER_X - margin) {
                    direcao = Direction.NORTH; rotation = 0; x = Config.LANE_NORTH_INNER_X; hasTurned = true;
                } else if (turnIntent == TurnIntent.RIGHT && x >= Config.LANE_SOUTH_OUTER_X - margin) {
                    direcao = Direction.SOUTH; rotation = 180; x = Config.LANE_SOUTH_OUTER_X; hasTurned = true;
                }
                break;
            case WEST:
                if (turnIntent == TurnIntent.LEFT && x <= Config.LANE_SOUTH_INNER_X + margin) {
                    direcao = Direction.SOUTH; rotation = 180; x = Config.LANE_SOUTH_INNER_X; hasTurned = true;
                } else if (turnIntent == TurnIntent.RIGHT && x <= Config.LANE_NORTH_OUTER_X + margin) {
                    direcao = Direction.NORTH; rotation = 0; x = Config.LANE_NORTH_OUTER_X; hasTurned = true;
                }
                break;
        }
    }

    private void checkIntersectionLogic() throws InterruptedException {
        boolean atStopLine = false;
        int vehiclesAhead = intersection.getVehiclesAhead(laneId, this);
        float offset = vehiclesAhead * (Config.VEHICLE_HEIGHT + 10f);
        
        if (direcao == Direction.NORTH && (y + Config.VEHICLE_HEIGHT >= Config.INTERSECTION_BOTTOM_Y - offset - 5) && (y < Config.INTERSECTION_BOTTOM_Y - offset)) atStopLine = true;
        if (direcao == Direction.SOUTH && y <= Config.INTERSECTION_TOP_Y + offset + 5 && y > Config.INTERSECTION_TOP_Y + offset) atStopLine = true;
        if (direcao == Direction.EAST && (x + Config.VEHICLE_HEIGHT >= Config.INTERSECTION_LEFT_X - offset - 5) && (x < Config.INTERSECTION_LEFT_X - offset)) atStopLine = true;
        if (direcao == Direction.WEST && x <= Config.INTERSECTION_RIGHT_X + offset + 5 && x > Config.INTERSECTION_RIGHT_X + offset) atStopLine = true;

        if (atStopLine && !insideIntersection) {
            while (rodando && (intersection.getLightState(direcao) != IntersectionController.LightState.GREEN || intersection.getVehiclesAhead(laneId, this) > 0)) {
                Thread.sleep(30); 
                
                vehiclesAhead = intersection.getVehiclesAhead(laneId, this);
                if (vehiclesAhead == 0 && intersection.getLightState(direcao) == IntersectionController.LightState.GREEN) {
                    break;
                } else {
                    offset = vehiclesAhead * (Config.VEHICLE_HEIGHT + 10f);
                    switch (direcao) {
                        case NORTH: if (y + Config.VEHICLE_HEIGHT < Config.INTERSECTION_BOTTOM_Y - offset) y += (velocidade * Config.globalSpeedMultiplier) * 0.03f; break;
                        case SOUTH: if (y > Config.INTERSECTION_TOP_Y + offset) y -= (velocidade * Config.globalSpeedMultiplier) * 0.03f; break;
                        case EAST:  if (x + Config.VEHICLE_HEIGHT < Config.INTERSECTION_LEFT_X - offset) x += (velocidade * Config.globalSpeedMultiplier) * 0.03f; break;
                        case WEST:  if (x > Config.INTERSECTION_RIGHT_X + offset) x -= (velocidade * Config.globalSpeedMultiplier) * 0.03f; break;
                    }
                }
            }
            
            if (!rodando) return;
            
            intersection.enterIntersection();
            insideIntersection = true;
            
            intersection.dequeueVehicle(laneId, this);
            dequeued = true;
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
