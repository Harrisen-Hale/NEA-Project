package io.github.some_example_name.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Framework.AssetDirectory;

public class Armour extends Item{
    protected int piece; // 0 - Head, 1 - Torso, 2 - Legs, 3 - Feet
    protected float defense;

    public Armour(int pieceArg, float defenseArg, String spritePath){
        super();
        piece = pieceArg;
        defense = defenseArg;
        icon = new Sprite(new Texture(Gdx.files.internal(spritePath)));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }

    public Armour(){
        super();
        piece = 0;
        defense = 0;
        icon = new Sprite(new Texture(AssetDirectory.Textures.Misc.EMPTY));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }
}
