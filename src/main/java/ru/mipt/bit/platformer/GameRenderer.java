package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public final class GameRenderer implements Disposable {

    private final Tank tank;
    private final Batch batch;
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TileMovement tileMovement;

    private final Texture tankTexture;
    private final TextureRegion tankGraphics;
    private final Rectangle tankRectangle;

    private final Texture treeTexture;
    private final TextureRegion treeGraphics;
    private final Rectangle treeRectangle;

    public GameRenderer(Tank tank, Tree tree) {
        this.tank = tank;

        batch = new SpriteBatch();
        level = new TmxMapLoader().load("level.tmx");

        TiledMapTileLayer groundLayer = getSingleLayer(level);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        tileMovement = new TileMovement(
                groundLayer,
                Interpolation.smooth
        );

        tankTexture = new Texture("images/tank_blue.png");
        tankGraphics = new TextureRegion(tankTexture);
        tankRectangle = createBoundingRectangle(tankGraphics);

        treeTexture = new Texture("images/greenTree.png");
        treeGraphics = new TextureRegion(treeTexture);
        treeRectangle = createBoundingRectangle(treeGraphics);
        moveRectangleAtTileCenter(
                groundLayer,
                treeRectangle,
                tree.getCoordinates()
        );
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        tileMovement.moveRectangleBetweenTileCenters(
                tankRectangle,
                tank.getCoordinates(),
                tank.getDestinationCoordinates(),
                tank.getMovementProgress()
        );

        levelRenderer.render();

        batch.begin();
        drawTextureRegionUnscaled(
                batch,
                tankGraphics,
                tankRectangle,
                tank.getDirection().getRotation()
        );
        drawTextureRegionUnscaled(
                batch,
                treeGraphics,
                treeRectangle,
                0f
        );
        batch.end();
    }

    @Override
    public void dispose() {
        tankTexture.dispose();
        treeTexture.dispose();
        level.dispose();
        batch.dispose();
    }
}
