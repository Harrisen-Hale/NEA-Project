package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;

public class StatusBar extends UIElement {

    private Sprite barSprite;
    private Sprite backGroundSprite;

    private float maxBarWidth;

    public StatusBar(int barType, Vector2 positionArg, float heightArg, float maxBarWidthArg){ // Bar types: 0 - Player Health, 1 - Player Stamina, 2 - Boss Health
        super();
        screenSpacePosition = positionArg;
        height = heightArg;
        maxBarWidth = maxBarWidthArg;

        loadTextures(barType);
    }

    public void draw(SpriteBatch batch, Vector2 screenCentre){
        Vector2 adjustedPosition = this.screenSpacePosition.cpy().add(screenCentre);
        barSprite.setPosition(adjustedPosition.x, adjustedPosition.y);
        backGroundSprite.setPosition(adjustedPosition.x, adjustedPosition.y);
        backGroundSprite.draw(batch);
        barSprite.draw(batch);
    }

    public void draw(SpriteBatch batch){
        backGroundSprite.draw(batch);
        barSprite.draw(batch);
    }

    private void loadTextures(int barType){

        if (barType == 0){
            barSprite = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.HEALTH_BAR)));
            barSprite.setSize(maxBarWidth, height);
        }else if (barType == 1) {
            barSprite = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.STAMINA_BAR)));
            barSprite.setSize(maxBarWidth, height);
        }else if (barType == 2){
            barSprite = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.HEALTH_BAR)));
            barSprite.setSize(maxBarWidth, height);
        }
        assert barSprite != null;
        barSprite.setOriginCenter();

        backGroundSprite = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.BACKGROUND_BAR)));
        backGroundSprite.setSize(maxBarWidth, height);
        backGroundSprite.setOriginCenter();
    }

    public void updateBar(float coefficient){ // coeff in interval [0,1]
        barSprite.setSize(maxBarWidth *coefficient, height);
    }

    public void setFullBarWidth(float lengthScale){
        maxBarWidth = lengthScale* maxBarWidth;
        barSprite.setSize(maxBarWidth, height);
        backGroundSprite.setSize(maxBarWidth, height);
    }

    public void setScreenSpacePosition(Vector2 positionArg) {
        barSprite.setPosition(positionArg.x, positionArg.y);
        backGroundSprite.setPosition(positionArg.x, positionArg.y);
    }

    public float getHeight() {
        return height;
    }

    public float getMaxBarWidth() {
        return maxBarWidth;
    }
}
