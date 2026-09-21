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

    public static int max(int num1, int num2){ // returns greatest of two integers
        if (num1 >= num2){
            return num1;
        }
        return num2;
    }

    public static float max(float num1, float num2){ // returns greatest of two floating point values
        if (num1 >= num2){
            return num1;
        }
        return num2;
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

    public static boolean pointInTriangle(Vector2 point, Vector2[] triangle){
        Vector2 side1 = triangle[1].cpy().sub(triangle[0]);
        Vector2 side2 = triangle[2].cpy().sub(triangle[1]);
        Vector2 side3 = triangle[0].cpy().sub(triangle[2]);

        float crossProduct1 =  side1.cpy().crs(point.cpy().sub(triangle[0]));
        float crossProduct2 =  side2.cpy().crs(point.cpy().sub(triangle[1]));
        float crossProduct3 =  side3.cpy().crs(point.cpy().sub(triangle[2]));

        return (crossProduct1 < 0 && crossProduct2 < 0 && crossProduct3 < 0 || crossProduct1 > 0 && crossProduct2 > 0 && crossProduct3 > 0);
    }

    public static boolean pointInPolygon(Vector2 point, Vector2[] polygon){ // returns true if point in (convex) polygon specified anticlockwise, false otherwise
        boolean[] crossProductSign = new boolean[polygon.length];
        for (int i = 0; i < polygon.length; i++){
            Vector2 side = polygon[(i+1)%polygon.length].cpy().sub(polygon[i]);
            crossProductSign[i] = side.cpy().crs(point.cpy().sub(polygon[i])) > 0;
            if (i > 0 && crossProductSign[i] != crossProductSign[i-1]){
                return false;
            }
        }
        return true;
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

    public static Vector2[][] findEdges(Vector2[] vertices){ // returns array of edges (line segments specified by endpoints) that form a polygon specified anticlockwise
        Vector2[][] edges = new Vector2[vertices.length][2];
        for (int i = 0; i < vertices.length; i++){
            edges[i] = new Vector2[]{vertices[i], vertices[(i+1)%vertices.length]};
        }
        return edges;
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


    public static boolean validIndex(int index, Object[] array){  // checks if given index is in range for given array
        return index >= 0 && index < array.length;
    }
    public static boolean validIndex(int index, int[] array){  // checks if given index is in range for given array
        return index >= 0 && index < array.length;
    }
    public static boolean validIndex(int index, float[] array){  // checks if given index is in range for given array
        return index >= 0 && index < array.length;
    }

    public static int[] reverseArray(int[] arr){
        Stack stack = new Stack(arr.length);
        int[] reversedArr = new int[arr.length];
        for (int i : arr){
            stack.push(i);
        }
        for (int j = 0; j < arr.length; j++){
            reversedArr[j] = stack.pop();
        }
        return reversedArr;
    }

    public static boolean detectIntersectionOfLineSegments(Vector2 line1PointA, Vector2 line1PointB, Vector2 line2PointC, Vector2 line2PointD){
        Vector2 AB = line1PointB.cpy().sub(line1PointA);
        Vector2 CD = line2PointD.cpy().sub(line2PointC);
        Vector2 AC = line2PointC.cpy().sub(line1PointA);

        if (findDeterminant(new float[]{AB.x, AB.y, -CD.x, -CD.y}) != 0) {
            float[] parameters = solveSimultaneousEquations(new float[]{AB.x, AB.y}, new float[]{-CD.x, -CD.y}, new float[]{AC.x, AC.y});
            return (parameters[0] >= 0 && parameters[0] <= 1 && parameters[1] >= 0 && parameters[1] <= 1);
        }else {
            return false;
        }
    }

    public static boolean detectIntersectionOfLineSegmentWithPolygon(Vector2 linePointA, Vector2 linePointB, Vector2[] vertices){
        Vector2[][] edges = findEdges(vertices);
        for (Vector2[] edge : edges) {
            if (detectIntersectionOfLineSegments(linePointA, linePointB, edge[0], edge[1])) {
                return true;
            }
        }
        return false;
    }

    public static float[] solveSimultaneousEquations(float[] xCoefficients, float[] yCoefficients, float[] constantVector){ // 2x2 system
        float a = xCoefficients[0];
        float b = yCoefficients[0];
        float c = xCoefficients[1];
        float d = yCoefficients[1];
        float p = constantVector[0];
        float q = constantVector[1];

        float y = (a*q - c*p) / (a*d - c*b);
        float x = (p / a) - ((b / a) * y);

        return new float[]{x,y};
    }

    public static float findDeterminant(float[] matrix){ // 2x2 column major
        return (matrix[0]*matrix[3])-(matrix[1]*matrix[2]);
    }
}
