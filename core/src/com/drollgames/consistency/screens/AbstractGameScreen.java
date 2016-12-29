package com.drollgames.consistency.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.drollgames.consistency.CJMain;
import com.drollgames.consistency.game.WorldRenderer;

public abstract class AbstractGameScreen implements Screen {

    // public DirectedGame game;
    public CJMain game;


    // protected I18NBundle myBundle;

    public AbstractGameScreen(CJMain game) {
        this.game = game;
        // skinUi = new Skin(Gdx.files.internal(Constants.SKIN_CRATEJUMP_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        //
        // /* load the language bundle*/
        // FileHandle baseFileHandle = Gdx.files.internal("i18n/strings");
        // Locale locale = Locale.getDefault();
        // myBundle = I18NBundle.createBundle(baseFileHandle, locale);
    }

    public abstract void render(float deltaTime);

    public abstract void resize(int width, int height);

    public abstract void show();

    public void hide() {
        // skinUi.dispose();
    }

    public abstract void pause();

    public abstract InputProcessor getInputProcessor();

    public void resume() {}

    public void dispose() {}

    public abstract WorldRenderer getWorldRenderer();

}
