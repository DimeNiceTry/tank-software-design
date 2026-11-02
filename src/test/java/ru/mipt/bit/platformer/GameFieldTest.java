package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class GameFieldTest {

    @Test
    public void reportsCellsOccupiedByObstacles() {
        GameField field = new GameField(10, 8, Arrays.asList(
                new Tree(new GridPoint2(1, 3)),
                new Tree(new GridPoint2(4, 2))
        ));

        assertFalse(field.isFree(new GridPoint2(1, 3)));
        assertFalse(field.isFree(new GridPoint2(4, 2)));
        assertTrue(field.isFree(new GridPoint2(2, 2)));
    }

    @Test
    public void copiesObstacleCollection() {
        List<GameObject> obstacles = new ArrayList<>();
        GameField field = new GameField(10, 8, obstacles);

        obstacles.add(new Tree(new GridPoint2(1, 3)));

        assertTrue(field.isFree(new GridPoint2(1, 3)));
    }

    @Test
    public void blocksCellsOutsideLevel() {
        GameField field = new GameField(10, 8, new ArrayList<>());

        assertFalse(field.isFree(new GridPoint2(-1, 0)));
        assertFalse(field.isFree(new GridPoint2(10, 0)));
        assertFalse(field.isFree(new GridPoint2(0, -1)));
        assertFalse(field.isFree(new GridPoint2(0, 8)));
    }
}
