package io.github.some_example_name.Enemies;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.*;
import io.github.some_example_name.Levels.Level;
import io.github.some_example_name.Player.Player;
import io.github.some_example_name.World.NavMesh;
import io.github.some_example_name.World.Obstacle;

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
            if (pathClear(target)) {
                nextPosition = target;
            }else {
                if (routeCalculationTimer.tick() || pathfindRouteStage >= pathfindRoute.length-1) {
                    pathfindRoute = mesh.pathfind(mesh.inhabitedNavNode(position), mesh.inhabitedNavNode(target));
                    pathfindRouteStage = 0;
                }
                if (pathfindRoute.length > 1) { // no need to repeatedly walk to the centre of the node it's already in
                    if(mesh.inhabitedNavNode(position) == pathfindRoute[pathfindRouteStage] && pathfindRouteStage < pathfindRoute.length-1){
                        pathfindRouteStage++;
                    }
                    nextPosition = mesh.getNodes()[pathfindRoute[pathfindRouteStage]].getCentre();

                    for (int i = pathfindRoute.length-1; i > pathfindRouteStage; i--){ // skips unneeded intermediate nodes
                        Vector2 futurePosition = mesh.getNodes()[pathfindRoute[i]].getCentre();
                        if (pathClear(futurePosition)){
                            nextPosition = futurePosition;
                            i = -1;
                        }
                    }
                }
            }
            velocity.add(Utils.findUnitVector(position, nextPosition).scl(speed));
            rotation(nextPosition);
        }

        public boolean pathClear(Vector2 target){
            for (Obstacle o : residentLevel.getObstacles()){
                for (Collider c : o.getBody()){
                    if (Utils.detectIntersectionOfLineSegmentWithPolygon(position, target, c.getVertices())){
                        return false;
                    }
                }
            }
            return true;
        }

    }

    public void drawDebug(ShapeRenderer sr){
        super.drawDebug(sr);
        //sr.line(position, residentLevel.getPlayer().getPosition());
    }

}
