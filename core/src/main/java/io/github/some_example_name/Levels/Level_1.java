package io.github.some_example_name.Levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.Framework.AssetDirectory;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.World.Tile;

public class Level_1 extends Level {

    public Level_1(Player playerArg, Vector3 cameraPosArg){
        super(playerArg);
        cameraPos = cameraPosArg;
        initialiseTiles();
        initialiseObstacles();
        initialiseEntities();
        defaultPlayerPosition = new Vector2(5,5);
    }

    private void initialiseTiles(){
        tiles = new Tile[5625];
        for (int i = 0; i < 5625; i++){
            String texture;
            double rand = Math.random();
            if (rand > 0.95){
                texture = AssetDirectory.Textures.Scene.MUD1;
            }else if (rand > 0.9){
                texture = AssetDirectory.Textures.Scene.MUD2;
            }else if (rand > 0.8){
                texture = AssetDirectory.Textures.Scene.MUD2;
            }else if (rand > 0.6){
                texture = AssetDirectory.Textures.Scene.MUD3;
            }else if (rand > 0.25){
                texture = AssetDirectory.Textures.Scene.GRASS1;
            }else {
                texture = AssetDirectory.Textures.Scene.FLOWERS;
            }
            tiles[i] = new Tile((i % 75)-10, (i/75)-10, texture); // fills left to right, bottom to top
        }

    }

    private void initialiseObstacles(){

    }

    private void initialiseEntities(){

    }

}
