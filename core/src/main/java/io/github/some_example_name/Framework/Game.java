package io.github.some_example_name.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
import io.github.some_example_name.IO.ControlsDirectory;
import io.github.some_example_name.Levels.Level;
import io.github.some_example_name.Levels.Level_1;
import io.github.some_example_name.Player.HUD;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.World.NavNode;
import io.github.some_example_name.World.Obstacle;

import java.util.ArrayList;

public class Game {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 cameraTarget;
    private ShapeRenderer sr;
    private FitViewport viewport;
    private TickManager tickManager;

    private Level_1 level1;
    private DeveloperTools developerTools;

    private Level currentLevel;

    private Player player;
    private HUD hud;

    private Entity lockedOnEntity;
    private Sprite lockDot;

    public void initialise(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(0f, 0f, 0);
        sr = new ShapeRenderer();
        tickManager = new TickManager();

        loadTextures();
        player = new Player(0);
        hud = new HUD();
        hud.updateMaxHealth(player.getMaxHealth());
        hud.updateMaxStamina(player.getMaxStamina());

        level1 = new Level_1(player, camera.position);
        currentLevel = level1;

        player.setResidentLevel(currentLevel);

        developerTools = new DeveloperTools();
    }

    public void gameUpdate(){
        tickManager.update();

        while (tickManager.acceptTick()){
            logicTick();
            renderTick();
            developerTools.navNodePlacement();
        }
    }

    public void logicTick(){
        currentLevel.logicTick();
        player.logicTick(camera);
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
        for (Entity e : currentLevel.getEntities()){
            e.collision(player); // entity-on-player collision
            player.hitboxOnHurtboxCollision(e.getDamageSources());
            for (Entity p : currentLevel.getEntities()){ // entity-on-entity collision
                if (p.getID() != e.getID()){ // no colliding with self
                    e.bodyCollision(p);
                }
            }
        }
        for (Obstacle o : currentLevel.getObstacles()){ // obstacle collision
            player.bodyCollision(o);
            for (Entity e : currentLevel.getEntities()){
                e.bodyCollision(o);
            }
        }
    }

    private void lockOn(){
        boolean isLockOnPressed = Gdx.input.isKeyJustPressed(ControlsDirectory.Combat.LOCK_ON);
        if (isLockOnPressed) {
            player.toggleLockOn();
            if (player.isLockedOn()){ // initial lock on
                lockedOnEntity = currentLevel.getEntities()[findClosestEntity(player.getPosition(), currentLevel.getEntities())];
            }
        }
        if (player.isLockedOn()){
            player.setLookTarget(lockedOnEntity.getPosition());
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
            cameraTarget = new Vector3((player.getPosition().x+lockedOnEntity.getPosition().x)/2f, (player.getPosition().y+lockedOnEntity.getPosition().y)/2f, 0); // centres camera between player and tracked enemy
        }
        camera.position.lerp(cameraTarget, 0.25f);
    }

    private void drawLockDot(){ // indicate lock-on
        if (player.isLockedOn()){
            lockDot.setPosition(lockedOnEntity.getPosition().x, lockedOnEntity.getPosition().y);
            lockDot.draw(batch);
        }
    }

    private void loadTextures(){
        float lockDotScale = 0.1f;
        Texture lockDotTexture = new Texture(Gdx.files.internal(AssetDirectory.Textures.Misc.LOCK_DOT));

        lockDot = new Sprite(lockDotTexture);
        lockDot.setSize(lockDotScale, lockDotScale);
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

    private class DeveloperTools{
        private Vector2[] currentNodeVertices = new Vector2[3];
        private int currentIndex = 0;
        private int numVertices = 0;

        private void navNodePlacement(){ // debug developer tool
            ArrayList<Vector2> environmentVertices = new ArrayList<>();
            for (Obstacle o : currentLevel.getObstacles()){
                for (Collider c : o.getBody()){
                    for (Vector2 v : c.getVertices()){
                        environmentVertices.add(v);
                    }
                }
            }
            for (NavNode n : currentLevel.getNavMesh().getNodes()){
                environmentVertices.add(n.getVertices()[0]);
                environmentVertices.add(n.getVertices()[1]);
                environmentVertices.add(n.getVertices()[2]);
            }
            Vector2 selectedVertex = environmentVertices.get(Utils.findClosestPosition(player.getWorldMousePosition(), Utils.vectorArrayListToArray(environmentVertices)));
            if (Utils.findDistance(player.getWorldMousePosition(), selectedVertex) > 0.5f){
                selectedVertex = player.getWorldMousePosition();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)){
                currentNodeVertices[currentIndex] = selectedVertex;
                currentIndex = (currentIndex + 1) % 3;
                numVertices ++;
                if (numVertices > 3){
                    numVertices = 3;
                }
            }else if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)){
                System.out.println("navNodes[i] = new NavNode(new Vector2[]{new Vector2("+currentNodeVertices[0].x+"f,"+currentNodeVertices[0].y+"f)"+", new Vector2("+currentNodeVertices[1].x+"f,"+currentNodeVertices[1].y+"f)"+", new Vector2("+currentNodeVertices[2].x+"f,"+currentNodeVertices[2].y+"f)});");
            }

            sr.setProjectionMatrix(camera.combined);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.CYAN);
            sr.circle(selectedVertex.x, selectedVertex.y, 0.1f, 20);
            if (numVertices == 1){
                sr.circle(currentNodeVertices[0].x, currentNodeVertices[0].y, 0.1f, 20);
            }
            else if (numVertices == 2){
                sr.rectLine(currentNodeVertices[(currentIndex - 1) % 3], currentNodeVertices[(currentIndex - 2) % 3], 0.1f);
            }else if (numVertices == 3){
                float[] floats = Utils.convertToPairwisePoints(currentNodeVertices);
                sr.triangle(floats[0], floats[1], floats[2], floats[3], floats[4], floats[5]);
            }
            sr.end();
            //System.out.println(currentNodeVertices[0] + " | " + currentNodeVertices[1] + " | " + currentNodeVertices[2]);
        }
    }
}
