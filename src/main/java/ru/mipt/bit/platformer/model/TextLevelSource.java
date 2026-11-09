package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class TextLevelSource implements LevelSource {

    private final Supplier<String> text;

    public TextLevelSource(Supplier<String> text) {
        this.text = text;
    }

    @Override
    public Level load() {
        String[] rows = text.get().split("\\R");
        if (rows.length == 0) {
            throw new IllegalArgumentException("Level must not be empty");
        }
        int height = rows.length;
        int width = rows[0].length();
        if (width == 0) {
            throw new IllegalArgumentException("Level must not be empty");
        }

        List<Tree> trees = new ArrayList<>();
        GridPoint2 tankStart = null;
        for (int row = 0; row < height; row++) {
            if (rows[row].length() != width) {
                throw new IllegalArgumentException("Level rows must have equal width");
            }
            for (int x = 0; x < width; x++) {
                GridPoint2 coordinates = new GridPoint2(x, height - 1 - row);
                switch (rows[row].charAt(x)) {
                    case 'T':
                        trees.add(new Tree(coordinates));
                        break;
                    case 'X':
                        if (tankStart != null) {
                            throw new IllegalArgumentException("Level must have one tank start");
                        }
                        tankStart = coordinates;
                        break;
                    case '_':
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown level cell: " + rows[row].charAt(x));
                }
            }
        }
        if (tankStart == null) {
            throw new IllegalArgumentException("Level must have one tank start");
        }
        return new Level(width, height, trees, tankStart);
    }
}
