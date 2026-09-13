package io.github.some_example_name.Enemies;

import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Entity;
import io.github.some_example_name.Framework.Timer;
import io.github.some_example_name.Framework.Utils;
import io.github.some_example_name.Levels.Level;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.World.NavMesh;

import java.util.Arrays;

public class Enemy extends Entity {

    protected Level residentLevel;
    protected Player player;
    protected Pathfinder pathfinder;
    protected float speed;

    public Enemy(Level residentLevelArg){
        residentLevel = residentLevelArg;
        player = residentLevelArg.getPlayer();
        pathfinder = new Pathfinder();
        speed = 0;
    }

    public Level getResidentLevel() {
        return residentLevel;
    }

    public void setResidentLevel(Level residentLevel) {
        this.residentLevel = residentLevel;
        player = residentLevel.getPlayer();
    }

    protected class Pathfinder{
        protected int[] pathfindRoute;
        protected  int pathfindRouteStage;
        protected Timer routeCalculationTimer;


        protected Pathfinder(){
            pathfindRoute = new int[]{};
            pathfindRouteStage = 0;
            routeCalculationTimer = new Timer(180);
        }

        protected void track(Vector2 target){
            NavMesh mesh = residentLevel.getNavMesh();
            Vector2 nextPosition = position;

            if (routeCalculationTimer.tick() || pathfindRouteStage >= pathfindRoute.length-1) {
                pathfindRoute = mesh.pathfind(mesh.inhabitedNavNode(position), mesh.inhabitedNavNode(target));
                pathfindRouteStage = 0;
            }
            if (pathfindRoute.length > 0) {
                if(mesh.inhabitedNavNode(position) == pathfindRoute[pathfindRouteStage] && pathfindRouteStage < pathfindRoute.length-1){
                    pathfindRouteStage++;
                }
                nextPosition = mesh.getNodes()[pathfindRoute[pathfindRouteStage]].getCentre();
            }

            velocity  = Utils.findUnitVector(position, nextPosition).scl(speed);
        }
    }

}
