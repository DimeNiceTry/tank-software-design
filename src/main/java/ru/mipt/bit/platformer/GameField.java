package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GameField {

    private final List<Obstacle> obstacles;

    public GameField(Collection<? extends Obstacle> obstacles) {
        this.obstacles = new ArrayList<>(obstacles);
    }

    public boolean isFree(GridPoint2 coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }
}
