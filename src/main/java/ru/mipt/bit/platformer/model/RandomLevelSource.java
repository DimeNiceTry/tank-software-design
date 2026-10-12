package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RandomLevelSource implements LevelSource {

    private final int width;
    private final int height;
    private final int treeCount;
    private final Random random;

    public RandomLevelSource(int width, int height, int treeCount, Random random) {
        if (width <= 0 || height <= 0 || treeCount < 0 || treeCount >= width * height) {
            throw new IllegalArgumentException("Invalid level size or tree count");
        }
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.random = random;
    }

    @Override
    public Level load() {
        List<GridPoint2> cells = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(cells, random);

        List<Tree> trees = new ArrayList<>();
        for (int i = 1; i <= treeCount; i++) {
            trees.add(new Tree(cells.get(i)));
        }
        return new Level(width, height, trees, cells.get(0));
    }
}
