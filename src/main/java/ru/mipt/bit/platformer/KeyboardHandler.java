package ru.mipt.bit.platformer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntPredicate;

public final class KeyboardHandler {

    private final IntPredicate isKeyPressed;
    private final Map<Integer, Runnable> actions = new LinkedHashMap<>();

    public KeyboardHandler(IntPredicate isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    public void bind(int key, Runnable action) {
        actions.put(key, action);
    }

    public void handleInput() {
        for (Map.Entry<Integer, Runnable> entry : actions.entrySet()) {
            if (isKeyPressed.test(entry.getKey())) {
                entry.getValue().run();
            }
        }
    }
}
