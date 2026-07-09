package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Collider extends GameObject{ // represents both types of collider, should use corresponding specification method when created

    private int type; // 0 - OBB, 1 - Circle
    private float xAdjust;
    private float yAdjust;
    // If circle
    private float radius;
    // If OBB
    private float width;
    private float height;
    private float angle; // radians

    public Collider(float x, float y, float xAdjustArg, float yAdjustArg) {
        position = new Vector2(x,y); // centroid of the shape
        type = -1;
        xAdjust = xAdjustArg;
        yAdjust = yAdjustArg;
    }

    public void setPosition(Vector2 positionArg) { // positionArg is centroid of parent object. Adjust values are coordinates relative to that centroid.
        this.position = new Vector2(positionArg.x + xAdjust, positionArg.y + yAdjust);
    }

    public void specifyCircle(float radiusArg){
        radius = radiusArg;
        type = 1;
    }

    public void specifyOBB(float widthArg, float heightArg, float angleArg){
        width = widthArg;
        height = heightArg;
        angle = angleArg;
        type = 0;
    }

    public Vector2 detectCollision(Collider refCollider){ // Returns minimum translation vector for this collider to separate with the reference collider. Returns 0 vector if not colliding
        int refType = refCollider.getType();
        Vector2 mtv = new Vector2(0,0);
        if (type == 1 && refType == 1){ // circle on circle
            float sumOfRadii = radius + refCollider.getRadius();
            Vector2 centresVector = position.cpy().sub(refCollider.getPosition());
            Vector2 pA = position.cpy().add(centresVector.cpy().nor().scl(-radius));
            Vector2 pB = refCollider.getPosition().cpy().add(centresVector.cpy().nor().scl(refCollider.getRadius()));
            float mtd = pA.cpy().sub(pB).len();
            if (centresVector.len() < sumOfRadii){
                mtv = centresVector.cpy().scl(mtd);
            }
        }else if (type == 0 && refType == 0){ // OBB on OBB

        }else if (type == 0 && refType == 1){ // OBB on circle

        }else if (type == 1 && refType == 0){ // circle on OBB

        }
        return mtv;
    }

    public int getType() {
        return type;
    }

    public float getRadius() {
        return radius;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAngle() {
        return angle;
    }

    public void debugRender(ShapeRenderer sr){
        sr.setColor(Color.BLUE);
        if (type == 1) {
            sr.circle(position.x, position.y, radius, 20);
        }else if (type == 0){
            // debug OBB visual
        }
    }

}
