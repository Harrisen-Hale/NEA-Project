package io.github.some_example_name.Framework;

public class Stack {
    int[] stack;
    int length;
    int pointer = 0;

    public Stack(int lengthArg){
        length = lengthArg;
        stack = new int[length];
    }

    public void push(int element){
        if (!isFull()) {
            stack[pointer] = element;
            pointer++;
        }
    }

    public int peek(){
        if (notEmpty()) {
            return stack[pointer-1];
        }
        else return -1;
    }

    public int pop(){
        if (notEmpty()){
            pointer--;
            return stack[pointer];
        }
        return -1;
    }

    public int getNumElements(){
        return pointer;
    }

    public boolean isFull(){
        return (pointer == length);
    }

    public boolean notEmpty(){
        return (pointer > 0);
    }

    public void wipe(){
        stack = new int[length];
        pointer = 0;
    }

    public int[] getStack() {
        return stack;
    }

    public void setStack(int[] stack) {
        this.stack = stack;
    }

    public void setElement(int index, int element) {
        if (Utils.validIndex(index, stack)){
            stack[index] = element;
        }
    }

    public int getPointer() {
        return pointer;
    }
}
