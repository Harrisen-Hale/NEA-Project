package io.github.some_example_name.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Utils;

public class NavNode {
    protected Vector2[] vertices;
    protected Vector2 centre;
    protected int index;
    protected int[] neighbours; // index values of neighbour nodes
    protected float weight;
    protected float distance;
    protected int priorNodeIndex;

    public NavNode(Vector2[] verticesArg, int indexArg, int[] neighboursArg){
        vertices = verticesArg;
        centre = Utils.findCentroid(verticesArg);
        index = indexArg;
        neighbours = neighboursArg;
        weight = 0;
        priorNodeIndex = index;
        distance = 0;
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

    public void setVertices(Vector2[] vertices) {
        this.vertices = vertices;
    }

    public void setNeighbours(int[] neighbours) {
        this.neighbours = neighbours;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getPriorNodeIndex() {
        return priorNodeIndex;
    }

    public void setPriorNodeIndex(int priorNodeIndex) {
        this.priorNodeIndex = priorNodeIndex;
    }

    public Vector2 getCentre() {
        return centre;
    }
}
