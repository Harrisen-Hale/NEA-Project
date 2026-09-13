package io.github.some_example_name.Framework;

import io.github.some_example_name.World.NavNode;

import java.util.Arrays;

public class PathfindingPriorityQueue extends Queue{

    private NavNode[] nodes;

    public PathfindingPriorityQueue(int lengthArg, NavNode[] nodesArg) {
        super(lengthArg);
        nodes = nodesArg;
    }

    public void sortQueue(int[] queue, int left, int right){ // customised merge sort that sorts indices of nodes based on node weights
        if (left < right){
            int midpoint = left+(right - left)/2;

            sortQueue(queue, left, midpoint); // sort left half
            sortQueue(queue, midpoint+1, right); // sort right half

            mergeHelper(queue, left, midpoint, right);
        }
    }

    private void mergeHelper(int[] nodeIndices, int left, int midpoint, int right){ // helper function for sortQueue

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
            if (compareNodeWeights(leftArray[l], rightArray[r]) == 0){
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

    private int compareNodeWeights(int index1, int index2){ // returns 0 if left, 1 if right
        if (Utils.validIndex(index1, nodes) && Utils.validIndex(index2, nodes)) {
            if (nodes[index1].getWeight() <= nodes[index2].getWeight()){
                return 0;
            }
            return 1;
        }
        return 0;
    }
    public boolean empty(){
        return !notEmpty();
    }
}
