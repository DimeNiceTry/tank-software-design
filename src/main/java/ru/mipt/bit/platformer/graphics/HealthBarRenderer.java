package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.function.BooleanSupplier;

import static com.badlogic.gdx.math.MathUtils.clamp;

public final class HealthBarRenderer implements EntityRenderer {

    private final EntityRenderer wrapped;
    private final Tank tank;
    private final TextureRegion pixel;
    private final BooleanSupplier visible;
    private final Rectangle bar = new Rectangle(0f, 0f, 64f, 6f);

    public HealthBarRenderer(EntityRenderer wrapped, Tank tank, TextureRegion pixel,
                             BooleanSupplier visible) {
        this.wrapped = wrapped;
        this.tank = tank;
        this.pixel = pixel;
        this.visible = visible;
    }

    @Override
    public void render(Batch batch, TileMovement tileMovement) {
        wrapped.render(batch, tileMovement);
        if (!visible.getAsBoolean()) {
            return;
        }

        tileMovement.moveRectangleBetweenTileCenters(bar, tank.getCoordinates(),
                tank.getDestinationCoordinates(), tank.getMovementProgress());
        bar.y += 50f;
        float fraction = tank.getMaxHealth() == 0 ? 0f
                : clamp((float) tank.getHealth() / tank.getMaxHealth(), 0f, 1f);
        float previousColor = batch.getPackedColor();
        batch.setColor(Color.DARK_GRAY);
        batch.draw(pixel, bar.x, bar.y, bar.width, bar.height);
        batch.setColor(Color.GREEN);
        batch.draw(pixel, bar.x, bar.y, bar.width * fraction, bar.height);
        batch.setPackedColor(previousColor);
    }
}
