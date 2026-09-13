package io.github.some_example_name.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.Enemies.Dummy;
import io.github.some_example_name.Enemies.Forlorn;
import io.github.some_example_name.Framework.Entity;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Collider;
import io.github.some_example_name.Framework.Utils;
import io.github.some_example_name.World.NavNode;
import io.github.some_example_name.World.Obstacle;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.World.Tile;

public class Level_1 extends Level {

    public Level_1(Player playerArg, Vector3 cameraPosArg){
        tiles = new Tile[2500];
        for (int i = 0; i < 2500; i++){
            String texture;
            double rand = Math.random();
            if (rand > 0.9){
                texture = AssetDirectory.Textures.Scene.MUD1;
            }else if (rand > 0.5){
                texture = AssetDirectory.Textures.Scene.MUD2;
            }else if (rand > 0.2){
                texture = AssetDirectory.Textures.Scene.MUD3;
            }else {
                texture = AssetDirectory.Textures.Scene.GRASS1;
            }
            tiles[i] = new Tile((i % 50)-24, (i/50)-24, texture);
        }

        obstacles = new Obstacle[6];
        obstacles[0] = new Rock(-2, -2);
        obstacles[1] = new Rock(-5, 2);
        obstacles[2] = new Rock(-1, 4);
        obstacles[3] = new Rock(-6, 6);
        obstacles[4] = new Rock(-4, -1);
        obstacles[5] = new Rock(-4, 4);
        entities = new Entity[2];
        entities[0] = new Dummy(1, new Vector2(2,2));
        entities[1] = new Forlorn(2, new Vector2(-3.475f,3.05f), this);
        initialiseTestNavNodes();
        player = playerArg;
        player.setPosition(new Vector2(-3.475f,2.95f));
        cameraPos = cameraPosArg;
    }

    private void initialiseTestNavNodes(){
        NavNode[] testNavNodes = new NavNode[3];
        testNavNodes[0] = new NavNode(new Vector2[]{new Vector2(-4.5f,2.0f), new Vector2(-3.75f,3.5669873f), new Vector2(-1.25f,3.5669873f)}, 0, new int[]{1});
        testNavNodes[1] = new NavNode(new Vector2[]{new Vector2(-4.5f,2.0f), new Vector2(-3.75f,3.5669873f), new Vector2(-4.25f,3.5669873f)}, 1, new int[]{0,2});
        testNavNodes[2] = new NavNode(new Vector2[]{new Vector2(-4.75f,2.4330127f), new Vector2(-4.5f,2.0f), new Vector2(-4.25f,3.5669873f)}, 2, new int[]{1});
//        testNavNodes[3] = new NavNode(new Vector2[]{new Vector2(-4.75f,2.4330127f), new Vector2(-6.25f,5.5669875f), new Vector2(-4.25f,3.5669873f)});
//        testNavNodes[4] = new NavNode(new Vector2[]{new Vector2(-4.25f,3.5669873f), new Vector2(-4.5f,4.0f), new Vector2(-6.25f,5.5669875f)});
//        testNavNodes[5] = new NavNode(new Vector2[]{new Vector2(-4.5f,4.0f), new Vector2(-5.75f,5.5669875f), new Vector2(-6.25f,5.5669875f)});
//        testNavNodes[6] = new NavNode(new Vector2[]{new Vector2(-4.5f,4.0f), new Vector2(-4.25f,4.4330125f), new Vector2(-5.75f,5.5669875f)});
//        testNavNodes[7] = new NavNode(new Vector2[]{new Vector2(-3.75f,3.5669873f), new Vector2(-1.25f,3.5669873f), new Vector2(-1.5f,4.0f)});
//        testNavNodes[8] = new NavNode(new Vector2[]{new Vector2(-3.75f,3.5669873f), new Vector2(-1.5f,4.0f), new Vector2(-3.5f,4.0f)});
//        testNavNodes[9] = new NavNode(new Vector2[]{new Vector2(-3.5f,4.0f), new Vector2(-1.5f,4.0f), new Vector2(-1.25f,4.4330125f)});
//        testNavNodes[10] = new NavNode(new Vector2[]{new Vector2(-3.5f,4.0f), new Vector2(-1.25f,4.4330125f), new Vector2(-3.75f,4.4330125f)});
//        testNavNodes[11] = new NavNode(new Vector2[]{new Vector2(-5.5f,6.0f), new Vector2(-5.75f,5.5669875f), new Vector2(-4.25f,4.4330125f)});
//        testNavNodes[12] = new NavNode(new Vector2[]{new Vector2(-4.25f,4.4330125f), new Vector2(-3.75f,4.4330125f), new Vector2(-5.5f,6.0f)});
//        testNavNodes[13] = new NavNode(new Vector2[]{new Vector2(-5.5f,6.0f), new Vector2(-3.75f,4.4330125f), new Vector2(-1.25f,4.4330125f)});
//        testNavNodes[14] = new NavNode(new Vector2[]{new Vector2(-5.25f,2.4330127f), new Vector2(-4.75f,2.4330127f), new Vector2(-6.25f,5.5669875f)});
//        testNavNodes[15] = new NavNode(new Vector2[]{new Vector2(-5.5f,2.0f), new Vector2(-5.25f,2.4330127f), new Vector2(-6.25f,5.5669875f)});
        navMesh.setNodes(testNavNodes);
    }

    private class Rock extends Obstacle{
        public Rock(int x, int y) {
            super(x, y, AssetDirectory.Textures.Scene.ROCK);
            body = new Collider[]{new Collider(position, 0,0, Utils.generateRegularPolygon(6, 0.5f), 0, 0, true, Color.GREEN, true, false)};
        }
    }

}
