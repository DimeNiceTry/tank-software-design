package ru.mipt.bit.platformer;

import org.junit.Test;
import ru.mipt.bit.platformer.input.KeyboardHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class KeyboardHandlerTest {

    @Test
    public void runsOnlyThePressedKeysAction() {
        List<String> actions = new ArrayList<>();
        KeyboardHandler handler = new KeyboardHandler(key -> key == W);
        handler.bind(W, () -> actions.add("up"));
        handler.bind(A, () -> actions.add("left"));

        List<Action> commands = new ArrayList<>();
        handler.collectActions(0f, commands::add);
        assertTrue(actions.isEmpty());
        commands.forEach(Action::execute);

        assertEquals(Arrays.asList("up"), actions);
    }

    @Test
    public void keepsTheBindingOrderWhenSeveralKeysArePressed() {
        List<String> actions = new ArrayList<>();
        KeyboardHandler handler = new KeyboardHandler(key -> key == W || key == A);
        handler.bind(W, () -> actions.add("up"));
        handler.bind(A, () -> actions.add("left"));

        List<Action> commands = new ArrayList<>();
        handler.collectActions(0f, commands::add);
        commands.forEach(Action::execute);

        assertEquals(Arrays.asList("up", "left"), actions);
    }

    @Test
    public void shootsOncePerPressWhileMovementCanRepeat() {
        AtomicBoolean pressed = new AtomicBoolean(true);
        KeyboardHandler handler = new KeyboardHandler(key -> pressed.get());
        List<String> executed = new ArrayList<>();
        handler.bindOnPress(SPACE, () -> executed.add("shoot"));
        handler.bind(W, () -> executed.add("move"));
        List<Action> commands = new ArrayList<>();
        handler.collectActions(0f, commands::add);
        handler.collectActions(0f, commands::add);
        pressed.set(false);
        handler.collectActions(0f, commands::add);
        pressed.set(true);
        handler.collectActions(0f, commands::add);
        commands.forEach(Action::execute);

        assertEquals(Arrays.asList("shoot", "move", "move", "shoot", "move"), executed);
    }
}
