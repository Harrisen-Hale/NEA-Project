package io.github.some_example_name.Player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Constants;
import io.github.some_example_name.UI.StatusBar;

public class HUD {

    private StatusBar playerHealth;
    private StatusBar playerStamina;
    private StatusBar bossHealth;

    private boolean isInBossFight;

    public HUD(){
        isInBossFight = false;
        playerHealth = new StatusBar(0, new Vector2(-7.9f, 4.25f), 1/6f, 8);
        playerStamina = new StatusBar(1, new Vector2(-7.9f, 4f), 1/6f, 8);
    }

    public void updatePlayerHealthAndStamina(float maxHealth, float health, float maxStamina, float stamina){
        float healthCoefficient = health/maxHealth;
        float staminaCoefficient = stamina/maxStamina;
        playerHealth.updateBar(healthCoefficient);
        playerStamina.updateBar(staminaCoefficient);
    }

    public void updateMaxHealth(float value){
        float lengthScale = value/ Constants.HEALTH_CAP;
        playerHealth.setFullBarWidth(lengthScale);
    }

    public void updateMaxStamina(float value){
        float lengthScale = value/Constants.STAMINA_CAP;
        playerStamina.setFullBarWidth(lengthScale);
    }

    public void draw(SpriteBatch batch, Vector2 screenCentre){
        playerHealth.draw(batch, screenCentre);
        playerStamina.draw(batch, screenCentre);
    }

}
