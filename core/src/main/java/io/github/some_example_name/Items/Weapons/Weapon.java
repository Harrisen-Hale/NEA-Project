package io.github.some_example_name.Items.Weapons;

import io.github.some_example_name.Attacks.Attack;
import io.github.some_example_name.Items.Item;
import io.github.some_example_name.Items.ItemType;

public class Weapon extends Item {
    protected float damageModifier;
    protected Attack attack;

    public Weapon(){
        super();
        itemType = ItemType.WEAPON;
        damageModifier = 1f;
        attack = new Attack();
        modifyAttackDamage(1);
    }

    public void attack(){
        attack.execute();
    }

    public void activateAttack() {
        this.attack.setActive(true);
    }

    public Attack getAttack() {
        return attack;
    }

    public float getDamageModifier() {
        return damageModifier;
    }

    public void modifyAttackDamage(float coefficient){
        attack.setDamage(attack.getBaseDamage()*damageModifier*coefficient);
    }
}
