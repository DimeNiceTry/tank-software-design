package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public interface GameObject extends Obstacle {

    GridPoint2 getCoordinates();

    default boolean blocksMovement() {
        return true;
    }

    default void update(float deltaTime) {
    }

    default void hit(int damage) {
    }
}
