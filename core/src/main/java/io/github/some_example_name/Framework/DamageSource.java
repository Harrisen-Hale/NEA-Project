package io.github.some_example_name.Framework;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DamageSource {
    protected Collider[] hitbox = new Collider[]{};
    protected DamageSourceFlagManager damageSourceFlagManager = new DamageSourceFlagManager();

    public DamageSource(){
    }

    public void execute(){
    }

    public Collider[] getHitbox(){
        return hitbox;
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
