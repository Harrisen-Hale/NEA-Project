package io.github.some_example_name.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.World.Obstacle;
import io.github.some_example_name.Player.Player;

public class Entity extends GameObject {
    protected int ID;
    protected float maxHealth;
    protected float health;
    protected Vector2 velocity;
    protected Vector2 moveVector; // unit vector in movement direction
    protected Vector2 lookVector; // unit vector in look direction
    protected float facing; // degrees
    protected int souls;
    protected boolean alive;

    protected Collider[] body; // collision region
    protected Collider[] hurtboxes; // damageable region

    protected Sprite currentSprite;

    public Entity(){
        loadTextures();
        initialiseBaseValuesAndConstants();
    }

    public void logicTick(Player player){
        transformColliders();
    }

    public void collision(Entity ref){
        bodyCollision(ref);
        hitboxOnHurtboxCollision(ref.getDamageSources());
    }

    public void bodyCollision(Entity ref){
        for (Collider c1 : body){
            if (c1.isActive()) {
                for (Collider c2 : ref.getBody()) {
                    if (c2.isActive()) {
                        Vector2 mtv = c1.detectCollision(c2);
                        move(mtv);
                        transformColliders();
                    }
                }
            }
        }
    }

    public void bodyCollision(Obstacle ref){
        for (Collider c1 : body){
            if (c1.isActive()) {
                for (Collider c2 : ref.getBody()) {
                    if (c2.isActive()) {
                        Vector2 mtv = c1.detectCollision(c2);
                        position.add(mtv);
                        transformColliders();
                    }
                }
            }
        }
    }

    public void hitboxOnHurtboxCollision(DamageSource[] damageSources){ // this entity's hurtboxes check external hitboxes
        for (Collider hurtbox : hurtboxes){
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

    protected void rotation(Vector2 target){
        Vector2 vToTarget = target.cpy().sub(position);
        float theta = vToTarget.angleDeg();
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

    public void transformColliders(){
        for (Collider c : body){
            c.setAngle(Utils.degreesToRadians(facing));
            c.setPosition(position);
            c.setVertices();
        }
        for (Collider h : hurtboxes){
            h.setAngle(Utils.degreesToRadians(facing));
            h.setPosition(position);
            h.setVertices();
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

    public Vector2 getVelocity() {
        return velocity;
    }

    public void move(Vector2 v){
        position.add(v);
    }

    public int getID() {
        return ID;
    }

    public Collider[] getBody() {
        return body;
    }

    public Collider[] getHurtbox() {
        return hurtboxes;
    }

    public DamageSource[] getDamageSources(){
        return new DamageSource[]{};
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
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
