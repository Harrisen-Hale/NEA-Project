package io.github.some_example_name.World;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.Framework.Utils;
import org.w3c.dom.Node;

import java.util.Arrays;

public class NavMesh {
    NavNode[] nodes;

    public NavMesh(){

    }

    public Vector2[] pathfind(int startNodeIndex, int targetNodeIndex){ // returns array of target coordinates
        if (startNodeIndex >= 0 && startNodeIndex < nodes.length && targetNodeIndex >= 0 && targetNodeIndex < nodes.length) { // check that both root and target are valid node indices
            for (NavNode n : nodes){
                n.setWeight(Integer.MAX_VALUE);
            }
            nodes[startNodeIndex].setWeight(0);
        }

        return new Vector2[]{};
    }

    public int currentNavNode(Vector2 position){ // identifies the index of the node currently inhabited by this entity, returns -1 if not found
        for (NavNode n : nodes){
            if (Utils.pointInTriangle(position, n.getVertices())){
                return n.getIndex();
            }
        }
        return -1;
    }

    private void sortNodeIndices(int[] nodeIndices, int left, int right){ // customised merge sort that sorts indices of nodes based on node weights
        if (left < right){
            int midpoint = left+(right - left)/2;

            sortNodeIndices(nodeIndices, left, midpoint); // sort left half
            sortNodeIndices(nodeIndices, midpoint+1, right); // sort right half

            mergeHelper(nodeIndices, left, midpoint, right);
            System.out.println(left);
            System.out.println(midpoint);
            System.out.println(right);
            System.out.println(Arrays.toString(nodeIndices));
            System.out.println(nodes[nodeIndices[0]].getWeight()+ " " +nodes[nodeIndices[1]].getWeight() + " "+ nodes[nodeIndices[2]].getWeight());
        }
    }

    private void mergeHelper(int[] nodeIndices, int left, int midpoint, int right){ // helper function for sortNodeIndices

        //subarray sizes
        int leftSize = midpoint + 1 - left;
        int rightSize = right - midpoint;

        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];

        // indices
        int l = 0; // index of left
        int r = 0; // index of right
        int i = left; // index of merged subarray

        for (int p = 0; p < leftSize; p++){
            leftArray[p] = nodeIndices[left+p];
        }
        for (int q = 0; q < rightSize; q++){
            rightArray[q] = nodeIndices[midpoint+1+q];
        }

        while (l < midpoint + 1 - left && r < right - midpoint){
            if (compareNodeWeights(nodeIndices[l], nodeIndices[r]) == l){
                nodeIndices[i] = leftArray[l];
                l++;
            }else {
                nodeIndices[i] = rightArray[r];
                r++;
            }
            i++;
        }

        // adding any leftovers
        for (int x = l; x < midpoint + 1 - left; x++){
            nodeIndices[i] = leftArray[x];
            i++;
        }
        for (int y = r; y < right - midpoint; y++){
            nodeIndices[i] = rightArray[y];
            i++;
        }
    }

    private int compareNodeWeights(int index1, int index2){
        if (Utils.validIndex(index1, nodes) && Utils.validIndex(index2, nodes)) {
            if (nodes[index1].getWeight() <= nodes[index2].getWeight()){
                return index1;
            }
            return index2;
        }
        return index1;
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
