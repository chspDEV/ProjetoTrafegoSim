package io.github.simuladorDeTrafego;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private TextureManager textureManager;
    private CopyOnWriteArrayList<Vehicle> vehicles;
    
    private IntersectionController intersection;
    private VehicleGenerator generator;
    
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        textureManager = new TextureManager();
        vehicles = new CopyOnWriteArrayList<>();
        
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        intersection = new IntersectionController();
        intersection.start();

        generator = new VehicleGenerator(textureManager, vehicles, intersection);
        generator.start();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        
        // FUNDO
        Texture bg = textureManager.getBackground();
        if (bg != null) {
            batch.draw(bg, 0, 0, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        }

        // SEMAFAROS E CONTADOR
        drawTrafficLightAndTimer(Direction.SOUTH, Config.LIGHT_NORTH_SUL_X, Config.LIGHT_NORTH_SUL_Y);
        drawTrafficLightAndTimer(Direction.NORTH, Config.LIGHT_SOUTH_NORTE_X, Config.LIGHT_SOUTH_NORTE_Y);
        drawTrafficLightAndTimer(Direction.EAST, Config.LIGHT_WEST_LESTE_X, Config.LIGHT_WEST_LESTE_Y);
        drawTrafficLightAndTimer(Direction.WEST, Config.LIGHT_EAST_OESTE_X, Config.LIGHT_EAST_OESTE_Y);

        // VEICULOS
        for (Vehicle v : vehicles) {
            if (v.getTextura() != null) {
                batch.draw(
                    v.getTextura(), 
                    v.getX(), v.getY(), 
                    Config.VEHICLE_WIDTH / 2f, Config.VEHICLE_HEIGHT / 2f, 
                    Config.VEHICLE_WIDTH, Config.VEHICLE_HEIGHT, 
                    1f, 1f, 
                    v.getRotation(), 
                    0, 0, 
                    v.getTextura().getWidth(), v.getTextura().getHeight(), 
                    false, false
                );
            }
        }
        
        batch.end();
    }
    
    private void drawTrafficLightAndTimer(Direction dir, float x, float y) {
        IntersectionController.LightState state = intersection.getLightState(dir);
        Texture tex = textureManager.getTrafficLightTexture(state);
        
        if (tex != null) {
            batch.draw(tex, x, y, Config.LIGHT_WIDTH, Config.LIGHT_HEIGHT);
            
            String text = "...";
            if (state != IntersectionController.LightState.RED) {
                long remainingMillis = intersection.getTimeRemaining(dir);
                text = String.valueOf((remainingMillis / 1000) + 1); // Segundos restantes
            }
            // Desenha o timer em cima do semáforo
            font.draw(batch, text, x, y + Config.LIGHT_HEIGHT + 20);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        textureManager.dispose();
        font.dispose();
        intersection.setRodando(false);
        generator.setRodando(false);
        for (Vehicle v : vehicles) {
            v.setRodando(false);
        }
    }
}
