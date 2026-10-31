package io.github.some_example_name.Items.Armour;

import io.github.some_example_name.Items.ItemType;

public class Feet extends Armour{
    public Feet(float defenseArg, String spritePath){
        super(defenseArg, spritePath);
        itemType = ItemType.FEET;
    }

    public Feet(){
        super();
        itemType = ItemType.FEET;
    }

}
