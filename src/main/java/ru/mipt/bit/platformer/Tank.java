package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public final class Tank {

    private static final float MOVEMENT_DURATION = 0.4f;

    private final GameField field;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;

    public Tank(GameField field, GridPoint2 initialCoordinates) {
        this.field = field;
        coordinates = new GridPoint2(initialCoordinates);
        destinationCoordinates = new GridPoint2(initialCoordinates);
    }

    public void move(Direction direction) {
        if (!hasReachedDestination()) {
            return;
        }

        this.direction = direction;
        GridPoint2 destination = direction.move(coordinates);

        if (field.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime) {
        movementProgress = clamp(
                movementProgress + deltaTime / MOVEMENT_DURATION,
                0f,
                1f
        );

        if (hasReachedDestination()) {
            coordinates.set(destinationCoordinates);
        }
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public Direction getDirection() {
        return direction;
    }

    private boolean hasReachedDestination() {
        return isEqual(movementProgress, 1f);
    }
}
