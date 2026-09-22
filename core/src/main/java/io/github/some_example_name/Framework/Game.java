package io.github.some_example_name.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.some_example_name.IO.ControlsDirectory;
import io.github.some_example_name.Levels.Level;
import io.github.some_example_name.Levels.Level_1;
import io.github.some_example_name.Menus.MainMenu;
import io.github.some_example_name.Menus.Menu;
import io.github.some_example_name.Player.HUD;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.World.Obstacle;

public class Game {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 cameraTarget;
    private ShapeRenderer sr;
    private FitViewport viewport;
    private TickManager tickManager;

    private Level_1 level1;
    //private DeveloperTools developerTools;

    private LevelManager levelManager;
    private MenuManager menuManager;

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

        levelManager = new LevelManager();
        menuManager = new MenuManager();

        level1 = new Level_1(player, camera.position);
        levelManager.setCurrentLevel(level1);

        //developerTools = new DeveloperTools();
    }

    public void gameUpdate(){
        tickManager.update();

        while (tickManager.acceptTick()){
            logicTick();
            renderTick();
            //developerTools.navNodePlacement(); //debug
        }
    }

    public void logicTick(){
        if (menuManager.getCurrentMenu().isActive() && menuManager.getCurrentMenu().getPausesGame()){
            menuManager.menuLogicTick();
        }else {
            inGameLogicTick();
        }
    }

    private void inGameLogicTick(){
        levelManager.getCurrentLevel().logicTick();
        player.logicTick(camera);
        levelManager.collision();
        hud.updatePlayerData(player);
        lockOn();
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){ // Debug
            player.damageHealth(25);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)){ // Debug
            player.healHealth(25);
            player.addSouls(10);
        }
    }

    public void renderTick(){
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if (menuManager.getCurrentMenu().isActive() && menuManager.getCurrentMenu().getPausesGame()){
            menuManager.menuRenderTick();
        }else {
            inGameRenderTick();
        }
    }

    private void inGameRenderTick(){
        batch.begin();

        // all worldly draw calls go here
        // first render layer (background, entity bodies etc.)
        levelManager.getCurrentLevel().drawAllBodies(batch);
        player.draw(batch);

        // second render layer (effects)
        drawLockDot();
        levelManager.getCurrentLevel().drawAllEffects(batch);

        // third render layer (UI)
        hud.draw(batch, new Vector2(camera.position.x, camera.position.y));
        batch.end();

        // debug render calls
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        levelManager.getCurrentLevel().drawAllDebug(sr);
        player.drawDebug(sr);
        sr.end();

        trackCamera();
    }

    private void lockOn(){
        boolean isLockOnPressed = Gdx.input.isKeyJustPressed(ControlsDirectory.Combat.LOCK_ON);
        if (isLockOnPressed) {
            player.toggleLockOn();
            if (player.isLockedOn()){ // initial lock on
                lockedOnEntity = levelManager.getCurrentLevel().getEntities()[findClosestEntity(player.getPosition(), levelManager.getCurrentLevel().getEntities())];
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

    private class LevelManager{
        protected Level[] levels;
        protected Level currentLevel;

        public LevelManager(){
            currentLevel = new Level(player);
            levels = new Level[]{};
        }

        public void collision(){
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

        public Level getCurrentLevel() {
            return currentLevel;
        }

        public void setCurrentLevel(Level currentLevel) {
            this.currentLevel = currentLevel;
        }
    }

    private class MenuManager{
        protected Menu currentMenu;

        protected MainMenu mainMenu;

        public MenuManager(){
            mainMenu = new MainMenu();
            currentMenu = mainMenu;
        }

        public void menuLogicTick(){

        }

        public void menuRenderTick(){

        }

        public MainMenu getMainMenu() {
            return mainMenu;
        }

        public Menu getCurrentMenu() {
            return currentMenu;
        }

        public void setCurrentMenu(Menu currentMenu) {
            this.currentMenu = currentMenu;
        }
    }

//    private class DeveloperTools{
//        private Vector2[] currentNodeVertices = new Vector2[3];
//        private int currentIndex = 0;
//        private int numVertices = 0;
//
//        private void navNodePlacement(){ // debug developer tool
//            ArrayList<Vector2> environmentVertices = new ArrayList<>();
//            for (Obstacle o : currentLevel.getObstacles()){
//                for (Collider c : o.getBody()){
//                    for (Vector2 v : c.getVertices()){
//                        environmentVertices.add(v);
//                    }
//                }
//            }
//            for (NavNode n : currentLevel.getNavMesh().getNodes()){
//                environmentVertices.add(n.getVertices()[0]);
//                environmentVertices.add(n.getVertices()[1]);
//                environmentVertices.add(n.getVertices()[2]);
//            }
//            Vector2 selectedVertex = environmentVertices.get(Utils.findClosestPosition(player.getWorldMousePosition(), Utils.vectorArrayListToArray(environmentVertices)));
//            if (Utils.findDistance(player.getWorldMousePosition(), selectedVertex) > 0.5f){
//                selectedVertex = player.getWorldMousePosition();
//            }
//
//            if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)){
//                currentNodeVertices[currentIndex] = selectedVertex;
//                currentIndex = (currentIndex + 1) % 3;
//                numVertices ++;
//                if (numVertices > 3){
//                    numVertices = 3;
//                }
//            }else if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)){
//                System.out.println("navNodes[i] = new NavNode(new Vector2[]{new Vector2("+currentNodeVertices[0].x+"f,"+currentNodeVertices[0].y+"f)"+", new Vector2("+currentNodeVertices[1].x+"f,"+currentNodeVertices[1].y+"f)"+", new Vector2("+currentNodeVertices[2].x+"f,"+currentNodeVertices[2].y+"f)});");
//            }
//
//            sr.setProjectionMatrix(camera.combined);
//            sr.begin(ShapeRenderer.ShapeType.Filled);
//            sr.setColor(Color.CYAN);
//            sr.circle(selectedVertex.x, selectedVertex.y, 0.1f, 20);
//            if (numVertices == 1){
//                sr.circle(currentNodeVertices[0].x, currentNodeVertices[0].y, 0.1f, 20);
//            }
//            else if (numVertices == 2){
//                sr.rectLine(currentNodeVertices[(currentIndex - 1) % 3], currentNodeVertices[(currentIndex - 2) % 3], 0.1f);
//            }else if (numVertices == 3){
//                float[] floats = Utils.convertToPairwisePoints(currentNodeVertices);
//                sr.triangle(floats[0], floats[1], floats[2], floats[3], floats[4], floats[5]);
//            }
//            sr.end();
//            //System.out.println(currentNodeVertices[0] + " | " + currentNodeVertices[1] + " | " + currentNodeVertices[2]);
//        }
//    }
}
