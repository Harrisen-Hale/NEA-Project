package io.github.some_example_name.Player;

import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Items.*;
import io.github.some_example_name.Items.Armour.*;
import io.github.some_example_name.Items.Weapons.Weapon;
import io.github.some_example_name.Levels.Level;

import java.util.ArrayList;

public class Inventory {

    protected ArrayList<Item> inventory;

    // slots
    protected Weapon weapon;
    protected Shield shield;
    protected Armour[] armour; // Head, Torso, Legs, Boots
    protected Ring[] rings;
    protected Equipment[] hotbar;
    protected int hotbarIndex;

    public Inventory(){
        inventory = new ArrayList<>();
        weapon = new Weapon();
        shield = new Shield();
        armour = new Armour[4];
        hotbar = new Equipment[4];
        hotbarIndex = 0;
        for (int i = 0; i < 4; i++){
            armour[i] = new Armour();
            hotbar[i] = new Equipment();
        }
        rings = new Ring[2];
        rings[0] = new Ring();
        rings[1] = new Ring();

        // debug loadout
        giveItem(new IgnisVial());
        giveItem(new Ring(AssetDirectory.Textures.Items.Rings.MERCHANTS_RING));
        giveItem(new Ring(AssetDirectory.Textures.Items.Rings.STAMINA_RING));
        inventory.get(1).setFlavourText("A gold ring that belonged to a\nwealthy merchant,\ngrants additional souls");
        inventory.get(2).setFlavourText("A ring set with a vibrant emerald,\nimproves stamina regeneration");
        armour[0] = new Head( 10, AssetDirectory.Textures.Items.Armour.SALLET_HELM);
        armour[1] = new Torso( 10, AssetDirectory.Textures.Items.Armour.STEEL_BREASTPLATE);
        armour[2] = new Legs( 10, AssetDirectory.Textures.Items.Armour.STEEL_GREAVES);
        armour[3] = new Feet( 10, AssetDirectory.Textures.Items.Armour.LEATHER_BOOTS);
        shield = new Shield(10f, AssetDirectory.Textures.Items.Shields.CROSS_HEATER);

    }

    public void logicTick(Level currentLevel){
        hotbar[hotbarIndex].logicTick(currentLevel);
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void giveItem(Item item){
        item.setIndex(inventory.size());
        inventory.add(item);
    }

    public Item takeItem(int index){
        return inventory.remove(index);
    }

    public Item getItem(int index){
        return inventory.get(index);
    }

    public void setItem(int index, Item item){
        inventory.set(index, item);
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

    public int getHotbarIndex() {
        return hotbarIndex;
    }

    public void setHotbarIndex(int hotbarIndex) {
        this.hotbarIndex = hotbarIndex;
    }

    public Ring[] getRings() {
        return rings;
    }

    public void setRings(Ring[] rings) {
        this.rings = rings;
    }
}
