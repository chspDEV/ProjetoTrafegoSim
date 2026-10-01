package io.github.simuladorDeTrafego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import java.util.concurrent.CopyOnWriteArrayList;

public class VehicleGenerator extends Thread {
    private boolean rodando = true;
    private TextureManager textureManager;
    private CopyOnWriteArrayList<Vehicle> vehiclesList;
    private IntersectionController intersection;

    public VehicleGenerator(TextureManager textureManager, CopyOnWriteArrayList<Vehicle> vehiclesList, IntersectionController intersection) {
        this.textureManager = textureManager;
        this.vehiclesList = vehiclesList;
        this.intersection = intersection;
    }

    @Override
    public void run() {
        while (rodando) {
            try {
                // Intervalo de spawn (0.5 a 2.5 segundos)
                Thread.sleep(MathUtils.random(500, 2500));
                
                Texture tex = textureManager.getRandomVehicle();
                if (tex != null) {
                    Direction dir = Direction.values()[MathUtils.random(0, 3)];
                    boolean isInnerLane = MathUtils.randomBoolean();
                    
                    Vehicle v = new Vehicle(tex, dir, isInnerLane, intersection);
                    vehiclesList.add(v);
                    v.start();
                }
                
                // Limpeza de veículos que já saíram da tela
                vehiclesList.removeIf(v -> !v.isRodando());
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRodando(boolean rodando) {
        this.rodando = rodando;
    }
}
