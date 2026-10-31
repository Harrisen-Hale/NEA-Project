package io.github.some_example_name.Items.Armour;

import io.github.some_example_name.Items.ItemType;

public class Head extends Armour{
    public Head(float defenseArg, String spritePath){
        super(defenseArg, spritePath);
        itemType = ItemType.HEAD;
    }

    public Head(){
        super();
        itemType = ItemType.HEAD;
    }

}
