package io.github.some_example_name.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Framework.AssetDirectory;

public class Item {
    protected Sprite icon;
    protected String name;
    protected String flavourText;

    protected int[] statRequirements; // Vit, End, Str, Dex, Kno
    protected boolean equipped;
    protected ItemType itemType;
    protected int index; // the index that this item is located at within the inventory

    public Item(){
        icon = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.Misc.EMPTY)));
        name = "";
        flavourText = "";
        statRequirements = new int[]{0,0,0,0,0};
        equipped = false;
        itemType = ItemType.EMPTY;
        index = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlavourText() {
        return flavourText;
    }

    public void setFlavourText(String flavourText) {
        this.flavourText = flavourText;
    }

    public int[] getStatRequirements() {
        return statRequirements;
    }

    public void setStatRequirements(int[] statRequirements) {
        this.statRequirements = statRequirements;
    }

    public Sprite getIcon() {
        return icon;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }
}
