package ru.mipt.bit.platformer;

/** Application input port: a command that can be executed independently of its source. */
@FunctionalInterface
public interface Action {

    void execute();
}
