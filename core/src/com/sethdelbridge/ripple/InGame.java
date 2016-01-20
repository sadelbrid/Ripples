package com.sethdelbridge.ripple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Random;

public class InGame extends State {
    public final int NUM_SPARKLES = 50;
    public ArrayList<Ripple> ripples;
    public ArrayList<Pulse> pulses;
    public NoteLayers noteManager;
    public TouchHelper touchHelper;
    public int selectedDis;
    public double angleFromCenter;
    public ArrayList<Sound> tones;
    public static int separation =  (int)(RippleGame.WIDTH*.075f);
    public Texture vignette;
    public Texture node;
    public float pulseRate = 0;
    public ArrayList<Shimmer> sparkles;
    public Random rand;
    public BeatManager beatManager;

    public InGame(GameStateManager gsm){
        super(gsm);
        angleFromCenter = -4;

        touchHelper = new TouchHelper();
        selectedDis = separation;
        ripples = new ArrayList<>();
        pulses = new ArrayList<>();

        tones = new ArrayList<>();
        tones.add(Gdx.audio.newSound(Gdx.files.internal("one.wav")));
        tones.add(Gdx.audio.newSound(Gdx.files.internal("two.wav")));
        tones.add(Gdx.audio.newSound(Gdx.files.internal("three.wav")));
        tones.add(Gdx.audio.newSound(Gdx.files.internal("four.wav")));
        tones.add(Gdx.audio.newSound(Gdx.files.internal("five.wav")));
        tones.add(Gdx.audio.newSound(Gdx.files.internal("six.wav")));

        noteManager = new NoteLayers(6, 6);
        beatManager = new BeatManager(6);
        cam.setToOrtho(false, RippleGame.WIDTH, RippleGame.HEIGHT);
        rand = new Random(System.currentTimeMillis());
        sparkles = new ArrayList<>();
        for(int i = 0; i < NUM_SPARKLES; i++){
            sparkles.add(new Shimmer(rand.nextInt(RippleGame.WIDTH),
                    rand.nextInt(RippleGame.HEIGHT),
                    rand.nextInt(3) + 1,
                    rand.nextInt(Shimmer.MAX_LIFE)));
        }

        vignette = new Texture("vignette.png");
        node = new Texture("node.png");
        Gdx.input.setInputProcessor(new TouchProcessor(this));
    }


    @Override
    public void update(float dt) {
        if(touchHelper.isTouching && touchHelper.rippleReady &&
                Math.sqrt(Math.pow(touchHelper.x_pos - (RippleGame.WIDTH/2),2) +
                        Math.pow(touchHelper.y_pos-(RippleGame.HEIGHT/2),2)) < separation/2){
            ripples.add(new Ripple());
            touchHelper.rippleReady = false;
        }
        for(int i = 0; i<ripples.size(); i++){
            ripples.get(i).update(dt);
            if(ripples.get(i).strength <= 0) ripples.remove(i--);
        }

        //Play note if struck
        for(int i = 0; i < ripples.size(); i++){
            //Get layer
            int layer = (int)(((ripples.get(i).size - separation)/separation)) + 1;
            //Check if beat is hit
            //...
            if(Math.abs(ripples.get(i).size - layer*separation) < 1){
                //Layer - 1 hit
                layer--;
                if(layer < noteManager.numLayers){
                    //Play notes
                    for(int note = 0; note < noteManager.notesPerLayer; note++){
                        if(noteManager.noteIsActive(layer, note)){
                            tones.get(note).play(1f);
                            noteManager.hitNote(layer, note);
                        }
                    }
                }
            }
        }

        //Update notes and beats hit
        noteManager.update(dt);
        beatManager.update(dt);



        for(int i = 0; i < pulses.size(); i++){
            pulses.get(i).update(dt);
            if(pulses.get(i).strength <= 0) pulses.remove(i--);
        }

        pulseRate = (pulseRate > 2*Math.PI) ? pulseRate - 2*(float)Math.PI : pulseRate + dt;

        //Sparkles
        for(int i = 0; i < NUM_SPARKLES; i++)
            sparkles.get(i).update(dt);
    }

    @Override
    public void render(ShapeRenderer sr, SpriteBatch sb) {
        float radiusPulse = (float)(5f*Math.sin(pulseRate));
        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.setProjectionMatrix(cam.combined);
        sr.setAutoShapeType(true);
        sr.begin();
        sr.setColor(Color.BLUE);

        //Draw sparkles
        sr.setColor(Color.WHITE);
        sr.set(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i <NUM_SPARKLES; i++){
            sr.setColor(1f, 1f, 1f, Math.abs((float) Math.sin(Math.PI * sparkles.get(i).life / Shimmer.MAX_LIFE)));
            sr.circle(sparkles.get(i).x, sparkles.get(i).y,
                    (float) Math.abs(Math.sin(Math.PI * sparkles.get(i).life / Shimmer.MAX_LIFE)) * sparkles.get(i).size);
        }

        sr.set(ShapeRenderer.ShapeType.Line);


        //Draw guide circles
        for (int i = 0; i < noteManager.numLayers; i++) {
            sr.setColor(1f, 1f, 1f, 1f - (i * (1f/10f)));
            sr.circle(RippleGame.WIDTH / 2, RippleGame.HEIGHT / 2,
                    (separation + (i * separation)) + radiusPulse);
        }

        //draw ripples
        for (int i = 0; i < ripples.size(); i++) {
            sr.setColor(1f, 1f, 1f, ripples.get(i).strength/255f);
            sr.circle(RippleGame.WIDTH / 2, RippleGame.HEIGHT / 2, ripples.get(i).size);
        }


        //Draw selection
        for (int i = 0; i < pulses.size(); i++) {
            sr.setColor(pulses.get(i).strength/255f, pulses.get(i).strength, 1f, pulses.get(i).strength/255f);
            //sr.circle(RippleGame.WIDTH / 2, RippleGame.HEIGHT / 2, pulses.get(i).position);
        }

        sr.setColor(Color.WHITE);
        sr.circle(RippleGame.WIDTH / 2, RippleGame.HEIGHT / 2, selectedDis + radiusPulse);

        //Draw Notes
        sr.end();
        sb.begin();
        sb.setProjectionMatrix(cam.combined);
        for (int layer = 0; layer < noteManager.numLayers; layer++) {
            for (int note = 0; note < noteManager.rep.get(layer).notes.size(); note++) {
                if (noteManager.noteIsActive(layer, note) || noteManager.getNote(layer, note).isHit) {
                    float distance = (separation + layer * separation) + radiusPulse;
                    double angle = Math.PI / 3 * note;
                    double x = RippleGame.WIDTH / 2 + Math.cos(angle) * distance;
                    double y = RippleGame.HEIGHT / 2 + Math.sin(angle) * distance;
                    float size = node.getWidth()*noteManager.getNote(layer, note).scale;
                    sb.setColor(1f, 1f, 1f, 1.5f - noteManager.getNote(layer, note).scale*1.5f);
                    sb.draw(node, (int)(x - size/2), (int)(y - size/2), size, size);
                }
            }
        }
        sb.setColor(Color.WHITE);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);

        //Try placement
        if (angleFromCenter != -4) {
            sr.setColor(100f/255f, 4f/5f, 100f/255f, .75f);
            sr.circle((int) (RippleGame.WIDTH / 2 + (Math.cos(angleFromCenter) * (selectedDis + radiusPulse))),
                    (int) (RippleGame.HEIGHT / 2 + (Math.sin(angleFromCenter) * (selectedDis + radiusPulse))), RippleGame.WIDTH*.05f);
        }
        sr.end();

        //Vignette
        sb.begin();
        sb.setProjectionMatrix(cam.combined);
        sb.draw(vignette, 0, 0);
        sb.end();

    }

    @Override
    public void dispose() {
        for(int i = 0; i < tones.size(); i++){
            tones.get(i).dispose();
        }
        vignette.dispose();
        node.dispose();
    }

    public class TouchHelper {
        boolean isTouching;
        boolean rippleReady;
        boolean isDragging;
        boolean multitouch;
        int x_pos, y_pos;
        ArrayList<Integer> touchPointers;
        public TouchHelper() {
            isTouching = false;
            rippleReady = true;
            isDragging = false;
            x_pos = y_pos = -1;
            multitouch = false;
            touchPointers = new ArrayList<>();
            touchPointers.add(0);
            touchPointers.add(0);
            touchPointers.add(0);
        }

        public boolean isMultiTouch(){
            int sum = 0;
            for(Integer i : touchPointers){
                sum += i;
            }
            return sum > 1;
        }
    }

    public class Ripple{
        float size;
        int nextMarker;
        float strength;
        public Ripple(){
            size = 0;
            strength = 255;
            nextMarker = 100;
        }
        public void update(float dt){
            size+=50*dt;
            strength-=40*dt;
            if(size > nextMarker && nextMarker < separation*6) nextMarker+=separation;
        }
    }

    public class Pulse{
        float size;
        float strength;
        int position;
        public Pulse(int p){
            size = 0;
            strength = 255;
            position = p;
        }
        public void update(float dt){
            size+=10*dt;
            strength-=20*dt;
        }
    }

    private class Shimmer{
        public static final int MAX_LIFE = 100;
        public float x, y;
        public float size;
        private float life;

        public Shimmer(float x, float y, float s, float l){
            Shimmer.this.x = x;
            Shimmer.this.y = y;
            Shimmer.this.size = s;
            Shimmer.this.life = l;
        }

        public void update(float dt){
            Shimmer.this.life += 50*dt;
        }
    }
}
