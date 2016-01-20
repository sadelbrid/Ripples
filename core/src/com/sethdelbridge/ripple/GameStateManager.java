package com.sethdelbridge.ripple;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Stack;

/**
 * Created by Seth on 8/10/15.
 */
public class GameStateManager {
    private Stack<State> states;
    public static final int MENU = 0;
    public static final int IN_GAME = 1;
    public int currentState;
    public GameStateManager(){
        states = new Stack<>();
    }

    public void pop(){
        states.peek().dispose();
        states.pop();
    }

    public void push(State s){
        states.push(s);
    }

    public State peek(){
        return states.peek();
    }

    public void setState(State s){
        states.pop();
        states.push(s);
    }

    public void update(float dt){
        updateStates(dt);
    }

    private void updateStates(float dt){
        if(states.size() > 1){
            State top = states.pop();
            updateStates(dt);
            states.push(top);
        }
        states.peek().update(dt);
    }

    public void render(ShapeRenderer sr, SpriteBatch sb){
        if(states.size() > 1){
            State top = states.pop();
            render(sr, sb);
            states.push(top);
        }
        states.peek().render(sr, sb);
    }
}
