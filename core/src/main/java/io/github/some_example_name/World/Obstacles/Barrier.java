package io.github.some_example_name.World.Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.Utils;

public class Barrier extends Obstacle{
    public Barrier(Vector2 centre, Vector2[] dVertices) {
        super(centre, AssetDirectory.Textures.Misc.EMPTY, 0, 0, 0); // Barriers are invisible by default
        body = new Collider[]{new Collider(position, 0,0, dVertices, 0,  true, Color.FIREBRICK, true, false)};
    }

    public Barrier(Vector2 centre, float width, float height, float angleArg) {
        super(centre, AssetDirectory.Textures.Misc.EMPTY, 0, 0, 0); // Barriers are invisible by default
        Vector2[] dVertices = new Vector2[]{new Vector2(-width/2f,-height/2f), new Vector2(width/2f, -height/2f), new Vector2(width/2f, height/2f), new Vector2(-width/2f, height/2f)};
        dVertices = Utils.rotatePolygon(dVertices, new Vector2(0,0), angleArg);
        body = new Collider[]{new Collider(position, 0,0, dVertices, 0,  true, Color.FIREBRICK, true, false)};
    }

}
