package io.github.some_example_name.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Framework.AssetDirectory;

public class Item {
    protected Sprite icon;
    protected String name;
    protected String flavourText;

    protected int ID;
    protected int[] statRequirements; // Vit, End, Str, Dex, Kno

    public Item(){
        icon = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.Misc.EMPTY)));
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

    public Sprite getIcon() {
        return icon;
    }
}
