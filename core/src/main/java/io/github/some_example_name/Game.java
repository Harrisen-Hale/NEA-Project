package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
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

    private Level currentLevel;

    private Player player;
    private HUD hud;

    private int lockedEntityIndex;
    private Vector2 lockedEntityPosition;
    private Sprite lockDot;

    public void initialise(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(0f, 0f, 0);
        sr = new ShapeRenderer();
        tickManager = new TickManager();

        player = new Player(0);
        hud = new HUD();
        hud.updateMaxHealth(player.getMaxHealth());
        hud.updateMaxStamina(player.getMaxStamina());
        loadTextures();


        level1 = new Level_1(player);
        currentLevel = level1;
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
        currentLevel.logicTick();
        collision();
        hud.updatePlayerHealthAndStamina(player.getMaxHealth(), player.getHealth(), player.getMaxStamina(), player.getStamina());
        lockOn();
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
            // first render layer (background, entity bodies etc.)
        currentLevel.drawAllBodies(batch);
        player.draw(batch);

            // second render layer (effects)
        drawLockDot();
        currentLevel.drawAllEffects(batch);
        batch.end();

        batch.setProjectionMatrix(new Matrix4(new float[]{1,0,0,0, // revert projection to identity, effectively switching to screen space coordinates
                                                          0,1,0,0,
                                                          0,0,1,0,
                                                          0,0,0,1}));
        batch.begin();
        // all screen space draw calls go here
        hud.draw(batch);
        batch.end();

        // debug render calls
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        currentLevel.drawAllDebug(sr);
        player.drawDebug(sr);
        sr.end();
    }

    private void collision(){
        for (Entity e : currentLevel.getEntities()){ // player-on-entity collision
            player.collision(e.getBody(), e.getDamageSources());
            e.collision(player.getBody(), player.getDamageSources());
        }
    }

    private void lockOn(){
        boolean isLockOnPressed = Gdx.input.isKeyJustPressed(ControlsDirectory.Combat.LOCK_ON);
        if (isLockOnPressed) {
            player.toggleLockOn();
            if (player.isLockedOn()){ // initial lock on
                lockedEntityIndex = findClosestEntity(player.getPosition(), currentLevel.getEntities());
            }
        }
        if (player.isLockedOn()){
            lockedEntityPosition = currentLevel.getEntities()[lockedEntityIndex].getPosition();
            player.setLookTarget(lockedEntityPosition);
        }
    }

    private int findClosestEntity(Vector2 refPos, Entity[] entities){ // returns index of closest entity
        Vector2[] entityPositions = new Vector2[entities.length];
        for (int i = 0; i < entities.length; i++){
            entityPositions[i] = entities[i].getPosition();
        }

        return Utils.findClosestPosition(refPos, entityPositions);
    }

    private void trackCamera(){
        if (!player.isLockedOn()){
            cameraTarget = new Vector3(player.getPosition().x, player.getPosition().y, 0);
        }else {
            cameraTarget = new Vector3((player.getPosition().x+lockedEntityPosition.x)/2f, (player.getPosition().y+lockedEntityPosition.y)/2f, 0); // centres camera between player and tracked enemy
        }
        camera.position.lerp(cameraTarget, 0.25f);
    }

    private void drawLockDot(){ // indicate lock-on
        if (player.isLockedOn()){
            lockDot.setPosition(lockedEntityPosition.x-0.05f, lockedEntityPosition.y-0.05f);
            lockDot.draw(batch);
        }
    }

    private void loadTextures(){
        Texture lockDotTexture = new Texture(Gdx.files.internal(AssetDirectory.Textures.Misc.LOCK_DOT));

        lockDot = new Sprite(lockDotTexture);
        lockDot.setSize(0.1f, 0.1f);
        lockDot.setOriginCenter();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    public void dispose(){
        batch.dispose();
        AssetDirectory.dispose();
        sr.dispose();
    }

    public static class EventHandler{
        public static void grantSouls(int numSouls, Player player){
            player.addSouls(numSouls);
        }
        public static void damagePlayer(float damage, Player player){
            if (player.isVulnerable()) {
                player.damageHealth(damage);
            }
        }
        public static void healPlayer(float heal, Player player){
            player.healHealth(heal);
        }

    }
}
