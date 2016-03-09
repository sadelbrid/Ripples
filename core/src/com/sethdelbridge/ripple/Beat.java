package com.sethdelbridge.ripple;

/**
 * Created by Seth on 1/18/16.
 */
public class Beat {
    private boolean live;
    private boolean isHit;
    public float scale;
    public int layer;
    public Beat(int layer){
        this.layer = layer;
        scale = .5f;
        live = false;
        isHit = false;
    }

    public int hit(){
        isHit = true;
        return layer;
    }

    public void update(float dt){
        scale += dt;
    }

    public void reset(){
        scale = .5f;
        isHit = false;
    }

    public boolean isActive(){
        return live;
    }

    public void setActive(boolean b){
        live = b;
    }
}
