package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Level_1 extends Level{
    private final Tile[][] tileArr = new Tile[9][16];
    private Dummy dummy;

    public Level_1(Player playerArg){
        for (int j = 0; j < 9; j++){
            for (int i = 0; i < 16; i++){
                tileArr[j][i] = new Tile(i, j, AssetDirectory.Scene.MUD);
            }
        }
        dummy = new Dummy(1, new Vector2(2,2));
        entities = new Entity[1];
        entities[0] = dummy;
    }

    public void drawAllBodies(SpriteBatch batch){
        drawTiles(batch);
        drawEntityBodies(batch);
    }

    public void drawAllEffects(SpriteBatch batch){
        drawEntityEffects(batch);
    }

    private void drawTiles(SpriteBatch batch){
        for (Tile[] r : tileArr){
            for (Tile t : r){
                t.draw(batch);
            }
        }
    }


    private void drawEntityBodies(SpriteBatch batch){
        for (Entity e : entities){
            e.drawBody(batch);
        }
    }
    private void drawEntityEffects(SpriteBatch batch){
        for (Entity e : entities){
            e.drawEffects(batch);
        }
    }
}
