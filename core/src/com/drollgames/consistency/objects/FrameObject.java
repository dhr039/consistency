package com.drollgames.consistency.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.drollgames.consistency.game.Assets;
import com.drollgames.consistency.game.GameState;
import com.drollgames.consistency.game.WorldController;
import com.drollgames.consistency.game.WorldRenderer;
import com.drollgames.consistency.util.AudioManager;
import com.drollgames.consistency.util.Direction;

import static com.drollgames.consistency.util.Constants.TILE_SIZE;

public class FrameObject {

    private int positionOfFrame = 0;

    /* avoid ugly move effect when using exact frame position: */
    private final static float MOVE_ADJUSTMENT = 5f;

    private Vector2 position;
    private Vector2 dimension;
    private TextureRegion textureRegionFrame;
    private WorldRenderer worldRenderer;

    private boolean isMoving = false;
    private Vector2 targetPosition;
    private Vector2 velocity = new Vector2(1400f, 1400f);

    private Direction direction;

    public FrameObject(WorldRenderer worldRenderer) {
        dimension = new Vector2(TILE_SIZE, TILE_SIZE);
        textureRegionFrame = Assets.instance.frame.frame;
        this.worldRenderer = worldRenderer;

        Rectangle rectangle = worldRenderer.arrTiles.get(positionOfFrame);
        position = new Vector2(rectangle.x, rectangle.y);

    }

    public void initPositionOfFrame(int positionOfFrame) {
        this.positionOfFrame = positionOfFrame;
        Rectangle rectangle = worldRenderer.arrTiles.get(positionOfFrame);
        position.set(rectangle.x, rectangle.y);

        isMoving = false;
    }

    public int getPositionOfFrame() {
        return positionOfFrame;
    }

    private void checkGameState() {
        switch (WorldController.gameState) {
            case START_CHANGING_LEVEL:
                WorldController.gameState = GameState.CHANGE_LEVEL;
                break;
        }
    }


    public void render(SpriteBatch batch) {
        batch.draw(textureRegionFrame, position.x, position.y, dimension.x, dimension.y);

        if(isMoving) {
            float dt = Gdx.graphics.getDeltaTime();

//            Gdx.app.log("DHR", "is moving " + position.y + "  /  " + targetPosition.y);

            switch (direction) {
                case RIGHT:
                    if(position.x >= (targetPosition.x - MOVE_ADJUSTMENT) ) {
                        position.x = targetPosition.x;
                        position.y = targetPosition.y;
                        isMoving = false;
                        AudioManager.instance.play(Assets.instance.sounds.click);
                        checkGameState();
                        return;
                    }
                    break;
                case LEFT:
                    if(position.x <= (targetPosition.x + MOVE_ADJUSTMENT) ) {
                        position.x = targetPosition.x;
                        position.y = targetPosition.y;
                        isMoving = false;
                        AudioManager.instance.play(Assets.instance.sounds.click);
                        checkGameState();
                        return;
                    }
                    break;
                case DOWN:
                    if(position.y <= (targetPosition.y + MOVE_ADJUSTMENT) ) {
                        position.x = targetPosition.x;
                        position.y = targetPosition.y;
                        isMoving = false;
                        AudioManager.instance.play(Assets.instance.sounds.click);
                        checkGameState();
                        return;
                    }
                    break;
                case UP:
                    if(position.y >= (targetPosition.y - MOVE_ADJUSTMENT) ) {
                        position.x = targetPosition.x;
                        position.y = targetPosition.y;
                        isMoving = false;
                        AudioManager.instance.play(Assets.instance.sounds.click);
                        checkGameState();
                        return;
                    }
                    break;
            }


            switch (direction) {
                case RIGHT:
                    position.x += velocity.x * dt;
                    break;
                case LEFT:
                    position.x -= velocity.x * dt;
                    break;
                case DOWN:
                    position.y -= velocity.y * dt;
                    break;
                case UP:
                    position.y += velocity.y * dt;
                    break;
            }

        }
    }

    private void startMovingTo(int nextFramePosValue) {
        isMoving = true;
        targetPosition = new Vector2(worldRenderer.arrTiles.get(nextFramePosValue).x, worldRenderer.arrTiles.get(nextFramePosValue).y);

        if (worldRenderer.arrTiles.get(nextFramePosValue).isMinus) {
            worldRenderer.arrTiles.get(nextFramePosValue).digit = worldRenderer.arrTiles.get(nextFramePosValue).digit - 1;
        } else {
            worldRenderer.arrTiles.get(nextFramePosValue).digit = worldRenderer.arrTiles.get(nextFramePosValue).digit + 1;
        }
        positionOfFrame = nextFramePosValue;
    }



    public void onUp() {
        direction = Direction.UP;

        switch (positionOfFrame) {
            case 6:
                startMovingTo(3);
                break;
            case 3:
                startMovingTo(0);
                break;
            case 7:
                startMovingTo(4);
                break;
            case 4:
                startMovingTo(1);
                break;
            case 8:
                startMovingTo(5);
                break;
            case 5:
                startMovingTo(2);
                break;
            default:
                break;

        }
    }

    public void onRight() {
        direction = Direction.RIGHT;

        switch (positionOfFrame) {
            case 0:
                startMovingTo(1);
                break;
            case 3:
                startMovingTo(4);
                break;
            case 6:
                startMovingTo(7);
                break;
            case 1:
                startMovingTo(2);
                break;
            case 4:
                startMovingTo(5);
                break;
            case 7:
                startMovingTo(8);
                break;
            default:
                break;
        }
    }

    public void onLeft() {
        direction = Direction.LEFT;

        switch (positionOfFrame) {
            case 2:
                startMovingTo(1);
                break;
            case 5:
                startMovingTo(4);
                break;
            case 8:
                startMovingTo(7);
                break;
            case 1:
                startMovingTo(0);
                break;
            case 4:
                startMovingTo(3);
                break;
            case 7:
                startMovingTo(6);
                break;
            default:
                break;
        }
    }

    public void onDown() {
        direction = Direction.DOWN;

        switch (positionOfFrame) {
            case 0:
                startMovingTo(3);
                break;
            case 3:
                startMovingTo(6);
                break;
            case 1:
                startMovingTo(4);
                break;
            case 4:
                startMovingTo(7);
                break;
            case 2:
                startMovingTo(5);
                break;
            case 5:
                startMovingTo(8);
                break;
            default:
                break;
        }
    }

}
