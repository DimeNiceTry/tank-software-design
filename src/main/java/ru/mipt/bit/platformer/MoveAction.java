package ru.mipt.bit.platformer;

import ru.mipt.bit.platformer.model.Tank;

public final class MoveAction implements Action {

    private final Tank tank;
    private final Direction direction;

    public MoveAction(Tank tank, Direction direction) {
        this.tank = tank;
        this.direction = direction;
    }

    @Override
    public void execute() {
        tank.move(direction);
    }
}
