package io.github.some_example_name.World.Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;

public class Barrier extends Obstacle{
    public Barrier(Vector2 positionArg, Vector2[] dVertices) {
        super(positionArg, AssetDirectory.Textures.Misc.EMPTY); // Barriers are invisible by default
        body = new Collider[]{new Collider(position, 0,0, dVertices, 0,  true, Color.GREEN, true, false)};
    }
}
