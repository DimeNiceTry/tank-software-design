package ru.mipt.bit.platformer.model;

/** Application output port for obtaining a logical level independently of its source. */
public interface LevelSource {

    Level load();
}
