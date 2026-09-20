package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class DirectionTest {

    @Test
    public void movesCoordinatesInEveryDirection() {
        GridPoint2 coordinates = new GridPoint2(2, 2);

        assertEquals(new GridPoint2(2, 3), Direction.UP.move(coordinates));
        assertEquals(new GridPoint2(1, 2), Direction.LEFT.move(coordinates));
        assertEquals(new GridPoint2(2, 1), Direction.DOWN.move(coordinates));
        assertEquals(new GridPoint2(3, 2), Direction.RIGHT.move(coordinates));
        assertEquals(new GridPoint2(2, 2), coordinates);
    }

    @Test
    public void providesRotationForEveryDirection() {
        assertEquals(90f, Direction.UP.getRotation(), 0f);
        assertEquals(180f, Direction.LEFT.getRotation(), 0f);
        assertEquals(-90f, Direction.DOWN.getRotation(), 0f);
        assertEquals(0f, Direction.RIGHT.getRotation(), 0f);
    }
}
