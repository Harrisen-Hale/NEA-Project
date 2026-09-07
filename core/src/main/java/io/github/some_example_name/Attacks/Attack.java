package io.github.some_example_name.Attacks;

import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.DamageSource;
import io.github.some_example_name.Framework.DamageSourceFlagManager;
import io.github.some_example_name.Framework.Entity;

public class Attack extends DamageSource {
    protected int currentAttackTick = 0;
    protected Entity owner = new Entity();
    protected float staminaCost = 0f;
    protected float duration = 0f; // ticks

    public Attack(){
        damageSourceFlagManager = new DamageSourceFlagManager();
        hitbox = new Collider[]{};
    }

    public float getStaminaCost() {
        return staminaCost;
    }

    public float getDuration() {
        return duration;
    }

    public Entity getOwner() {
        return owner;
    }
}
