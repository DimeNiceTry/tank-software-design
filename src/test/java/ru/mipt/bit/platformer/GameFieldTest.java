package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class GameFieldTest {

    @Test
    public void reportsCellsOccupiedByObstacles() {
        GameField field = new GameField(Arrays.asList(
                new Tree(new GridPoint2(1, 3)),
                new Tree(new GridPoint2(4, 2))
        ));

        assertFalse(field.isFree(new GridPoint2(1, 3)));
        assertFalse(field.isFree(new GridPoint2(4, 2)));
        assertTrue(field.isFree(new GridPoint2(2, 2)));
    }

    @Test
    public void copiesObstacleCollection() {
        List<Obstacle> obstacles = new ArrayList<>();
        GameField field = new GameField(obstacles);

        obstacles.add(new Tree(new GridPoint2(1, 3)));

        assertTrue(field.isFree(new GridPoint2(1, 3)));
    }
}
