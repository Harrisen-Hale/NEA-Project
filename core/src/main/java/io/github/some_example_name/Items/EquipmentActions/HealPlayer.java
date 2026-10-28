package io.github.some_example_name.Items.EquipmentActions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.some_example_name.IO.ControlsDirectory;
import io.github.some_example_name.Levels.Level;

public class HealPlayer extends EquipmentAction{
    protected float healAmount;

    public HealPlayer(float healAmountArg){
        healAmount = healAmountArg;
    }

    public void logicTick(Level currentLevel){
        if (Gdx.input.isKeyJustPressed(ControlsDirectory.Combat.USE_EQUIPMENT)) {
            currentLevel.getPlayer().healHealth(healAmount);
            // sound effect
        }
    }

}
