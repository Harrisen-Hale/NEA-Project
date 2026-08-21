package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

public class NavNode {
    Vector2[] vertices;

    public NavNode(Vector2[] verticesArg){
        vertices = verticesArg;
    }

    public void drawDebug(ShapeRenderer sr){
        sr.setColor(Color.CYAN);
        float[] p = Utils.convertToPairwisePoints(vertices);
        sr.triangle(p[0], p[1], p[2], p[3], p[4], p[5]);
    }

    public Vector2[] getVertices() {
        return vertices;
    }
}
