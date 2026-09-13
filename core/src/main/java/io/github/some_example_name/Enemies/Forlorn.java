package io.github.some_example_name.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.Entity;
import io.github.some_example_name.Framework.Utils;
import io.github.some_example_name.Levels.Level;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.UI.StatusBar;
import io.github.some_example_name.World.NavMesh;

public class Forlorn extends Enemy {
    private StatusBar healthBar;

    public Forlorn(int IDArg, Vector2 positionArg, Level residentLevelArg){
        super(residentLevelArg);
        loadTextures();
        initialiseBaseValuesAndConstants();
        ID = IDArg;
        position = positionArg;
        body = new Collider[]{new Collider(position, -1/32f, 0, Utils.generateRegularPolygon(8, 0.15f), 0, 0, true, Color.BLUE, true, false)};
        hurtboxes = new Collider[]{new Collider(position, 0,0,new Vector2[]{new Vector2(-3/16f, -5/16f), new Vector2(1/8f, -5/16f), new Vector2(1/8f, 5/16f), new Vector2(-3/16f, 5/16f)}, 0, 0, true, Color.RED, true, false)};
        healthBar = new StatusBar(0, 0, 0, 1/15f, 1);
        maxHealth = 500;
        health = maxHealth;
        healthBar.updateBar(health/maxHealth);
        speed = 1/64f;
    }

    public void logicTick(){
        super.logicTick();
        healthBar.updateBar(health/maxHealth);
        rotation(player.getPosition());
        pathfinder.track(player.getPosition());
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

}
