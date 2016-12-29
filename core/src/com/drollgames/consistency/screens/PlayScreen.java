package com.drollgames.consistency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.drollgames.consistency.CJMain;
import com.drollgames.consistency.game.Assets;
import com.drollgames.consistency.game.WorldController;
import com.drollgames.consistency.game.WorldRenderer;
import com.drollgames.consistency.util.Constants;
import com.drollgames.consistency.util.GamePreferences;

import static com.drollgames.consistency.util.Constants.GRID_TOTAL_SIZE;
import static com.drollgames.consistency.util.Constants.SCENE_HEIGHT;
import static com.drollgames.consistency.util.Constants.TILE_SIZE;

public class PlayScreen extends AbstractGameScreen {

    private CustomInputAdapter customInputAdapter;
    private WorldRenderer worldRenderer;
    private WorldController worldController;
    private Skin skinUi;
    private Stage stage;

    private static final float BTN_WIDTH = 43;
    private static final float BTN_HEIGHT = 75;
    private static final float BTN_L_R_PADDING_BOTTOM = 50;
    public Button btn_left;
    public Button btn_right;
    public Label label_level;
    public Label label_level_100;
    public Image imageLevelSolved;
    public Image imageArrow;
    public CJMain game;

    public PlayScreen(CJMain game, boolean comingFormFinishedGame) {
        super(game);

        this.game = game;

        imageLevelSolved = new Image(Assets.instance.assetStampSolved.stampSolved);
        imageArrow = new Image(Assets.instance.assetArrow.arrow);
        customInputAdapter = new CustomInputAdapter();
        worldRenderer = new WorldRenderer();

        skinUi = new Skin(Gdx.files.internal(Constants.SKIN_CRATEJUMP_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        btn_left = new Button(skinUi, "btn_back");
        btn_right = new Button(skinUi, "btn_next_level");
        label_level = new Label("1", skinUi);
        label_level_100 = new Label("/100", skinUi);
        worldController = new WorldController(worldRenderer, PlayScreen.this, comingFormFinishedGame);

    }

    @Override
    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resizeWorld(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
        stage = new Stage();
        stage.setViewport(worldRenderer.viewport);

        stage.clear();

        imageLevelSolved.setSize(130, 130);
        imageLevelSolved.setPosition(10, SCENE_HEIGHT - (imageLevelSolved.getHeight() + 10));
        stage.addActor(imageLevelSolved);


        imageArrow.setSize(380, 380);
        float baseArrowX = worldRenderer.arrTiles.get(0).getX();
        float baseArrowY = worldRenderer.arrTiles.get(0).getY();
        float arrowX = baseArrowX + (TILE_SIZE / 4);
        float arrowY = (baseArrowY + (TILE_SIZE / 4)) - imageArrow.getHeight();
        imageArrow.setPosition(arrowX, arrowY);
        stage.addActor(imageArrow);


        stage.addActor(buildLeftBtn());
        stage.addActor(buildRightBtn());
        stage.addActor(buildRestartBtn());
        stage.addActor(buildSoundBtn());


        /* label_level position is adjusted in loadLevel() */
        label_level.setWidth(50);
        stage.addActor(label_level);

        label_level_100.setPosition(worldRenderer.arrTiles.get(1).x + 60, worldRenderer.arrTiles.get(1).getY() + Constants.TILE_SIZE + 55);
        stage.addActor(label_level_100);

//        stage.setDebugAll(true);
    }

    @Override
    public void hide() {
        super.hide();
        stage.dispose();
        worldRenderer.dispose();
        skinUi.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {
        worldRenderer.viewport.apply();
    }

    @Override
    public void render(float deltaTime) {
        worldController.update();
        worldRenderer.renderWorld();
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(new GestureDetector(worldController));
        im.addProcessor(customInputAdapter);
        im.addProcessor(stage);
        return im;
    }

    private class CustomInputAdapter extends InputAdapter {
        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                exitScreen();
            }
            return false;
        }
    }

    private void exitScreen() {
        // ScreenTransition transition = ScreenTransitionFade.init(0.75f);
        // game.setScreen(new FirstMenuScreen(game), transition);

        Gdx.app.exit();
    }


    private Button buildSoundBtn() {

        final Button btn_sound = new Button(skinUi, "btn_sound");
        btn_sound.setChecked(!GamePreferences.instance.sound);
        btn_sound.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (btn_sound.isChecked()) {
                    GamePreferences.instance.sound = false;
                } else {
                    GamePreferences.instance.sound = true;
                }
                GamePreferences.instance.save();
            }

        });

        btn_sound.setSize(130, 130);

        float boardX = 70;
        float boardy = (SCENE_HEIGHT * 0.5f) - ( (GRID_TOTAL_SIZE * 0.5f) + btn_sound.getHeight() + 80);
        btn_sound.setPosition(boardX, boardy);

        return btn_sound;

    }

    private Button buildRestartBtn() {

        final Button btn_reload_levels = new Button(skinUi, "btn_reload_levels");

        btn_reload_levels.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                worldController.reloadCurrentLevel();
            }

        });

        btn_reload_levels.setSize(130,130);

        float boardX = 430;
        float boardy = (SCENE_HEIGHT * 0.5f) - ( (GRID_TOTAL_SIZE * 0.5f) + btn_reload_levels.getHeight() + 80);
        btn_reload_levels.setPosition(boardX, boardy);

        return btn_reload_levels;

    }

    private Button buildLeftBtn() {

        btn_left.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                worldController.currLevelIndex--;
                worldController.loadLevel(worldController.currLevelIndex);
                if (worldController.currLevelIndex < 1) {
                    btn_left.setVisible(false);
                }
            }

        });
        btn_left.setSize(BTN_WIDTH, BTN_HEIGHT);

        float rightTopTileX = worldRenderer.arrTiles.get(0).getX();
        float rightTopTileY = worldRenderer.arrTiles.get(0).getY();

        float boardX = rightTopTileX;
        float boardy = rightTopTileY + TILE_SIZE + BTN_L_R_PADDING_BOTTOM;
        btn_left.setPosition(boardX, boardy);

        if (worldController.currLevelIndex < 1) {
            btn_left.setVisible(false);
        }

        return btn_left;
    }

    private Button buildRightBtn() {

        btn_right.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                worldController.currLevelIndex++;
                worldController.loadLevel(worldController.currLevelIndex);
                btn_left.setVisible(true);
                if (worldController.currLevelIndex > 99) {
                    btn_right.setVisible(false);
                }
            }

        });
        btn_right.setSize(BTN_WIDTH, BTN_HEIGHT);

        float rightTopTileX = worldRenderer.arrTiles.get(2).getX();
        float rightTopTileY = worldRenderer.arrTiles.get(2).getY();

        float boardX = (rightTopTileX + TILE_SIZE) - BTN_WIDTH;
        float boardY = rightTopTileY + TILE_SIZE + BTN_L_R_PADDING_BOTTOM;
        btn_right.setPosition(boardX, boardY);

        if (worldController.currLevelIndex > 99) {
            btn_right.setVisible(false);
        }

        return btn_right;
    }

}
