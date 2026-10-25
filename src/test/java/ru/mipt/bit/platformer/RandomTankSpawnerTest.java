package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.RandomTankSpawner;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class RandomTankSpawnerTest {

    @Test
    public void placesTanksOnlyOnDifferentFreeCells() {
        Level level = new Level(3, 2, Collections.singletonList(new Tree(new GridPoint2(0, 0))),
                new GridPoint2(1, 0));
        GameField field = new GameField(3, 2, level.getTrees());
        field.addObstacle(new Tank(field, level.getTankStart(), 0.4f));

        List<GridPoint2> starts = new RandomTankSpawner(new Random(42)).generate(level, field, 3);

        assertEquals(3, starts.size());
        assertEquals(3, new HashSet<>(starts).size());
        for (GridPoint2 start : starts) {
            assertTrue(field.isFree(start));
            assertFalse(start.equals(level.getTankStart()));
        }
    }

    @Test
    public void usesOnlyAvailableCells() {
        Level level = new Level(2, 1, Collections.singletonList(new Tree(new GridPoint2(0, 0))),
                new GridPoint2(1, 0));
        GameField field = new GameField(2, 1, level.getTrees());
        field.addObstacle(new Tank(field, level.getTankStart(), 0.4f));

        assertTrue(new RandomTankSpawner(new Random(42)).generate(level, field, 3).isEmpty());
        assertTrue(new RandomTankSpawner(new Random(42)).generate(level, field, 0).isEmpty());
    }
}
