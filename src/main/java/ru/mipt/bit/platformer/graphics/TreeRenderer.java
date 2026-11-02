package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tree;

import static ru.mipt.bit.platformer.graphics.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.drawTextureRegionUnscaled;

public final class TreeRenderer implements EntityRenderer {

    private final Tree tree;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeRenderer(Tree tree, TextureRegion graphics) {
        this.tree = tree;
        this.graphics = graphics;
        rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch, TileMovement tileMovement) {
        GridPoint2 coordinates = tree.getCoordinates();
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, coordinates, 1f);
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}
