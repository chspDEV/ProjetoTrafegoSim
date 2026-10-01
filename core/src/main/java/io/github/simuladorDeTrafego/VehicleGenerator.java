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
                Thread.sleep(MathUtils.random(250, 1000));
                
                Texture tex = textureManager.getRandomVehicle();
                if (tex != null && vehiclesList.size() < Config.MAX_VEHICLES) {
                    Direction dir = Direction.values()[MathUtils.random(0, 3)];
                    boolean isInnerLane = MathUtils.randomBoolean();
                    
                    Vehicle v = new Vehicle(tex, dir, isInnerLane, intersection);
                    vehiclesList.add(v);
                    v.start();
                }
                
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
