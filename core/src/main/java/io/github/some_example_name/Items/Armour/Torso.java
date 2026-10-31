package io.github.some_example_name.Items.Armour;

import io.github.some_example_name.Items.ItemType;

public class Torso extends Armour{
    public Torso(float defenseArg, String spritePath){
        super(defenseArg, spritePath);
        itemType = ItemType.TORSO;
    }

    public Torso(){
        super();
        itemType = ItemType.TORSO;
    }

}
