package com.drollgames.consistency.util;

import com.drollgames.consistency.objects.FrameObject;

public class Utils {

    /**
     * @param array
     * @return
     */
    public static boolean allElementsTheSame(int[] array) {
        if (array.length == 0) {
            return true;
        } else {
            int first = array[0];
            for (int element : array) {
                if (element != first) {
                    return false;
                }
            }
            return true;
        }
    }

    public static Direction calculateDirection(float velocityX, float velocityY) {

        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (velocityY > 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }

    public static Direction getFrameDirectionFromTouch(int currentFramePosition, int touchPosition) {

        if(currentFramePosition == touchPosition) {
            return null;
        }

        switch (currentFramePosition) {
            case 0:
                switch (touchPosition) {
                    case 1:
                        return Direction.RIGHT;
                    case 3:
                        return Direction.DOWN;
                    default:
                        return null;
                }
            case 1:
                switch (touchPosition) {
                    case 0:
                        return Direction.LEFT;
                    case 2:
                        return Direction.RIGHT;
                    case 4:
                        return Direction.DOWN;
                    default:
                        return null;
                }
            case 2:
                switch (touchPosition) {
                    case 1:
                        return Direction.LEFT;
                    case 5:
                        return Direction.DOWN;
                    default:
                        return null;
                }
            case 3:
                switch (touchPosition) {
                    case 0:
                        return Direction.UP;
                    case 4:
                        return Direction.RIGHT;
                    case 6:
                        return Direction.DOWN;
                    default:
                        return null;
                }
            case 4:
                switch (touchPosition) {
                    case 1:
                        return Direction.UP;
                    case 7:
                        return Direction.DOWN;
                    case 3:
                        return Direction.LEFT;
                    case 5:
                        return Direction.RIGHT;
                    default:
                        return null;
                }
            case 5:
                switch (touchPosition) {
                    case 2:
                        return Direction.UP;
                    case 4:
                        return Direction.LEFT;
                    case 8:
                        return Direction.DOWN;
                    default:
                        return null;
                }
            case 6:
                switch (touchPosition) {
                    case 3:
                        return Direction.UP;
                    case 7:
                        return Direction.RIGHT;
                    default:
                        return null;
                }
            case 7:
                switch (touchPosition) {
                    case 4:
                        return Direction.UP;
                    case 6:
                        return Direction.LEFT;
                    case 8:
                        return Direction.RIGHT;
                    default:
                        return null;
                }
            case 8:
                switch (touchPosition) {
                    case 5:
                        return Direction.UP;
                    case 7:
                        return Direction.LEFT;
                    default:
                        return null;
                }
            default:
                return null;

        }

    }

    public static void moveFrame(FrameObject frameObject, Direction direction) {
        switch (direction) {
            case RIGHT:
                frameObject.onRight();
                break;
            case LEFT:
                frameObject.onLeft();
                break;
            case DOWN:
                frameObject.onDown();
                break;
            case UP:
                frameObject.onUp();
                break;
        }
    }
}
