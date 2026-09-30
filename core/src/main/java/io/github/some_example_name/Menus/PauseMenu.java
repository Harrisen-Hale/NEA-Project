package io.github.some_example_name.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.IO.ControlsDirectory;
import io.github.some_example_name.UI.Button;

public class PauseMenu extends Menu{

    public PauseMenu(OrthographicCamera cameraArg, MenuManager menuManagerArg){
        super();

        menuManager = menuManagerArg;

        camera = cameraArg;
        initialiseButtons();
        active = false;
        pausesGame = false; // ironically, the pause menu does not pause the game
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
            quitToTitle();
        }
    }

    private void quitToTitle(){
        menuManager.setCurrentMenu(menuManager.mainMenu);
        menuManager.getCurrentMenu().setActive(true);
    }

    public void toggle(){
        if (Gdx.input.isKeyJustPressed(ControlsDirectory.Menu.PAUSE)){
            active = !active;
            menuManager.setCurrentMenu(this);
        }
    }

    private void initialiseButtons(){
        Vector2[] buttonShape = new Vector2[]{new Vector2(-1.5f, -0.5f),new Vector2(1.5f, -0.5f),new Vector2(1.5f, 0.5f),new Vector2(-1.5f, 0.5f)};
        float buttonFontScale = 0.03f;

        buttons = new Button[2];
        buttons[0] = new Button(new Vector2(2.5f,3.75f), buttonShape, "Inventory", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND1,3, 1, 0.3f, 0.65f);
        buttons[1] = new Button(new Vector2(5.85f,3.75f), buttonShape, "Save & Quit", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND1,3, 1,0.3f, 0.3f);

    }

}
