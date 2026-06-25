package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class StatusBar extends UIElement{

    private Sprite barSprite;
    private Sprite backGroundSprite;

    private final float barHeight;
    private float barWidth;

    public StatusBar(int barType, float xPos, float yPos, float barHeightArg, float barWidthArg){ // Bar types: 0 - Player Health, 1 - Player Stamina, 2 - Boss Health
        barHeight = barHeightArg;
        barWidth = barWidthArg;

        loadTextures(barType);
        barSprite.setPosition(xPos, yPos);
        backGroundSprite.setPosition(xPos,yPos);
    }

    public void draw(Batch batch){
        backGroundSprite.draw(batch);
        barSprite.draw(batch);
    }

    private void loadTextures(int barType){

        if (barType == 0){
            barSprite = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.HEALTH_BAR)));
            barSprite.setSize(barWidth, barHeight);
        }else if (barType == 1) {
            barSprite = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.STAMINA_BAR)));
            barSprite.setSize(barWidth, barHeight);
        }else if (barType == 2){
            barSprite = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.HEALTH_BAR)));
            barSprite.setSize(barWidth, barHeight);
        }
        assert barSprite != null;
        barSprite.setOriginCenter();

        backGroundSprite = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.BACKGROUND_BAR)));
        backGroundSprite.setSize(barWidth, barHeight);
        backGroundSprite.setOriginCenter();
    }

    public void updateBar(float coefficient){ // coeff in interval [0,1]
        barSprite.setSize(barWidth*coefficient, barHeight);
    }

    public void setBarMaxSize(float lengthScale){ // coeff in interval [0,1]
        barWidth = lengthScale;
        barSprite.setSize(barWidth, barHeight);
        backGroundSprite.setSize(barWidth, barHeight);
    }

    public void setPosition(Vector2 positionArg) {
        barSprite.setPosition(positionArg.x, positionArg.y);
        backGroundSprite.setPosition(positionArg.x, positionArg.y);
    }

    public float getBarHeight() {
        return barHeight;
    }

    public float getBarWidth() {
        return barWidth;
    }
}
