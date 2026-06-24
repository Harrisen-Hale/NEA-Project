package io.github.some_example_name;

import com.badlogic.gdx.audio.Sound;

public class SoundLooper {
    private final float period; // ticks
    private final Sound sound;
    private final float volume;
    private float elapsedTicks;

    public SoundLooper(float periodArg, Sound soundArg, float volumeArg){
        period = periodArg;
        sound = soundArg;
        volume = volumeArg;
        elapsedTicks = (float) (periodArg - Math.ceil(0.15f*periodArg));
    }

    public void play(){
        if (elapsedTicks >= period){
            sound.play(volume);
            elapsedTicks = 0;
        }
        elapsedTicks++;
    }

    public void reset(){
        sound.stop();
        elapsedTicks = (float) (period - Math.ceil(0.15f*period));
    }
}
