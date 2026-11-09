package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.Direction;
import ru.mipt.bit.platformer.GameObject;
import ru.mipt.bit.platformer.GameWorld;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public final class Tank implements GameObject {

    private final GameWorld world;
    private final float movementDuration;
    private final int maxHealth;
    private final int bulletDamage;
    private final float bulletStepDuration;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;
    private int health;

    public Tank(GameWorld world, GridPoint2 initialCoordinates, float movementDuration) {
        this(world, initialCoordinates, movementDuration, 100, 40, 0.1f);
    }

    public Tank(GameWorld world, GridPoint2 initialCoordinates, float movementDuration,
                int health, int bulletDamage, float bulletStepDuration) {
        if (!(movementDuration > 0f) || !(bulletStepDuration > 0f) || health < 0 || bulletDamage < 0) {
            throw new IllegalArgumentException("Invalid tank or bullet settings");
        }
        this.world = world;
        this.movementDuration = movementDuration;
        this.maxHealth = health;
        this.health = health;
        this.bulletDamage = bulletDamage;
        this.bulletStepDuration = bulletStepDuration;
        coordinates = new GridPoint2(initialCoordinates);
        destinationCoordinates = new GridPoint2(initialCoordinates);
    }

    public void move(Direction direction) {
        if (!isAlive() || !hasReachedDestination()) {
            return;
        }

        this.direction = direction;
        GridPoint2 destination = direction.move(coordinates);

        if (world.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
    }

    @Override
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

    public void shoot() {
        if (isAlive() && world.contains(this)) {
            Bullet bullet = new Bullet(world, this, direction.move(coordinates), direction,
                    bulletDamage, bulletStepDuration);
            world.add(bullet);
            bullet.update(0f);
        }
    }

    @Override
    public void hit(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must not be negative");
        }
        if (isAlive()) {
            health -= damage;
            if (!isAlive()) {
                world.remove(this);
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return health >= 0;
    }

    private boolean hasReachedDestination() {
        return isEqual(movementProgress, 1f);
    }
}
