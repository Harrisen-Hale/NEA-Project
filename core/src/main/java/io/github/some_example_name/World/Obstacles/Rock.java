package io.github.some_example_name.World.Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.Utils;

public class Rock extends Obstacle {
    public Rock(Vector2 positionArg, float scaleArg, float angleArg) {
        super(positionArg, AssetDirectory.Textures.Scene.ROCK, scaleArg, scaleArg, angleArg);
        body = new Collider[]{new Collider(position, 0,0, Utils.generateRegularPolygon(6, 0.5f*scaleArg), angleArg,  true, Color.GREEN, true, false)};
    }

}
