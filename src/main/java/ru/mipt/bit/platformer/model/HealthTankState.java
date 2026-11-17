package ru.mipt.bit.platformer.model;

/** Domain: the three reusable, stateless behaviors selected by remaining health. */
enum HealthTankState implements TankState {
    LIGHT(1f),
    MEDIUM(2f),
    HEAVY(3f) {
        @Override
        public void shoot(Tank tank) {
            // A heavily damaged tank cannot shoot.
        }
    };

    private final float slowdown;

    HealthTankState(float slowdown) {
        this.slowdown = slowdown;
    }

    @Override
    public void update(Tank tank, float deltaTime) {
        tank.advanceMovement(deltaTime / slowdown);
    }

    @Override
    public void shoot(Tank tank) {
        tank.fireBullet();
    }

    static TankState fromHealth(int health, int maxHealth) {
        if (health * 100L > maxHealth * 70L) {
            return LIGHT;
        }
        if (maxHealth > 0 && health * 100L >= maxHealth * 15L) {
            return MEDIUM;
        }
        return HEAVY;
    }
}
