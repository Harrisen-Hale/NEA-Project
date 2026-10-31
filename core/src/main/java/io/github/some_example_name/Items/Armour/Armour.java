package io.github.some_example_name.Items.Armour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Items.Item;
import io.github.some_example_name.Items.ItemType;

public class Armour extends Item {
    protected float defense;

    public Armour( float defenseArg, String spritePath){
        super();
        itemType = ItemType.ARMOUR;
        defense = defenseArg;
        icon = new Sprite(new Texture(Gdx.files.internal(spritePath)));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }

    public Armour(){
        super();
        itemType = ItemType.ARMOUR;
        defense = 0;
        icon = new Sprite(new Texture(AssetDirectory.Textures.Misc.EMPTY));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }
}
