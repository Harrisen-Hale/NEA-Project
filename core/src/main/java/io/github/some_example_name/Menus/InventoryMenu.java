package io.github.some_example_name.Menus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Player.Inventory;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.UI.Button;
import io.github.some_example_name.UI.TextBox;
import io.github.some_example_name.UI.UIElement;

public class InventoryMenu extends Menu{
    protected Player player;

    public InventoryMenu(OrthographicCamera cameraArg, MenuManager menuManagerArg, Player playerArg){
        super();
        player = playerArg;

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
        UIElements = new UIElement[6];
        UIElements[0] = new UIElement(new Vector2(0,0f), AssetDirectory.Textures.UI.BACKGROUND2, 12, 6.75f);

        // Armour label
        UIElements[1] = new TextBox(new Vector2(-4f,2.75f), 1, 1, "Armour", 0.025f, 0, -0.1f);

        // Weapon label
        UIElements[2] = new TextBox(new Vector2(-2.75f,1.25f), 1, 1, "Weapon", 0.015f, 0, 0.05f);

        // Shield label
        UIElements[3] = new TextBox(new Vector2(-5.25f,1.25f), 1, 1, "Shield", 0.015f, 0, 0.15f);

        // Rings label
        UIElements[4] = new TextBox(new Vector2(-0.75f,-1f), 1, 1, "Rings", 0.025f, 0, 0.4f);

        // Equipment label
        UIElements[5] = new TextBox(new Vector2(-0.75f,2.75f), 1, 1, "Equipment", 0.025f, 0, 0f);

        textBoxes = new TextBox[6];

        // Player level
        textBoxes[0] = new TextBox(new Vector2(2.75f,2.75f), 1, 1, "Level: ", 0.025f, 0, 0f);
        // Vitality
        textBoxes[1] = new TextBox(new Vector2(2.75f,2.0f), 1, 1, "Vitality: ", 0.025f, 0, 0f);
        // Endurance
        textBoxes[2] = new TextBox(new Vector2(2.75f,1.25f), 1, 1, "Endurance: ", 0.025f, 0, 0f);
        // Strength
        textBoxes[3] = new TextBox(new Vector2(2.75f,0.5f), 1, 1, "Strength: ", 0.025f, 0, 0f);
        // Dexterity
        textBoxes[4] = new TextBox(new Vector2(2.75f,-0.25f), 1, 1, "Dexterity: ", 0.025f, 0, 0f);
        // Knowledge
        textBoxes[5] = new TextBox(new Vector2(2.75f,-1f), 1, 1, "Knowledge: ", 0.025f, 0, 0f);
    }

    private void updatePlayerStats(){
        textBoxes[0].setText("Level: "+player.getLevel());
        textBoxes[1].setText("Vitality: "+player.getVitality());
        textBoxes[2].setText("Endurance: "+player.getEndurance());
        textBoxes[3].setText("Strength: "+player.getStrength());
        textBoxes[4].setText("Dexterity: "+player.getDexterity());
        textBoxes[5].setText("Knowledge: "+player.getKnowledge());

    }

    public void toggle(){
        active = !active;
        if (active) {
            menuManager.setCurrentMenu(this);
            menuManager.deactivateAll();
            active = true;
            updatePlayerStats();
        }
    }

}
