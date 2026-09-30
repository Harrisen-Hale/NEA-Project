package io.github.some_example_name.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Attacks.Attack;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.CircularQueue;
import io.github.some_example_name.Framework.Utils;
import io.github.some_example_name.IO.ControlsDirectory;

public class PlayerController {
    protected Player player;
    private int ticksSinceMoveInput;
    private int ticksWhileMoveInput;
    private boolean inControl;
    private boolean rotationalTrackingEnabled;
    private boolean rolling;
    private int currentRollTick;
    private CircularQueue actionBuffer;

    public PlayerController(Player playerArg){
        player = playerArg;
        initialiseValues();
    }

    public void logicTick(){
        resolveAttack();
        inputController();
        if (rotationalTrackingEnabled) {
            player.rotation(player.getLookTarget());
        }
        resolveRoll();
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
        Vector2 playerMoveVector = new Vector2(0,0);


        if (sprint && player.getStamina() > 0){
            speedCoefficient = 1/28f;
            if (anyDirPressed) {
                player.takeStamina(SPRINT_STAMINA_COST);
            }
        }else {
            speedCoefficient = 1/55f;
        }

        if (player.isLockedOn()){ // strafe
            Vector2 f = (player.getLookTarget().cpy().sub(player.getPosition())).nor(); // unit direction vector from player to target
            if (up){
                playerMoveVector.add(f);
            }
            if (down){
                playerMoveVector.mulAdd(f, -1);
            }
            if (left){
                playerMoveVector.add(new Vector2(-f.y, f.x));
            }
            if (right){
                playerMoveVector.add(new Vector2(f.y, -f.x));
            }
        }
        else {
            player.setLookTarget(player.getWorldMousePosition());
            if (up){
                playerMoveVector.add(new Vector2(0,1));
            }
            if (down){
                playerMoveVector.add(new Vector2(0,-1));
            }
            if (left){
                playerMoveVector.add(new Vector2(-1,0));
            }
            if (right){
                playerMoveVector.add(new Vector2(1,0));
            }
        }

        if (anyDirPressed){
            ticksSinceMoveInput = 0;
            playerMoveVector.nor(); // normalise vector
            float gainFactor;
            if (ticksWhileMoveInput <=20){
                gainFactor = (float) -((Math.E / (Math.E - 1)) * Math.exp(-ticksWhileMoveInput /20f) - (Math.E / (Math.E - 1)));
            }else {
                gainFactor = 1;
            }
            ticksWhileMoveInput++;
            player.setVelocity(playerMoveVector.cpy().scl(speedCoefficient*gainFactor));
        }else {
            ticksWhileMoveInput = 0;
            ticksSinceMoveInput++;
            float dampingFactor;
            if (ticksSinceMoveInput <= 20){
                dampingFactor = (float) ((Math.E / (Math.E - 1)) * Math.exp(-ticksSinceMoveInput /20f) + (1 - (Math.E / (Math.E - 1))));
            }else{
                dampingFactor = 0;
                ticksSinceMoveInput = 21;
            }
            player.setVelocity(playerMoveVector.cpy().scl(dampingFactor*speedCoefficient));
        }

    }

    private void combatController(){
        boolean rollPressed = Gdx.input.isKeyJustPressed(ControlsDirectory.Movement.ROLL);
        boolean attackPressed = Gdx.input.isButtonJustPressed(ControlsDirectory.Combat.ATTACK);
        boolean blockHeld = Gdx.input.isButtonPressed(ControlsDirectory.Combat.BLOCK);
        int action = 0;

        // full action buffer is accounted for in enqueue method
        if (player.getStamina() > 0) {
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

        if (inControl && player.getStamina() > 0 && actionBuffer.notEmpty()){
            action = actionBuffer.dequeue();
        }
        block(blockHeld); // cannot be doing any other action to block
        if (action == 1){
            rolling = true;
        }else if (action == 2){
            beginAttack(player.getInventory().getWeapon().getAttack());
        }
    }

    private void beginAttack(Attack attackArg){
        inControl = false;
        rotationalTrackingEnabled = false;
        player.takeStamina(attackArg.getStaminaCost());
        player.getInventory().getWeapon().activateAttack();
    }

    private void resolveAttack(){
        player.getInventory().getWeapon().attack();
    }

    private void resolveRoll(){
        if (rolling){
            rolling = roll();
        }
    }

    private void block(boolean blockHeld){ // handles use of the shield
        if (blockHeld && inControl  && player.getStamina() > 0){
            player.getInventory().getShield().activate();
            player.setStaminaRegenCoefficient(player.getStaminaRegenCoefficient()*0.25f); // stamina regen is slowed if blocking
            player.getInventory().getShield().logicTick(player.getPosition().cpy().add(player.getLookVector().cpy().scl(1/8f)), Utils.degreesToRadians(player.getFacing()));
        }else {
            player.getInventory().getShield().deactivate();
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
            player.takeStamina(ROLL_STAMINA_COST);
            player.setSprite(player.getRollAnim().getCurrentFrame());
            AssetDirectory.Audio.Player.ROLL.play(ROLL_VOLUME);
        }
        if (player.getRollAnim().update()){ // change frame of animation
            player.setSprite(player.getRollAnim().getCurrentFrame());
        }

        if (currentRollTick > (ROLL_DURATION - INVINCIBILITY_FRAMES)/2 && currentRollTick < (ROLL_DURATION + INVINCIBILITY_FRAMES)/2){ // i-frames
            player.setVulnerable(false);
        }else if (currentRollTick >= (ROLL_DURATION + INVINCIBILITY_FRAMES)/2){
            player.setVulnerable(true);
        }
        player.setVelocity(player.getLookVector().cpy().scl(-1*((float) ((1/25f)*(-4)*(Math.pow(((double) currentRollTick / ROLL_DURATION), 2))+((double) (4 * (currentRollTick / ROLL_DURATION)))))));
        currentRollTick++;
        if (currentRollTick == ROLL_DURATION){ // end of roll
            inControl = true;
            rotationalTrackingEnabled = true;
            currentRollTick = 0;
            player.getRollAnim().reset();
            return false;
        }
        return true;
    }

    private void inputController(){
        if (inControl){
            directionalMovement();
        }
        player.setPosition(player.getPosition().cpy().add(player.getVelocity()));
        combatController();
    }

    private void initialiseValues(){
        ticksSinceMoveInput = 0;
        ticksWhileMoveInput = 0;
        inControl = true;
        rotationalTrackingEnabled = true;
        currentRollTick = 0;
        actionBuffer = new CircularQueue(2);

    }

    public void setInControl(boolean inControl) {
        this.inControl = inControl;
    }

    public int getTicksSinceMoveInput() {
        return ticksSinceMoveInput;
    }

    public int getTicksWhileMoveInput() {
        return ticksWhileMoveInput;
    }

    public boolean isInControl() {
        return inControl;
    }

    public boolean isRotationalTrackingEnabled() {
        return rotationalTrackingEnabled;
    }

    public boolean isRolling() {
        return rolling;
    }

    public int getCurrentRollTick() {
        return currentRollTick;
    }

    public CircularQueue getActionBuffer() {
        return actionBuffer;
    }

    public void setRotationalTrackingEnabled(boolean rotationalTrackingEnabled) {
        this.rotationalTrackingEnabled = rotationalTrackingEnabled;
    }

}
