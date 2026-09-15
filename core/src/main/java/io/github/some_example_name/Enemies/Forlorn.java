package io.github.some_example_name.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Attacks.ForlornSwipe;
import io.github.some_example_name.Framework.*;
import io.github.some_example_name.Levels.Level;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.UI.StatusBar;
import io.github.some_example_name.World.NavMesh;

public class Forlorn extends Enemy {
    private StatusBar healthBar;
    private AI ai;

    public Forlorn(int IDArg, Vector2 positionArg, Level residentLevelArg){
        super(residentLevelArg);
        loadTextures();
        initialiseBaseValuesAndConstants();
        ID = IDArg;
        position = positionArg;
        body = new Collider[]{new Collider(position, -1/32f, 0, Utils.generateRegularPolygon(8, 0.15f), 0, true, Color.BLUE, true, false)};
        hurtboxes = new Collider[]{new Collider(position, 0,0,new Vector2[]{new Vector2(-3/16f, -5/16f), new Vector2(1/8f, -5/16f), new Vector2(1/8f, 5/16f), new Vector2(-3/16f, 5/16f)}, 0, true, Color.RED, true, false)};
        healthBar = new StatusBar(0, 0, 0, 1/15f, 1);
        maxHealth = 500;
        health = maxHealth;
        healthBar.updateBar(health/maxHealth);
        speed = 1/128f;
        ai = new AI();
        ai.setQuarry(player);
    }

    public void logicTick(){
        super.logicTick();
        healthBar.updateBar(health/maxHealth);
        ai.tick();
    }

    public void drawEffects(Batch batch){
        healthBar.setPosition(new Vector2(position.x-(healthBar.getBarWidth()/2f), position.y+0.45f));
        healthBar.draw(batch);
    }

    protected void loadTextures(){
        Texture textureIdle = new Texture(Gdx.files.internal(AssetDirectory.Textures.Entity.Forlorn.IDLE));

        currentSprite = new Sprite(textureIdle);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
    }

    public void drawDebug(ShapeRenderer sr){
        super.drawDebug(sr);
        //sr.line(position, residentLevel.getPlayer().getPosition());
        ai.f.debugRender(sr);
    }


    public DamageSource[] getDamageSources() {
        return new DamageSource[]{ai.f};
    }

    protected class AI{
        protected int state; // 0 - Idle, 1 - Pursuit, 2 - Combat
        protected float engagementRadius;
        protected Entity quarry;
        protected Vector2 targetPosition;
        public ForlornSwipe f = new ForlornSwipe(Forlorn.this);

        protected AI(){
            state = 1;
            engagementRadius = 1.25f;
            quarry = new Entity();
            targetPosition = quarry.getPosition();
        }

        protected void tick(){
            if (state == 0){
                idle();
            }else if(state == 1){
                pursuit();
            }else if(state == 2){
                combat();
            }
            updateState();
            setTargetPosition(quarry.getPosition());
        }

        private void idle(){

        }

        private void pursuit(){
            velocity = new Vector2(0,0);
            pathfinder.track(targetPosition);
        }

        private void combat(){
            velocity = new Vector2(0,0);
            f.execute();
        }

        private void updateState(){
            if (canPursue()){
                state = 1;
            }
            if (canEngage()) {
                state = 2;
            }
        }

        private boolean canPursue(){
            float sightRange = 7.5f;
            boolean seenTarget = state == 0 && pathfinder.pathClear(targetPosition) && Utils.findDistance(position, targetPosition) <= sightRange;
            boolean leftCombat = state == 2 && Utils.findDistance(position, targetPosition) > engagementRadius;
            boolean attacking = f.isActive();
            return (seenTarget || leftCombat) && !attacking;
        }

        private boolean canEngage(){
            return Utils.findDistance(position, targetPosition) <= engagementRadius;
        }

        public Entity getQuarry() {
            return quarry;
        }

        public void setQuarry(Entity quarry) {
            this.quarry = quarry;
        }

        public Vector2 getTargetPosition() {
            return targetPosition;
        }

        public void setTargetPosition(Vector2 targetPosition) {
            this.targetPosition = targetPosition;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

}
