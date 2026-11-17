package ru.mipt.bit.platformer;

/** Domain output port: world membership events, independent of graphics. */
public interface GameFieldListener {

    void objectAdded(GameObject object);

    void objectRemoved(GameObject object);
}
