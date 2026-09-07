package io.github.some_example_name.Framework;

import com.badlogic.gdx.math.Vector2;

public class GameObject {
    protected Vector2 position;

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
