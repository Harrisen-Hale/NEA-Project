package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Level {

    protected Entity[] entities; // stores all non-player entities in the level


    public void logicTick(){
        for (Entity e : entities){
            e.logicTick();
        }
    }

    public void drawAllBodies(SpriteBatch batch) {

    }

    public void drawAllEffects(SpriteBatch batch) {

    }

    public void drawAllDebug(ShapeRenderer sr){
        for (Entity e : entities){
            e.drawDebug(sr);
        }
    }

    public Entity[] getEntities() {
        return entities;
    }
}
