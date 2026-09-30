package io.github.some_example_name.Items;

import io.github.some_example_name.Attacks.Attack;
import io.github.some_example_name.Attacks.PlayerLightAttack;
import io.github.some_example_name.Framework.Entity;

public class TestWeapon extends Weapon{

    public TestWeapon(Entity ownerArg){
        super();
        damageModifier = 1.25f;
        attack = new PlayerLightAttack(ownerArg);
        attack.setDamage(attack.getDamage()*damageModifier);
    }


}
