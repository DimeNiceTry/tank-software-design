package ru.mipt.bit.platformer.model;

/** Domain: state-dependent tank behavior; an internal contract, not an infrastructure port. */
public interface TankState {

    void update(Tank tank, float deltaTime);

    void shoot(Tank tank);
}
