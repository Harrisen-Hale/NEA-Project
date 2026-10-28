package io.github.some_example_name.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Items.EquipmentActions.HealPlayer;

public class IgnisVial extends Equipment{ // testing class, should be a serialised Equipment in full game
    public IgnisVial(){
        super();
        action = new HealPlayer(10f);
        icon = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.Items.Equipment.IGNIS_VIAL)));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }
}
