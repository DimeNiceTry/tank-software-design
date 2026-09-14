package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public final class GameField implements Disposable {

    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TileMovement tileMovement;
    private final Tree tree;

    public GameField(Batch batch) {
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);

        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        tree = new Tree(groundLayer, new GridPoint2(1, 3));
    }

    public boolean isFree(GridPoint2 coordinates) {
        return !tree.occupies(coordinates);
    }

    public void moveBetweenTiles(
            Rectangle rectangle,
            GridPoint2 from,
            GridPoint2 to,
            float progress
    ) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                from,
                to,
                progress
        );
    }

    public void renderLevel() {
        levelRenderer.render();
    }

    public void renderObstacles(Batch batch) {
        tree.render(batch);
    }

    @Override
    public void dispose() {
        tree.dispose();
        level.dispose();
    }
}
