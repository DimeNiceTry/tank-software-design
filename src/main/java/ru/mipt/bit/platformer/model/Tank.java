package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.CellAvailability;
import ru.mipt.bit.platformer.Direction;
import ru.mipt.bit.platformer.Obstacle;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public final class Tank implements Obstacle {

    private final CellAvailability cellAvailability;
    private final float movementDuration;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;

    public Tank(CellAvailability cellAvailability, GridPoint2 initialCoordinates, float movementDuration) {
        if (!(movementDuration > 0f)) {
            throw new IllegalArgumentException("Movement duration must be positive");
        }
        this.cellAvailability = cellAvailability;
        this.movementDuration = movementDuration;
        coordinates = new GridPoint2(initialCoordinates);
        destinationCoordinates = new GridPoint2(initialCoordinates);
    }

    public void move(Direction direction) {
        if (!hasReachedDestination()) {
            return;
        }

        this.direction = direction;
        GridPoint2 destination = direction.move(coordinates);

        if (cellAvailability.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime) {
        movementProgress = clamp(
                movementProgress + deltaTime / movementDuration,
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

    @Override
    public boolean occupies(GridPoint2 cell) {
        return coordinates.equals(cell) || destinationCoordinates.equals(cell);
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
