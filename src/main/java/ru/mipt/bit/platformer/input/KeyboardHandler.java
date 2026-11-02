package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.Action;
import ru.mipt.bit.platformer.ControlStrategy;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

public final class KeyboardHandler implements ControlStrategy {

    private final IntPredicate isKeyPressed;
    private final Map<Integer, Action> actions = new LinkedHashMap<>();
    private final Set<Integer> onPress = new HashSet<>();
    private final Set<Integer> heldKeys = new HashSet<>();

    public KeyboardHandler(IntPredicate isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    public void bind(int key, Action action) {
        actions.put(key, action);
        onPress.remove(key);
    }

    public void bindOnPress(int key, Action action) {
        bind(key, action);
        onPress.add(key);
    }

    @Override
    public void collectActions(float deltaTime, Consumer<Action> commands) {
        for (Map.Entry<Integer, Action> entry : actions.entrySet()) {
            if (isKeyPressed.test(entry.getKey())) {
                if (!onPress.contains(entry.getKey()) || !heldKeys.contains(entry.getKey())) {
                    commands.accept(entry.getValue());
                }
                heldKeys.add(entry.getKey());
            } else {
                heldKeys.remove(entry.getKey());
            }
        }
    }
}
