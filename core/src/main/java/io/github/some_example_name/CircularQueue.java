package io.github.some_example_name;


public class CircularQueue {
    int[] queue;
    int length;
    int frontPointer = 0;
    int rearPointer = 0;

    public CircularQueue(int lengthArg){
        length = lengthArg;
        queue = new int[length];
    }

    public void enqueue(int element){
        if (!isFull()) {
            queue[rearPointer] = element;
            rearPointer++;
            rearPointer = rearPointer % length; // cyclic structure
        }
    }

    public int peek(){
        if (notEmpty()) {
            return queue[frontPointer];
        }
        else return -1;
    }

    public int dequeue(){
        if (notEmpty()){
            int foreElement = queue[frontPointer];
            frontPointer++;
            frontPointer = frontPointer % length;
            return foreElement;
        }
        return -1;
    }

    public int getNumElements(){
        if (rearPointer == frontPointer){
            return 0;
        }else {
            return Math.abs(frontPointer - rearPointer);
        }
    }

    public boolean isFull(){
        return (getNumElements() == length);
    }

    public boolean notEmpty(){
        return (getNumElements() != 0);
    }

    public void wipe(){
        queue = new int[length];
        rearPointer = 0;
        frontPointer = 0;
    }
}
