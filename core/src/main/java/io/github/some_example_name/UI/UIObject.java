package io.github.some_example_name.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class UIObject {
    protected ArrayList<TextBox> textBoxes;
    protected ArrayList<UIElement> UIElements;
    protected Vector2 screenSpacePosition;

    public UIObject(){
        textBoxes = new ArrayList<>();
        UIElements = new ArrayList<>();
        screenSpacePosition = new Vector2(0,0);
    }

    public void addTextBoxes(TextBox[] newTextBoxes){
        textBoxes.addAll(Arrays.asList(newTextBoxes));
    }

    public void addUIElements(UIElement[] newUIElements){
        UIElements.addAll(Arrays.asList(newUIElements));
    }

    public void draw(SpriteBatch batch, Vector2 screenCentre){
        for (UIElement u : UIElements){
            u.draw(batch, screenCentre);
        }
        for (TextBox t : textBoxes){
            t.draw(batch, screenCentre);
        }
    }

    public void draw(SpriteBatch batch){
        for (UIElement u : UIElements){
            u.draw(batch);
        }
        for (TextBox t : textBoxes){
            t.draw(batch);
        }
    }

    public ArrayList<TextBox> getTextBoxes() {
        return textBoxes;
    }

    public void setTextBoxes(ArrayList<TextBox> textBoxes) {
        this.textBoxes = textBoxes;
    }

    public Vector2 getScreenSpacePosition() {
        return screenSpacePosition;
    }

    public void setScreenSpacePosition(Vector2 screenSpacePosition) {
        this.screenSpacePosition = screenSpacePosition;
    }

    public ArrayList<UIElement> getUIElements() {
        return UIElements;
    }

    public void setUIElements(ArrayList<UIElement> UIElements) {
        this.UIElements = UIElements;
    }
}
