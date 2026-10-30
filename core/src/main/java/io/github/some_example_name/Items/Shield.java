package io.github.some_example_name.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.Utils;

public class Shield extends Item{
    private Collider shieldCollider;
    protected float blockStaminaCost;

    public Shield(float blockStaminaCostArg, String spritePath){
        super();
        itemType = ItemType.SHIELD;
        blockStaminaCost = blockStaminaCostArg;
        shieldCollider =  new Collider(new Vector2(0,0), 0,0,new Vector2[]{new Vector2(0, -5/16f), new Vector2(1/8f, -5/16f), new Vector2(1/8f, 5/16f), new Vector2(0, 5/16f)}, 0, false, Color.GREEN, false, false);
        icon = new Sprite(new Texture(Gdx.files.internal(spritePath)));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }

    public Shield(){
        super();
        itemType = ItemType.SHIELD;
        blockStaminaCost = 0f;
        shieldCollider =  new Collider(new Vector2(0,0), 0,0,new Vector2[]{new Vector2(0, -5/16f), new Vector2(1/8f, -5/16f), new Vector2(1/8f, 5/16f), new Vector2(0, 5/16f)}, 0, false, Color.GREEN, false, false);
    }

    public void logicTick(Vector2 position, float angle){
        transformShield(position, angle);
    }

    public void activate(){
        shieldCollider.activate();
    }

    public void deactivate(){
        shieldCollider.deactivate();
    }

    private void transformShield(Vector2 position, float angle){ // radians
        shieldCollider.setPosition(position);
        shieldCollider.setAngle(angle);
        shieldCollider.setVertices();
    }

    public Collider getShieldCollider() {
        return shieldCollider;
    }
}
