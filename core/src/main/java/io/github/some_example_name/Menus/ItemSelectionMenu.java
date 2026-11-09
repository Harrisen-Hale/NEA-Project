package io.github.some_example_name.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.IO.ControlsDirectory;
import io.github.some_example_name.Items.Item;
import io.github.some_example_name.Player.Inventory;
import io.github.some_example_name.UI.*;

public class ItemSelectionMenu extends Menu{

    private ItemSlot[] itemSlots;
    private int itemSlotIndex;
    private InventoryMenu inventoryMenu;
    private Inventory playerInventory;

    public ItemSelectionMenu(Item[] items, int index, int maxIndex, int itemSlotIndexArg, InventoryMenu inventoryMenuArg, OrthographicCamera cameraArg){
        initialiseItemSlotsButtonsAndTextboxes(items, index, maxIndex);
        initialiseVisuals();
        active = true;
        camera = cameraArg;
        itemSlotIndex = itemSlotIndexArg;
        inventoryMenu = inventoryMenuArg;
        playerInventory = inventoryMenu.getPlayerInventory();
    }

    public ItemSelectionMenu(){
        itemSlots = new ItemSlot[]{};
        itemSlotIndex = 0;
        //ensure that inventoryMenu is never accessed from this object
    }

    public void logicTick(){
        super.logicTick();
        checkItemSlotClicks();
        equipItem(playerInventory);
        buttonFunctionality();
    }

    private void buttonFunctionality(){

    }

    private void equipItem(Inventory playerInventory){
        for (ItemSlot i : itemSlots) {
            if (i.readValue()) {
                playerInventory.equipItem(i.getItem(), itemSlotIndex);
                inventoryMenu.toggle();
            }
        }
    }

    private void checkItemSlotClicks(){
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);
        for (Button i : itemSlots){
            if (Gdx.input.isButtonJustPressed(ControlsDirectory.Menu.CLICK)) {
                i.click(new Vector2(mouse.x, mouse.y).sub(camera.position.x, camera.position.y));
            }
        }
    }

    private void initialiseVisuals(){
        UIElements = new UIElement[1];
        UIElements[0] = new UIElement(new Vector2(0,0f), AssetDirectory.Textures.UI.BACKGROUND2, 12, 6.75f);
    }

    private void initialiseItemSlotsButtonsAndTextboxes(Item[] items, int index, int maxIndex){
        float VERT_SPACING = 1.25f;
        float HOR_SPACING = 5.75f;

        itemSlots = new ItemSlot[items.length];
        textBoxes = new TextBox[items.length+1];
        buttons = new Button[3];
        for (int i = 0; i < items.length; i++){
            itemSlots[i] = new ItemSlot(new Vector2(-5f+(HOR_SPACING*((2*i)/items.length)),2.5f-(VERT_SPACING*(i%(items.length/2)))), "", 1f, 0f, 0f, items[i]);
            textBoxes[i] = new TextBox(new Vector2(-4.5f+(HOR_SPACING*((2*i)/items.length)),3f-(VERT_SPACING*(i%(items.length/2)))), 0.25f, 0.25f, items[i].getFlavourText(), 0.02f, 0 ,0.15f);
        }
        textBoxes[items.length] = new TextBox(new Vector2(2.75f,-2.3f), 1, 1, (index+1)+"/"+maxIndex, 0.035f, 0,0);
        buttons[0] = new Button(new Vector2(4.65f,-2.5f), new Vector2[]{}, "", 1, AssetDirectory.Textures.UI.ARROW_BUTTON, 2, 1, 0, 0);// need to do vertices
        buttons[1] = new Button(new Vector2(1.5f,-2.5f), new Vector2[]{}, "", 1, AssetDirectory.Textures.UI.ARROW_BUTTON, 2, 1, 0, 0);// need to do vertices
        buttons[1].flipSprite();
        buttons[2] = new Button(new Vector2(-4.5f,-2.5f), new Vector2[]{}, "Back To\nInventory", 0.02f, AssetDirectory.Textures.UI.BACKGROUND1, 2, 1, 0.2f, 0.4f);// need to do vertices

    }

    public void draw(SpriteBatch batch){
        for (UIObject u : UIObjects){
            u.draw(batch, new Vector2(camera.position.x, camera.position.y));
        }
        for (UIElement v : UIElements){
            v.draw(batch, new Vector2(camera.position.x, camera.position.y));
        }
        for (TextBox t : textBoxes){
            t.draw(batch, new Vector2(camera.position.x, camera.position.y));
        }
        for (Button b : buttons){
            b.draw(batch, new Vector2(camera.position.x, camera.position.y));
        }
        for (ItemSlot i : itemSlots){
            i.draw(batch, new Vector2(camera.position.x, camera.position.y));
        }
    }
}
