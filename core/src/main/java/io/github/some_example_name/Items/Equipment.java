package io.github.some_example_name.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Framework.AssetDirectory;

public class Equipment extends Item{

    public Equipment(String spritePath){
        super();
        icon = new Sprite(new Texture(Gdx.files.internal(spritePath)));
    }

    public Equipment(){
        super();
    }

}
