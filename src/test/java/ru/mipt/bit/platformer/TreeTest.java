package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class TreeTest {

    @Test
    public void occupiesOnlyItsCoordinates() {
        Tree tree = new Tree(new GridPoint2(1, 3));

        assertTrue(tree.occupies(new GridPoint2(1, 3)));
        assertFalse(tree.occupies(new GridPoint2(1, 2)));
    }

    @Test
    public void protectsItsCoordinatesFromExternalChanges() {
        GridPoint2 coordinates = new GridPoint2(1, 3);
        Tree tree = new Tree(coordinates);

        coordinates.set(2, 4);
        tree.getCoordinates().set(5, 6);

        assertEquals(new GridPoint2(1, 3), tree.getCoordinates());
    }
}
