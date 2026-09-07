package io.github.some_example_name.UI;

import com.badlogic.gdx.math.Vector2;

public class UIElement {
    protected Vector2 position;

    public void setPosition(Vector2 position) {
        this.position = new Vector2(position.x, position.y);
    }
}
