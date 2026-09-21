package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.util.Direction;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public final class TankTest {

    @Test
    public void movesToFreeNeighbouringCell() {
        Tank tank = createTankWithoutObstacles();

        tank.move(Direction.UP);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress(), 0f);

        tank.update(0.2f);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(0.5f, tank.getMovementProgress(), 0.0001f);

        tank.update(0.2f);
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress(), 0f);
    }

    @Test
    public void turnsButDoesNotMoveIntoObstacle() {
        Tree tree = new Tree(new GridPoint2(1, 2));
        GameField field = new GameField(Collections.singletonList(tree));
        Tank tank = new Tank(field, new GridPoint2(1, 1));

        tank.move(Direction.UP);

        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
    }

    @Test
    public void ignoresNewDirectionWhileMoving() {
        Tank tank = createTankWithoutObstacles();

        tank.move(Direction.UP);
        tank.move(Direction.RIGHT);

        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
    }

    private Tank createTankWithoutObstacles() {
        return new Tank(
                new GameField(Collections.emptyList()),
                new GridPoint2(1, 1)
        );
    }
}
