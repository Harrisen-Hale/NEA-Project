package io.github.some_example_name.Framework;

public class Queue {
    int[] queue;
    int length;
    int frontPointer = 0;
    int rearPointer = -1;

    public Queue(int lengthArg){
        length = lengthArg;
        queue = new int[length];
    }

    public void enqueue(int element){
        if (!isFull()) {
            rearPointer++;
            queue[rearPointer] = element;
        }
    }

    public int peek(){
        if (notEmpty()) {
            return queue[frontPointer];
        }
        else throw new RuntimeException("Queue is empty");
    }

    public int dequeue(){
        if (notEmpty()){
            int foreElement = queue[frontPointer];
            frontPointer++;
            return foreElement;
        }
        else throw new RuntimeException("Queue is empty");
    }

    public int getNumElements(){
        return rearPointer-frontPointer+1;
    }

    public boolean isFull(){
        return (rearPointer == length-1);
    }

    public boolean notEmpty(){
        return (getNumElements() >= 0);
    }

    public void wipe(){
        queue = new int[length];
        rearPointer = -1;
        frontPointer = 0;
    }

    public int[] getQueue() {
        return queue;
    }

    public void setQueue(int[] queue) {
        this.queue = queue;
    }

    public int getRearPointer() {
        return rearPointer;
    }

    public int getFrontPointer() {
        return frontPointer;
    }

    public void setFrontPointer(int frontPointer) {
        this.frontPointer = frontPointer;
    }

    public void setRearPointer(int rearPointer) {
        this.rearPointer = rearPointer;
    }
}
