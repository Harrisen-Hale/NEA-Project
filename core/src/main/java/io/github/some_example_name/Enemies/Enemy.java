package io.github.some_example_name.Enemies;

import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Entity;
import io.github.some_example_name.Framework.Utils;
import io.github.some_example_name.Levels.Level;
import io.github.some_example_name.World.NavMesh;

public class Enemy extends Entity {

    protected Level residentLevel;
    protected Pathfinder pathfinder;

    public Enemy(Level residentLevelArg){
        residentLevel = residentLevelArg;
        pathfinder = new Pathfinder();
    }


    public Level getResidentLevel() {
        return residentLevel;
    }

    public void setResidentLevel(Level residentLevel) {
        this.residentLevel = residentLevel;
    }

    protected class Pathfinder{
        protected int[] pathfindRoute;

        protected Pathfinder(){
            pathfindRoute = new int[]{};
        }

        protected void track(Vector2 target){
            NavMesh mesh = residentLevel.getNavMesh();
            pathfindRoute = mesh.pathfind(mesh.inhabitedNavNode(position), mesh.inhabitedNavNode(residentLevel.getPlayer().getPosition()));
            if(pathfindRoute.length>=1){
                position.add(Utils.findUnitVector(position, mesh.getNodes()[pathfindRoute[0]].getCentre()).scl(0.01f));
            }
        }
    }

}
