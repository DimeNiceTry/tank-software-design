package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GameField implements GameWorld {

    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();
    private final List<GameFieldListener> listeners = new ArrayList<>();

    public GameField(int width, int height, Collection<? extends GameObject> objects) {
        this.width = width;
        this.height = height;
        for (GameObject object : objects) {
            add(object);
        }
    }

    @Override
    public void add(GameObject object) {
        if (!objects.contains(object)) {
            objects.add(object);
            for (GameFieldListener listener : new ArrayList<>(listeners)) {
                listener.objectAdded(object);
            }
        }
    }

    @Override
    public void remove(GameObject object) {
        if (objects.remove(object)) {
            for (GameFieldListener listener : new ArrayList<>(listeners)) {
                listener.objectRemoved(object);
            }
        }
    }

    public void addListener(GameFieldListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            for (GameObject object : new ArrayList<>(objects)) {
                listener.objectAdded(object);
            }
        }
    }

    public void removeListener(GameFieldListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean contains(GameObject object) {
        return objects.contains(object);
    }

    public void update(float deltaTime) {
        for (GameObject object : new ArrayList<>(objects)) {
            if (contains(object)) {
                object.update(deltaTime);
            }
        }
    }

    @Override
    public boolean isInside(GridPoint2 cell) {
        return cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height;
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        return isInside(coordinates) && objectsAt(coordinates).stream().noneMatch(GameObject::blocksMovement);
    }

    @Override
    public List<GameObject> objectsAt(GridPoint2 cell) {
        List<GameObject> result = new ArrayList<>();
        for (GameObject object : objects) {
            if (object.occupies(cell)) {
                result.add(object);
            }
        }
        return result;
    }
}
