package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Game {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 cameraTarget;
    private ShapeRenderer sr;
    private FitViewport viewport;
    private TickManager tickManager;

    private Level_1 level1;

    private Player player;
    private HUD hud;

    public void initialise(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(0f, 0f, 0);
        sr = new ShapeRenderer();
        tickManager = new TickManager();

        player = new Player();
        hud = new HUD();
        hud.updateMaxHealth(player.getMaxHealth());
        hud.updateMaxStamina(player.getMaxStamina());

        level1 = new Level_1();
    }

    public void gameUpdate(){
        tickManager.update();

        while (tickManager.acceptTick()){
            logicTick();
            renderTick();
        }
    }

    public void logicTick(){
        player.logicTick(camera);
        hud.updatePlayerHealthAndStamina(player.getMaxHealth(), player.getHealth(), player.getMaxStamina(), player.getStamina());
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){ // Debug
            EventHandler.damagePlayer(25, player);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)){ // Debug
            EventHandler.healPlayer(25, player);
        }
    }

    public void renderTick(){
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        viewport.apply();
        camera.update();
        trackCamera();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // all worldly draw calls go here
        level1.drawTiles(batch);
        player.draw(batch);
        batch.end();

        batch.setProjectionMatrix(new Matrix4(new float[]{1,0,0,0, // revert projection to identity, effectively switching to screen space coordinates
                                                          0,1,0,0,
                                                          0,0,1,0,
                                                          0,0,0,1}));
        batch.begin();
        // all screen space draw calls go here
        hud.draw(batch);
        batch.end();
    }

    private void trackCamera(){
        if (!player.isLockedOn()){
            cameraTarget = new Vector3(player.getPosition().cpy().x, player.getPosition().cpy().y, 0);
        }else {

        }
        camera.position.lerp(cameraTarget, 0.25f);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    public void dispose(){
        batch.dispose();
        AssetDirectory.dispose();
    }

    public static class EventHandler{
        public static void grantSouls(int numSouls, Player player){
            player.addSouls(numSouls);
        }
        public static void damagePlayer(float damage, Player player){
            player.damageHealth(damage);
        }
        public static void healPlayer(float heal, Player player){
            player.healHealth(heal);
        }

    }
}
