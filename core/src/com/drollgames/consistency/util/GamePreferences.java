package com.drollgames.consistency.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class GamePreferences {
    private static final String CURR_LEVEL = "curr_level";
    private static final String LATEST_PASSED_LEVEL = "latest_level";

    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public int currentLevel;
    public int latestPassedLevel;

    private Preferences prefs;

    // singleton: prevent instantiation from other classes
    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load() {
        sound = prefs.getBoolean("sound", true);
        currentLevel = prefs.getInteger(CURR_LEVEL, 0);
        latestPassedLevel = prefs.getInteger(LATEST_PASSED_LEVEL, -1);
//        latestPassedLevel = 98;
    }

    public void save() {
        prefs.putBoolean("sound", sound);
        prefs.putInteger(CURR_LEVEL, currentLevel);
        prefs.putInteger(LATEST_PASSED_LEVEL, latestPassedLevel);
        prefs.flush();
    }

}
