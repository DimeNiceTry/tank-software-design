package ru.mipt.bit.platformer;

import java.util.function.Consumer;

/** Application port for collecting commands from input adapters or internal AI. */
public interface ControlStrategy {

    void collectActions(float deltaTime, Consumer<Action> actions);
}
