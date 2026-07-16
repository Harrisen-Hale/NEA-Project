package io.github.some_example_name;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class Utils {
    public static float[] findDistances(Vector2 refPos, Vector2[] positions){ // returns array of distances
        float[] distances = new float[positions.length];
        for (int i = 0; i < positions.length; i++){
            distances[i] = refPos.cpy().sub(positions[i]).len();
        }
        return distances;
    }

    public static int findMinimumValue(float[] values, int currentIndex, int minimumIndex){ // returns index of minimum value
        if (currentIndex == values.length){
            return minimumIndex;
        }
        if (values[currentIndex] < values[minimumIndex]){
            minimumIndex = currentIndex;
        }
        return findMinimumValue(values, currentIndex+1, minimumIndex);
    }

    public static int findClosestPosition(Vector2 refPos, Vector2[] positions){ // returns index of closest position to refPos
        return findMinimumValue(findDistances(refPos, positions), 0, 0);
    }

    public static Vector2 rotate(Vector2 v, float angle){ //rotate v by angle radians about the origin
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        Matrix3 rotation = new Matrix3(new float[]{c, s, 0, -s, c, 0, 0, 0, 1});
        return v.cpy().mul(rotation);
    }

    public static Vector2[] rotatePolygon(Vector2[] vertices, Vector2 originOfRotation, float angle){
        Vector2[] relativePoints = new Vector2[vertices.length];
        for (int i = 0; i < vertices.length; i++){
            relativePoints[i] = vertices[i].cpy().sub(originOfRotation);
            relativePoints[i] = rotate(relativePoints[i], angle);
            vertices[i] = originOfRotation.cpy().add(relativePoints[i]);
        }
        return vertices;
    }

    public static float[] project(Vector2[] points, Vector2 axis){ // project an array of points onto a unit vector axis, returns min and max value on axis
        float min = axis.cpy().dot(points[0]);
        float max = min;
        for (int i = 0; i < points.length; i++){
            float point = axis.cpy().dot(points[i]);
            if (point < min) {
                min = point;
            } else if (point > max) {
                max = point;
            }
        }
        return new float[]{min, max};
    }

    public static float overlap(float[] projection1, float[] projection2){ // find the (magnitude of the) overlap distance of 2 projected shapes
        float min1 = projection1[0];
        float max1 = projection1[1];
        float min2 = projection2[0];
        float max2 = projection2[1];
        float overlap = 0;

        if (min1 == min2 && max1 == max2){
            overlap = Math.abs(max1 - min1);
        }else if (max1 >= min2 && max1 <= max2){
            overlap = Math.abs(max1 - min2);
        }else if (max2 >= min1 && max2 <= max1){
            overlap = Math.abs(max2 - min1);
        }

        return overlap;
    }

    public static Vector2[] getNormals(Vector2[] OBBVertices){ // generates unit normal vectors for an OBB
        Vector2[] normals = new Vector2[4];
        normals[0] = rotate(OBBVertices[1].cpy().sub(OBBVertices[0]).nor(), (float) (-Math.PI/2f)); // down
        normals[1] = rotate(OBBVertices[2].cpy().sub(OBBVertices[1]).nor(), (float) (-Math.PI/2f)); // right
        normals[2] = rotate(OBBVertices[3].cpy().sub(OBBVertices[2]).nor(), (float) (-Math.PI/2f)); // up
        normals[3] = rotate(OBBVertices[0].cpy().sub(OBBVertices[3]).nor(), (float) (-Math.PI/2f)); // left
        return normals;
    }

    public static float degreesToRadians(float angleDeg){
        return (float) (angleDeg*(Math.PI/180));
    }
}
