package io.github.simuladorDeTrafego;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    
    
    private CopyOnWriteArrayList<Explosion> explosions = new CopyOnWriteArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        textureManager = new TextureManager();
        vehicles = new CopyOnWriteArrayList<>();
        
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(Config.FONT_SIZE);

        intersection = new IntersectionController();
        intersection.start();

        generator = new VehicleGenerator(textureManager, vehicles, intersection);
        generator.start();
    }

    @Override
    public void render() 
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            Config.globalSpeedMultiplier = Math.max(0.1f, Config.globalSpeedMultiplier - 0.2f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            Config.globalSpeedMultiplier += 0.2f;
        }

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v1 = vehicles.get(i);
            if (!v1.isRodando()) continue;
            
            for (int j = i + 1; j < vehicles.size(); j++) {
                Vehicle v2 = vehicles.get(j);
                if (!v2.isRodando()) continue;
                
                float dx = v1.getX() - v2.getX();
                float dy = v1.getY() - v2.getY();
                float dist = (float)Math.sqrt(dx*dx + dy*dy);
                
                if (dist < Config.VEHICLE_WIDTH) {
                    v1.setRodando(false);
                    v2.setRodando(false);
                    Config.accidentCount++;
                    explosions.add(new Explosion((v1.getX() + v2.getX()) / 2f, (v1.getY() + v2.getY()) / 2f));
                }
            }
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        
        //FUNDO
        Texture bg = textureManager.getBackground();
        if (bg != null) {
            batch.draw(bg, 0, 0, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        }

        //SEMAFAROS
        
        drawTrafficLightAndTimer(Direction.SOUTH, Config.LIGHT_NORTH_SUL_X, Config.LIGHT_NORTH_SUL_Y);
        drawTrafficLightAndTimer(Direction.NORTH, Config.LIGHT_SOUTH_NORTE_X, Config.LIGHT_SOUTH_NORTE_Y);
        drawTrafficLightAndTimer(Direction.EAST, Config.LIGHT_WEST_LESTE_X, Config.LIGHT_WEST_LESTE_Y);
        drawTrafficLightAndTimer(Direction.WEST, Config.LIGHT_EAST_OESTE_X, Config.LIGHT_EAST_OESTE_Y);

        //VEICULOS
        for (Vehicle v : vehicles) {
            if (v.getTextura() != null) {
                batch.setColor(0f, 0f, 0f, 0.4f);
                batch.draw(
                    v.getTextura(), 
                    v.getX() + 4f, v.getY() - 4f, 
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
        batch.setColor(1f, 1f, 1f, 1f);

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
        
        //EXPLOSAO
        Texture texExp = textureManager.getExplosionTexture();
        long now = System.currentTimeMillis();
        for (Explosion exp : explosions) {
            if (now > exp.expireTime) {
                explosions.remove(exp);
            } else if (texExp != null) {
                batch.draw(texExp, exp.x - 32, exp.y - 32, 64, 64);
            }
        }
        
        //HUD
        font.draw(batch, "Veiculos na tela: " + vehicles.size(), 20, Config.SCREEN_HEIGHT - 20);
        font.draw(batch, "Acidentes: " + Config.accidentCount, 20, Config.SCREEN_HEIGHT - 50);
        font.draw(batch, "Velocidade (1 p/ reduzir, 2 p/ aumentar): " + String.format("%.1fx", Config.globalSpeedMultiplier), 20, Config.SCREEN_HEIGHT - 80);
        
        batch.end();
    }
    
    private void drawTrafficLightAndTimer(Direction dir, float x, float y) {
        IntersectionController.LightState state = intersection.getLightState(dir);
        Texture tex = textureManager.getTrafficLightTexture(state);
        int offsetText = 26;
        
        if (tex != null) {
            batch.draw(tex, x, y, Config.LIGHT_WIDTH, Config.LIGHT_HEIGHT);
            
            String text = "...";
            if (state != IntersectionController.LightState.RED) {
                long remainingMillis = intersection.getTimeRemaining(dir);
                text = String.valueOf((remainingMillis / 1000) + 1);
            }
            font.draw(batch, text, x + offsetText, y + Config.LIGHT_HEIGHT + 20);
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
