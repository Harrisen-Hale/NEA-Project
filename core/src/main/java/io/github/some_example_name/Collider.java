package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class Collider extends GameObject{ // represents both types of collider, must use corresponding specification method when created

    private int type; // 0 - OBB, 1 - Circle
    private float xAdjust;
    private float yAdjust;
    // If circle
    private float radius;
    // If OBB
    private float width;
    private float height;
    private float angle; // radians
    private Vector2[] vertices;

    //debug
    private boolean visible;
    private Color debugColour;

    public Collider(float x, float y, float xAdjustArg, float yAdjustArg, Color debugColourArg, boolean visibleArg) {
        position = new Vector2(x,y); // centroid of the shape
        type = -1;
        xAdjust = xAdjustArg;
        yAdjust = yAdjustArg;
        debugColour = debugColourArg;
        visible = visibleArg;
    }

    public void setPosition(Vector2 positionArg) { // positionArg is centroid of parent object. Adjust values are coordinates relative to that centroid.
        this.position = new Vector2(positionArg.x + xAdjust, positionArg.y + yAdjust);
        if (type == 0){
            setVertices();
        }
    }

    public void specifyCircle(float radiusArg){
        radius = radiusArg;
        type = 1;
    }

    public void specifyOBB(float widthArg, float heightArg, float angleArg){
        width = widthArg;
        height = heightArg;
        angle = angleArg;
        vertices = new Vector2[4];
        setVertices();
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

    public void setAngle(float angle) {
        this.angle = angle;
    }

    private void setVertices(){
        vertices[0] = new Vector2(position.x - width/2f, position.y - height/2f);
        vertices[1] = new Vector2(position.x + width/2f, position.y - height/2f);
        vertices[2] = new Vector2(position.x + width/2f, position.y + height/2f);
        vertices[3] = new Vector2(position.x - width/2f, position.y + height/2f);
        vertices = Utils.rotatePolygon(vertices, position, angle); // This warning is incorrect
    }

    public Vector2[] getVertices() {
        return vertices;
    }

    public void debugRender(ShapeRenderer sr){
        if (visible) {
            sr.setColor(debugColour);
            if (type == 1) {
                sr.circle(position.x, position.y, radius, 20);
            }else if (type == 0){
                sr.polygon(new float[]{vertices[0].x, vertices[0].y,vertices[1].x, vertices[1].y,vertices[2].x, vertices[2].y,vertices[3].x, vertices[3].y});
            }
        }
    }

}
