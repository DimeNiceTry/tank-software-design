package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

/** Internal domain query for free cells; implemented by the logical world. */
@FunctionalInterface
public interface CellAvailability {

    boolean isFree(GridPoint2 coordinates);
}
