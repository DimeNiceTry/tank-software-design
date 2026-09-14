package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.Direction;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public final class Tank implements Disposable {

    private static final float MOVEMENT_DURATION = 0.4f;

    private final GameField field;
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float movementProgress = 1f;
    private float rotation;

    public Tank(GameField field, GridPoint2 initialCoordinates) {
        this.field = field;

        texture = new Texture("images/tank_blue.png");
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);

        coordinates = new GridPoint2(initialCoordinates);
        destinationCoordinates = new GridPoint2(initialCoordinates);
        field.moveBetweenTiles(
                rectangle,
                coordinates,
                destinationCoordinates,
                movementProgress
        );
    }

    public void move(Direction direction) {
        if (!hasReachedDestination()) {
            return;
        }

        rotation = direction.getRotation();
        GridPoint2 destination = direction.move(coordinates);

        if (field.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime) {
        field.moveBetweenTiles(
                rectangle,
                coordinates,
                destinationCoordinates,
                movementProgress
        );

        movementProgress = continueProgress(
                movementProgress,
                deltaTime,
                MOVEMENT_DURATION
        );

        if (hasReachedDestination()) {
            coordinates.set(destinationCoordinates);
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    private boolean hasReachedDestination() {
        return isEqual(movementProgress, 1f);
    }
}
