package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Level {

    protected Entity[] entities; // stores all non-player entities in the level
    protected Tile[] tiles; // stores all decorative tiles in the level
    protected Obstacle[] obstacles; // stores all non-entity collision objects in the level
    protected NavNode[] navNodes; // stores all entity navigation nodes
    protected Player player; // stores a reference to the player
    protected Vector3 cameraPos;

    public Level(){
        entities = new Entity[0];
        tiles = new Tile[0];
        obstacles = new Obstacle[0];
    }


    public void logicTick(){
        for (Entity e : entities){
            e.logicTick(player);
        }
    }

    public void drawAllBodies(SpriteBatch batch){
        drawTiles(batch);
        drawObstacles(batch);
        drawEntityBodies(batch);
    }

    public void drawAllEffects(SpriteBatch batch){
        drawEntityEffects(batch);
    }

    public void drawAllDebug(ShapeRenderer sr){
        for (Entity e : entities){
            e.drawDebug(sr);
        }
        for (Obstacle o : obstacles){
            o.drawDebug(sr);
        }
    }

    public void drawNavNodes(ShapeRenderer sr){
        for (NavNode n : navNodes){
            n.drawDebug(sr);
        }
    }

    private void drawTiles(SpriteBatch batch){
        for (Tile t : tiles){
            if (cullCheck(t.getPosition())) {
                t.draw(batch);
            }
        }
    }

    private void drawEntityBodies(SpriteBatch batch){
        for (Entity e : entities){
            if (cullCheck(e.getPosition())) {
                e.drawBody(batch);
            }
        }
    }

    private void drawObstacles(SpriteBatch batch){
        for (Obstacle o : obstacles){
            if (cullCheck(o.getPosition())) {
                o.draw(batch);
            }
        }
    }

    private void drawEntityEffects(SpriteBatch batch){
        for (Entity e : entities){
            if (cullCheck(e.getPosition())) {
                e.drawEffects(batch);
            }
        }
    }

    public Entity[] getEntities() {
        return entities;
    }

    public Obstacle[] getObstacles() {
        return obstacles;
    }

    public NavNode[] getNavNodes() {
        return navNodes;
    }

    private boolean cullCheck(Vector2 refPos){
        return (refPos.cpy().sub(new Vector2(cameraPos.x, cameraPos.y)).y < 5 && refPos.cpy().sub(new Vector2(cameraPos.x, cameraPos.y)).x < 9);
    }
}
