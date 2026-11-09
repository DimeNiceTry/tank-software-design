package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public final class GameFieldEventsTest {

    @Test
    public void observerTracksInitialPopulationAndLaterRemovals() {
        GameField field = new GameField(3, 3, Collections.emptyList());
        List<GameObject> observed = new ArrayList<>();
        GameFieldListener listener = mirror(observed);
        field.addListener(listener);
        Tree first = new Tree(new GridPoint2(0, 0));
        Tree second = new Tree(new GridPoint2(1, 0));

        field.add(first);
        field.add(second);
        field.add(first);
        assertEquals(Arrays.asList(first, second), observed);
        field.remove(first);
        field.remove(first);
        assertEquals(Collections.singletonList(second), observed);

        field.removeListener(listener);
        field.remove(second);
        assertEquals(Collections.singletonList(second), observed);
    }

    @Test
    public void newObserverReceivesExistingObjectsThroughSameEvents() {
        Tree tree = new Tree(new GridPoint2(0, 0));
        GameField field = new GameField(3, 3, Collections.singletonList(tree));
        List<GameObject> observed = new ArrayList<>();
        GameFieldListener listener = mirror(observed);

        field.addListener(listener);
        field.addListener(listener);

        assertEquals(Collections.singletonList(tree), observed);
    }

    @Test
    public void safelyAddsAndRemovesObjectsDuringTick() {
        GameField field = new GameField(3, 3, Collections.emptyList());
        List<String> ticks = new ArrayList<>();
        GameObject victim = updating(() -> ticks.add("removed"));
        GameObject child = updating(() -> ticks.add("new"));
        GameObject parent = updating(() -> {
            ticks.add("parent");
            field.remove(victim);
            field.add(child);
        });
        field.add(parent);
        field.add(victim);

        field.update(0.1f);
        assertEquals(Collections.singletonList("parent"), ticks);
        field.update(0.1f);
        assertEquals(Arrays.asList("parent", "parent", "new"), ticks);
    }

    private GameFieldListener mirror(List<GameObject> objects) {
        return new GameFieldListener() {
            public void objectAdded(GameObject object) { objects.add(object); }
            public void objectRemoved(GameObject object) { objects.remove(object); }
        };
    }

    private GameObject updating(Runnable action) {
        return new GameObject() {
            public GridPoint2 getCoordinates() { return new GridPoint2(0, 0); }
            public boolean occupies(GridPoint2 cell) { return false; }
            public void update(float deltaTime) { action.run(); }
        };
    }
}
