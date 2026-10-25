package ru.mipt.bit.platformer;

import org.junit.Test;
import ru.mipt.bit.platformer.input.KeyboardHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.W;
import static org.junit.Assert.assertEquals;

public final class KeyboardHandlerTest {

    @Test
    public void runsOnlyThePressedKeysAction() {
        List<String> actions = new ArrayList<>();
        KeyboardHandler handler = new KeyboardHandler(key -> key == W);
        handler.bind(W, () -> actions.add("up"));
        handler.bind(A, () -> actions.add("left"));

        handler.update(0f);

        assertEquals(Arrays.asList("up"), actions);
    }

    @Test
    public void keepsTheBindingOrderWhenSeveralKeysArePressed() {
        List<String> actions = new ArrayList<>();
        KeyboardHandler handler = new KeyboardHandler(key -> key == W || key == A);
        handler.bind(W, () -> actions.add("up"));
        handler.bind(A, () -> actions.add("left"));

        handler.update(0f);

        assertEquals(Arrays.asList("up", "left"), actions);
    }
}
