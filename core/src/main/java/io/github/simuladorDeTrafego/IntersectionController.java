package io.github.simuladorDeTrafego;

import java.util.concurrent.Semaphore;

public class IntersectionController extends Thread {
    public enum LightState {
        GREEN, YELLOW, RED
    }

    // estados
    private volatile LightState verticalLight = LightState.GREEN;
    private volatile LightState horizontalLight = LightState.RED;

    private volatile long verticalStateEndTime;
    private volatile long horizontalStateEndTime;

    //Mutex para o cruzamento.
    private Semaphore intersectionMutex = new Semaphore(2); 

    private boolean rodando = true;

    @Override
    public void run() {
        while (rodando) {
            try {
                // Lógica do Semáforo (Ciclo)
                long now = System.currentTimeMillis();
                
                // Vertical Verde, Horizontal Vermelho
                verticalLight = LightState.GREEN;
                horizontalLight = LightState.RED;
                verticalStateEndTime = now + 5000;
                horizontalStateEndTime = now + 7000; // 5 + 2
                Thread.sleep(5000); 
                
                // Vertical Amarelo
                verticalLight = LightState.YELLOW;
                now = System.currentTimeMillis();
                verticalStateEndTime = now + 2000;
                Thread.sleep(2000); 
                
                // Vertical Vermelho, Horizontal Verde
                verticalLight = LightState.RED;
                horizontalLight = LightState.GREEN;
                now = System.currentTimeMillis();
                verticalStateEndTime = now + 7000;
                horizontalStateEndTime = now + 5000;
                Thread.sleep(5000);
                
                // Horizontal Amarelo
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

    // Veículo tenta entrar no cruzamento
    public void enterIntersection() throws InterruptedException {
        intersectionMutex.acquire();
    }

    // Veículo sai do cruzamento
    public void exitIntersection() {
        intersectionMutex.release();
    }

    public void setRodando(boolean rodando) {
        this.rodando = rodando;
    }
}
