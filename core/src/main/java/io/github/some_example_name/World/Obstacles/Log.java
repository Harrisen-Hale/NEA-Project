package io.github.some_example_name.World.Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.Utils;

public class Log extends Obstacle{
    public Log(Vector2 positionArg, float scaleArg, float angleArg) {
        super(positionArg, AssetDirectory.Textures.Scene.LOG, 2*scaleArg, scaleArg, angleArg);
        float boxHeight = 0.8f*scaleArg;
        float boxWidth = 1.6f*scaleArg;
        Vector2[] dVertices = new Vector2[]{new Vector2(-boxWidth/2f,-boxHeight/2f),new Vector2(boxWidth/2f,-boxHeight/2f),new Vector2(boxWidth/2f,boxHeight/2f),new Vector2(-boxWidth/2f,boxHeight/2f)};
        body = new Collider[]{new Collider(position, 0,0, dVertices, Utils.degreesToRadians(angleArg),  true, Color.GREEN, true, false)};

    }
}
