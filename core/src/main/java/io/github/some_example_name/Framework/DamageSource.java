package io.github.some_example_name.Framework;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DamageSource {
    protected Collider[] hitbox;
    protected DamageSourceFlagManager damageSourceFlagManager;
    protected float knockback;
    protected float damage;

    public DamageSource(){
        hitbox = new Collider[]{};
        damageSourceFlagManager = new DamageSourceFlagManager();
        knockback = 0;
        damage = 0;
    }

    public void execute(){
    }

    public Collider[] getHitbox(){
        return hitbox;
    }

    public float getKnockback() {
        return knockback;
    }

    public void setKnockback(float knockback) {
        this.knockback = knockback;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void flagEntity(int ID){
        damageSourceFlagManager.flagEntity(ID);
    }

    public boolean notFlagged(int ID){
        return !damageSourceFlagManager.isFlagged(ID);
    }

    public void clearFlags(){
        damageSourceFlagManager.clear();
    }

    public void debugRender(ShapeRenderer sr){
        for (Collider c : hitbox){
            c.debugRender(sr);
        }
    }
}
