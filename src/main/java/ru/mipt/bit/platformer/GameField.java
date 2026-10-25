package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GameField implements CellAvailability {

    private final int width;
    private final int height;
    private final List<Obstacle> obstacles;

    public GameField(int width, int height, Collection<? extends Obstacle> obstacles) {
        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>(obstacles);
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        if (coordinates.x < 0 || coordinates.x >= width || coordinates.y < 0 || coordinates.y >= height) {
            return false;
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }
}
