package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public final class GameLoopTest {

    @Test
    public void collectsAllCommandsBeforeExecutionAndUpdatesObjectsLast() {
        List<String> events = new ArrayList<>();
        GameObject object = new GameObject() {
            public GridPoint2 getCoordinates() { return new GridPoint2(0, 0); }
            public boolean occupies(GridPoint2 cell) { return getCoordinates().equals(cell); }
            public void update(float deltaTime) { events.add("tick"); }
        };
        GameField field = new GameField(2, 2, Collections.singletonList(object));
        Tank tank = new Tank(field, new GridPoint2(1, 1), 0.4f);
        field.add(tank);
        GameLoop loop = new GameLoop(field);
        loop.control(object, (tick, commands) -> {
            events.add("collect1");
            commands.accept(() -> events.add("execute1"));
        });
        loop.control(tank, (tick, commands) -> {
            events.add("collect2");
            commands.accept(() -> events.add("execute2"));
        });

        loop.update(0.1f);

        assertEquals(Arrays.asList("collect1", "collect2", "execute1", "execute2", "tick"), events);
    }

    @Test
    public void killedTankCannotExecuteQueuedShotOrReceiveNewCommands() {
        GameField field = new GameField(4, 2, Collections.emptyList());
        Tank player = new Tank(field, new GridPoint2(0, 0), 0.4f, 100, 120, 0.1f);
        Tank enemy = new Tank(field, new GridPoint2(1, 0), 0.4f);
        field.add(player);
        field.add(enemy);
        GameLoop loop = new GameLoop(field);
        List<String> collected = new ArrayList<>();
        loop.control(player, (tick, actions) -> actions.accept(new ShootAction(player)));
        loop.control(enemy, (tick, actions) -> {
            collected.add("enemy");
            actions.accept(new ShootAction(enemy));
        });

        loop.update(0f);

        assertFalse(field.contains(enemy));
        assertTrue(field.objectsAt(new GridPoint2(2, 0)).isEmpty());
        loop.update(0f);
        assertEquals(Collections.singletonList("enemy"), collected);
    }
}
