package com.sethdelbridge.ripple;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

/**
 * Created by Seth on 1/25/16.
 */
public class TouchableManager {
    private ArrayList<Touchable> touchables;
    public TouchableManager(){
        touchables = new ArrayList<>();
    }

    /*
        Adds a Touchable to be monitored
     */
    public void add(Touchable t){
        touchables.add(t);
    }

    /*
        Discards touchable
     */
    public void remove(String id){
        for(Touchable t : touchables){
            if(t.getId().equals(id)){
                t.dispose();
                touchables.remove(t);
                return;
            }
        }
    }

    /*
        Clears the TouchManager in preparation for
        a new touch state
     */
    public void clear(){
        for(Touchable t : touchables){
            t.dispose();
        }
        touchables.clear();
    }

    /*
        Returns a Touchable if touched, null otherwise
     */
    public Touchable isTouchableTouched(int x, int y){
        for(Touchable t : touchables){
            if(t.isTouched(x, y)) return t;
        }
        Gdx.app.log("touchable", "returning null");
        return null;
    }

    /*
        Retrieves a touchable by ID
     */
    public Touchable getTouchableById(String id){
        for(Touchable t : touchables){
            if(t.getId().equals(id)) return t;
        }
        return  null;
    }

    public void dispose() {
        for (Touchable t : touchables) {
            t.dispose();
        }
    }
}
