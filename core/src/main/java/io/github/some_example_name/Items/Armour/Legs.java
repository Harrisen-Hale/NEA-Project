package io.github.some_example_name.Items.Armour;

import io.github.some_example_name.Items.ItemType;

public class Legs extends Armour{
    public Legs(float defenseArg, String spritePath){
        super(defenseArg, spritePath);
        itemType = ItemType.LEGS;
    }

    public Legs(){
        super();
        itemType = ItemType.LEGS;
    }

}
