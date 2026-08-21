package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Tile extends GameObject{

    protected Texture texture;

    public Tile(int x, int y, String textureName){
        position = new Vector2(x,y);
        loadTextures(textureName);
    }

    public void draw(Batch batch){
        batch.draw(texture, position.x, position.y, 1f, 1f);
    }

    private void loadTextures(String textureDirectory){
        texture = new Texture(Gdx.files.internal(textureDirectory));
    }
}
