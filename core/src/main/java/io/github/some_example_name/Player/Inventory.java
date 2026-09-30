package io.github.some_example_name.Player;

import io.github.some_example_name.Items.*;
import io.github.some_example_name.Menus.InventoryMenu;

import java.util.ArrayList;

public class Inventory {

    protected ArrayList<Item> inventory;

    // slots
    protected Weapon weapon;
    protected Shield shield;
    protected Armour[] armour; // Head, Torso, Legs, Boots
    protected Equipment[] hotbar;
    protected int hotbarIndex;

    public Inventory(){
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

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Shield getShield() {
        return shield;
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }

    public Armour[] getArmour() {
        return armour;
    }

    public void setArmour(Armour[] armour) {
        this.armour = armour;
    }

    public Equipment[] getHotbar() {
        return hotbar;
    }

    public void setHotbar(Equipment[] hotbar) {
        this.hotbar = hotbar;
    }
}
