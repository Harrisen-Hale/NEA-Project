package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class StatusBar extends UIElement{

    private Texture textureHealth;
    private Texture textureStamina;
    private Texture textureBackground;
    private Sprite barSprite;
    private Sprite backGroundSprite;

    private final float barHeight;
    private float barWidth;

    public StatusBar(int barType, float xPos, float yPos){ // Bar types: 0 - Player Health, 1 - Player Stamina, 2 - Boss Health
        barHeight = 1/27.5f;
        barWidth = 1;

        loadTextures(barType);
        barSprite.setPosition(xPos, yPos);
        backGroundSprite.setPosition(xPos,yPos);
    }

    public void draw(Batch batch){
        backGroundSprite.draw(batch);
        barSprite.draw(batch);
    }

    private void loadTextures(int barType){
        textureHealth = new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.HEALTH_BAR));
        textureStamina = new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.STAMINA_BAR));
        textureBackground = new Texture(Gdx.files.internal(AssetDirectory.Textures.UI.BACKGROUND_BAR));

        if (barType == 0){
            barSprite = new Sprite(textureHealth);
            barSprite.setSize(barWidth, barHeight);
        }else if (barType == 1) {
            barSprite = new Sprite(textureStamina);
            barSprite.setSize(barWidth, barHeight);
        }else if (barType == 2){
            barSprite = new Sprite(textureHealth);
            barSprite.setSize(barWidth, barHeight);
        }
        assert barSprite != null;
        barSprite.setOriginCenter();

        backGroundSprite = new Sprite(textureBackground);
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
}
