package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.mipt.bit.platformer.graphics.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.drawTextureRegionUnscaled;

public final class TreeRenderer implements EntityRenderer {

    private final List<Tree> trees;
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeRenderer(Collection<Tree> trees) {
        this.trees = new ArrayList<>(trees);
        texture = new Texture("images/greenTree.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch, TileMovement tileMovement) {
        for (Tree tree : trees) {
            GridPoint2 coordinates = tree.getCoordinates();
            tileMovement.moveRectangleBetweenTileCenters(
                    rectangle,
                    coordinates,
                    coordinates,
                    1f
            );
            drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
