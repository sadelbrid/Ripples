package com.sethdelbridge.ripple;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RippleGame extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

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
	public void dispose() {
		super.dispose();
		gsm.pop();
		sb.dispose();
		sr.dispose();
	}
}
