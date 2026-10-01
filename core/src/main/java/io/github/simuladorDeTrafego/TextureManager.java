package io.github.simuladorDeTrafego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;

import java.io.File;

public class TextureManager {
    private Array<Texture> texturasVeiculos = new Array<>();
    private Texture background;
    private Texture texVerde, texAmarelo, texVermelho, texExplosao;

    public TextureManager() {
        loadVehicles();
        loadBackground();
        loadTrafficLights();
        loadExplosion();
    }
    
    private void loadExplosion() {
        File assetsDir = findAssetsFolder();
        if (assetsDir == null) return;
        
        File fExp = new File(assetsDir, "explosao.png");
        if (fExp.exists()) {
            texExplosao = new Texture(Gdx.files.absolute(fExp.getAbsolutePath()));
        }
    }

    private File findAssetsFolder() {
        File dir = new File(System.getProperty("user.dir"));
        System.out.println("[TextureManager] Working directory: " + dir.getAbsolutePath());

        if (dir.getName().equals("assets")) {
            return dir;
        }

        for (int i = 0; i < 5; i++) {
            File candidato = new File(dir, "assets");
            if (candidato.exists() && candidato.isDirectory()) {
                System.out.println("[TextureManager] Pasta assets encontrada em: " + candidato.getAbsolutePath());
                return candidato;
            }
            dir = dir.getParentFile();
            if (dir == null) break;
        }

        System.err.println("[TextureManager] Nao foi possivel localizar a pasta 'assets'.");
        return null;
    }

    private void loadTrafficLights() {
        File assetsDir = findAssetsFolder();
        if (assetsDir == null) return;
        
        File fVerde = new File(assetsDir, "semafaro_verde.png");
        File fAmarelo = new File(assetsDir, "semafaro_amarelo.png");
        File fVermelho = new File(assetsDir, "semafaro_vermelho.png");
        
        if (fVerde.exists()) texVerde = new Texture(Gdx.files.absolute(fVerde.getAbsolutePath()));
        if (fAmarelo.exists()) texAmarelo = new Texture(Gdx.files.absolute(fAmarelo.getAbsolutePath()));
        if (fVermelho.exists()) texVermelho = new Texture(Gdx.files.absolute(fVermelho.getAbsolutePath()));
    }

    private void loadVehicles() {
        File assetsDir = findAssetsFolder();
        if (assetsDir == null) return;

        File pastaVeiculosFile = new File(assetsDir, "veiculos");
        if (!pastaVeiculosFile.exists() || !pastaVeiculosFile.isDirectory()) {
            System.err.println("[TextureManager] Pasta 'veiculos' nao encontrada em: " + pastaVeiculosFile.getAbsolutePath());
            return;
        }

        FileHandle pastaVeiculos = Gdx.files.absolute(pastaVeiculosFile.getAbsolutePath());
        FileHandle[] arquivos = pastaVeiculos.list();

        for (FileHandle arquivo : arquivos) {
            if (arquivo.extension().equalsIgnoreCase("png") || arquivo.extension().equalsIgnoreCase("jpg")) {
                Texture textura = new Texture(arquivo);
                texturasVeiculos.add(textura);
                System.out.println("[TextureManager] Veiculo carregado: " + arquivo.name());
            }
        }

        if (texturasVeiculos.size == 0) {
            System.err.println("[TextureManager] Nenhum arquivo de imagem encontrado em: " + pastaVeiculosFile.getAbsolutePath());
        }
    }

    private void loadBackground() {
        File assetsDir = findAssetsFolder();
        if (assetsDir == null) return;

        File bgFile = new File(assetsDir, "background.png");
        if (bgFile.exists()) {
            background = new Texture(Gdx.files.absolute(bgFile.getAbsolutePath()));
            System.out.println("[TextureManager] Fundo carregado: " + bgFile.getAbsolutePath());
        } else {
            System.err.println("[TextureManager] 'background.png' nao encontrado em: " + bgFile.getAbsolutePath());
        }
    }

    public Texture getTrafficLightTexture(IntersectionController.LightState state) {
        switch (state) {
            case GREEN: return texVerde;
            case YELLOW: return texAmarelo;
            case RED: return texVermelho;
        }
        return texVermelho;
    }

    public Texture getRandomVehicle() {
        if (texturasVeiculos.size > 0) {
            int index = MathUtils.random(texturasVeiculos.size - 1);
            return texturasVeiculos.get(index);
        }
        return null;
    }

    public Texture getExplosionTexture() {
        return texExplosao;
    }

    public Texture getBackground() {
        return background;
    }

    public void dispose() {
        for (Texture t : texturasVeiculos) {
            t.dispose();
        }
        if (background != null) background.dispose();
        if (texVerde != null) texVerde.dispose();
        if (texAmarelo != null) texAmarelo.dispose();
        if (texVermelho != null) texVermelho.dispose();
    }
}
