
package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Player extends Entity{

    private float rollCoefficient;
    private float sprintStaminaCost;
    private float rollStaminaCost;
    private float lightAttackStaminaCost;
    private final int iTicks = 26;
    private final int ROLL_DURATION = 40; // ticks
    private final int LIGHT_ATTACK_DURATION = 22; // ticks, must be an even number for symmetric path
    private final float STAMINA_REGEN_DELAY = 90; // ticks - 90
    private final float BASE_STAMINA_REGEN_RATE = 0.5f; // per tick
    private float staminaRegenCoefficient;

    private float maxStamina;
    private float stamina;
    private boolean vulnerable;

    private Vector2 lookTarget;
    private int ticksSinceMoveInput;
    private int ticksWhileMoveInput;
    private int ticksSinceStaminaUsed;
    private boolean inControl;
    private boolean canMove;
    private boolean rotationalTrackingEnabled;
    private boolean lockedOn;
    private int currentRollTick;
    private int actionState; // 0 - Idle, 1 - Roll, 2 - Light Attack, 3 - Heavy Attack
    private CircularQueue actionBuffer;

    private Collider shield;

    // attacks
    private LightAttack lightAttack;
    private  HeavyAttack heavyAttack;

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
        if (velocity.len() > 0 && actionState == 0){
            walkLoop.play();
        }else{
            walkLoop.reset();
        }
    }

    private void combatController(){
        boolean rollPressed = Gdx.input.isKeyJustPressed(ControlsDirectory.Movement.ROLL);
        boolean attackPressed = Gdx.input.isButtonJustPressed(ControlsDirectory.Combat.ATTACK);
        boolean blockHeld = Gdx.input.isButtonPressed(ControlsDirectory.Combat.BLOCK);

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

        if (actionState == 0  && stamina > 0 && actionBuffer.notEmpty()){
            actionState = actionBuffer.dequeue();
        }
        block(blockHeld); // action state must be 0 to block
        if (actionState == 1){
            roll();
        }else if (actionState == 2){
            lightAttack.execute();
        }else if (actionState == 3){
            heavyAttack.execute();
        }
    }

    public void collision(Collider[] refBody, DamageSource[] damageSources){
        bodyCollision(refBody);
        hitboxOnHurtboxCollision(damageSources);
    }

    private void hitboxOnHurtboxCollision(DamageSource[] damageSources){ // this entity's hurtboxes check external hitboxes
        if (vulnerable) {
            for (DamageSource d : damageSources) { // shield blocking incoming attacks
                for (Collider h : d.getHitbox()){
                    if (h.isActive() && shield.detectCollision(h).len() > 0 && shield.isActive() && !d.isFlagged(ID)){
                        d.flagEntity(ID);
                    }
                }
            }

            for (Collider hurtbox : hurtboxes){ // same as super method
                if (hurtbox.isActive()) {
                    for (DamageSource d : damageSources) {
                        for (Collider h : d.getHitbox()){
                            if (h.isActive() && hurtbox.detectCollision(h).len() > 0 && !d.isFlagged(ID)){
                                damageHealth(h.getDamageValue());
                                d.flagEntity(ID);
                            }
                        }
                    }
                }
            }
        }
    }

    public void transformShield(){
        shield.setPosition(position.cpy().add(lookVector.cpy().scl(1/8f)));
        shield.setAngle(Utils.degreesToRadians(facing));
        shield.setVertices();
    }

    public void block(boolean blockHeld){ // handles use of the shield
        if (blockHeld && actionState == 0  && stamina > 0){
            shield.activate();
            staminaRegenCoefficient *= 0.25f; // stamina regen is slowed if blocking
        }else {
            shield.deactivate();
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
        if (rollAnim.update()){ // change frame of animation
            setSprite(rollAnim.getCurrentFrame());
        }
        if (currentRollTick > (ROLL_DURATION -iTicks)/2 && currentRollTick < (ROLL_DURATION +iTicks)/2){
            vulnerable = false;
        }else if (currentRollTick >= (ROLL_DURATION +iTicks)/2){
            vulnerable = true;
        }
        velocity = lookVector.cpy().scl(-1*((float) (rollCoefficient*(-4)*(Math.pow(((double) currentRollTick / ROLL_DURATION), 2))+((double) (4 * (currentRollTick / ROLL_DURATION))))));
        currentRollTick++;
        if (currentRollTick == ROLL_DURATION){ // end of roll
            actionState = 0;
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
        float angle = Utils.degreesToRadians(facing);
        lookVector = Utils.rotate(new Vector2(1,0), angle);
    }

    private void staminaRegeneration(){
        if (ticksSinceStaminaUsed > STAMINA_REGEN_DELAY && stamina < maxStamina){
            stamina += calculateStaminaRegenRate();
            if (stamina > maxStamina){
                stamina = maxStamina;
            }
        }
        ticksSinceStaminaUsed++;
    }

    private float calculateStaminaRegenRate(){
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
        lightAttack.debugRender(sr);
        shield.debugRender(sr);
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
        rollCoefficient = 1/25f;
        sprintStaminaCost = 0.25f;
        rollStaminaCost = 30f;
        lightAttackStaminaCost = 30f;
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
        actionState = 0;
        maxHealth = 500;
        health = maxHealth;
        maxStamina = 500;
        stamina = maxStamina;
        staminaRegenCoefficient = 1;
        souls = 0;
        vulnerable = true;
        actionBuffer = new CircularQueue(2);

        walkLoop = new SoundLooper(90, AssetDirectory.Audio.Player.WALK, 0.3f);

        body = new Collider[]{new Collider(position, 0, 0, Utils.generateRegularPolygon(20, 0.35f), 0, 0, true, Color.BLUE, false, false)};
        hurtboxes = new Collider[]{new Collider(position, 0, 0, new Vector2[]{new Vector2(-0.2f, -0.3f), new Vector2(0.2f, -0.3f), new Vector2(0.2f, 0.3f), new Vector2(-0.2f, 0.3f)}, 0, 0, true, Color.RED, true, false)};
        shield = new Collider(position, 0, 0, new Vector2[]{new Vector2(0, -5/16f), new Vector2(1/8f, -5/16f), new Vector2(1/8f, 5/16f), new Vector2(0, 5/16f)}, 0, 0, false, Color.GREEN, false, false);
        lightAttack = new LightAttack();
        heavyAttack = new HeavyAttack();
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
        return new DamageSource[]{lightAttack};
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

    // attacks

    private class LightAttack extends DamageSource{
        private LightAttack(){
            hitbox = new Collider[]{new Collider(position, 0, 0, new Vector2[]{new Vector2(-0.1f, -0.15f), new Vector2(0.1f, -0.15f), new Vector2(0.1f, 0.6f), new Vector2(-0.1f, 0.6f)}, 0, 100f, false, Color.MAGENTA, false, false)};
            attackFlagManager = new AttackFlagManager();
        }

        public void execute(){
            if (currentActionTick == 0){ // start of attack
                canMove = false;
                rotationalTrackingEnabled = false;
                takeStamina(lightAttackStaminaCost);
                hitbox[0].activate();
                clearFlags();
            }

            float t = (2*(currentActionTick - LIGHT_ATTACK_DURATION/2f))/LIGHT_ATTACK_DURATION; // parametric variable
            float P = 0.25f; // change in (relative) x
            float Q = 0.5f; // change in (relative) y
            Vector2 relativePosition = new Vector2(-P*t, (float) (Q*(-(Math.pow(t, 2)) + 1)));
            float playerAngle = Utils.degreesToRadians(facing-90);
            hitbox[0].setPosition(position.cpy().add(Utils.rotate(relativePosition, playerAngle)));
            hitbox[0].setAngle(playerAngle);
            hitbox[0].setVertices();

            currentActionTick++;

            if (currentActionTick == LIGHT_ATTACK_DURATION){ // end of attack
                actionState = 0;
                canMove = true;
                rotationalTrackingEnabled = true;
                currentActionTick = 0;
                hitbox[0].deactivate();
            }
        }

        public void debugRender(ShapeRenderer sr){
            hitbox[0].debugRender(sr);
        }
    }

    private class HeavyAttack extends  DamageSource{
        public void execute(){
            actionState = 0;
        }
    }
}
