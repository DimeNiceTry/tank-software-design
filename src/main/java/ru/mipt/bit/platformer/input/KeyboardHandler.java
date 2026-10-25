package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.Action;
import ru.mipt.bit.platformer.ControlStrategy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntPredicate;

public final class KeyboardHandler implements ControlStrategy {

    private final IntPredicate isKeyPressed;
    private final Map<Integer, Action> actions = new LinkedHashMap<>();

    public KeyboardHandler(IntPredicate isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    public void bind(int key, Action action) {
        actions.put(key, action);
    }

    @Override
    public void update(float deltaTime) {
        for (Map.Entry<Integer, Action> entry : actions.entrySet()) {
            if (isKeyPressed.test(entry.getKey())) {
                entry.getValue().execute();
            }
        }
    }
}
