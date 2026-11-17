package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.Direction;
import ru.mipt.bit.platformer.GameObject;
import ru.mipt.bit.platformer.GameWorld;

public final class Bullet implements GameObject {

    private final GameWorld world;
    private final GameObject owner;
    private final GridPoint2 coordinates;
    private final Direction direction;
    private final int damage;
    private final float stepDuration;
    private float elapsed;

    public Bullet(GameWorld world, GameObject owner, GridPoint2 coordinates, Direction direction,
                  int damage, float stepDuration) {
        if (damage < 0 || !(stepDuration > 0f)) {
            throw new IllegalArgumentException("Invalid bullet settings");
        }
        this.world = world;
        this.owner = owner;
        this.coordinates = new GridPoint2(coordinates);
        this.direction = direction;
        this.damage = damage;
        this.stepDuration = stepDuration;
    }

    @Override
    public void update(float deltaTime) {
        if (!world.contains(this) || collide()) {
            return;
        }
        elapsed += deltaTime;
        while (elapsed >= stepDuration) {
            elapsed -= stepDuration;
            coordinates.set(direction.move(coordinates));
            if (collide()) {
                return;
            }
        }
    }

    private boolean collide() {
        if (!world.isInside(coordinates)) {
            world.remove(this);
            return true;
        }
        for (GameObject object : world.objectsAt(coordinates)) {
            if (object != this && object != owner) {
                object.hit(damage);
                world.remove(this);
                return true;
            }
        }
        return false;
    }

    @Override
    public void hit(int damage) {
        world.remove(this);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    @Override
    public boolean occupies(GridPoint2 cell) {
        return coordinates.equals(cell);
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }
}
