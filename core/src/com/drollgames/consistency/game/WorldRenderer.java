package com.drollgames.consistency.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drollgames.consistency.objects.FrameObject;
import com.drollgames.consistency.objects.Tile;
import com.drollgames.consistency.util.Colors;

import static com.drollgames.consistency.util.Constants.GRID_TOTAL_SIZE;
import static com.drollgames.consistency.util.Constants.SCENE_HEIGHT;
import static com.drollgames.consistency.util.Constants.SCENE_WIDTH;
import static com.drollgames.consistency.util.Constants.SPACE;
import static com.drollgames.consistency.util.Constants.TILES_PER_ROW;
import static com.drollgames.consistency.util.Constants.TILE_SIZE;

/*
 * 
 * Mapping To And From Camera to Screen Coordinates: http://www.gamefromscratch.com/post/2014/12/09/LibGDX-Tutorial-Part-17-Viewports.aspx
 */
public class WorldRenderer implements Disposable {

    public Viewport viewport;
    private SpriteBatch batch;
    public OrthographicCamera camera;
    public FrameObject frameObject;
    public Array<Tile> arrTiles = new Array<Tile>();

    public WorldRenderer() {
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0);

        batch = new SpriteBatch();

        populateTilesGrid();
        frameObject = new FrameObject(this);
        // frameObject.position = arrTiles.get(positionOfFrame).position;
    }

    public void resizeWorld(int width, int height) {
        viewport.update(width, height, false);
    }

    public void renderWorld() {

        Color colorBackground = Colors.CUSTOM_1;
        Gdx.gl.glClearColor(colorBackground.r, colorBackground.g, colorBackground.b, colorBackground.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Tile v : arrTiles) {
            v.render(batch);
        }

        frameObject.render(batch);

        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
    }


    private void populateTilesGrid() {

        arrTiles.clear();

        float s = TILE_SIZE;
        float s2 = (s * 0.5f) - (TILE_SIZE * 0.5f);

        float boardX = (SCENE_WIDTH * 0.5f) - (GRID_TOTAL_SIZE * 0.5f);
        float boardy = (SCENE_HEIGHT * 0.5f) - (GRID_TOTAL_SIZE * 0.5f);

        for (int x = 0; x < TILES_PER_ROW; ++x) {
            for (int y = 0; y < TILES_PER_ROW; ++y) {

                float xbase = boardX + (TILE_SIZE + SPACE) * x - s2;
                float ybase = boardy + (TILE_SIZE + SPACE) * y - s2;

                Tile tile = new Tile(new Vector2(xbase, ybase));
                arrTiles.add(tile);
            }
        }

        /* rearrange the array to be according to the incoming JSON format */
        Array<Tile> arrTemp = new Array<Tile>();
        arrTemp.add(arrTiles.get(2));
        arrTemp.add(arrTiles.get(5));
        arrTemp.add(arrTiles.get(8));
        arrTemp.add(arrTiles.get(1));
        arrTemp.add(arrTiles.get(4));
        arrTemp.add(arrTiles.get(7));
        arrTemp.add(arrTiles.get(0));
        arrTemp.add(arrTiles.get(3));
        arrTemp.add(arrTiles.get(6));

        arrTiles = new Array<Tile>(arrTemp);

    }

}
