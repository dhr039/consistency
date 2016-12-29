package com.drollgames.consistency.util;

public class Constants {

    public static final boolean ENABLE_ADS = true;

    public static final float SCENE_WIDTH = 640f;
    public static final float SCENE_HEIGHT = 1136f;

    public static final float TILE_SIZE = 175;
    public static final int TILES_PER_ROW = 3;

    public static final float SPACE = 10;

    public static final float GRID_TOTAL_SIZE = (TILE_SIZE * TILES_PER_ROW) + SPACE * TILES_PER_ROW;

    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "images/cratejump.atlas";
    public static final String TEXTURE_ATLAS_UI = "images/cratejump_ui.atlas";

    // Location of description file for skins
    public static final String SKIN_CRATEJUMP_UI = "images/cratejump-ui.json";

    // Game preferences file
    public static final String PREFERENCES = "cratejump.prefs";

}
