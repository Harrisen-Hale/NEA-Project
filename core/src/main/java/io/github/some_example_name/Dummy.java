package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Dummy extends Entity{

    private StatusBar healthBar;
    private Attack attack;

    public Dummy(int IDArg, Vector2 positionArg){
        loadTextures();
        initialiseBaseValuesAndConstants();
        ID = IDArg;
        position = positionArg;
        body = new Collider[]{new Collider(position, 0, 0, Utils.generateRegularPolygon(8, 0.15f), 0, 0, true, Color.BLUE, false, false)};
        hurtboxes = new Collider[]{new Collider(position, 0, 0, Utils.generateRegularPolygon(8, 0.4f), 0, 0, true, Color.RED, true, false)};
        healthBar = new StatusBar(0, 0, 0, 1/15f, 1);
        maxHealth = 500;
        health = maxHealth;
        healthBar.updateBar(health/maxHealth);
        attack = new Attack();
    }

    public void logicTick(Player player){
        transformColliders();
        healthBar.updateBar(health/maxHealth);
    }

    protected void loadTextures(){
        Texture textureIdle = new Texture(Gdx.files.internal(AssetDirectory.Textures.Entity.DUMMY));

        currentSprite = new Sprite(textureIdle);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
    }

    public void drawEffects(Batch batch){
        healthBar.setPosition(new Vector2(position.x-(healthBar.getBarWidth()/2f), position.y+0.45f));
        healthBar.draw(batch);
    }

    public void drawDebug(ShapeRenderer sr){
        for (Collider c : body){
            c.debugRender(sr);
        }
        for (Collider h : hurtboxes){
            h.debugRender(sr);
        }
        attack.getHitbox()[0].debugRender(sr);
    }

    public void damageHealth(float damage) {
        this.health -= damage;
        if (health <= 0){
            health = maxHealth;
        }
        attack.reset();
    }

    public DamageSource[] getDamageSources(){
        return new DamageSource[]{attack};
    }

    private class Attack extends DamageSource{

        private Attack(){
            hitbox = new Collider[]{new Collider(position.cpy().add(0, -1f), 0,0, new Vector2[]{new Vector2(-0.1f, -0.15f), new Vector2(0.1f, -0.15f), new Vector2(0.1f, 0.6f), new Vector2(-0.1f, 0.6f)}, 0, 10f, true, Color.ORANGE, true, false)};
            hitbox[0].setVertices();
            attackFlagManager = new AttackFlagManager();
        }

        public void reset(){
            attackFlagManager.clear();
        }
    }
}
