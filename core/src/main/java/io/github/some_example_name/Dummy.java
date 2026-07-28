package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Dummy extends Entity{

    private StatusBar healthBar;

    public Dummy(Vector2 positionArg){
        loadTextures();
        initialiseBaseValuesAndConstants();
        position = positionArg;
        colliders = new Collider[]{new Collider(position, 0, 0, Utils.generateRegularPolygon(8, 0.15f), 0, Color.BLUE, true, true)};
        hitbox = new Collider[]{new Collider(position, 0, 0, Utils.generateRegularPolygon(8, 0.4f), 0, Color.RED, true, true)};
        healthBar = new StatusBar(0, 0, 0, 1/15f, 1);
        health = 75;
        healthBar.updateBar(health/maxHealth);
    }

    protected void loadTextures(){
        Texture textureIdle = new Texture(Gdx.files.internal(AssetDirectory.Textures.Entity.DUMMY));

        currentSprite = new Sprite(textureIdle);
        currentSprite.setSize(1f, 1f);
        currentSprite.setOriginCenter();
    }

    public void drawBody(Batch batch){
        currentSprite.setPosition(position.x-0.5f, position.y-0.5f);
        currentSprite.setRotation(facing);
        currentSprite.draw(batch);
    }

    public void drawEffects(Batch batch){
        healthBar.setPosition(new Vector2(position.x-(healthBar.getBarWidth()/2f), position.y+0.45f));
        healthBar.draw(batch);
    }

    public void damageHealth(float damage) {
        this.health -= damage;
        if (health < 0){
            health = maxHealth;
        }
    }
}
