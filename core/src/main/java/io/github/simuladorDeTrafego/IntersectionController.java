package io.github.simuladorDeTrafego;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.HashMap;
import java.util.Map;

public class IntersectionController extends Thread {
    public enum LightState {
        GREEN, YELLOW, RED
    }

    public enum LaneId {
        NORTH_INNER, NORTH_OUTER,
        SOUTH_INNER, SOUTH_OUTER,
        EAST_INNER, EAST_OUTER,
        WEST_INNER, WEST_OUTER
    }

    private volatile LightState verticalLight = LightState.GREEN;
    private volatile LightState horizontalLight = LightState.RED;

    private volatile long verticalStateEndTime;
    private volatile long horizontalStateEndTime;

    private Semaphore intersectionMutex = new Semaphore(2); 

    private boolean rodando = true;

    private Map<LaneId, ConcurrentLinkedQueue<Vehicle>> laneQueues = new HashMap<>();

    public IntersectionController() {
        for (LaneId id : LaneId.values()) {
            laneQueues.put(id, new ConcurrentLinkedQueue<>());
        }
    }

    public void enqueueVehicle(LaneId laneId, Vehicle v) {
        laneQueues.get(laneId).add(v);
    }

    public void dequeueVehicle(LaneId laneId, Vehicle v) {
        laneQueues.get(laneId).remove(v);
    }

    public int getVehiclesAhead(LaneId laneId, Vehicle v) {
        int count = 0;
        for (Vehicle waitingCar : laneQueues.get(laneId)) {
            if (waitingCar == v) {
                break;
            }
            count++;
        }
        return count;
    }

    @Override
    public void run() {
        while (rodando) {
            try {
                long now = System.currentTimeMillis();
                
                verticalLight = LightState.GREEN;
                horizontalLight = LightState.RED;
                verticalStateEndTime = now + 5000;
                horizontalStateEndTime = now + 7000;
                Thread.sleep(5000); 
                
                verticalLight = LightState.YELLOW;
                now = System.currentTimeMillis();
                verticalStateEndTime = now + 2000;
                Thread.sleep(2000); 
                
                verticalLight = LightState.RED;
                horizontalLight = LightState.GREEN;
                now = System.currentTimeMillis();
                verticalStateEndTime = now + 7000;
                horizontalStateEndTime = now + 5000;
                Thread.sleep(5000);
                
                horizontalLight = LightState.YELLOW;
                now = System.currentTimeMillis();
                horizontalStateEndTime = now + 2000;
                Thread.sleep(2000);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public LightState getLightState(Direction direction) 
    {
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            return verticalLight;
        } else {
            return horizontalLight;
        }
    }

    public long getTimeRemaining(Direction direction) {
        long endTime = (direction == Direction.NORTH || direction == Direction.SOUTH) ? verticalStateEndTime : horizontalStateEndTime;
        long remaining = endTime - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    public void enterIntersection() throws InterruptedException {
        intersectionMutex.acquire();
    }

    public void exitIntersection() {
        intersectionMutex.release();
    }

    public void setRodando(boolean rodando) {
        this.rodando = rodando;
    }
}
