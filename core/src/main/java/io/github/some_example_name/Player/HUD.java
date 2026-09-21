package io.github.some_example_name.Player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Constants;
import io.github.some_example_name.UI.StatusBar;

public class HUD {
    SpriteBatch hudBatch;

    private StatusBar playerHealth;
    private StatusBar playerStamina;
    private StatusBar bossHealth;

    private boolean isInBossFight;

    public HUD(){
        hudBatch = new SpriteBatch();
        isInBossFight = false;
        playerHealth = new StatusBar(0, new Vector2(-0.975f, 0.935f), 1/27.5f, 1);
        playerStamina = new StatusBar(1, new Vector2(-0.975f, 0.885f), 1/27.5f, 1);
    }

    public void updatePlayerHealthAndStamina(float maxHealth, float health, float maxStamina, float stamina){
        float healthCoefficient = health/maxHealth;
        float staminaCoefficient = stamina/maxStamina;
        playerHealth.updateBar(healthCoefficient);
        playerStamina.updateBar(staminaCoefficient);
    }

    public void updateMaxHealth(float value){
        float lengthScale = value/ Constants.HEALTH_CAP;
        playerHealth.setBarMaxSize(lengthScale);
    }

    public void updateMaxStamina(float value){
        float lengthScale = value/Constants.STAMINA_CAP;
        playerStamina.setBarMaxSize(lengthScale);
    }

    public void draw(SpriteBatch batch){
        playerHealth.draw(batch);
        playerStamina.draw(batch);
    }

}
