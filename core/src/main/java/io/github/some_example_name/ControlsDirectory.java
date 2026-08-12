package io.github.some_example_name;


import com.badlogic.gdx.Input;

public final class ControlsDirectory {
    public static final class Movement{
        public static final int UP = Input.Keys.W;
        public static final int DOWN = Input.Keys.S;
        public static final int LEFT = Input.Keys.A;
        public static final int RIGHT = Input.Keys.D;
        public static final int SPRINT = Input.Keys.SHIFT_LEFT;
        public static final int ROLL = Input.Keys.SPACE;
    }
    public static final class Combat{
        public static final int LOCK_ON = Input.Keys.F;
        public static final int ATTACK = Input.Buttons.LEFT;
        public static final int BLOCK = Input.Buttons.RIGHT;
    }
}
