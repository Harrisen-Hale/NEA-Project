package io.github.some_example_name.Items;

import io.github.some_example_name.Attacks.Attack;

public class Weapon extends Item{
    protected float damageModifier;
    protected Attack attack;

    public Weapon(){
        super();
        damageModifier = 1f;
        attack = new Attack();
    }
}
