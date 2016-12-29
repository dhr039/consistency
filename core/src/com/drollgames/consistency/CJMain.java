package com.drollgames.consistency;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;
import com.drollgames.consistency.game.Assets;
import com.drollgames.consistency.screens.DirectedGame;
import com.drollgames.consistency.screens.PlayScreen;
import com.drollgames.consistency.util.GamePreferences;

/* DirectedGame implements ApplicationListener */
public class CJMain extends DirectedGame {

	public static IActivityRequestHandler adsRequestHandler = null;

	public CJMain(IActivityRequestHandler handler) {

		if (handler == null) {
			adsRequestHandler = new IActivityRequestHandler() {
				@Override
				public void showIntersitial() {
					Gdx.app.log("CJMain", "handler was null, showIntersitial() will do nothing");
				}

			};
		} else {
			adsRequestHandler = handler;
		}
	}

	@Override
	public void create() {
		// TODO: change to LOG_NONE before publishing
		Gdx.app.setLogLevel(Application.LOG_NONE);
//        Gdx.app.setLogLevel(Application.LOG_DEBUG);

		GamePreferences.instance.load();
		// Load assets
		Assets.instance.init(new AssetManager());
		PlayScreen firstScreen = new PlayScreen(this, false);
		setScreen(firstScreen, null);

		Gdx.app.log("CJMain", "create()");

	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.instance.dispose();
	}
}


