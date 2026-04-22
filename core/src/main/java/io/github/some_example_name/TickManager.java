package io.github.some_example_name;

import com.badlogic.gdx.Gdx;

public class TickManager {
    private float accumulatedTime = 0;

    public void update(){
        accumulatedTime += Gdx.graphics.getDeltaTime();
    }

    public boolean acceptTick(){
        float step = Constants.SECONDS_PER_TICK;
        if (accumulatedTime >= step){
            accumulatedTime-= step;
            return true;
        }else {
            return false;
        }
    }

}
