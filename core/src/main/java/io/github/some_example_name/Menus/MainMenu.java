package io.github.some_example_name.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.UI.Button;

public class MainMenu extends Menu{

    public MainMenu(){
        super();
        initialiseButtons();
        initialiseVisuals();
        active = true;
        pausesGame = true;
    }

    private void initialiseButtons(){
        Vector2[] buttonShape = new Vector2[]{};
        float buttonFontScale = 0.03f;

        buttons = new Button[2];
        buttons[0] = new Button(new Vector2(0,1), buttonShape, "Play Game", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND1,3, 1);
        buttons[1] = new Button(new Vector2(0,-1), buttonShape, "Quit Game", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND1,3, 1);

    }

    private void initialiseVisuals(){

    }

}
