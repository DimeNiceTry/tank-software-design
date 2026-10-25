package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public final class MoveActionTest {

    @Test
    public void delegatesMovementToTank() {
        Tank tank = new Tank(new GameField(3, 3, Collections.emptyList()), new GridPoint2(1, 1), 0.4f);

        new MoveAction(tank, Direction.UP).execute();

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
    }
}
