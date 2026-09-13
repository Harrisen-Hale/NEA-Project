package io.github.some_example_name.Framework;



public class Timer { // timer operates on ticks, not seconds
    private int tick;
    private int duration;

    public Timer(int durationArg){
        tick = 0;
        duration = durationArg;
    }

    public boolean tick(){ // returns true if duration reached, false otherwise.
        tick++;
        if(tick == duration){
            tick = 0;
            return true;
        }else {
            return false;
        }
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
