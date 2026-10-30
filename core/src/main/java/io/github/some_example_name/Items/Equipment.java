package io.github.some_example_name.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Items.EquipmentActions.EquipmentAction;
import io.github.some_example_name.Levels.Level;

public class Equipment extends Item{
    protected EquipmentAction action;

    public Equipment(EquipmentAction actionArg, String spritePath){
        super();
        itemType = ItemType.EQUIPMENT;
        action = actionArg;
        icon = new Sprite(new Texture(Gdx.files.internal(spritePath)));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }

    public Equipment(){
        super();
        itemType = ItemType.EQUIPMENT;
        action = new EquipmentAction();
    }

    public void logicTick(Level currentLevel){
        action.logicTick(currentLevel);
    }

}
