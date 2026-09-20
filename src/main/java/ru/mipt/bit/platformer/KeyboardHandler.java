package ru.mipt.bit.platformer;

import ru.mipt.bit.platformer.util.Direction;

import java.util.function.IntPredicate;

import static com.badlogic.gdx.Input.Keys.*;

public final class KeyboardHandler {

    private final Tank tank;
    private final IntPredicate isKeyPressed;

    public KeyboardHandler(Tank tank, IntPredicate isKeyPressed) {
        this.tank = tank;
        this.isKeyPressed = isKeyPressed;
    }

    public void handleInput() {
        moveIfPressed(Direction.UP, UP, W);
        moveIfPressed(Direction.LEFT, LEFT, A);
        moveIfPressed(Direction.DOWN, DOWN, S);
        moveIfPressed(Direction.RIGHT, RIGHT, D);
    }

    private void moveIfPressed(
            Direction direction,
            int firstKey,
            int secondKey
    ) {
        if (isKeyPressed.test(firstKey)
                || isKeyPressed.test(secondKey)) {
            tank.move(direction);
        }
    }
}
