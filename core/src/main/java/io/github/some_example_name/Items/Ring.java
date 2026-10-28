package io.github.some_example_name.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Framework.AssetDirectory;

public class Ring extends Item{
    public Ring(String spritePath){
        super();
        icon = new Sprite(new Texture(spritePath));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }

    public Ring(){
        super();
    }
}
