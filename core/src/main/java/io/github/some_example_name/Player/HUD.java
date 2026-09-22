package io.github.some_example_name.Player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Constants;
import io.github.some_example_name.UI.StatusBar;
import io.github.some_example_name.UI.TextBox;
import io.github.some_example_name.UI.UIElement;
import io.github.some_example_name.UI.UIObject;

public class HUD {

    private StatusBar playerHealth;
    private StatusBar playerStamina;
    private StatusBar bossHealth;
    private UIObject soulCounter;

    private boolean isInBossFight;

    public HUD(){
        isInBossFight = false;
        playerHealth = new StatusBar(0, new Vector2(-7.9f, 4.25f), 1/6f, 8);
        playerStamina = new StatusBar(1, new Vector2(-7.9f, 4f), 1/6f, 8);
        initialiseSoulCounter();
    }

    private void initialiseSoulCounter(){
        soulCounter = new UIObject();
        UIElement background = new UIElement(new Vector2(5.25f,-4.25f), AssetDirectory.Textures.UI.BACKGROUND1, 1.8f, 0.6f);
        soulCounter.addUIElements(new UIElement[]{background});
        TextBox soulCount = new TextBox(new Vector2(1,1), 0.5f, 2, "0", 0.075f);
        soulCounter.addTextBoxes(new TextBox[]{soulCount});
    }

    public void updatePlayerData(Player player){
        float healthCoefficient = player.getHealth()/player.getMaxHealth();
        float staminaCoefficient = player.getStamina()/player.getMaxStamina();
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
        soulCounter.draw(batch, screenCentre);
    }

}
