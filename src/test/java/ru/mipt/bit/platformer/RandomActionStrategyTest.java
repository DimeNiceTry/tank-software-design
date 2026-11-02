package ru.mipt.bit.platformer;

import org.junit.Test;
import ru.mipt.bit.platformer.input.RandomActionStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class RandomActionStrategyTest {

    @Test
    public void collectsRandomActionAtEachInterval() {
        List<String> executed = new ArrayList<>();
        Random random = new Random() {
            @Override
            public int nextInt(int bound) {
                return bound - 1;
            }
        };
        RandomActionStrategy strategy = new RandomActionStrategy(Arrays.asList(
                () -> executed.add("up"),
                () -> executed.add("right")
        ), random, 0.5f);

        List<Action> commands = new ArrayList<>();
        strategy.collectActions(0f, commands::add);
        strategy.collectActions(0.25f, commands::add);
        assertEquals(1, commands.size());
        assertTrue(executed.isEmpty());

        strategy.collectActions(0.25f, commands::add);
        commands.forEach(Action::execute);
        assertEquals(Arrays.asList("right", "right"), executed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMissingActions() {
        new RandomActionStrategy(Collections.emptyList(), new Random(), 0.5f);
    }
}
