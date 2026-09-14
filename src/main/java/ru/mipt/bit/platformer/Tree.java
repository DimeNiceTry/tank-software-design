package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public final class Tree implements Disposable {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;

    public Tree(TiledMapTileLayer tileLayer, GridPoint2 coordinates) {
        texture = new Texture("images/greenTree.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);
        this.coordinates = new GridPoint2(coordinates);
        moveRectangleAtTileCenter(tileLayer, rectangle, coordinates);
    }

    public boolean occupies(GridPoint2 coordinates) {
        return this.coordinates.equals(coordinates);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
