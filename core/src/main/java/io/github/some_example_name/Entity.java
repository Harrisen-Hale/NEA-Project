package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

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

    protected Collider[] body; // collision region
    protected Collider[] hurtboxes; // damageable region
    protected Collider[] hitboxes; // damaging region

    protected Sprite currentSprite;

    public Entity(){
        loadTextures();
        initialiseBaseValuesAndConstants();
    }

    public void logicTick(){
        transformColliders();
    }

    public void collision(Collider[] refBody, Collider[] refHitBoxes){
        bodyCollision(refBody);
        hitboxOnHurtboxCollision(refHitBoxes);
    }

    private void bodyCollision(Collider[] refBody){
        for (Collider c1 : body){
            for (Collider c2 : refBody) {
                Vector2 mtv = c1.detectCollision(c2);
                position.add(mtv);
                transformColliders();
            }
        }
    }

    private void hitboxOnHurtboxCollision(Collider[] refHitboxes){
        for (Collider h1 : hurtboxes){
            for (Collider h2 : refHitboxes) {
                if (h2.isHitbox() && h1.detectCollision(h2).len() > 0){
                    damageHealth(h2.getDamageValue());
                }
            }
        }
    }

    public void transformColliders(){
        for (Collider c : body){
            c.setAngle(Utils.degreesToRadians(facing));
            c.setPosition(position);
        }
        for (Collider h : hurtboxes){
            h.setAngle(Utils.degreesToRadians(facing));
            h.setPosition(position);
        }
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
        maxHealth = 0;
        health = 0;
        souls = 0;
        body = new Collider[0];
        hurtboxes = new Collider[0];
        hitboxes = new Collider[0];
    }

    public void drawDebug(ShapeRenderer sr){
        for (Collider c : body){
            c.debugRender(sr);
        }
        for (Collider h : hurtboxes){
            h.debugRender(sr);
        }
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

    public Collider[] getBody() {
        return body;
    }

    public Collider[] getHurtbox() {
        return hurtboxes;
    }

    public Collider[] getHitboxes() {
        return hitboxes;
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
