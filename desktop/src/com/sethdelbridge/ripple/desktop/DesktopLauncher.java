package com.sethdelbridge.ripple.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sethdelbridge.ripple.RippleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = RippleGame.WIDTH;
		config.height = RippleGame.HEIGHT;
		new LwjglApplication(new RippleGame(), config);
	}
}
