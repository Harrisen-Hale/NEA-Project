package io.github.some_example_name.Framework;

public class Queue {
    int[] queue;
    int length;
    int frontPointer = 0;
    int rearPointer = 0;
    int numElements = 0;

    public Queue(int lengthArg){
        length = lengthArg;
        queue = new int[length];
    }

    public void enqueue(int element){
        if (!isFull()) {
            queue[rearPointer] = element;
            rearPointer++;
            numElements++;
        }
    }

    public int peek(){
        if (!empty()) {
            return queue[frontPointer];
        }
        else return -1;
    }

    public int dequeue(){
        if (!empty()){
            int foreElement = queue[frontPointer];
            frontPointer++;
            numElements--;
            return foreElement;
        }
        return -1;
    }

    public int getNumElements(){
        return numElements;
    }

    public boolean isFull(){
        return (getNumElements() == length);
    }

    public boolean empty(){
        return (getNumElements() == 0);
    }

    public void wipe(){
        queue = new int[length];
        rearPointer = 0;
        frontPointer = 0;
        numElements = 0;
    }

    public int[] getQueue() {
        return queue;
    }

    public void setQueue(int[] queue) {
        this.queue = queue;
    }

    public void setElement(int index, int element) {
        if (Utils.validIndex(index, queue)){
            queue[index] = element;
        }
    }

    public int getRearPointer() {
        return rearPointer;
    }

    public int getFrontPointer() {
        return frontPointer;
    }
}
