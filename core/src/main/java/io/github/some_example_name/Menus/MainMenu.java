package io.github.some_example_name.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.UI.Button;
import io.github.some_example_name.UI.UIElement;

public class MainMenu extends Menu{

    public MainMenu(OrthographicCamera cameraArg, MenuManager menuManagerArg){
        super();

        menuManager = menuManagerArg;

        initialiseButtons();
        initialiseVisuals();
        active = true;
        pausesGame = true;
        camera = cameraArg;
    }

    public void logicTick(){
        super.logicTick();
        buttonFunctionality();
    }

    private void buttonFunctionality(){
        if (buttons[0].readValue()){
            active = false;
        }
        if (buttons[1].readValue()){
            Gdx.app.exit();
        }
    }

    private void initialiseButtons(){
        Vector2[] buttonShape = new Vector2[]{new Vector2(-1.5f, -0.5f),new Vector2(1.5f, -0.5f),new Vector2(1.5f, 0.5f),new Vector2(-1.5f, 0.5f)};
        float buttonFontScale = 0.03f;

        buttons = new Button[2];
        buttons[0] = new Button(new Vector2(0,-0.5f), buttonShape, "Play Game", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND1,3, 1, 0.3f, 0.45f);
        buttons[1] = new Button(new Vector2(0,-2), buttonShape, "Quit Game", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND1,3, 1,0.3f, 0.45f);

    }

    private void initialiseVisuals(){
        UIElements = new UIElement[1];
        UIElements[0] = new UIElement(new Vector2(0,2.5f), AssetDirectory.Textures.UI.MAIN_TITLE, 12, 5);
    }

}
