package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Player extends GameObject{

    private float rollCoefficient;
    private float sprintStaminaCost;
    private float rollStaminaCost;
    private final int iTicks = 26;
    private final int ROLL_LENGTH = 40; // ticks
    private final float STAMINA_REGEN_DELAY = 100; // ticks
    private final float STAMINA_REGEN_RATE = 1; // per tick

    private float maxHealth;
    private float health;
    private float maxStamina;
    private float stamina;
    private int souls;

    private Vector2 velocity;
    private Vector2 moveVector; // unit vector in movement direction
    private Vector2 lookVector; // unit vector in facing direction
    private Vector2 lookTarget;
    private int ticksSinceMoveInput;
    private int ticksWhileMoveInput;
    private int ticksSinceStaminaUsed;
    private float facing;
    private boolean inControl;
    private boolean canMove;
    private boolean rotationalTrackingEnabled;
    private boolean lockedOn;
    private int currentRollTick;
    private boolean isRolling;

    private Sprite currentSprite;
    private Vector2 worldMousePosition;

    private AnimationStateMachine rollAnim;

    public Player(){
        loadTextures();
        initialiseBaseValuesAndConstants();
    }

    public void logicTick(OrthographicCamera camera){
        setWorldMousePosition(camera);
        playerController();
        staminaRegeneration();
    }

    private void playerController(){
        if (inControl) {
            inputController();
        }
        if (rotationalTrackingEnabled) {
            rotationController(lookTarget);
        }
    }

    private void inputController(){
        if (canMove){
            directionalMovement();
        }
        combatController();
        position.add(velocity);
    }

    private void directionalMovement(){
        boolean up = Gdx.input.isKeyPressed(ControlsDirectory.Movement.UP);
        boolean left = Gdx.input.isKeyPressed(ControlsDirectory.Movement.LEFT);
        boolean down = Gdx.input.isKeyPressed(ControlsDirectory.Movement.DOWN);
        boolean right = Gdx.input.isKeyPressed(ControlsDirectory.Movement.RIGHT);
        boolean sprint = Gdx.input.isKeyPressed(ControlsDirectory.Movement.SPRINT);
        boolean anyDirPressed = (up || down || left || right);
        float speedCoefficient;

        if (sprint && stamina > 0){
            speedCoefficient = 1/28f;
            if (anyDirPressed) {
                takeStamina(sprintStaminaCost);
            }
        }else {
            speedCoefficient = 1/55f;
        }

        if (anyDirPressed){
            moveVector = new Vector2(0,0);
        }
        if (lockedOn){

        }
        else {
            setLookTarget(worldMousePosition);
            if (up){
                moveVector.add(new Vector2(0,1));
            }
            if (down){
                moveVector.add(new Vector2(0,-1));
            }
            if (left){
                moveVector.add(new Vector2(-1,0));
            }
            if (right){
                moveVector.add(new Vector2(1,0));
            }
        }

        if (anyDirPressed){
            ticksSinceMoveInput = 0;
            moveVector.nor();
            float gainFactor;
            if (ticksWhileMoveInput <=20){
                gainFactor = (float) -((Math.E / (Math.E - 1)) * Math.exp(-ticksWhileMoveInput /20f) - (Math.E / (Math.E - 1)));
            }else {
                gainFactor = 1;
            }
            ticksWhileMoveInput++;
            velocity = moveVector.cpy().scl(speedCoefficient*gainFactor);
        }else {
            ticksWhileMoveInput = 0;
            ticksSinceMoveInput++;
            float dampingFactor;
            if (ticksSinceMoveInput <= 20){
                dampingFactor = (float) ((Math.E / (Math.E - 1)) * Math.exp(-ticksSinceMoveInput /20f) + (1 - (Math.E / (Math.E - 1))));
            }else{
                dampingFactor = 0;
            }
            velocity = moveVector.cpy().scl(dampingFactor*speedCoefficient);
        }
    }

    private void combatController(){
        boolean isRollPressed = Gdx.input.isKeyJustPressed(ControlsDirectory.Movement.ROLL);
        if (canMove) {
            if (isRollPressed && !isRolling && stamina > 0){
                isRolling = true;
            }
        }
        if (isRolling){
            roll();
        }
    }

    private void roll(){
        if (currentRollTick == 0){ // start of roll
            canMove = false;
            rotationalTrackingEnabled = false;
            takeStamina(rollStaminaCost);
            setSprite(rollAnim.getCurrentFrame());
            AssetDirectory.Audio.Player.ROLL.play(0.3f);
        }
        if (rollAnim.update()){
            setSprite(rollAnim.getCurrentFrame());
        }
        velocity = lookVector.cpy().scl(-1*((float) (rollCoefficient*(-4)*(Math.pow(((double) currentRollTick / ROLL_LENGTH), 2))+((double) (4 * (currentRollTick / ROLL_LENGTH))))));
        currentRollTick++;
        if (currentRollTick == ROLL_LENGTH){
            isRolling = false;
            canMove = true;
            rotationalTrackingEnabled = true;
            currentRollTick = 0;
            rollAnim.reset();

        }
    }

    private void rotationController(Vector2 target){
        Vector2 playerToTarget = target.cpy().sub(position);
        float theta = playerToTarget.angleDeg();
        float phi = theta - facing;
        while (phi > 180){
            phi -= 360;
        }                   // restrict phi to the interval (-180, 180]
        while (phi <= -180){
            phi += 360;
        }
        if (Math.abs(phi) >= 0.005){ // eliminate asymptotic behaviour
            facing += phi/20f;
        }else {
            facing = theta;
        }
        float angle = (float) (facing*(Math.PI/180));//converting to radians
        lookVector = new Vector2(1,0).mul(new Matrix3(new float[]{(float) Math.cos(angle),(float)Math.sin(angle),0,(float)-Math.sin(angle),(float)Math.cos(angle),0,0,0,0}));
    }

    private void staminaRegeneration(){
        if (ticksSinceStaminaUsed > STAMINA_REGEN_DELAY && stamina < maxStamina){
            stamina += STAMINA_REGEN_RATE;
            if (stamina > maxStamina){
                stamina = maxStamina;
            }
        }
        ticksSinceStaminaUsed++;
    }

    private void setWorldMousePosition(OrthographicCamera camera){
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);
        worldMousePosition = new Vector2(mouse.x, mouse.y);
    }

    public void draw(Batch batch){
        currentSprite.setPosition(position.x-0.5f, position.y-0.5f);
        currentSprite.setRotation(facing);
        currentSprite.draw(batch);
    }

    private void loadTextures(){
        Texture textureIdle = new Texture(Gdx.files.internal(AssetDirectory.Textures.Player.IDLE));
        Texture textureRoll = new Texture(Gdx.files.internal(AssetDirectory.Textures.Player.ROLL));

        rollAnim = new AnimationStateMachine(new Texture[]{textureIdle, textureRoll, textureIdle}, new int[]{10,20,10});

        currentSprite = new Sprite(textureIdle);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
    }

    private void initialiseBaseValuesAndConstants(){
        rollCoefficient = 1/25f;
        sprintStaminaCost = 0.25f;
        rollStaminaCost = 30f;
        position = new Vector2(0,0);
        velocity = new Vector2(0,0);
        moveVector = new Vector2(0,0);
        facing = 0;
        ticksSinceMoveInput = 0;
        ticksWhileMoveInput = 0;
        ticksSinceStaminaUsed = 0;
        inControl = true;
        canMove = true;
        rotationalTrackingEnabled = true;
        lockedOn = false;
        currentRollTick = 0;
        isRolling = false;
        maxHealth = 100;
        health = maxHealth;
        maxStamina = 100;
        stamina = maxStamina;
        souls = 0;
    }

    // getters and setters

    private void setSprite(Texture newTexture){
        currentSprite = new Sprite(newTexture);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    public float getStamina() {
        return stamina;
    }

    public int getSouls() {
        return souls;
    }

    public void setLookTarget(Vector2 lookTarget) {
        this.lookTarget = lookTarget;
    }

    public void addSouls(int numSouls) {
        this.souls += numSouls;
    }

    public void damageHealth(float damage) {
        this.health -= damage;
        if (health < 0){
            health = 0;
        }
        AssetDirectory.Audio.Player.HURT.play(0.3f);
    }

    public void healHealth(float heal) {
        this.health += heal;
        if (health > maxHealth){
            health = maxHealth;
        }
    }

    public void takeStamina(float amount){
        stamina -= amount;
        if (stamina < 0){
            stamina = 0;
        }
        ticksSinceStaminaUsed = 0;
    }

    public boolean isLockedOn() {
        return lockedOn;
    }
}
