package io.github.some_example_name.World.Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.Utils;

public class Stump extends Obstacle{
    public Stump(Vector2 positionArg) {
        super(positionArg, AssetDirectory.Textures.Scene.STUMP);
        body = new Collider[]{new Collider(position, 0,0, Utils.generateRegularPolygon(6, 0.4f), 0,  true, Color.GREEN, true, false)};
    }
    public Stump(int x, int y) {
        super(x, y, AssetDirectory.Textures.Scene.STUMP);
        body = new Collider[]{new Collider(position, 0,0, Utils.generateRegularPolygon(6, 0.4f), 0,  true, Color.GREEN, true, false)};
    }
}
