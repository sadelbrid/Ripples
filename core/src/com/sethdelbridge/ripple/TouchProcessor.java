package com.sethdelbridge.ripple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Seth on 1/20/16.
 */
public class TouchProcessor implements InputProcessor {
    private InGame inGame;
    public TouchProcessor(InGame ig){
        inGame = ig;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 3) inGame.touchHelper.touchPointers.set(pointer, 1);
        inGame.touchHelper.isTouching = true;
        float scaleX = (float) Gdx.graphics.getWidth()/(float)RippleGame.WIDTH;
        float scaleY = (float)Gdx.graphics.getHeight()/(float)RippleGame.HEIGHT;
        inGame.touchHelper.x_pos = (int)(screenX/scaleX);
        inGame.touchHelper.y_pos = (int)(screenY/scaleY);
        inGame.touchHelper.y_pos = RippleGame.HEIGHT - inGame.touchHelper.y_pos;


        double dis = Math.sqrt(Math.pow(inGame.touchHelper.x_pos - (RippleGame.WIDTH/2),2)
                + Math.pow(inGame.touchHelper.y_pos-(RippleGame.HEIGHT/2),2));
        inGame.touchHelper.isDragging = true;
        if(dis>=inGame.separation/2) {
            inGame.selectedDis = ((int) ((dis - inGame.separation) / inGame.separation)) * inGame.separation + inGame.separation;
            if(dis > inGame.separation*7) inGame.selectedDis = 6;
            //update angle
            inGame.angleFromCenter = Math.acos((inGame.touchHelper.x_pos - (RippleGame.WIDTH / 2)) / dis);
            if (inGame.touchHelper.y_pos < RippleGame.HEIGHT / 2) {
                inGame.angleFromCenter = Math.PI + (Math.PI - inGame.angleFromCenter);
            }

            //normalize
            double subAngle = Math.PI / 3;
            inGame.angleFromCenter += subAngle / 2; // Fix this offset
            int noteAngle = (int) (inGame.angleFromCenter / subAngle);
            inGame.angleFromCenter = noteAngle * subAngle;
        }
        //outside touch
        else if(dis > inGame.separation*7){
            inGame.selectedDis = -1;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 3) inGame.touchHelper.touchPointers.set(pointer, 0);
        float scaleX = (float)Gdx.graphics.getWidth()/(float)RippleGame.WIDTH;
        float scaleY = (float)Gdx.graphics.getHeight()/(float)RippleGame.HEIGHT;
        inGame.touchHelper.x_pos = (int)(screenX/scaleX);
        inGame.touchHelper.y_pos = (int)(screenY/scaleY);
        inGame.touchHelper.y_pos = RippleGame.HEIGHT - inGame.touchHelper.y_pos;


        double dis = Math.sqrt(Math.pow(inGame.touchHelper.x_pos - (RippleGame.WIDTH/2),2)
                + Math.pow(inGame.touchHelper.y_pos-(RippleGame.HEIGHT/2),2));
        inGame.touchHelper.isTouching = false;
        inGame.touchHelper.isDragging = false;
        inGame.touchHelper.rippleReady = true;
        if(dis <= inGame.separation*6.5 && dis > inGame.separation/2){
            //add/remove note selected
            int layer = inGame.selectedDis/inGame.separation - 1;
            double note = inGame.angleFromCenter / (Math.PI/3.0);
            if(note >=6) note = 0;
            if(inGame.noteManager.noteIsActive(layer, (int)note)) inGame.noteManager.remove(layer, (int)note);
            else inGame.noteManager.add(layer, (int)note, 1);

        }
        inGame.angleFromCenter = -4;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float scaleX = (float)Gdx.graphics.getWidth()/(float)RippleGame.WIDTH;
        float scaleY = (float)Gdx.graphics.getHeight()/(float)RippleGame.HEIGHT;
        inGame.touchHelper.x_pos = (int)(screenX/scaleX);
        inGame.touchHelper.y_pos = (int)(screenY/scaleY);
        inGame.touchHelper.y_pos = RippleGame.HEIGHT - inGame.touchHelper.y_pos;


        double dis = Math.sqrt(Math.pow(inGame.touchHelper.x_pos - (RippleGame.WIDTH/2),2)
                + Math.pow(inGame.touchHelper.y_pos-(RippleGame.HEIGHT/2),2));
        inGame.touchHelper.isDragging = true;
        if(dis>=inGame.separation/2) {
            inGame.selectedDis = ((int) ((dis - inGame.separation) / inGame.separation)) * inGame.separation + inGame.separation;
            if(dis > inGame.separation*7) inGame.selectedDis = 6;
            //update angle
            inGame.angleFromCenter = Math.acos((inGame.touchHelper.x_pos - (RippleGame.WIDTH / 2)) / dis);
            if (inGame.touchHelper.y_pos < RippleGame.HEIGHT / 2) {
                inGame.angleFromCenter = Math.PI + (Math.PI - inGame.angleFromCenter);
            }

            //normalize
            double subAngle = Math.PI / 3;
            inGame.angleFromCenter += subAngle / 2; // Fix this offset
            int noteAngle = (int) (inGame.angleFromCenter / subAngle);
            inGame.angleFromCenter = noteAngle * subAngle;
        }
        //outside touch
        else if(dis > inGame.separation*7){
            inGame.selectedDis = -1;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
