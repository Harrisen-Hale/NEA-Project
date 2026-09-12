package io.github.some_example_name.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Utils;

public class NavNode {
    Vector2[] vertices;
    int index;
    int[] neighbours; // index values of neighbour nodes
    float weight;

    public NavNode(Vector2[] verticesArg, int indexArg, int[] neighboursArg){
        vertices = verticesArg;
        index = indexArg;
        neighbours = neighboursArg;
        weight = 0;
    }

    public void drawDebug(ShapeRenderer sr){
        sr.setColor(Color.CYAN);
        float[] p = Utils.convertToPairwisePoints(vertices);
        sr.triangle(p[0], p[1], p[2], p[3], p[4], p[5]);
    }

    public Vector2[] getVertices() {
        return vertices;
    }

    public int getIndex() {
        return index;
    }

    public int[] getNeighbours() {
        return neighbours;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
