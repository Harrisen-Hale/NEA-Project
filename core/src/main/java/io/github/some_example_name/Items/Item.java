package io.github.some_example_name.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Item {
    protected Sprite icon;
    protected String name;
    protected String flavourText;

    protected int ID;
    protected int[] statRequirements; // Vit, End, Str, Dex, Kno

    public Item(){
        icon = new Sprite();
        name = "";
        flavourText = "";
        statRequirements = new int[]{0,0,0,0,0};
        ID = -1;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int[] getStatRequirements() {
        return statRequirements;
    }

    public void setStatRequirements(int[] statRequirements) {
        this.statRequirements = statRequirements;
    }
}
