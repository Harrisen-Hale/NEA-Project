package io.github.some_example_name.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Utils;
import io.github.some_example_name.Items.Item;
import io.github.some_example_name.Items.ItemType;
import io.github.some_example_name.Player.Inventory;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.UI.*;

import java.util.ArrayList;

public class InventoryMenu extends Menu{
    protected Player player;
    protected Inventory playerInventory;
    protected ItemSlot[] itemSlots;

    private ItemSelectionMenu[] itemSelectionMenus;
    private int itemSelectionMenuIndex;

    public InventoryMenu(OrthographicCamera cameraArg, MenuManager menuManagerArg, Player playerArg){
        super();
        player = playerArg;
        playerInventory = player.getInventory();

        initialiseButtons();
        initialiseVisuals();
        menuManager = menuManagerArg;


        camera = cameraArg;
        active = false;
        pausesGame = false;

        itemSelectionMenus = new ItemSelectionMenu[]{new ItemSelectionMenu()};
        itemSelectionMenuIndex = 0;
    }

    public void logicTick(){
        super.logicTick();
        buttonFunctionality();
    }

    private void buttonFunctionality(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
            itemSelection(10);
        }
    }

    private Item[] collateItemsOfType(ItemType itemType){
        ArrayList<Item> arrayList = new ArrayList<>();
        for (int i = 0; i < playerInventory.getInventory().size(); i++){
            if (playerInventory.getInventory().get(i).getItemType() == itemType){
                arrayList.add(playerInventory.getInventory().get(i));
            }
        }
        return Utils.itemArrayListToArray(arrayList);
    }

    private ItemSelectionMenu[] generateItemSelectionMenus(ItemType slotItemType){
        int ITEMS_PER_PAGE = 8; // must be even
        Item[] itemsOfType = collateItemsOfType(slotItemType);
        int numOfItems = itemsOfType.length;
        if (numOfItems > 0) {
            int pages = (int) Math.ceil((double) numOfItems / ITEMS_PER_PAGE);
            int remainder = numOfItems % ITEMS_PER_PAGE;
            Item[][] itemPages = new Item[pages][ITEMS_PER_PAGE];

            int count = 0;
            for (int i = 0; i < pages; i++) {
                for (int j = 0; j < ITEMS_PER_PAGE; j++) {
                    if (i == pages - 1 && j >= remainder) { // last page may not be full
                        itemPages[i][j] = new Item();
                    } else {
                        itemPages[i][j] = itemsOfType[count];
                        count++;
                    }
                }
            }

            ItemSelectionMenu[] menus = new ItemSelectionMenu[pages];
            for (int k = 0; k < pages; k++) {
                menus[k] = new ItemSelectionMenu(itemPages[k], k, pages);
            }
            return menus;
        }else {
            return new ItemSelectionMenu[]{new ItemSelectionMenu()};
        }
    }

    private void itemSelection(int itemSlotIndex){
        ItemType slotItemType = ItemType.EMPTY;
        if (itemSlotIndex == 0){
            slotItemType = ItemType.HEAD;
        }
        else if (itemSlotIndex == 1){
            slotItemType = ItemType.TORSO;
        }else if (itemSlotIndex == 2){
            slotItemType = ItemType.LEGS;
        }else if (itemSlotIndex == 3){
            slotItemType = ItemType.FEET;
        }else if (itemSlotIndex == 4){
            slotItemType = ItemType.WEAPON;
        }else if (itemSlotIndex == 5){
            slotItemType = ItemType.SHIELD;
        }else if (itemSlotIndex >= 6 && itemSlotIndex <= 9){
            slotItemType = ItemType.EQUIPMENT;
        }else if (itemSlotIndex == 10 || itemSlotIndex == 11){
            slotItemType = ItemType.RING;
        }

        itemSelectionMenus = generateItemSelectionMenus(slotItemType);
    }


    private void initialiseButtons(){ // includes item slots, which inherit from Button class
        float buttonFontScale = 0.03f;

        itemSlots = new ItemSlot[12];
        // armour
        itemSlots[0] = new ItemSlot(new Vector2(-3.5f,1.75f),  "", buttonFontScale   ,   0.3f, 0.45f);
        itemSlots[1] = new ItemSlot(new Vector2(-3.5f,0.5f),  "", buttonFontScale   ,  0.3f, 0.45f);
        itemSlots[2] = new ItemSlot(new Vector2(-3.5f,-0.75f),   "", buttonFontScale   ,  0.3f, 0.45f);
        itemSlots[3] = new ItemSlot(new Vector2(-3.5f,-2f),   "", buttonFontScale   ,  0.3f, 0.45f);

        //weapon
        itemSlots[4] = new ItemSlot(new Vector2(-2.25f,0.5f),   "", buttonFontScale   ,  0.3f, 0.45f);

        //shield
        itemSlots[5] = new ItemSlot(new Vector2(-4.75f,0.5f),   "", buttonFontScale   ,  0.3f, 0.45f);

        // equipment
        itemSlots[6] = new ItemSlot(new Vector2(-0.5f,1.75f),   "", buttonFontScale   ,  0.3f, 0.45f);
        itemSlots[7] = new ItemSlot(new Vector2(0.75f,1.75f),   "", buttonFontScale   ,  0.3f, 0.45f);
        itemSlots[8] = new ItemSlot(new Vector2(-0.5f,0.5f),   "", buttonFontScale   ,  0.3f, 0.45f);
        itemSlots[9] = new ItemSlot(new Vector2(0.75f,0.5f),   "", buttonFontScale   ,  0.3f, 0.45f);

        //rings
        itemSlots[10] = new ItemSlot(new Vector2(-0.5f,-2f),   "", buttonFontScale   ,  0.3f, 0.45f);
        itemSlots[11] = new ItemSlot(new Vector2(0.75f,-2f),   "", buttonFontScale   ,  0.3f, 0.45f);

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

    private void updatePlayerItems(){
        // armour
        itemSlots[0].setItem(playerInventory.getArmour()[0]);
        itemSlots[1].setItem(playerInventory.getArmour()[1]);
        itemSlots[2].setItem(playerInventory.getArmour()[2]);
        itemSlots[3].setItem(playerInventory.getArmour()[3]);

        //weapon
        itemSlots[4].setItem(playerInventory.getWeapon());

        //shield
        itemSlots[5].setItem(playerInventory.getShield());

        // equipment
        itemSlots[6].setItem(playerInventory.getHotbar()[0]);
        itemSlots[7].setItem(playerInventory.getHotbar()[1]);
        itemSlots[8].setItem(playerInventory.getHotbar()[2]);
        itemSlots[9].setItem(playerInventory.getHotbar()[3]);

        //rings
        itemSlots[10].setItem(playerInventory.getRings()[0]);
        itemSlots[11].setItem(playerInventory.getRings()[1]);
    }

    public void toggle(){
        active = !active;
        if (active) {
            menuManager.setCurrentMenu(this);
            menuManager.deactivateAll();
            active = true;
            updatePlayerStats();
            updatePlayerItems();

        }
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
        itemSelectionMenus[itemSelectionMenuIndex].draw(batch, new Vector2(camera.position.x, camera.position.y));
    }

    private class ItemSelectionMenu extends Menu{

        private ItemSlot[] itemSlots;

        public ItemSelectionMenu(Item[] items, int index, int maxIndex){
            initialiseItemSlotsButtonsAndTextboxes(items, index, maxIndex);
            initialiseVisuals();
        }

        public ItemSelectionMenu(){
            itemSlots = new ItemSlot[]{};
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

        public void draw(SpriteBatch batch, Vector2 adjust){
            for (UIObject u : UIObjects){
                u.draw(batch, adjust);
            }
            for (UIElement v : UIElements){
                v.draw(batch, adjust);
            }
            for (TextBox t : textBoxes){
                t.draw(batch, adjust);
            }
            for (Button b : buttons){
                b.draw(batch, adjust);
            }
            for (ItemSlot i : itemSlots){
                i.draw(batch, adjust);
            }
        }
    }

}
