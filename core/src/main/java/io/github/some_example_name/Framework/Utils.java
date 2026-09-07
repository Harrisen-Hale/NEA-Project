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

    public static Vector2 rotate(Vector2 vector, float angle){ //rotate vector by angle radians anticlockwise about the origin
        Matrix3 rotation = new Matrix3(new float[]{(float) Math.cos(angle), (float) Math.sin(angle), 0, -(float) Math.sin(angle), (float) Math.cos(angle), 0, 0, 0, 1});
        return vector.cpy().mul(rotation);
    }

    public static Vector2[] rotatePolygon(Vector2[] vertices, Vector2 originOfRotation, float angle){ // rotates polygon (specified by vertices) angle radians about originOfRotation
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

    public static float[] project(Vector2[] points, Vector2 axis){ // project an array of points onto a unit vector axis, returns min and max value on axis at index 0 and 1 respectively
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
        float overlap = Math.min(projection1[1], projection2[1]) - Math.max(projection1[0], projection2[0]);
        return Math.max(0, overlap); // eliminates negative overlap
    }

    public static Vector2 findUnitVector(Vector2 point1, Vector2 point2){ // returns unit vector from point1 towards point2
        return point2.cpy().sub(point1).nor();
    }

    public static Vector2 findNormal(Vector2 point1, Vector2 point2){ // returns unit normal vector for the line specified by 2 points (normal is middle finger if index finger points from point1 to point2 in left-hand rule)
        return rotate(findUnitVector(point1, point2), (float) ((3*Math.PI)/2f));
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
        float total = 0;
        for (float value : values) {
            total += value;
        }
        return total/values.length;
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

    public static float findGradient(Vector2 point1, Vector2 point2){ // returns the gradient of the line specified by 2 points
        return (point1.y-point2.y)/(point1.x-point2.x);
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

    public static float findDistance(Vector2 point1, Vector2 point2){
        return point1.cpy().sub(point2).len();
    }
}
