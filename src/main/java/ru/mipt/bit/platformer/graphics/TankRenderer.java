package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

import static ru.mipt.bit.platformer.graphics.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.drawTextureRegionUnscaled;

public final class TankRenderer implements EntityRenderer {

    private final Tank tank;
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TankRenderer(Tank tank) {
        this.tank = tank;
        texture = new Texture("images/tank_blue.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                tank.getCoordinates(),
                tank.getDestinationCoordinates(),
                tank.getMovementProgress()
        );
        drawTextureRegionUnscaled(
                batch,
                graphics,
                rectangle,
                tank.getDirection().getRotation()
        );
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
