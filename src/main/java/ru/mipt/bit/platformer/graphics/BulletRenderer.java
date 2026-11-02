package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Bullet;

import static ru.mipt.bit.platformer.graphics.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.drawTextureRegionUnscaled;

public final class BulletRenderer implements EntityRenderer {

    private final Bullet bullet;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public BulletRenderer(Bullet bullet, TextureRegion graphics) {
        this.bullet = bullet;
        this.graphics = graphics;
        rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch, TileMovement tileMovement) {
        GridPoint2 coordinates = bullet.getCoordinates();
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, coordinates, 1f);
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}
