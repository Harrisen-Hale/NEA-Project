package io.github.some_example_name.Menus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.UI.Button;
import io.github.some_example_name.UI.UIElement;

public class InventoryMenu extends Menu{

    public InventoryMenu(OrthographicCamera cameraArg, MenuManager menuManagerArg){
        super();
        initialiseButtons();
        initialiseVisuals();
        menuManager = menuManagerArg;

        camera = cameraArg;
        active = false;
        pausesGame = false;
    }

    public void logicTick(){
        super.logicTick();
        buttonFunctionality();
    }

    private void buttonFunctionality(){

    }

    private void initialiseButtons(){
        Vector2[] buttonShape = new Vector2[]{new Vector2(-0.5f, -0.5f),new Vector2(0.5f, -0.5f),new Vector2(0.5f, 0.5f),new Vector2(-0.5f, 0.5f)};
        float buttonFontScale = 0.03f;

        buttons = new Button[12];
        // armour
        buttons[0] = new Button(new Vector2(-3.5f,1.75f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1, 0.3f, 0.45f);
        buttons[1] = new Button(new Vector2(-3.5f,0.5f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);
        buttons[2] = new Button(new Vector2(-3.5f,-0.75f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);
        buttons[3] = new Button(new Vector2(-3.5f,-2f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);

        //weapon
        buttons[4] = new Button(new Vector2(-2.25f,0.5f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);

        //shield
        buttons[5] = new Button(new Vector2(-4.75f,0.5f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);

        // equipment
        buttons[6] = new Button(new Vector2(-0.5f,1.75f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);
        buttons[7] = new Button(new Vector2(0.75f,1.75f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);
        buttons[8] = new Button(new Vector2(-0.5f,0.5f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);
        buttons[9] = new Button(new Vector2(0.75f,0.5f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);

        //rings
        buttons[10] = new Button(new Vector2(-0.5f,-2f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);
        buttons[11] = new Button(new Vector2(0.75f,-2f), buttonShape, "", buttonFontScale, AssetDirectory.Textures.UI.BACKGROUND3,1, 1,0.3f, 0.45f);

    }

    private void initialiseVisuals(){
        UIElements = new UIElement[1];
        UIElements[0] = new UIElement(new Vector2(0,0f), AssetDirectory.Textures.UI.BACKGROUND2, 12, 6.75f);
    }

    public void toggle(){
        active = !active;
        if (active) {
            menuManager.setCurrentMenu(this);
            menuManager.deactivateAll();
            active = true;
        }
    }

}
