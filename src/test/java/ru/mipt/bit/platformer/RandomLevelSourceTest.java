package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.RandomLevelSource;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class RandomLevelSourceTest {

    @Test
    public void placesTankAndTreesInDifferentCells() {
        Level level = new RandomLevelSource(10, 8, 15, new Random(42)).load();
        Set<GridPoint2> occupied = new HashSet<>();

        assertEquals(10, level.getWidth());
        assertEquals(8, level.getHeight());
        assertEquals(15, level.getTrees().size());
        assertTrue(occupied.add(level.getTankStart()));
        for (Tree tree : level.getTrees()) {
            GridPoint2 cell = tree.getCoordinates();
            assertTrue(cell.x >= 0 && cell.x < level.getWidth());
            assertTrue(cell.y >= 0 && cell.y < level.getHeight());
            assertTrue(occupied.add(cell));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMoreTreesThanAvailableCells() {
        new RandomLevelSource(2, 2, 4, new Random());
    }
}
