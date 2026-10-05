
package io.github.some_example_name.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.Attacks.Attack;
import io.github.some_example_name.Framework.DamageSource;
import io.github.some_example_name.Attacks.PlayerLightAttack;
import io.github.some_example_name.Audio.SoundLooper;
import io.github.some_example_name.Framework.*;
import io.github.some_example_name.IO.ControlsDirectory;
import io.github.some_example_name.Items.Shield;
import io.github.some_example_name.Items.TestWeapon;

import java.util.PrimitiveIterator;


public class Player extends Entity {

    private float staminaRegenCoefficient;

    private float maxStamina;
    private float stamina;
    private boolean vulnerable;

    private boolean lockedOn;

    private Vector2 lookTarget;

    private int ticksSinceStaminaUsed;

    private Vector2 worldMousePosition;

    private AnimationStateMachine rollAnim;

    private PlayerController playerController;
    private Inventory inventory;

    private int[] playerStats; // Vitality, Endurance, Strength, Dexterity, Knowledge
    private int level;

    public Player(int IDArg){
        loadTextures();
        initialiseBaseValuesAndConstants(IDArg);
    }

    public void logicTick(OrthographicCamera camera){
        setWorldMousePosition(camera);
        playerControllerTick();
        staminaRegeneration();
        transformColliders();
    }

    private void playerControllerTick(){
        playerController.logicTick();
    }


    public void collision(Entity ref){
        bodyCollision(ref);
        hitboxOnHurtboxCollision(ref.getDamageSources());
    }

    public void hitboxOnHurtboxCollision(DamageSource[] damageSources){ // this entity's hurtboxes check external hitboxes
        if (vulnerable) {
            for (DamageSource d : damageSources) { // shield blocking incoming attacks
                for (Collider h : d.getHitbox()){
                    if (h.isActive() && inventory.getShield().getShieldCollider().detectCollision(h).len() > 0 && inventory.getShield().getShieldCollider().isActive() && d.notFlagged(ID)){
                        d.flagEntity(ID);
                        takeStamina(30);
                    }
                }
            }

            super.hitboxOnHurtboxCollision(damageSources);
        }
    }

    private void staminaRegeneration(){
        int STAMINA_REGEN_DELAY = 0; // ticks

        if (ticksSinceStaminaUsed > STAMINA_REGEN_DELAY && stamina < maxStamina){
            stamina += calculateStaminaRegenRate();
            if (stamina > maxStamina){
                stamina = maxStamina;
            }
        }
        ticksSinceStaminaUsed++;
        if (ticksSinceStaminaUsed > STAMINA_REGEN_DELAY){
            ticksSinceStaminaUsed = STAMINA_REGEN_DELAY+1;
        }
    }

    private float calculateStaminaRegenRate(){
        float BASE_STAMINA_REGEN_RATE = 100f; // per tick

        float staminaRegenRate = BASE_STAMINA_REGEN_RATE * staminaRegenCoefficient;
        staminaRegenCoefficient = 1;
        return staminaRegenRate;
    }

    private void setWorldMousePosition(OrthographicCamera camera){
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);
        worldMousePosition = new Vector2(mouse.x, mouse.y);
    }

    public void concludeAttack(){
        playerController.setInControl(true);
        playerController.setRotationalTrackingEnabled(true);
    }

    public void draw(Batch batch){
        currentSprite.setPosition(position.x-0.5f, position.y-0.5f);
        currentSprite.setRotation(facing);
        currentSprite.draw(batch);
    }

    public void drawDebug(ShapeRenderer sr){
        for (Collider c : body){
            c.drawDebug(sr);
        }
        for (Collider hu : hurtboxes){
            if (!vulnerable){
                hu.setDebugColour(Color.PINK);
            }else{
                hu.setDebugColour(Color.RED);
            }
            hu.drawDebug(sr);
        }
        inventory.getShield().getShieldCollider().drawDebug(sr);
        inventory.weapon.getAttack().debugRender(sr);
    }

    protected void loadTextures(){
        Texture textureIdle = new Texture(Gdx.files.internal(AssetDirectory.Textures.Player.IDLE));
        Texture textureRoll = new Texture(Gdx.files.internal(AssetDirectory.Textures.Player.ROLL));

        rollAnim = new AnimationStateMachine(new Texture[]{textureIdle, textureRoll, textureIdle}, new int[]{10,20,10});

        currentSprite = new Sprite(textureIdle);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
    }

    protected void initialiseBaseValuesAndConstants(int IDArg){
        ID = IDArg;
        position = new Vector2(0,0);
        velocity = new Vector2(0,0);
        facing = 0;
        lookTarget = new Vector2(0,0);

        playerController = new PlayerController(this);
        inventory = new Inventory();
        inventory.setWeapon(new TestWeapon(this));
        inventory.setShield(new Shield());

        maxHealth = 500;
        health = maxHealth;
        maxStamina = 500;
        stamina = maxStamina;
        staminaRegenCoefficient = 1;
        souls = 0;
        vulnerable = true;
        ticksSinceStaminaUsed = 0;
        lockedOn = false;
        playerStats = new int[]{0,0,0,0,0};
        level = 1;

        body = new Collider[]{new Collider(position, 0,0, Utils.generateRegularPolygon(20, 0.35f), 0, true, Color.BLUE, true, false)};
        hurtboxes = new Collider[]{new Collider(position, 0,0, new Vector2[]{new Vector2(-0.2f, -0.3f), new Vector2(0.2f, -0.3f), new Vector2(0.2f, 0.3f), new Vector2(-0.2f, 0.3f)}, 0, true, Color.RED, true, false)};
    }

    // getters and setters

    public float getMaxStamina() {
        return maxStamina;
    }

    public float getStamina() {
        return stamina;
    }

    public DamageSource[] getDamageSources(){
        return new DamageSource[]{inventory.getWeapon().getAttack()};
    }

    public void setLookTarget(Vector2 lookTarget) {
        this.lookTarget = lookTarget;
    }

    public void addSouls(int numSouls) {
        this.souls += numSouls;
    }

    public void damageHealth(float damage) {
        if (vulnerable) { // redundant safety check in case not checked by cause of damage
            this.health -= damage;
            if (health < 0){
                health = 0;
            }
            AssetDirectory.Audio.Player.HURT.play(0.3f);
        }
    }

    public void takeStamina(float amount){
        stamina -= amount;
        if (stamina < 0){
            stamina = 0;
        }
        ticksSinceStaminaUsed = 0;
    }

    public Vector2 getLookTarget() {
        return lookTarget;
    }

    public int getTicksSinceStaminaUsed() {
        return ticksSinceStaminaUsed;
    }

    public void toggleLockOn(){
        lockedOn = !lockedOn;
    }

    public boolean isLockedOn() {
        return lockedOn;
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public Vector2 getWorldMousePosition() {
        return worldMousePosition;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public AnimationStateMachine getRollAnim() {
        return rollAnim;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int[] getPlayerStats(){
        return playerStats;
    }

    public int getVitality(){
        return playerStats[0];
    }

    public int getEndurance(){
        return playerStats[1];
    }

    public int getStrength(){
        return playerStats[2];
    }

    public int getDexterity(){
        return playerStats[3];
    }

    public int getKnowledge(){
        return playerStats[4];
    }

    public int getLevel() { // stat level, not world level
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public float getStaminaRegenCoefficient() {
        return staminaRegenCoefficient;
    }

    public void setStaminaRegenCoefficient(float staminaRegenCoefficient) {
        this.staminaRegenCoefficient = staminaRegenCoefficient;
    }

    public void setMaxStamina(float maxStamina) {
        this.maxStamina = maxStamina;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }
}
