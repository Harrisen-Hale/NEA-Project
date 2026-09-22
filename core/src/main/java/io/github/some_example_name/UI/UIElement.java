package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class UIElement {
    protected Vector2 screenSpacePosition;
    protected Sprite currentSprite;

    protected float height;
    protected float width;

    public UIElement(){
        currentSprite = new Sprite();
        screenSpacePosition = new Vector2(0,0);
    }

    public UIElement(Vector2 startPosition, String spritePath, float width, float height){
        currentSprite = new Sprite(new Texture(Gdx.files.internal(spritePath)));
        currentSprite.setSize(width, height);
        currentSprite.setOriginCenter();
        screenSpacePosition = startPosition;
    }

    public void setScreenSpacePosition(Vector2 screenSpacePosition) {
        this.screenSpacePosition = new Vector2(screenSpacePosition.x, screenSpacePosition.y);
    }

    public void draw(SpriteBatch batch, Vector2 screenCentre){
        Vector2 adjustedPosition = this.screenSpacePosition.cpy().add(screenCentre);
        currentSprite.setPosition(adjustedPosition.x, adjustedPosition.y);
        currentSprite.draw(batch);
    }
}
