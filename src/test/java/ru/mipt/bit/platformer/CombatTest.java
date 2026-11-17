package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.input.RandomActionStrategy;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public final class CombatTest {

    private final GameField field = new GameField(8, 4, Collections.emptyList());

    @Test
    public void shootsInFacingDirectionWithoutHittingMovingShooter() {
        Tank shooter = tank(1, 1);
        shooter.move(Direction.UP);

        new ShootAction(shooter).execute();

        Bullet bullet = bulletAt(1, 2);
        assertEquals(100, shooter.getHealth());
        bullet.update(0.1f);
        assertEquals(new GridPoint2(1, 3), bullet.getCoordinates());
    }

    @Test
    public void damagesAdjacentEnemyImmediately() {
        Tank shooter = tank(1, 1);
        Tank target = tank(2, 1);

        new ShootAction(shooter).execute();

        assertEquals(60, target.getHealth());
        assertEquals(Collections.singletonList(target), field.objectsAt(new GridPoint2(2, 1)));
    }

    @Test
    public void treeStopsBulletWithoutBeingDestroyed() {
        Tank shooter = tank(1, 1);
        Tree tree = new Tree(new GridPoint2(3, 1));
        field.add(tree);
        shooter.shoot();
        Bullet bullet = bulletAt(2, 1);

        field.update(0.1f);

        assertFalse(field.contains(bullet));
        assertTrue(field.contains(tree));
    }

    @Test
    public void largeTickDoesNotSkipTargetCells() {
        Tank shooter = tank(1, 1);
        Tank target = tank(4, 1);
        shooter.shoot();
        Bullet bullet = bulletAt(2, 1);

        field.update(1f);

        assertEquals(60, target.getHealth());
        assertFalse(field.contains(bullet));
    }

    @Test
    public void collidingBulletsBothDisappear() {
        Tank owner = tank(0, 0);
        Bullet right = new Bullet(field, owner, new GridPoint2(2, 1), Direction.RIGHT, 40, 0.1f);
        Bullet left = new Bullet(field, owner, new GridPoint2(4, 1), Direction.LEFT, 40, 0.1f);
        field.add(right);
        field.add(left);

        field.update(0.1f);

        assertFalse(field.contains(right));
        assertFalse(field.contains(left));
    }

    @Test
    public void adjacentBulletsCannotSwapCells() {
        Tank owner = tank(0, 0);
        Bullet right = new Bullet(field, owner, new GridPoint2(2, 1), Direction.RIGHT, 40, 0.1f);
        Bullet left = new Bullet(field, owner, new GridPoint2(3, 1), Direction.LEFT, 40, 0.1f);
        field.add(right);
        field.add(left);

        field.update(0.1f);

        assertFalse(field.contains(right));
        assertFalse(field.contains(left));
    }

    @Test
    public void bulletDisappearsAtLevelBoundary() {
        Tank shooter = tank(6, 1);
        shooter.shoot();
        Bullet bullet = bulletAt(7, 1);

        field.update(0.1f);

        assertFalse(field.contains(bullet));
    }

    @Test
    public void shootingOutOfLevelImmediatelyRemovesBullet() {
        Tank shooter = tank(7, 1);
        List<GameObject> removed = new ArrayList<>();
        field.addListener(new GameFieldListener() {
            public void objectAdded(GameObject object) { }
            public void objectRemoved(GameObject object) { removed.add(object); }
        });

        shooter.shoot();

        assertEquals(1, removed.size());
        assertTrue(removed.get(0) instanceof Bullet);
        assertFalse(field.contains(removed.get(0)));
    }

    @Test
    public void tankMovingIntoBulletTakesDamage() {
        Tank target = tank(3, 1);
        Tank owner = tank(0, 0);
        Bullet bullet = new Bullet(field, owner, new GridPoint2(3, 2), Direction.LEFT, 25, 0.1f);
        field.add(bullet);

        target.move(Direction.UP);
        field.update(0.05f);

        assertEquals(new GridPoint2(3, 2), target.getDestinationCoordinates());
        assertEquals(75, target.getHealth());
        assertFalse(field.contains(bullet));
    }

    @Test
    public void negativeHealthRemovesTankAndFreesBothReservedCells() {
        Tank shooter = tank(1, 1);
        Tank target = new Tank(field, new GridPoint2(2, 1), 0.4f, 40, 40, 0.1f);
        field.add(target);
        target.move(Direction.UP);

        shooter.shoot();
        assertEquals(0, target.getHealth());
        assertTrue(field.contains(target));
        shooter.shoot();

        assertFalse(target.isAlive());
        assertFalse(field.contains(target));
        assertTrue(field.isFree(new GridPoint2(2, 1)));
        assertTrue(field.isFree(new GridPoint2(2, 2)));
        target.shoot();
        assertTrue(field.objectsAt(new GridPoint2(2, 2)).isEmpty());
    }

    @Test
    public void randomStrategyCanChooseShooting() {
        Tank shooter = tank(1, 1);
        List<Action> actions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            actions.add(new MoveAction(shooter, direction));
        }
        actions.add(new ShootAction(shooter));
        Random random = new Random() {
            @Override public int nextInt(int bound) { return bound - 1; }
        };
        GameLoop loop = new GameLoop(field);
        loop.control(shooter, new RandomActionStrategy(actions, random, 0.4f));

        loop.update(0f);

        assertNotNull(bulletAt(2, 1));
    }

    private Tank tank(int x, int y) {
        Tank tank = new Tank(field, new GridPoint2(x, y), 0.4f);
        field.add(tank);
        return tank;
    }

    private Bullet bulletAt(int x, int y) {
        return (Bullet) field.objectsAt(new GridPoint2(x, y)).stream()
                .filter(object -> object instanceof Bullet).findFirst().get();
    }
}
