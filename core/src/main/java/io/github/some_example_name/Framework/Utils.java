package io.github.some_example_name.Framework;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Utils {
    public static float[] findDistances(Vector2 refPos, Vector2[] positions){ // returns array of distances from refPos to each element of positions
        float[] distances = new float[positions.length];
        for (int i = 0; i < positions.length; i++){
            distances[i] = findDistance(refPos, positions[i]);
        }
        return distances;
    }

    public static int findMinimumValue(float[] values, int currentIndex, int minimumIndex){ // returns index of minimum value, when called currentIndex and minimumIndex should be defaulted to 0
        if (currentIndex == values.length){
            return minimumIndex;
        }
        if (values[currentIndex] < values[minimumIndex]){
            minimumIndex = currentIndex;
        }
        return findMinimumValue(values, currentIndex+1, minimumIndex); // recursive call
    }

    public static int findClosestPosition(Vector2 refPos, Vector2[] positions){ // returns index of closest position to refPos
        return findMinimumValue(findDistances(refPos, positions), 0, 0);
    }

    public static Vector2 rotate(Vector2 v, float angle){ //rotate v by angle radians anticlockwise about the origin
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        Matrix3 rotation = new Matrix3(new float[]{c, s, 0, -s, c, 0, 0, 0, 1});
        return v.cpy().mul(rotation);
    }

    public static Vector2[] rotatePolygon(Vector2[] vertices, Vector2 originOfRotation, float angle){ // rotates polygon (specified by vertices) angle degrees about originOfRotation
        Vector2[] relativePoints = new Vector2[vertices.length];
        Vector2[] rotatedPoints = new Vector2[vertices.length];
        for (int i = 0; i < vertices.length; i++){
            relativePoints[i] = vertices[i].cpy().sub(originOfRotation);
            relativePoints[i] = rotate(relativePoints[i], angle);
            rotatedPoints[i] = originOfRotation.cpy().add(relativePoints[i]);
        }
        return rotatedPoints;
    }

    public static Vector2[] translatePolygon(Vector2[] vertices, Vector2 translationVector){
        Vector2[] translatedVertices = new Vector2[vertices.length];
        for (int i = 0; i < vertices.length; i++){
            translatedVertices[i] = vertices[i].cpy().add(translationVector);
        }
        return translatedVertices;
    }

    public static float[] project(Vector2[] points, Vector2 axis){ // project an array of points onto a unit vector axis, returns min and max value on axis
        float min = axis.cpy().dot(points[0]);
        float max = min;
        for (Vector2 p : points) {
            float point = axis.cpy().dot(p);
            if (point < min) {
                min = point;
            } else if (point > max) {
                max = point;
            }
        }
        return new float[]{min, max};
    }

    public static float findOverlap(float[] projection1, float[] projection2){ // return the (magnitude of the) overlap distance of 2 projected shapes
        float min1 = projection1[0];
        float max1 = projection1[1];
        float min2 = projection2[0];
        float max2 = projection2[1];

        float overlap = Math.min(max1, max2) - Math.max(min1, min2);
        return Math.max(0, overlap);
    }

    public static Vector2 findUnitVector(Vector2 p1, Vector2 p2){ // returns unit vector from p1 towards p2
        return p2.cpy().sub(p1).nor();
    }

    public static Vector2 findNormal(Vector2 p1, Vector2 p2){ // returns unit normal vector for the line specified by 2 points (follows left hand rule)
        return rotate(findUnitVector(p1, p2), (float) ((3*Math.PI)/2f));
    }

    public static Vector2[] findNormals(Vector2[] vertices){ // returns unit normal vectors for a polygon (specified anticlockwise by vertices)
        Vector2[] normals = new Vector2[vertices.length];
        for (int i = 0; i < vertices.length; i++){
            normals[i] = findNormal(vertices[i], vertices[(i+1)%vertices.length]);
        }
        return normals;
    }

    public static float degreesToRadians(float angleDeg){
        return (float) (angleDeg*(Math.PI/180));
    }

    public static float[] convertToPairwisePoints(Vector2[] points){ // converts from array of vectors to array of pairs of coordinates, e.g. {x1,y1,x2,y2}
        float[] pairwisePoints = new float[2*points.length];
        for (int i = 0; i < pairwisePoints.length; i++){
            if (i % 2 == 0){ // even i means x coordinate
                pairwisePoints[i] = points[i/2].x;
            }else { // odd i means y coordinate
                pairwisePoints[i] = points[i/2].y;
            }
        }
        return pairwisePoints;
    }

    public static float arithmeticMean(float[] values){
        float tot = 0;
        for (float value : values) {
            tot += value;
        }
        return tot/values.length;
    }

    public static Vector2 findCentroid(Vector2[] points){
        float[] xValues = new float[points.length];
        float[] yValues = new float[points.length];
        for (int i = 0; i < points.length; i++){
            xValues[i] = points[i].x;
            yValues[i] = points[i].y;
        }
        return new Vector2(arithmeticMean(xValues), arithmeticMean(yValues));
    }

    public static Vector2[] generateRegularPolygon(int n, float circumradius){ // domain {n: n > 2}, uses roots of unity to generate a regular n-gon relative to the origin
        Vector2[] vertices = new Vector2[n];
        for (int p = 0; p < n; p++){
            vertices[p] = new Vector2((float) Math.cos((2*Math.PI*p)/n), (float) Math.sin((2*Math.PI*p)/n)).scl(circumradius); // uses standard De Moivre's Theorem formula for roots of unity
        }
        return vertices;
    }

    public static float findGradient(Vector2 p1, Vector2 p2){ // returns the gradient of the line specified by 2 points
        return (p1.y-p2.y)/(p1.x-p2.x);
    }

    public static int[] intArrayListToArray(ArrayList<Integer> arrayList){
        int[] output = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++){
            output[i] = arrayList.get(i);
        }
        return output;
    }

    public static float[] floatArrayListToArray(ArrayList<Float> arrayList){
        float[] output = new float[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++){
            output[i] = arrayList.get(i);
        }
        return output;
    }

    public static Vector2[] vectorArrayListToArray(ArrayList<Vector2> arrayList){
        Vector2[] output = new Vector2[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++){
            output[i] = arrayList.get(i);
        }
        return output;
    }

    public static int linearSearch(int target, int[] array){ // returns index of item, or -1 if not found
        for (int i = 0; i < array.length; i++){
            if (array[i] == target){
                return i;
            }
        }
        return -1;
    }

    public static float findDistance(Vector2 p1, Vector2 p2){
        return p1.cpy().sub(p2).len();
    }
}
