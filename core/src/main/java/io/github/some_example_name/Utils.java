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

    public static float degreesToRadians(float angleDeg){
        return (float) (angleDeg*(Math.PI/180));
    }
}
