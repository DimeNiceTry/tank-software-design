package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public interface GameWorld extends CellAvailability {

    void add(GameObject object);

    void remove(GameObject object);

    boolean contains(GameObject object);

    boolean isInside(GridPoint2 cell);

    List<GameObject> objectsAt(GridPoint2 cell);
}
