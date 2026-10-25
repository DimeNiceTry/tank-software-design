package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.Action;
import ru.mipt.bit.platformer.ControlStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public final class RandomActionStrategy implements ControlStrategy {

    private final List<Action> actions;
    private final Random random;
    private final float interval;
    private float elapsed;

    public RandomActionStrategy(Collection<? extends Action> actions, Random random, float interval) {
        if (actions.isEmpty() || !(interval > 0f)) {
            throw new IllegalArgumentException("Actions must be nonempty and interval positive");
        }
        this.actions = new ArrayList<>(actions);
        this.random = random;
        this.interval = interval;
        elapsed = interval;
    }

    @Override
    public void update(float deltaTime) {
        elapsed += deltaTime;
        if (elapsed >= interval) {
            actions.get(random.nextInt(actions.size())).execute();
            elapsed = 0f;
        }
    }
}
