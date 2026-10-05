package io.github.some_example_name.Items;

public class Armour extends Item{
    protected int piece; // 0 - Head, 1 - Torso, 2 - Legs, 3 - Feet
    protected float defense;

    public Armour(){
        super();
        piece = 0;
        defense = 0;
    }
}
