package ru.mipt.bit.platformer;

import java.util.function.Consumer;

public interface ControlStrategy {

    void collectActions(float deltaTime, Consumer<Action> actions);
}
