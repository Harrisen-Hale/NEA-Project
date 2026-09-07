
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


public class Player extends Entity {

    private float staminaRegenCoefficient;

    private float maxStamina;
    private float stamina;
    private boolean vulnerable;

    private Vector2 lookTarget;
    private int ticksSinceMoveInput;
    private int ticksWhileMoveInput;
    private int ticksSinceStaminaUsed;
    private boolean inControl;
    private boolean rotationalTrackingEnabled;
    private boolean lockedOn;
    private boolean rolling;
    private int currentRollTick;
    private CircularQueue actionBuffer;

    private Collider shield;

    private Attack currentAttack;

    private Vector2 worldMousePosition;

    private AnimationStateMachine rollAnim;

    private SoundLooper walkLoop;

    public Player(int IDArg){
        loadTextures();
        initialiseBaseValuesAndConstants(IDArg);
    }

    public void logicTick(OrthographicCamera camera){
        setWorldMousePosition(camera);
        playerController();
        staminaRegeneration();
        transformColliders();
        transformShield();
        resolveRoll();
        resolveAttack();
    }

    private void playerController(){
        inputController();
        if (rotationalTrackingEnabled) {
            rotation(lookTarget);
        }
    }

    private void inputController(){
        if (inControl){
            directionalMovement();
        }
        combatController();
        position.add(velocity);
    }

    private void directionalMovement(){
        float SPRINT_STAMINA_COST = 0.25f;

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
                takeStamina(SPRINT_STAMINA_COST);
            }
        }else {
            speedCoefficient = 1/55f;
        }

        if (anyDirPressed){
            moveVector = new Vector2(0,0);
        }
        if (lockedOn){ // strafe
            Vector2 f = (lookTarget.cpy().sub(position)).nor(); // direction vector from player to target
            if (up){
                moveVector.add(f);
            }
            if (down){
                moveVector.mulAdd(f, -1);
            }
            if (left){
                moveVector.add(new Vector2(-f.y, f.x));
            }
            if (right){
                moveVector.add(new Vector2(f.y, -f.x));
            }
        }
        else {
            lookTarget = worldMousePosition;
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
            moveVector.nor(); // normalise vector
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

        // sound fx
        if (velocity.len() > 0 && inControl){
            walkLoop.play();
        }else{
            walkLoop.stop();
        }
    }

    private void combatController(){
        boolean rollPressed = Gdx.input.isKeyJustPressed(ControlsDirectory.Movement.ROLL);
        boolean attackPressed = Gdx.input.isButtonJustPressed(ControlsDirectory.Combat.ATTACK);
        boolean blockHeld = Gdx.input.isButtonPressed(ControlsDirectory.Combat.BLOCK);
        int action = 0;

        // full action buffer is accounted for in enqueue method
        if (stamina > 0) {
            if (rollPressed){
                actionBuffer.enqueue(1);
            }
            if(attackPressed){
                if (blockHeld){
                    actionBuffer.enqueue(3);
                }else {
                    actionBuffer.enqueue(2);
                }
            }
        }

        if (inControl && stamina > 0 && actionBuffer.notEmpty()){
            action = actionBuffer.dequeue();
        }
        block(blockHeld); // cannot be doing any other action to block
        if (action == 1){
            rolling = true;
        }else if (action == 2){
            beginAttack(new PlayerLightAttack(this));
        }
    }

    private void beginAttack(Attack attackArg){
        currentAttack = attackArg;
        inControl = false;
        rotationalTrackingEnabled = false;
        takeStamina(attackArg.getStaminaCost());
    }

    private void resolveAttack(){
        currentAttack.execute();
    }

    private void resolveRoll(){
        if (rolling){
            rolling = roll();
        }
    }

    public void concludeAttack(){
        inControl = true;
        rotationalTrackingEnabled = true;
        currentAttack = new Attack(); // blank attack, does nothing except return false
    }

    public void collision(Entity ref){
        bodyCollision(ref);
        hitboxOnHurtboxCollision(ref.getDamageSources());
    }

    public void hitboxOnHurtboxCollision(DamageSource[] damageSources){ // this entity's hurtboxes check external hitboxes
        if (vulnerable) {
            for (DamageSource d : damageSources) { // shield blocking incoming attacks
                for (Collider h : d.getHitbox()){
                    if (h.isActive() && shield.detectCollision(h).len() > 0 && shield.isActive() && d.notFlagged(ID)){
                        d.flagEntity(ID);
                        takeStamina(30);
                    }
                }
            }

            for (Collider hurtbox : hurtboxes){ // same as super method
                if (hurtbox.isActive()) {
                    for (DamageSource d : damageSources) {
                        for (Collider h : d.getHitbox()){
                            if (h.isActive() && hurtbox.detectCollision(h).len() > 0 && d.notFlagged(ID)){
                                damageHealth(h.getDamageValue());
                                d.flagEntity(ID);
                            }
                        }
                    }
                }
            }
        }
    }

    private void transformShield(){
        shield.setPosition(position.cpy().add(lookVector.cpy().scl(1/8f)));
        shield.setAngle(Utils.degreesToRadians(facing));
        shield.setVertices();
    }

    private void block(boolean blockHeld){ // handles use of the shield
        if (blockHeld && inControl  && stamina > 0){
            shield.activate();
            staminaRegenCoefficient *= 0.25f; // stamina regen is slowed if blocking
        }else {
            shield.deactivate();
        }
    }

    private boolean roll(){
        int ROLL_DURATION = 40; // ticks
        int INVINCIBILITY_FRAMES = 26;
        float ROLL_STAMINA_COST = 30f;
        float ROLL_VOLUME = 0.3f;


        if (currentRollTick == 0){ // start of roll
            inControl = false;
            rotationalTrackingEnabled = false;
            takeStamina(ROLL_STAMINA_COST);
            setSprite(rollAnim.getCurrentFrame());
            AssetDirectory.Audio.Player.ROLL.play(ROLL_VOLUME);
        }
        if (rollAnim.update()){ // change frame of animation
            setSprite(rollAnim.getCurrentFrame());
        }

        if (currentRollTick > (ROLL_DURATION - INVINCIBILITY_FRAMES)/2 && currentRollTick < (ROLL_DURATION + INVINCIBILITY_FRAMES)/2){ // i-frames
            vulnerable = false;
        }else if (currentRollTick >= (ROLL_DURATION + INVINCIBILITY_FRAMES)/2){
            vulnerable = true;
        }
        velocity = lookVector.cpy().scl(-1*((float) ((1/25f)*(-4)*(Math.pow(((double) currentRollTick / ROLL_DURATION), 2))+((double) (4 * (currentRollTick / ROLL_DURATION))))));
        currentRollTick++;
        if (currentRollTick == ROLL_DURATION){ // end of roll
            inControl = true;
            rotationalTrackingEnabled = true;
            currentRollTick = 0;
            rollAnim.reset();
            return false;
        }
        return true;
    }

    private void staminaRegeneration(){
        float STAMINA_REGEN_DELAY = 90; // ticks

        if (ticksSinceStaminaUsed > STAMINA_REGEN_DELAY && stamina < maxStamina){
            stamina += calculateStaminaRegenRate();
            if (stamina > maxStamina){
                stamina = maxStamina;
            }
        }
        ticksSinceStaminaUsed++;
    }

    private float calculateStaminaRegenRate(){
        float BASE_STAMINA_REGEN_RATE = 0.5f; // per tick

        float staminaRegenRate = BASE_STAMINA_REGEN_RATE * staminaRegenCoefficient;
        staminaRegenCoefficient = 1;
        return staminaRegenRate;
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

    public void drawDebug(ShapeRenderer sr){
        for (Collider c : body){
            c.debugRender(sr);
        }
        for (Collider hu : hurtboxes){
            if (!vulnerable){
                hu.setDebugColour(Color.PINK);
            }else{
                hu.setDebugColour(Color.RED);
            }
            hu.debugRender(sr);
        }
        shield.debugRender(sr);
        currentAttack.debugRender(sr);
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
        moveVector = new Vector2(0,0);
        facing = 0;
        lookTarget = new Vector2(0,0);
        ticksSinceMoveInput = 0;
        ticksWhileMoveInput = 0;
        ticksSinceStaminaUsed = 0;
        inControl = true;
        rotationalTrackingEnabled = true;
        lockedOn = false;
        currentRollTick = 0;
        maxHealth = 500;
        health = maxHealth;
        maxStamina = 500;
        stamina = maxStamina;
        staminaRegenCoefficient = 1;
        souls = 0;
        vulnerable = true;
        actionBuffer = new CircularQueue(2);

        walkLoop = new SoundLooper(90, AssetDirectory.Audio.Player.WALK, 0.3f);

        body = new Collider[]{new Collider(position, 0,0, Utils.generateRegularPolygon(20, 0.35f), 0, 0, true, Color.BLUE, true, false)};
        hurtboxes = new Collider[]{new Collider(position, 0,0, new Vector2[]{new Vector2(-0.2f, -0.3f), new Vector2(0.2f, -0.3f), new Vector2(0.2f, 0.3f), new Vector2(-0.2f, 0.3f)}, 0, 0, true, Color.RED, true, false)};
        shield = new Collider(position, 0,0,new Vector2[]{new Vector2(0, -5/16f), new Vector2(1/8f, -5/16f), new Vector2(1/8f, 5/16f), new Vector2(0, 5/16f)}, 0, 0, false, Color.GREEN, false, false);
        currentAttack = new Attack();
    }

    // getters and setters

    public void toggleLockOn(){
        lockedOn = !lockedOn;
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    public float getStamina() {
        return stamina;
    }

    public DamageSource[] getDamageSources(){
        return new DamageSource[]{currentAttack};
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

    public boolean isLockedOn() {
        return lockedOn;
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public Vector2 getWorldMousePosition() {
        return worldMousePosition;
    }

    public void setInControl(boolean inControl) {
        this.inControl = inControl;
    }

    public void setRotationalTrackingEnabled(boolean rotationalTrackingEnabled) {
        this.rotationalTrackingEnabled = rotationalTrackingEnabled;
    }
}
