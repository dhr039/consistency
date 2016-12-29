package com.drollgames.consistency.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.drollgames.consistency.game.Assets;

import static com.drollgames.consistency.util.Constants.TILE_SIZE;

public class Tile extends Rectangle {

    private TextureRegion textureRegionTile;
    private TextureRegion textureTileMinus;
    public int digit;
    private BitmapFont font;
    public boolean isMinus = false;
    private static final float yOffset = 100;

    public Tile(Vector2 position) {
        textureRegionTile = Assets.instance.tile.tile;
        textureTileMinus = Assets.instance.tileMinus.tileMinus;
        digit = 0;
        font = Assets.instance.font;

        this.x = position.x;
        this.y = position.y;
        this.width = TILE_SIZE;
        this.height = TILE_SIZE;
    }

    public void render(SpriteBatch batch) {
        if (isMinus) {
            batch.draw(textureTileMinus, x, y, TILE_SIZE, TILE_SIZE);
        } else {
            batch.draw(textureRegionTile, x, y, TILE_SIZE, TILE_SIZE);
        }

        if (digit < 0) {

            if (digit < -9) {
                font.draw(batch, Integer.toString(digit), x + 25, y + yOffset);
            } else {
                font.draw(batch, Integer.toString(digit), x + 45, y + yOffset);
            }
        } else {
            if (digit > 9) {
                font.draw(batch, Integer.toString(digit), x + 38, y + yOffset);
            } else {
                font.draw(batch, Integer.toString(digit), x + 60, y + yOffset);
            }
        }


    }
}
