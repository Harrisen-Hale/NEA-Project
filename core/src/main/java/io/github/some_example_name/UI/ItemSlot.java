package io.github.some_example_name.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Items.Item;

public class ItemSlot extends Button{
    Item item;

    public ItemSlot(Vector2 screenSpacePositionArg, String text, float fontScale, float topMarginArg, float leftMarginArg) {
        super(screenSpacePositionArg, new Vector2[]{new Vector2(-0.5f, -0.5f),new Vector2(0.5f, -0.5f),new Vector2(0.5f, 0.5f),new Vector2(-0.5f, 0.5f)}, text, fontScale, AssetDirectory.Textures.UI.BACKGROUND3, 1,1, topMarginArg, leftMarginArg);
        item = new Item();
    }

    public ItemSlot(Vector2 screenSpacePositionArg, String text, float fontScale, float topMarginArg, float leftMarginArg, Item itemArg) {
        super(screenSpacePositionArg, new Vector2[]{new Vector2(-0.5f, -0.5f),new Vector2(0.5f, -0.5f),new Vector2(0.5f, 0.5f),new Vector2(-0.5f, 0.5f)}, text, fontScale, AssetDirectory.Textures.UI.BACKGROUND3, 1,1, topMarginArg, leftMarginArg);
        item = itemArg;
    }

    public void draw(SpriteBatch batch, Vector2 screenCentre){
        Vector2 adjustedPosition = this.screenSpacePosition.cpy().add(screenCentre).sub(0.5f*width, 0.5f*height);
        currentSprite.setPosition(adjustedPosition.x, adjustedPosition.y);
        currentSprite.draw(batch);
        Sprite icon = item.getIcon();
        icon.setPosition(adjustedPosition.x, adjustedPosition.y);
        icon.draw(batch);
        textBox.draw(batch, screenCentre);
    }

    public void draw(SpriteBatch batch){
        currentSprite.setPosition(screenSpacePosition.x-(0.5f*width), screenSpacePosition.y-(0.5f*height));
        currentSprite.draw(batch);
        Sprite icon = item.getIcon();
        icon.setPosition(screenSpacePosition.x-(0.5f*width), screenSpacePosition.y-(0.5f*height));
        icon.draw(batch);
        textBox.draw(batch);
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
