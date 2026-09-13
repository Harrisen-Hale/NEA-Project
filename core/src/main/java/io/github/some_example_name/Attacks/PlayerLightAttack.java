package io.github.some_example_name.Attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.*;

public class PlayerLightAttack extends Attack{

    public PlayerLightAttack(Entity ownerArg){
        owner = ownerArg;
        duration = 22;
        staminaCost = 30f;
        damage = 20f;
        knockback = 1/4f;
        hitbox = new Collider[]{new Collider(owner.getPosition(), 0,0, new Vector2[]{new Vector2(-0.1f, -0.15f), new Vector2(0.1f, -0.15f), new Vector2(0.1f, 0.6f), new Vector2(-0.1f, 0.6f)}, 0, false, Color.MAGENTA, false, false)};
        damageSourceFlagManager = new DamageSourceFlagManager();
    }

    public void execute(){
        if (currentAttackTick == 0){ // start of attack
            clearFlags();
            hitbox[0].activate();
        }

        float t = (2*(currentAttackTick - duration /2f))/ duration; // parametric variable
        float P = 0.25f; // change in (relative) x
        float Q = 0.5f; // change in (relative) y
        Vector2 relativePosition = new Vector2(-P*t, (float) (Q*(-(Math.pow(t, 2)) + 1)));
        float executionAngle = Utils.degreesToRadians(owner.getFacing()-90); // facing takes positive x direction as 0 degrees
        hitbox[0].setPosition(owner.getPosition().cpy().add(Utils.rotate(relativePosition, executionAngle)));
        hitbox[0].setAngle(executionAngle);
        hitbox[0].setVertices();

        currentAttackTick++;
        if (currentAttackTick >= duration){ // end of attack
            currentAttackTick = 0;
            hitbox[0].deactivate();
            owner.concludeAttack();
        }
    }
}
