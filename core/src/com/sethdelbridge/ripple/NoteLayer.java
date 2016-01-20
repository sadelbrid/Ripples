package com.sethdelbridge.ripple;
import java.util.ArrayList;

/**
 * Created by Seth on 6/7/15.
 */
public class NoteLayer {
    //ArrayList<Integer> notes;
    ArrayList<Note> notes;
    public NoteLayer(int numNotes){
        notes = new ArrayList<>();
        for(int i = 0; i < numNotes; i++){
            notes.add(new Note(0));
        }
    }

    public void activate(int n, int s){
        notes.get(n).strength = s;
        notes.get(n).isHit = false;
    }

    public void deactivate(int n){
        notes.get(n).strength = 0;
    }

}
