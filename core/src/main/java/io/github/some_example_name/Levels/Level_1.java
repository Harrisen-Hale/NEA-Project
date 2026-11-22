package io.github.some_example_name.Levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.Enemies.Forlorn;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Framework.Entity;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.World.Obstacles.Barrier;
import io.github.some_example_name.World.Obstacles.Log;
import io.github.some_example_name.World.Obstacles.Obstacle;
import io.github.some_example_name.World.Obstacles.Stump;
import io.github.some_example_name.World.Tile;

public class Level_1 extends Level {

    public Level_1(Player playerArg, Vector3 cameraPosArg){
        super(playerArg);
        cameraPos = cameraPosArg;
        initialiseTiles();
        initialiseObstacles();
        initialiseEntities();
        defaultPlayerPosition = new Vector2(1.5f,3.5f);
    }

    public void loadLevel(Vector2 playerEntryPoint){
        super.loadLevel();
        player.setPosition(playerEntryPoint);
    }

    private void initialiseTiles(){
        tiles = new Tile[5625]; // 75 * 75 square

        // main space
        for (int i = 0; i < 5625; i++){
            String texture;
            double rand = Math.random();
            if (rand > 0.6){
                texture = AssetDirectory.Textures.Scene.MUD3;
            }else if (rand > 0.2){
                texture = AssetDirectory.Textures.Scene.GRASS1;
            }else {
                texture = AssetDirectory.Textures.Scene.FLOWERS;
            }
            tiles[i] = new Tile((i % 75), (i/75), texture); // fills left to right, bottom to top
        }

        // outer borders
        for (int j = 0; j < 75; j++){
            tiles[j] = new Tile(j, 0, AssetDirectory.Textures.Scene.CANOPY);
            tiles[5625-75+j] = new Tile(j, 74, AssetDirectory.Textures.Scene.CANOPY);
            tiles[75*j] = new Tile(0, j, AssetDirectory.Textures.Scene.CANOPY);
            tiles[75*(j+1)-1] = new Tile(75-1, j, AssetDirectory.Textures.Scene.CANOPY);
        }

        // dividing canopy
        for (int p = 6*75; p < 6*75 + 35; p++){
            tiles[p] = new Tile(p-(6*75),6, AssetDirectory.Textures.Scene.CANOPY);
        }

    }

    private void initialiseObstacles(){
        obstacles = new Obstacle[9];
        // boundary walls
        obstacles[0] = new Barrier(new Vector2(75/2f, 0.5f), 75, 1, 0);
        obstacles[1] = new Barrier(new Vector2(75/2f, 74.5f), 75, 1, 0);
        obstacles[2] = new Barrier(new Vector2(0.5f, 75/2f), 1, 75, 0);
        obstacles[3] = new Barrier(new Vector2(74.5f, 75/2f), 1, 75, 0);

        // other walls
        obstacles[4] = new Barrier(new Vector2(35/2f, 6.5f), 35, 1, 0);

        // scatter obstacles
        obstacles[5] = new Stump(new Vector2(7, 2.3f), 1, 0);
        obstacles[6] = new Stump(new Vector2(15, 4.7f), 1, 35);
        obstacles[7] = new Stump(new Vector2(10.5f, 3.4f), 1.55f, 65);
        obstacles[8] = new Log(new Vector2(8f, 4.2f), 1.25f, -55);

    }

    private void initialiseEntities(){
        entities = new Entity[1];

        // enemies
        entities[0] = new Forlorn(1, new Vector2(12.5f, 3.5f), this);
    }

}
