package io.github.some_example_name.Items.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Attacks.PlayerLightAttack;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Entity;

public class TestWeapon extends Weapon {

    public TestWeapon(Entity ownerArg){
        super();
        damageModifier = 1.25f;
        attack = new PlayerLightAttack(ownerArg);
        attack.setDamage(attack.getDamage()*damageModifier);
        icon = new Sprite(new Texture(Gdx.files.internal(AssetDirectory.Textures.Items.Weapons.ARMING_SWORD)));
        icon.setSize(1,1);
        icon.setOriginCenter();
    }


}
