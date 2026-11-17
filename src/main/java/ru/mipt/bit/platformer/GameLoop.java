package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GameLoop {

    private final GameField field;
    private final List<ControlStrategy> globalControls;
    private final Map<GameObject, ControlStrategy> controls = new LinkedHashMap<>();

    public GameLoop(GameField field, ControlStrategy... globalControls) {
        this.field = field;
        this.globalControls = Arrays.asList(globalControls);
    }

    public void control(GameObject object, ControlStrategy strategy) {
        controls.put(object, strategy);
    }

    public void update(float deltaTime) {
        controls.entrySet().removeIf(entry -> !field.contains(entry.getKey()));
        List<Action> commands = new ArrayList<>();
        for (ControlStrategy control : globalControls) {
            control.collectActions(deltaTime, commands::add);
        }
        for (ControlStrategy control : controls.values()) {
            control.collectActions(deltaTime, commands::add);
        }
        for (Action command : commands) {
            command.execute();
        }
        field.update(deltaTime);
    }
}
