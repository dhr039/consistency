package com.drollgames.consistency.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.drollgames.consistency.util.Colors;
import com.drollgames.consistency.util.Constants;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public AssetSounds sounds;
    public AssetTile tile;
    public AssetArrow assetArrow;
    public AssetTileMinus tileMinus;
    public AssetFrame frame;
    public BitmapFont font;
    public BitmapFont fontDialog;
    public FileHandle handleToJsonFile;
    public AssetStampSolved assetStampSolved;
    public AssetSemiTransparent rectHoriz;

    // singleton: prevent instantiation from other classes
    private Assets() {}

    public class AssetSemiTransparent {
        public final TextureAtlas.AtlasRegion background_darker;

        public AssetSemiTransparent(TextureAtlas atlas) {
            background_darker = atlas.findRegion("darker_background");
        }
    }

    public class AssetStampSolved {
        public final TextureAtlas.AtlasRegion stampSolved;

        public AssetStampSolved(TextureAtlas atlas) {
            stampSolved = atlas.findRegion("stamp_approved");
        }
    }

    public class AssetTile {
        public final TextureAtlas.AtlasRegion tile;

        public AssetTile(TextureAtlas atlas) {
            tile = atlas.findRegion("tile");
        }
    }

    public class AssetArrow {
        public final TextureAtlas.AtlasRegion arrow;

        public AssetArrow(TextureAtlas atlas) {
            arrow = atlas.findRegion("arrow");
        }
    }

    public class AssetTileMinus {
        public final TextureAtlas.AtlasRegion tileMinus;

        public AssetTileMinus(TextureAtlas atlas) {
            tileMinus = atlas.findRegion("tile_minus");
        }
    }

    public class AssetFrame {
        public final TextureAtlas.AtlasRegion frame;

        public AssetFrame(TextureAtlas atlas) {
            frame = atlas.findRegion("frame");
        }
    }

    public class AssetSounds {
        public final Sound cash;
        public final Sound click;
        public final Sound reload;

        public AssetSounds(AssetManager am) {
            cash = am.get("sounds/201159_kiddpark_cash_register.mp3", Sound.class);
            click = am.get("sounds/215772__otisjames__click.wav", Sound.class);
            reload = am.get("sounds/glock_slide_cock.mp3", Sound.class);
        }
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        /* set asset manager error handler */
        assetManager.setErrorListener(this);

        /* load texture atlas */
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

        /* load sounds */
        assetManager.load("sounds/201159_kiddpark_cash_register.mp3", Sound.class);
        assetManager.load("sounds/215772__otisjames__click.wav", Sound.class);
        assetManager.load("sounds/glock_slide_cock.mp3", Sound.class);

        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        sounds = new AssetSounds(assetManager);
        tile = new AssetTile(atlas);
        assetArrow = new AssetArrow(atlas);
        tileMinus = new AssetTileMinus(atlas);
        frame = new AssetFrame(atlas);
        assetStampSolved = new AssetStampSolved(atlas);

        font = new BitmapFont(Gdx.files.internal("images/gotham_rounded_bold.fnt"), Gdx.files.internal("images/gotham_rounded_bold.png"), false);
//        font.setColor(1.0f, 0.212f, 0.31f, 0.42f);
        font.setColor(Colors.CUSTOM_1);

        fontDialog = new BitmapFont(Gdx.files.internal("images/gotham_rounded_bold.fnt"), Gdx.files.internal("images/gotham_rounded_bold.png"), false);
//        fontDialog.setColor(1.0f, 0.961f, 0.961f, 0.961f);
        fontDialog.setColor(Colors.CUSTOM_3);

        handleToJsonFile = Gdx.files.internal("data/story.json");

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        rectHoriz = new AssetSemiTransparent(atlas);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        font.dispose();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", throwable);
    }

}
