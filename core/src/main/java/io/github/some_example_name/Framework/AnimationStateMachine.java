package io.github.some_example_name.Framework;

import com.badlogic.gdx.graphics.Texture;

public class AnimationStateMachine {
    private Texture[] animFrames;
    private int[] durations;
    private int currentFrameIndex;
    private int currentDurationRemaining;

    public AnimationStateMachine(Texture[] textures, int[] durationsArg){
        animFrames = textures;
        durations = durationsArg;
        currentFrameIndex = 0;
        currentDurationRemaining = durations[0];
    }

    public boolean update(){ // returns true if frame changed
        if (currentDurationRemaining == 0){
            currentFrameIndex++;
            currentDurationRemaining = durations[currentFrameIndex];
            return true;
        }else {
            currentDurationRemaining--;
        }
        return false;
    }

    public Texture getCurrentFrame(){
        return animFrames[currentFrameIndex];
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void reset(){
        currentFrameIndex = 0;
        currentDurationRemaining = durations[0];
    }
}
