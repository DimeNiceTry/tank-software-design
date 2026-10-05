package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public final class TreeRenderer implements EntityRenderer {

    private final Tree tree;
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeRenderer(Tree tree) {
        this.tree = tree;
        texture = new Texture("images/greenTree.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch, TileMovement tileMovement) {
        GridPoint2 coordinates = tree.getCoordinates();
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                coordinates,
                coordinates,
                1f
        );
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
