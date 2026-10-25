package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.CellAvailability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RandomTankSpawner {

    private final Random random;

    public RandomTankSpawner(Random random) {
        this.random = random;
    }

    public List<GridPoint2> generate(Level level, CellAvailability cells, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Tank count must not be negative");
        }
        List<GridPoint2> positions = new ArrayList<>();
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                positions.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(positions, random);

        List<GridPoint2> starts = new ArrayList<>();
        for (GridPoint2 position : positions) {
            if (starts.size() == count) {
                break;
            }
            if (cells.isFree(position)) {
                starts.add(position);
            }
        }
        return starts;
    }
}
