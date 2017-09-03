package com.drollgames.consistency.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Json;
import com.drollgames.consistency.CJMain;
import com.drollgames.consistency.objects.Tile;
import com.drollgames.consistency.screens.PlayScreen;
import com.drollgames.consistency.screens.transitions.ScreenTransition;
import com.drollgames.consistency.screens.transitions.ScreenTransitionSlide;
import com.drollgames.consistency.util.AudioManager;
import com.drollgames.consistency.util.Constants;
import com.drollgames.consistency.util.Direction;
import com.drollgames.consistency.util.GamePreferences;
import com.drollgames.consistency.util.Utils;

public class WorldController implements GestureDetector.GestureListener {
    private static final String TAG = "WorldController";

    private WorldRenderer worldRenderer;
    private Levels levels;
    public static int currLevelIndex;

    private PlayScreen playScreen;

    public static GameState gameState;

    public WorldController(WorldRenderer worldRenderer, PlayScreen playScreen, boolean comingFromFinishedGame) {
        this.worldRenderer = worldRenderer;
        this.playScreen = playScreen;
        parseLevelsFromJson();

        if(comingFromFinishedGame) {
            currLevelIndex = 0;
        } else {
            currLevelIndex = GamePreferences.instance.currentLevel;
        }


        gameState = GameState.PLAYING;

        loadLevel(currLevelIndex);
    }

    private boolean isLevelSolved() {
        int levelFromPrefs = GamePreferences.instance.latestPassedLevel;
        if (currLevelIndex <= levelFromPrefs) {
            return true;
        }
        return false;
    }

    public void loadLevel(int levelNumber) {

        if (WorldController.currLevelIndex == 0) {
            playScreen.imageArrow.setVisible(true);
        } else {
            playScreen.imageArrow.setVisible(false);
        }

        GamePreferences.instance.currentLevel = levelNumber;
        GamePreferences.instance.save();

        /* adjust the level label position */
        float labelY = worldRenderer.arrTiles.get(1).getY() + Constants.TILE_SIZE + 55;
        float labelXBase = worldRenderer.arrTiles.get(1).getX() - 50;
        if (currLevelIndex == 99) {
            playScreen.label_level.setPosition(labelXBase + 0, labelY);
        } else if (currLevelIndex > 8) {
            playScreen.label_level.setPosition(labelXBase + 30, labelY);
        } else {
            playScreen.label_level.setPosition(labelXBase + 70, labelY);
        }

        playScreen.label_level.setText(Integer.toString(currLevelIndex + 1));

        if (levelNumber < 1) {
            playScreen.btn_left.setVisible(false);
        } else {
            playScreen.btn_left.setVisible(true);
        }

        if (isLevelSolved()) {
            playScreen.imageLevelSolved.setVisible(true);
        } else {
            playScreen.imageLevelSolved.setVisible(false);
        }

        if (levelNumber > 98 || (!isLevelSolved())) {
            playScreen.btn_right.setVisible(false);
        } else {
            playScreen.btn_right.setVisible(true);
        }

        Item currentLevel = levels.items.get(levelNumber);

        /* load the digit numbers into the Tiles: */
        int index = 0;
        for (Integer i : currentLevel.contents) {
            worldRenderer.arrTiles.get(index).digit = i;
            index++;
        }

        /* load colors of Tile if needed: */
        index = 0;
        if (!currentLevel.isVanilla()) {
            for (String strColor : currentLevel.colors) {
                if (strColor.equalsIgnoreCase("w")) {
                    worldRenderer.arrTiles.get(index).isMinus = false;
                } else {
                    worldRenderer.arrTiles.get(index).isMinus = true;
                }
                index++;
            }
        } else {
            for (Tile tile : worldRenderer.arrTiles) {
                worldRenderer.arrTiles.get(index).isMinus = false;
                index++;
            }
        }

        putFrameToWhereItBelongs(currentLevel);

    }

    private void putFrameToWhereItBelongs(Item item) {
        /* position of the initially selected frame, taken from the json file: */
        int initX = item.initialSelected.x;
        int initY = item.initialSelected.y;
        /* position within the array: */
        int postition = 0;
        if (initX == 0) {
            if (initY == 0) {
                postition = 0;
            } else if (initY == 1) {
                postition = 3;
            } else if (initY == 2) {
                postition = 6;
            }
        } else if (initX == 1) {
            if (initY == 0) {
                postition = 1;
            } else if (initY == 1) {
                postition = 4;
            } else if (initY == 2) {
                postition = 7;
            }
        } else if (initX == 2) {
            if (initY == 0) {
                postition = 2;
            } else if (initY == 1) {
                postition = 5;
            } else if (initY == 2) {
                postition = 8;
            }
        }
        worldRenderer.frameObject.initPositionOfFrame(postition);
    }

    private boolean isLevelComplete() {
        IntArray arrOfInts = new IntArray();
        for (Tile t : worldRenderer.arrTiles) {
            arrOfInts.add(t.digit);
        }
        if (Utils.allElementsTheSame(arrOfInts.toArray())) {
            return true;
        } else {
            return false;
        }
    }

    public void reloadCurrentLevel() {
        loadLevel(currLevelIndex);
        AudioManager.instance.play(Assets.instance.sounds.reload);
    }

    /* JSON parsing */
    private void parseLevelsFromJson() {
        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        json.setElementType(Levels.class, "items", Item.class);
        levels = json.fromJson(Levels.class, Assets.instance.handleToJsonFile);
    }

    public static class InitialSelected {
        private int x;
        private int y;
    }

    public static class Item {
        private int number;
        private String mode;
        public Array<Integer> contents = new Array<Integer>();
        public Array<String> colors = new Array<String>();
        public InitialSelected initialSelected = new InitialSelected();
        public Array<String> solution = new Array<String>();

        public boolean isVanilla() {
            if (mode != null && mode.equalsIgnoreCase("vanilla")) {
                return true;
            }
            return false;
        }
    }

    public static class Levels {
        public Array<Item> items = new Array<Item>();
    }
    /* END JSON parsing */

    /* GestureListener */
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
//        switch (Utils.calculateDirection(velocityX, velocityY)) {
//            case RIGHT:
//                worldRenderer.frameObject.onRight();
//                break;
//            case LEFT:
//                worldRenderer.frameObject.onLeft();
//                break;
//            case DOWN:
//                worldRenderer.frameObject.onDown();
//                break;
//            case UP:
//                worldRenderer.frameObject.onUp();
//                break;
//        }
//
//        if (isLevelComplete()) {
//            AudioManager.instance.play(Assets.instance.sounds.cash);
//            if (GamePreferences.instance.latestPassedLevel < currLevelIndex) {
//                GamePreferences.instance.latestPassedLevel = currLevelIndex;
//                GamePreferences.instance.save();
//            }
//            currLevelIndex++;
//            loadLevel(currLevelIndex);
//        }
        return false;
    }


    @Override
    public boolean pan(float x, float y, float deltaX, float deltgetDirectionFromTouchaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }


    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }


    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        /*transform from screen to world coordinates:*/
        Vector3 vector3 = new Vector3(x, y, 0);
        worldRenderer.camera.unproject(vector3);
        Vector2 newTouch = new Vector2(vector3.x, vector3.y);

        int tilePositionIndex = 0;
        for(Rectangle rectangle:worldRenderer.arrTiles) {

            if(rectangle.contains(newTouch.x, newTouch.y)) {
                Direction direction = Utils.getFrameDirectionFromTouch(worldRenderer.frameObject.getPositionOfFrame(), tilePositionIndex);
                if(direction != null) {
                    Utils.moveFrame(worldRenderer.frameObject, direction);
                    break;
                }
            } else {
                /*Gdx.app.log(TAG, "!rectangle.contains");*/
            }

            ++tilePositionIndex;
        }

        if (isLevelComplete()) {

//            if(currLevelIndex > 9 ) {
//
//                if((currLevelIndex) % 5 == 0) {
//                    CJMain.adsRequestHandler.loadIntersitial();
//                }
//
//                if((currLevelIndex+1) % 5 == 0) {
//                    CJMain.adsRequestHandler.showIntersitial();
//                }
//            }

            gameState = GameState.START_CHANGING_LEVEL;
        }

        return false;
    }

    public void changeToNextLeve() {
        AudioManager.instance.play(Assets.instance.sounds.cash);
        if (GamePreferences.instance.latestPassedLevel < currLevelIndex) {
            GamePreferences.instance.latestPassedLevel = currLevelIndex;
            GamePreferences.instance.save();
        }
        if(currLevelIndex > 98) {
            /* game finished */
            Gdx.app.log("WorldController", "game finished");
            ScreenTransition transition = ScreenTransitionSlide.init(1.1f, ScreenTransitionSlide.RIGHT, false, Interpolation.pow3Out);
            playScreen.game.setScreen(new PlayScreen(playScreen.game, true), transition);
        } else {
            currLevelIndex++;
            ScreenTransition transition = ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.LEFT, false, Interpolation.pow3Out);
            GamePreferences.instance.currentLevel = currLevelIndex;
            GamePreferences.instance.save();
            playScreen.game.setScreen(new PlayScreen(playScreen.game, false), transition);
        }

    }

    public void update() {
        switch (gameState) {
            case CHANGE_LEVEL:
                changeToNextLeve();
                break;
        }
    }


    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }


    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

}
