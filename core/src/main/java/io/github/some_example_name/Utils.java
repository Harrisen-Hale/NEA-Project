package io.github.some_example_name;

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
}
