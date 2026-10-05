package io.github.some_example_name.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.IO.ControlsDirectory;
import io.github.some_example_name.Player.Player;

public class MenuManager{
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected Menu currentMenu;

    protected MainMenu mainMenu;
    protected PauseMenu pauseMenu;
    protected InventoryMenu inventoryMenu;

    public MenuManager(OrthographicCamera cameraArg, SpriteBatch batchArg, Player playerArg){
        camera = cameraArg;
        batch = batchArg;

        mainMenu = new MainMenu(camera, this);
        pauseMenu = new PauseMenu(camera, this);
        inventoryMenu = new InventoryMenu(camera, this, playerArg);

        currentMenu = mainMenu;
    }

    public void menuLogicTick(){
        currentMenu.logicTick();
    }

    public void menuRenderTick(){
        batch.begin();
        currentMenu.draw(batch);
        batch.end();
    }

    public void inputs(){
        if (!(currentMenu.getPausesGame() && currentMenu.isActive())) {
            if (Gdx.input.isKeyJustPressed(ControlsDirectory.Menu.PAUSE)){
                pauseMenu.toggle();
            }else if(Gdx.input.isKeyJustPressed(ControlsDirectory.Menu.INVENTORY)){
                inventoryMenu.toggle();
            }
        }
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public InventoryMenu getInventoryMenu() {
        return inventoryMenu;
    }

    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void deactivateAll(){
        mainMenu.setActive(false);
        pauseMenu.setActive(false);
        inventoryMenu.setActive(false);
    }
}
