package io.github.some_example_name.Player;

import io.github.some_example_name.Items.*;
import io.github.some_example_name.Menus.InventoryMenu;

import java.util.ArrayList;

public class Inventory {
    protected InventoryMenu inventoryMenu;

    protected ArrayList<Item> inventory;

    // slots
    protected Weapon weapon;
    protected Shield shield;
    protected Armour[] armour; // Head, Torso, Legs, Boots
    protected Equipment[] hotbar;
    protected int hotbarIndex;

    public Inventory(){
        inventoryMenu = new InventoryMenu();
        inventory = new ArrayList<>();
        weapon = new Weapon();
        shield = new Shield();
        armour = new Armour[]{};
        hotbar = new Equipment[]{};
        hotbarIndex = 0;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public int getNumItems(){
        return inventory.size();
    }
}
