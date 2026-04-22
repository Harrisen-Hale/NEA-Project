package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Player {
    private int health;
    private int stamina;
    private int sprintStaminaCost;

    private Vector2 position;
    private Vector2 velocity;
    private Vector2 moveVector; // unit vector in movement direction
    private Vector2 lookTarget;
    private int ticksSinceMoveInput;
    private int ticksWhileMoveInput;
    private float facing;
    private boolean inControl;
    private boolean canMove;
    private boolean rotationalTrackingEnabled;
    private boolean lockedOn;

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
        inControl = true;
        canMove = true;
        rotationalTrackingEnabled = true;
        lockedOn = false;

        health = 100;
        stamina = 100;
        sprintStaminaCost = 1;
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)){
            Sound fah = Gdx.audio.newSound(Gdx.files.internal(AssetDirectory.Player.Audio.FAH));
            fah.play(0.3f);
        }
    }

    private void directionalMovement(){
        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean sprint = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        boolean anyDirPressed = (up || down || left || right);
        float speedCoefficient;

        if (sprint && stamina > 0){
            speedCoefficient = 1/35f;
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
        position.add(velocity);
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
        textureIdle = new Texture(Gdx.files.internal(AssetDirectory.Player.Textures.IDLE));
        textureRoll = new Texture(Gdx.files.internal(AssetDirectory.Player.Textures.ROLL));

        rollAnim = new AnimationStateMachine(new Texture[]{textureIdle,textureRoll,textureIdle}, new int[]{7,26,7});

        currentSprite = new Sprite(textureIdle);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setLookTarget(Vector2 lookTarget) {
        this.lookTarget = lookTarget;
    }
}
