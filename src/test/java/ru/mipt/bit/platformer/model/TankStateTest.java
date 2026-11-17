package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.Direction;
import ru.mipt.bit.platformer.GameField;
import ru.mipt.bit.platformer.ShootAction;

import java.util.Collections;

import static org.junit.Assert.*;

public final class TankStateTest {

    private final GameField field = new GameField(5, 3, Collections.emptyList());

    @Test
    public void healthThresholdsControlMovementAndShooting() {
        assertBehavior(100, 100, 1f, true);
        assertBehavior(100, 71, 1f, true);
        assertBehavior(100, 70, 2f, true);
        assertBehavior(100, 15, 2f, true);
        assertBehavior(100, 14, 3f, false);
        assertBehavior(100, 0, 3f, false);
        assertBehavior(0, 0, 3f, false);
    }

    @Test
    public void thresholdsAreRelativeToInitialHealth() {
        assertBehavior(80, 57, 1f, true);
        assertBehavior(80, 56, 2f, true);
        assertBehavior(80, 12, 2f, true);
        assertBehavior(80, 11, 3f, false);
    }

    @Test
    public void damageChangesSpeedDuringMovementWithoutResettingProgress() {
        Tank tank = tank();
        tank.move(Direction.RIGHT);
        tank.update(0.2f);

        tank.hit(30);
        tank.update(0.2f);
        assertEquals(0.3f, tank.getMovementProgress(), 0.0001f);

        tank.hit(56);
        tank.update(0.3f);
        assertEquals(0.4f, tank.getMovementProgress(), 0.0001f);
        assertFalse(field.isFree(new GridPoint2(1, 1)));
        assertFalse(field.isFree(new GridPoint2(2, 1)));

        tank.update(1.8f);
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertTrue(field.isFree(new GridPoint2(1, 1)));
    }

    @Test
    public void queuedShotUsesStateAtExecutionTime() {
        Tank tank = tank();
        ShootAction shot = new ShootAction(tank);

        tank.hit(86);
        shot.execute();

        assertTrue(field.objectsAt(new GridPoint2(2, 1)).isEmpty());
    }

    @Test
    public void additionalStateCanChangeBehaviorWithoutChangingTank() {
        Tank tank = tank();
        tank.hit(86);
        tank.changeState(new TankState() {
            public void update(Tank context, float deltaTime) { context.advanceMovement(deltaTime * 2f); }
            public void shoot(Tank context) { context.fireBullet(); }
        });

        tank.move(Direction.RIGHT);
        tank.update(0.5f);
        new ShootAction(tank).execute();

        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertTrue(field.objectsAt(new GridPoint2(3, 1)).get(0) instanceof Bullet);
    }

    private Tank tank() {
        Tank tank = new Tank(field, new GridPoint2(1, 1), 1f);
        field.add(tank);
        return tank;
    }

    private void assertBehavior(int maxHealth, int health, float slowdown, boolean canShoot) {
        GameField world = new GameField(5, 3, Collections.emptyList());
        Tank tank = new Tank(world, new GridPoint2(1, 1), 1f, maxHealth, 40, 0.1f);
        world.add(tank);
        tank.hit(maxHealth - health);
        tank.move(Direction.RIGHT);
        tank.update(1f);

        String description = health + "/" + maxHealth;
        assertEquals(description, 1f / slowdown, tank.getMovementProgress(), 0.0001f);
        new ShootAction(tank).execute();
        GridPoint2 nextCell = Direction.RIGHT.move(tank.getCoordinates());
        assertEquals(description, canShoot,
                world.objectsAt(nextCell).stream().anyMatch(object -> object instanceof Bullet));
    }
}
