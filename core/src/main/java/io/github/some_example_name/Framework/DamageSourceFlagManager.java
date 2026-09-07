package io.github.some_example_name.Framework;

import java.util.ArrayList;

public class DamageSourceFlagManager {
    ArrayList<Integer> flaggedEntities;

    public DamageSourceFlagManager(){
        flaggedEntities = new ArrayList<>();
    }

    public void flagEntity(int ID){
        flaggedEntities.add(ID);
    }

    public void clear(){
        flaggedEntities = new ArrayList<>();
    }

    public boolean isFlagged(int ID){
        return (Utils.linearSearch(ID, Utils.intArrayListToArray(flaggedEntities)) >= 0); // the index returned by search is greater than or equal to zero if and only if the target is present
    }
}
