package ru.mipt.bit.platformer;

import org.junit.Test;
import ru.mipt.bit.platformer.input.RandomActionStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public final class RandomActionStrategyTest {

    @Test
    public void executesRandomActionAtEachInterval() {
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

        strategy.update(0f);
        strategy.update(0.25f);
        assertEquals(Collections.singletonList("right"), executed);

        strategy.update(0.25f);
        assertEquals(Arrays.asList("right", "right"), executed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMissingActions() {
        new RandomActionStrategy(Collections.emptyList(), new Random(), 0.5f);
    }
}
