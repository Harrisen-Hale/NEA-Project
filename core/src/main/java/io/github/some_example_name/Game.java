package io.github.some_example_name;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Game {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private ShapeRenderer sr;
    private FitViewport viewport;
    private TickManager tickManager;

    private Level_1 level1;

    private Player player;

    public void initialise(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(0f, 0f, 0);
        sr = new ShapeRenderer();
        tickManager = new TickManager();

        player = new Player();

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
    }

    public void renderTick(){
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        viewport.apply();
        camera.update();
        centreCamera();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // all draw calls go here
        level1.drawTiles(batch);
        player.draw(batch);

        batch.end();
    }

    private void centreCamera(){ // will be generalised to accept an anchor later
        Vector2 target = player.getPosition().cpy();
        camera.position.set(target.x, target.y, 0);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    public void dispose(){
        batch.dispose();
    }

}
