package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.Collections;

import static com.badlogic.gdx.Input.Keys.*;
import static org.junit.Assert.assertEquals;

public final class KeyboardHandlerTest {

    @Test
    public void mapsArrowsAndWasdToMovement() {
        assertMovesTo(UP, new GridPoint2(1, 2));
        assertMovesTo(W, new GridPoint2(1, 2));
        assertMovesTo(LEFT, new GridPoint2(0, 1));
        assertMovesTo(A, new GridPoint2(0, 1));
        assertMovesTo(DOWN, new GridPoint2(1, 0));
        assertMovesTo(S, new GridPoint2(1, 0));
        assertMovesTo(RIGHT, new GridPoint2(2, 1));
        assertMovesTo(D, new GridPoint2(2, 1));
    }

    @Test
    public void doesNothingWhenNoMovementKeyIsPressed() {
        Tank tank = createTank();
        KeyboardHandler handler = new KeyboardHandler(tank, key -> false);

        handler.handleInput();

        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
    }

    private void assertMovesTo(int key, GridPoint2 expectedCoordinates) {
        Tank tank = createTank();
        KeyboardHandler handler = new KeyboardHandler(
                tank,
                pressedKey -> pressedKey == key
        );

        handler.handleInput();

        assertEquals(expectedCoordinates, tank.getDestinationCoordinates());
    }

    private Tank createTank() {
        return new Tank(
                new GameField(Collections.emptyList()),
                new GridPoint2(1, 1)
        );
    }
}
