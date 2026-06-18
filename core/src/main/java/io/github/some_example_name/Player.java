package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Player extends GameObject{
    private float maxHealth;
    private float health;
    private float maxStamina;
    private float stamina;
    private float sprintStaminaCost;
    private float rollStaminaCost;
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
    private float rollCoefficient;
    private int currentRollTick;
    private boolean isRolling;

    private Texture textureIdle;
    private Texture textureRoll;
    private Sprite currentSprite;
    private Vector2 worldMousePosition;

    private AnimationStateMachine rollAnim;

    public Player(){
        loadTextures();
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
        rollCoefficient = 1/25f;
        currentRollTick = 0;
        isRolling = false;

        maxHealth = 100;
        health = maxHealth;
        maxStamina = 100;
        stamina = maxStamina;
        sprintStaminaCost = 0.25f;
        rollStaminaCost = 15f;
        souls = 0;
    }

    public void logicTick(OrthographicCamera camera){
        setWorldMousePosition(camera);
        playerController();
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

        if (sprint && stamina > sprintStaminaCost){
            speedCoefficient = 1/28f;
            if (anyDirPressed) {
                stamina -= sprintStaminaCost;
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
            if (isRollPressed && !isRolling && stamina >= rollStaminaCost){
                isRolling = true;
                stamina -= rollStaminaCost;
            }
        }
        if (isRolling){
            roll();
        }
    }

    private void roll(){
        if (currentRollTick == 0){
            canMove = false;
            rotationalTrackingEnabled = false;
            setSprite(rollAnim.getCurrentFrame());
            Sound rollSFX = AssetDirectory.Audio.Player.ROLL;
            rollSFX.play(0.3f);
        }
        if (rollAnim.update()){
            setSprite(rollAnim.getCurrentFrame());
        }
        velocity = lookVector.cpy().scl(-1*((float) (rollCoefficient*(-4)*(Math.pow(((double) currentRollTick / Constants.ROLL_LENGTH), 2))+((double) (4 * (currentRollTick / Constants.ROLL_LENGTH))))));
        currentRollTick++;
        if (currentRollTick == Constants.ROLL_LENGTH){
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
        textureIdle = new Texture(Gdx.files.internal(AssetDirectory.Textures.Player.IDLE));
        textureRoll = new Texture(Gdx.files.internal(AssetDirectory.Textures.Player.ROLL));

        rollAnim = new AnimationStateMachine(new Texture[]{textureIdle,textureRoll,textureIdle}, new int[]{10,20,10});

        currentSprite = new Sprite(textureIdle);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
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
    }

    public void healHealth(float heal) {
        this.health += heal;
        if (health > maxHealth){
            health = maxHealth;
        }
    }
}
