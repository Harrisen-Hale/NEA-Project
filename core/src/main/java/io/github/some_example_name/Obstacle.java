package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Obstacle extends Tile{
    protected Collider[] body; // collision region

    public Obstacle(int x, int y, String textureName){
        super(x, y, textureName);
    }

    public void drawDebug(ShapeRenderer sr){
        for (Collider c : body){
            c.debugRender(sr);
        }
    }

    public void draw(Batch batch){
        batch.draw(texture, position.x - (float) texture.getWidth() /(Constants.PIXELS_PER_UNIT*2), position.y - (float) texture.getHeight() /(Constants.PIXELS_PER_UNIT*2), 1f, 1f);
    }

    public Collider[] getBody() {
        return body;
    }
}
