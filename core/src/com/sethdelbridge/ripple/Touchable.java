package com.sethdelbridge.ripple;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Seth on 1/25/16.
 */
public class Touchable {
    /*
        x and y being the center of the touchable
     */
    private Vector2 position;
    private Vector2 anchor;
    protected Texture texture;
    private String id;
    public  Touchable(int x, int y, float ax,float ay, Texture t, String id){
        position = new Vector2(x, y);
        anchor = new Vector2(ax, ay);
        texture = t;
        this.id = id;
    }

    /*
        Returns whether touch point (x,y) lies within the touchable.
        (x, y) represents a point on the devices screen, not in LibGDX screenspace
     */
    public boolean isTouched(int x, int y){
        //First convert to LibGDX screen;pace
        float scaleX = (float)Gdx.graphics.getWidth()/(float)RippleGame.WIDTH;
        float scaleY = (float)Gdx.graphics.getHeight()/(float)RippleGame.HEIGHT;
        x /= scaleX;
        y /= scaleY;
        //Flip y
        y = RippleGame.HEIGHT - y;
        //Gdx.app.log("", "(" + x + ", " + y + ")");5
        //Gdx.app.log("position", "(" + x + ", " + y + ")");
        //Gdx.app.log("screen ration", )
        return (x > position.x - texture.getWidth() / 2
            && x < position.x + texture.getWidth() / 2
            && y > position.y - texture.getHeight() / 2
            && y < position.y + texture.getHeight() / 2);
    }

    public void dispose(){
        if(this.texture != null)
            this.texture.dispose();
    }

    public String getId(){
        return id;
    }

    public Vector2 getPosition(){
        return position;
    }

    public Vector2 getAnchor() {
        return anchor;
    }
}
