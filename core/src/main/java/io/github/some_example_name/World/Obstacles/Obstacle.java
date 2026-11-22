package io.github.some_example_name.World.Obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.Constants;
import io.github.some_example_name.Framework.GameObject;
import io.github.some_example_name.World.Tile;

public class Obstacle extends GameObject {
    protected Collider[] body; // collision region
    protected Sprite currentSprite;

    public Obstacle(Vector2 positionArg, String spritePath, float widthArg, float heightArg, float angle){ // angle in degrees
        position = positionArg;
        currentSprite = new Sprite(new Texture(Gdx.files.internal(spritePath)));
        currentSprite.setSize(widthArg, heightArg);
        currentSprite.setOriginCenter();
        currentSprite.setPosition(position.x-(0.5f*widthArg), position.y-(0.5f*heightArg));
        currentSprite.rotate(angle);
    }

    public void drawDebug(ShapeRenderer sr){
        for (Collider c : body){
            c.drawDebug(sr);
        }
    }

    public void draw(Batch batch){
        currentSprite.draw(batch);
    }

    public Collider[] getBody() {
        return body;
    }
}
