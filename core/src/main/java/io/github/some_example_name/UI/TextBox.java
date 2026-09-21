package io.github.some_example_name.UI;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextBox extends UIElement{

    String text;
    BitmapFont font;

    public TextBox(Vector2 positionArg, float heightArg, float widthArg, String textArg, float fontScale){
        super();
        screenSpacePosition = positionArg;
        height = heightArg;
        width = widthArg;
        text = textArg;
        font = new BitmapFont();
        font.getData().setScale(fontScale);
        font.setUseIntegerPositions(false);
    }

    public void draw(SpriteBatch batch, Vector2 screenCentre){
        Vector2 adjustedPosition = this.screenSpacePosition.cpy().add(screenCentre);
        font.draw(batch, text, adjustedPosition.x, adjustedPosition.y);
    }

    public void draw(SpriteBatch batch){
        font.draw(batch, text, screenSpacePosition.x, screenSpacePosition.y);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
