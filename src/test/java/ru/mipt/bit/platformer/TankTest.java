package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        GameField field = new GameField(10, 8, Collections.singletonList(tree));
        Tank tank = new Tank(field, new GridPoint2(1, 1), 0.4f);

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

    @Test
    public void movingTankReservesStartAndDestinationCells() {
        GameField field = new GameField(3, 3, Collections.emptyList());
        Tank moving = new Tank(field, new GridPoint2(1, 1), 1f);
        Tank atStart = new Tank(field, new GridPoint2(0, 1), 1f);
        Tank atDestination = new Tank(field, new GridPoint2(2, 2), 1f);
        field.add(moving);
        field.add(atStart);
        field.add(atDestination);

        moving.move(Direction.UP);
        atStart.move(Direction.RIGHT);
        atDestination.move(Direction.LEFT);

        assertFalse(field.isFree(new GridPoint2(1, 1)));
        assertFalse(field.isFree(new GridPoint2(1, 2)));
        assertEquals(new GridPoint2(0, 1), atStart.getDestinationCoordinates());
        assertEquals(new GridPoint2(2, 2), atDestination.getDestinationCoordinates());

        moving.update(1f);

        assertTrue(field.isFree(new GridPoint2(1, 1)));
        assertFalse(field.isFree(new GridPoint2(1, 2)));
        atStart.move(Direction.RIGHT);
        assertEquals(new GridPoint2(1, 1), atStart.getDestinationCoordinates());
    }

    @Test
    public void doesNotLeaveLevelBorders() {
        GameField field = new GameField(2, 2, Collections.emptyList());
        Tank bottomLeft = new Tank(field, new GridPoint2(0, 0), 0.4f);
        Tank topRight = new Tank(field, new GridPoint2(1, 1), 0.4f);

        bottomLeft.move(Direction.LEFT);
        bottomLeft.move(Direction.DOWN);
        topRight.move(Direction.RIGHT);
        topRight.move(Direction.UP);

        assertEquals(new GridPoint2(0, 0), bottomLeft.getDestinationCoordinates());
        assertEquals(new GridPoint2(1, 1), topRight.getDestinationCoordinates());
    }

    @Test
    public void usesConfiguredMovementDuration() {
        Tank tank = new Tank(new GameField(3, 3, Collections.emptyList()), new GridPoint2(1, 1), 0.8f);

        tank.move(Direction.UP);
        tank.update(0.4f);

        assertEquals(0.5f, tank.getMovementProgress(), 0.0001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonPositiveMovementDuration() {
        new Tank(new GameField(3, 3, Collections.emptyList()), new GridPoint2(1, 1), 0f);
    }

    private Tank createTankWithoutObstacles() {
        return new Tank(
                new GameField(10, 8, Collections.emptyList()),
                new GridPoint2(1, 1),
                0.4f
        );
    }
}
