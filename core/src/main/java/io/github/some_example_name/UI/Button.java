package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Utils;

public class Button extends UIElement{

    private boolean on;

    private Sprite currentSprite;

    private Sprite buttonSpriteOn;
    private Sprite buttonSpriteOff;

    private Vector2[] vertices;


    public Button(Vector2 position, Vector2[] dVertices, String text, String buttonSpriteOnPath, String buttonSpriteOffPath, float spriteWidth, float spriteHeight){
        vertices = Utils.translatePolygon(dVertices, position);
        on = false;

        buttonSpriteOn = new Sprite(new Texture(Gdx.files.internal(buttonSpriteOnPath)));
        buttonSpriteOff = new Sprite(new Texture(Gdx.files.internal(buttonSpriteOffPath)));
        buttonSpriteOn.setSize(spriteWidth, spriteHeight);
        buttonSpriteOff.setSize(spriteWidth, spriteHeight);
        buttonSpriteOn.setOriginCenter();
        buttonSpriteOff.setOriginCenter();
        buttonSpriteOn.setPosition(position.x, position.y);
        buttonSpriteOff.setPosition(position.x, position.y);
        currentSprite = buttonSpriteOff;
    }

    public void click(Vector2 clickPosition){ // clicks must be polled for after tick is called
        if (Utils.pointInPolygon(clickPosition, vertices)){
            turnOn();
        }
    }

    private void turnOn(){
        on = true;
        currentSprite = buttonSpriteOn;
    }

    private void turnOff(){
        on = false;
        currentSprite = buttonSpriteOff;
    }

    public boolean readValue(){
        if (on){
            turnOff();
            return true;
        }
        return false;
    }

    public void draw(SpriteBatch batch){
        currentSprite.draw(batch);
    }

}
