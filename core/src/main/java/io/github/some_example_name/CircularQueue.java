package io.github.some_example_name;

public class CircularQueue {
    int[] queue;
    int length;
    int frontPointer = 0;
    int rearPointer = -1;

    public CircularQueue(int lengthArg){
        length = lengthArg;
        queue = new int[length];
    }

    public void enqueue(int element){
        if (!isFull()) {
            rearPointer++;
            rearPointer = rearPointer % length; // cyclic structure
            queue[rearPointer] = element;
        }
    }

    public int peek(){
        if (!isEmpty()) {
            return queue[frontPointer];
        }
        else return -1;
    }

    public int dequeue(){
        if (!isEmpty()){
            int foreElement = queue[frontPointer];
            frontPointer++;
            frontPointer = frontPointer % length;
            return foreElement;
        }
        return -1;
    }

    public int getNumElements(){
        if (frontPointer <= rearPointer){
            return rearPointer - frontPointer + 1;
        }else {
            return length - (frontPointer - rearPointer) + 1;
        }
    }

    public boolean isFull(){
        return (getNumElements() == length);
    }

    public boolean isEmpty(){
        return (getNumElements() == 0);
    }
}
