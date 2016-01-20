package com.sethdelbridge.ripple;

/**
 * Created by Seth on 1/14/16.
 */
public class Note {
    int strength;
    float scale;
    boolean isHit;
    public Note(int s){
        strength = s;
        scale = .5f;
        isHit = false;
    }

    public Note hit(){
        strength--;
        isHit = true;
        return this;
    }

    public void update(float dt){
        scale += dt;
    }

    public void reset(){
        scale = .5f;
        isHit = false;
    }
}
