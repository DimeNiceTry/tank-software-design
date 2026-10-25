package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Level {

    private final int width;
    private final int height;
    private final List<Tree> trees;
    private final GridPoint2 tankStart;

    public Level(int width, int height, List<Tree> trees, GridPoint2 tankStart) {
        this.width = width;
        this.height = height;
        this.trees = Collections.unmodifiableList(new ArrayList<>(trees));
        this.tankStart = new GridPoint2(tankStart);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public GridPoint2 getTankStart() {
        return new GridPoint2(tankStart);
    }
}
