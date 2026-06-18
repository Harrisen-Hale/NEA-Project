package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUD {
    SpriteBatch hudBatch;

    private StatusBar playerHealth;
    private StatusBar playerStamina;
    private StatusBar bossHealth;

    private boolean isInBossFight;

    public HUD(){
        hudBatch = new SpriteBatch();

        isInBossFight = false;
        playerHealth = new StatusBar(0, -0.975f, 0.935f);
        playerStamina = new StatusBar(1, -0.975f, 0.885f);
    }

    public void updatePlayerHealthAndStamina(float maxHealth, float health, float maxStamina, float stamina){
        float healthCoefficient = health/maxHealth;
        float staminaCoefficient = stamina/maxStamina;
        playerHealth.updateBar(healthCoefficient);
        playerStamina.updateBar(staminaCoefficient);
    }

    public void updateMaxHealth(float value){
        float lengthScale = value/Constants.HEALTH_CAP;
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
