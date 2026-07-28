package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Collider extends GameObject{

    private float xAdjust;
    private float yAdjust;
    private float angle; // radians
    private Vector2[] vertices; // position vectors relative to world origin
    private Vector2[] dVertices; // position vectors relative to centroid of shape, no rotation
    private Vector2[] normals;

    //debug
    private boolean visible;
    private boolean normalsVisible;
    private Color debugColour;

    public Collider(Vector2 positionArg, float xAdjustArg, float yAdjustArg, Vector2[] dVerticesArg, float initialAngleArg, Color debugColourArg, boolean visibleArg, boolean normalsVisibleArg) {
        position = positionArg.cpy();
        xAdjust = xAdjustArg;
        yAdjust = yAdjustArg;
        debugColour = debugColourArg;
        visible = visibleArg;
        normalsVisible = normalsVisibleArg;
        normals = new Vector2[0];
        angle = initialAngleArg;
        dVertices = dVerticesArg;
        setVertices();
    }

    public void setPosition(Vector2 positionArg) { // positionArg should be centroid of parent object. Adjust values shift relative to that centroid.
        this.position = new Vector2(positionArg.x + xAdjust, positionArg.y + yAdjust);
        setVertices();
    }

    public Vector2 detectCollision(Collider refCollider){ // Returns minimum translation vector for this collider to separate with the reference collider. Returns 0 vector if not colliding
        Vector2 mtv = new Vector2(0,0);
        // Polygon on Polygon, uses SAT
        normals = Utils.findNormals(vertices);
        Vector2[] refNormals = Utils.findNormals(refCollider.getVertices());
        Vector2 mtvAxis = new Vector2(0,0);
        float overlap = Float.MAX_VALUE; // so any overlap value is smaller

        for (Vector2 axis : normals) {
            float[] projection1 = Utils.project(vertices, axis);
            float[] projection2 = Utils.project(refCollider.getVertices(), axis);
            float thisOverlap = Utils.findOverlap(projection1, projection2);
            if (thisOverlap <= 0) { // Early exit via SAT logic
                return new Vector2(0, 0);
            } else if (thisOverlap < overlap) {
                overlap = thisOverlap;
                mtvAxis = axis.cpy();
            }
        }
        for (Vector2 axis : refNormals) {
            float[] projection1 = Utils.project(vertices, axis);
            float[] projection2 = Utils.project(refCollider.getVertices(), axis);
            float thisOverlap = Utils.findOverlap(projection1, projection2);
            if (thisOverlap <= 0) { // Early exit via SAT logic
                return new Vector2(0, 0);
            } else if (thisOverlap < overlap) {
                overlap = thisOverlap;
                mtvAxis = axis.cpy();
            }
        }
        Vector2 refCentreToCentre = position.cpy().sub(refCollider.getPosition());
        if (mtvAxis.dot(refCentreToCentre) < 0){
            mtvAxis.scl(-1);
        }
        mtv = mtvAxis.cpy().scl(overlap);
        return mtv;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    private void setVertices(){
        vertices = Utils.translatePolygon(dVertices, position);
        vertices = Utils.rotatePolygon(vertices, position, angle);
    }

    public Vector2[] getVertices() {
        return vertices;
    }

    public void debugRender(ShapeRenderer sr){
        if (visible) {
            sr.setColor(debugColour);
            sr.polygon(Utils.convertToPairwisePoints(vertices));
        }
        if (normalsVisible){
            for (Vector2 n : normals) {
                sr.rectLine(position, position.cpy().add(n.cpy().scl(2)), 0.035f);
            }
        }
    }

}
