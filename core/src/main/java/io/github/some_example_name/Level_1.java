package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Level_1 extends Level{
    private Tile[][] tileArr = new Tile[9][16];
    public Level_1(){
        for (int j = 0; j < 9; j++){
            for (int i = 0; i < 16; i++){
                tileArr[j][i] = new Tile(i, j, AssetDirectory.Scene.MUD);
            }
        }
    }

    public void drawTiles(SpriteBatch batch){
        for (Tile[] r : tileArr){
            for (Tile t : r){
                t.draw(batch);
            }
        }
    }
}
