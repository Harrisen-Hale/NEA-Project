package io.github.some_example_name.World;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class NavMesh {
    NavNode[] nodes;

    public NavMesh(){

    }

    public Vector2[] pathfind(int startNodeIndex, int targetNodeIndex){ // returns array of target coordinates


        return new Vector2[]{};
    }

    public NavNode[] getNodes() {
        return nodes;
    }

    public void setNodes(NavNode[] nodes) {
        this.nodes = nodes;
    }

    public void drawNavNodes(ShapeRenderer sr){
        for (NavNode n : nodes){
            n.drawDebug(sr);
        }
    }
}
