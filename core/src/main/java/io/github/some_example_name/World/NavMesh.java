package io.github.some_example_name.World;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.PathfindingPriorityQueue;
import io.github.some_example_name.Framework.Stack;
import io.github.some_example_name.Framework.Utils;

import java.util.Arrays;

public class NavMesh {
    NavNode[] nodes;

    public NavMesh(){
        nodes = new NavNode[]{};
    }

    public int[] pathfind(int startNodeIndex, int targetNodeIndex){ // returns array of target node indices, uses A*
        if (startNodeIndex >= 0 && startNodeIndex < nodes.length && targetNodeIndex >= 0 && targetNodeIndex < nodes.length) { // check that both root and target are valid node indices
            PathfindingPriorityQueue frontier = new PathfindingPriorityQueue(nodes.length, nodes);
            for (int i = 0; i < nodes.length; i++){
                nodes[i].setWeight(Integer.MAX_VALUE);
                frontier.enqueue(i);
            }
            nodes[startNodeIndex].setWeight(0);

            int currentNodeIndex = startNodeIndex;
            while (currentNodeIndex != targetNodeIndex){
                if (frontier.empty()){
                    return new int[]{}; // safety exit in case target cannot be found
                }
                frontier.sortQueue(frontier.getQueue(), frontier.getFrontPointer(), frontier.getRearPointer());
                currentNodeIndex = frontier.dequeue();
                for (int i = 0; i < nodes[currentNodeIndex].getNeighbours().length; i++){
                    NavNode neighbourNode = nodes[nodes[currentNodeIndex].getNeighbours()[i]];
                    if (!neighbourNode.isExplored()) {
                        float newWeight = calculateWeight(neighbourNode, nodes[currentNodeIndex], nodes[targetNodeIndex]);
                        if (newWeight < neighbourNode.getWeight()){
                            neighbourNode.setWeight(newWeight);
                            neighbourNode.setPriorNodeIndex(currentNodeIndex);
                        }
                    }
                }
                nodes[currentNodeIndex].setExplored(true);
            }

            // backtracking to form pathStack
            boolean pathComplete = false;
            Stack pathStack = new Stack(nodes.length);
            NavNode currentPathNode = nodes[targetNodeIndex];
            while (!pathComplete){
                if (currentPathNode.getIndex() == startNodeIndex){
                    pathStack.push(startNodeIndex);
                    pathComplete = true;
                }else {
                    pathStack.push(currentPathNode.getIndex());
                    currentPathNode = nodes[currentPathNode.priorNodeIndex];
                }
            }
            int[] pathIndices = new int[pathStack.getNumElements()];
            for (int i = 0; i < pathIndices.length; i++){
                pathIndices[i] = pathStack.pop();
            }
            clearNodeData();
            return pathIndices;
        }
        return new int[]{};
    }

    private float calculateWeight(NavNode node, NavNode priorNode, NavNode targetNode){
        return calculateDistance(node, priorNode) + calculateHeuristic(node, targetNode);
    }

    private float calculateDistance(NavNode node, NavNode priorNode){ // distance as in sum of graph edge weights from root
        return priorNode.getDistance()+Utils.findDistance(node.getCentre(), priorNode.getCentre());
    }

    private float calculateHeuristic(NavNode node, NavNode targetNode) {
        return Utils.findDistance(node.getCentre(), targetNode.getCentre());
    }

    public int inhabitedNavNode(Vector2 position){ // identifies the index of the node currently inhabited by this entity, returns -1 if not found
        for (NavNode n : nodes){
            if (Utils.pointInTriangle(position, n.getVertices())){
                return n.getIndex();
            }
        }
        return -1;
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

    private void clearNodeData(){ // clears temporary node data used in pathfinding
        for (NavNode n : nodes){
            n.clearNodeData();
        }
    }
}
