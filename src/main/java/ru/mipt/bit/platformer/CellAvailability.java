package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

@FunctionalInterface
public interface CellAvailability {

    boolean isFree(GridPoint2 coordinates);
}
