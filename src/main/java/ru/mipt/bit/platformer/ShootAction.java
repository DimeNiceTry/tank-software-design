package ru.mipt.bit.platformer;

import ru.mipt.bit.platformer.model.Tank;

public final class ShootAction implements Action {

    private final Tank tank;

    public ShootAction(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.shoot();
    }
}
