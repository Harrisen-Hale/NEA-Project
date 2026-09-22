package io.github.some_example_name.Menus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.UI.Button;
import io.github.some_example_name.UI.UIObject;

public class Menu {
    protected Button[] buttons;
    protected UIObject[] UIObjects;
    protected boolean active;
    protected boolean pausesGame; // whether this menu stops the ordinary game logic running

    public Menu(){
        buttons = new Button[]{};
        UIObjects = new UIObject[]{};
        active = false;
        pausesGame = false;
    }

    public void draw(SpriteBatch batch){
        for (UIObject u : UIObjects){
            u.draw(batch);
        }
        for (Button b : buttons){
            b.draw(batch);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getPausesGame() {
        return pausesGame;
    }
}
