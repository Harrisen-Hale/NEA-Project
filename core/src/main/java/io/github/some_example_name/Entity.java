package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Entity extends GameObject{
    protected float maxHealth;
    protected float health;
    protected Vector2 velocity;
    protected Vector2 moveVector; // unit vector in movement direction
    protected Vector2 lookVector; // unit vector in look direction
    protected float facing;
    protected int souls;
    protected boolean alive;
    protected boolean hostile;

    protected Sprite currentSprite;

    public Entity(){
        loadTextures();
        initialiseBaseValuesAndConstants();
    }

    public void logicTick(){

    }

    public void drawBody(Batch batch){
        currentSprite.setPosition(position.x-0.5f, position.y-0.5f);
        currentSprite.setRotation(facing);
        currentSprite.draw(batch);
    }
    public void drawEffects(Batch batch){

    }

    protected void loadTextures(){
        Texture textureIdle = new Texture(Gdx.files.internal(AssetDirectory.Textures.Misc.NOT_FOUND));

        currentSprite = new Sprite(textureIdle);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
    }

    protected void initialiseBaseValuesAndConstants(){
        position = new Vector2(0,0);
        velocity = new Vector2(0,0);
        moveVector = new Vector2(0,0);
        facing = 0;
        maxHealth = 100;
        health = maxHealth;
        souls = 100;
    }

    // getters and setters
    public float getMaxHealth() {
        return maxHealth;
    }

    public float getHealth() {
        return health;
    }

    public int getSouls() {
        return souls;
    }

    public Vector2 getPosition() {
        return position;
    }


    protected void setSprite(Texture newTexture){
        currentSprite = new Sprite(newTexture);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
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
