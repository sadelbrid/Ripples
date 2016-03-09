package com.sethdelbridge.ripple;

import java.util.ArrayList;

/**
 * Created by Seth on 6/7/15.
 */
public class NoteLayers {
    ArrayList<NoteLayer> rep;
    private ArrayList<Integer> layerDistances;
    private ArrayList<Note> notesHit;
    public int notesPerLayer;
    public int numLayers;
    private static int distance = (int)(RippleGame.WIDTH*.1f);
    public NoteLayers(int numLayers, int notesPerLayer){
        this.notesPerLayer = notesPerLayer;
        this.numLayers = numLayers;
        layerDistances = new ArrayList<>();
        rep = new ArrayList<>();
        for(int i = 0; i < numLayers; i++){
            rep.add(new NoteLayer(notesPerLayer));
            layerDistances.add(distance + distance*i);
        }
        notesHit = new ArrayList<>();
    }

    public void add(int layer, int note, int s){
        rep.get(layer).activate(note, s);
    }

    public void remove(int layer, int note){
        rep.get(layer).deactivate(note);
    }

    public void hitNote(int layer, int note){
        notesHit.add(rep.get(layer).notes.get(note).hit());
    }

    public boolean noteIsActive(int layer, int note){
        return rep
                .get(layer)
                .notes.get(note).strength > 0;
    }

    public Note getNote(int layer, int note){
        return rep.get(layer).notes.get(note);
    }

    public void update(float dt){
        for(int i = 0; i < notesHit.size(); i++){
            notesHit.get(i).update(dt);
            if(notesHit.get(i).scale > 1) {
                notesHit.get(i).reset();
                notesHit.remove(i);
                i--;
            }
        }
    }
}
