package io.github.some_example_name.Framework;

public class DamageSource {
    protected Collider[] hitbox;
    protected int currentActionTick;
    protected AttackFlagManager attackFlagManager;

    public DamageSource(){
        attackFlagManager = new AttackFlagManager();
    }

    public Collider[] getHitbox(){
        return hitbox;
    }

    public void flagEntity(int ID){
        attackFlagManager.flagEntity(ID);
    }

    public boolean isFlagged(int ID){
        return attackFlagManager.isFlagged(ID);
    }

    public void clearFlags(){
        attackFlagManager.clear();
    }
}
