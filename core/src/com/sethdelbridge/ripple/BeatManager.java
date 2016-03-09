package com.sethdelbridge.ripple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

/**
 * Created by Seth on 1/18/16.
 */
public class BeatManager {
    public ArrayList<Beat> beats;
    private ArrayList<Integer> beatsHit;
    private Sound beatSound;
    public BeatManager(int numBeats){
        beats = new ArrayList<>();
        beatsHit = new ArrayList<>();
        for(int i = 0; i < numBeats; i++){
            beats.add(new Beat(i));
        }

        //beatSound = Gdx.audio.newSound(Gdx.files.internal(""));
    }

    public void setActive(int layer, boolean a){
        beats.get(layer).setActive(a);
    }

    public void hitBeat(int layer){
        beatsHit.add(beats.get(layer).hit());
        beatSound.play();
    }

    public boolean beatIsActive(int layer){
        return beats.get(layer).isActive();
    }

    public Beat getBeat(int layer){
        return beats.get(layer);
    }

    public void update(float dt){
        for(int i = 0; i < beatsHit.size(); i++){
            beats.get(beatsHit.get(i)).update(dt);
            if(beats.get(beatsHit.get(i)).scale > 1){
                beats.get(beatsHit.get(i)).reset();
                beatsHit.remove(i);
                i--;
            }
        }
    }

    public void dispose(){
        beatSound.dispose();
    }
}
