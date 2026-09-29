package io.github.some_example_name.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.IO.ControlsDirectory;
import io.github.some_example_name.UI.Button;
import io.github.some_example_name.UI.UIElement;
import io.github.some_example_name.UI.UIObject;

public class Menu {
    protected Button[] buttons;
    protected UIObject[] UIObjects;
    protected UIElement[] UIElements;
    protected boolean active;
    protected boolean pausesGame; // whether this menu stops the ordinary game logic running
    protected OrthographicCamera camera;

    public Menu(){
        buttons = new Button[]{};
        UIObjects = new UIObject[]{};
        UIElements = new UIElement[]{};
        active = false;
        pausesGame = false;
        camera = new OrthographicCamera();
    }

    public void logicTick(){
        checkButtonClicks(camera);
    }

    protected void checkButtonClicks(OrthographicCamera camera){
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);
        for (Button b : buttons){
            if (Gdx.input.isButtonJustPressed(ControlsDirectory.Menu.CLICK)) {
                b.click(new Vector2(mouse.x, mouse.y));
            }
        }
    }

    public void draw(SpriteBatch batch){
        for (UIObject u : UIObjects){
            u.draw(batch, new Vector2(camera.position.x, camera.position.y));
        }
        for (UIElement v : UIElements){
            v.draw(batch, new Vector2(camera.position.x, camera.position.y));
        }
        for (Button b : buttons){
            b.draw(batch, new Vector2(camera.position.x, camera.position.y));
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
