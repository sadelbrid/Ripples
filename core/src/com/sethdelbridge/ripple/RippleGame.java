package com.sethdelbridge.ripple;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;

public class RippleGame extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static float ASPECT_RATIO = (float)WIDTH/(float)HEIGHT;
	public static com.badlogic.gdx.math.Rectangle viewport;

	SpriteBatch sb;
	ShapeRenderer sr;
	GameStateManager gsm;

	@Override
	public void create () {
		sb = new SpriteBatch();
		gsm = new GameStateManager();
		sr = new ShapeRenderer();
		gsm.push(new InGame(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(sr, sb);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float newRatio = (float)width/(float)height;
		Gdx.app.log("Viewport issue tracking", "New ratio: " + newRatio);
		float scale;
		Vector2 crop = new Vector2(0f, 0f);

		if(newRatio < ASPECT_RATIO){
			Gdx.app.log("", "new ratio less");
			scale = (float)width/(float)WIDTH;
			crop.y = (height - HEIGHT*scale)/2f;
		}
		else if(newRatio > ASPECT_RATIO){
			Gdx.app.log("", "new ratio more");
			scale = (float)height/(float)HEIGHT;
			crop.x = (width - WIDTH*scale)/2f;
		}
		else{
			scale = (float)width/(float)WIDTH;
		}

		float w = (float)WIDTH*scale;
		float h = (float)HEIGHT*scale;
		viewport = new com.badlogic.gdx.math.Rectangle(crop.x, crop.y, w, h);
		Gdx.app.log("Viewport issue tracking", "Scale: " + scale);
		Gdx.app.log("Viewport issue tracking", "Viewport: " + viewport.toString());
		gsm.peek().uponResize();
	}

	@Override
	public void dispose() {
		super.dispose();
		gsm.pop();
		sb.dispose();
		sr.dispose();
	}
}
