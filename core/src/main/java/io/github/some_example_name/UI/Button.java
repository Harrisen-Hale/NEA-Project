package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Utils;

public class Button extends UIElement{

    private boolean on;

    private Vector2[] vertices;

    private TextBox textBox;


    public Button(Vector2 screenSpacePositionArg, Vector2[] dVertices, String text, float fontScale, String spritePath, float widthArg, float heightArg, float topMarginArg, float leftMarginArg){
        vertices = Utils.translatePolygon(dVertices, screenSpacePositionArg);
        on = false;

        width = widthArg;
        height = heightArg;



        currentSprite = new Sprite(new Texture(Gdx.files.internal(spritePath)));
        currentSprite.setSize(widthArg, heightArg);
        currentSprite.setOriginCenter();

        textBox = new TextBox(screenSpacePositionArg.cpy().add(-widthArg/2f, heightArg/2f), heightArg, widthArg, text, fontScale, topMarginArg, leftMarginArg);

        screenSpacePosition = screenSpacePositionArg;
    }

    public void click(Vector2 clickPosition){
        if (Utils.pointInPolygon(clickPosition, vertices)){
            turnOn();
        }
    }

    private void turnOn(){
        on = true;
    }

    private void turnOff(){
        on = false;
    }

    public boolean readValue(){
        if (on){
            turnOff();
            return true;
        }
        return false;
    }

    public void draw(SpriteBatch batch, Vector2 screenCentre){
        Vector2 adjustedPosition = this.screenSpacePosition.cpy().add(screenCentre).add(0.5f*width, 0.5f*height);
        currentSprite.setPosition(adjustedPosition.x, adjustedPosition.y);
        currentSprite.draw(batch);
        textBox.draw(batch);
    }

    public void draw(SpriteBatch batch){
        currentSprite.setPosition(screenSpacePosition.x-(0.5f*width), screenSpacePosition.y-(0.5f*height)); // top left if no margins
        currentSprite.draw(batch);
        textBox.draw(batch);
    }

}
