package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.TankRenderer;
import ru.mipt.bit.platformer.graphics.TreeRenderer;
import ru.mipt.bit.platformer.input.KeyboardHandler;
import ru.mipt.bit.platformer.input.RandomActionStrategy;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelSource;
import ru.mipt.bit.platformer.model.RandomTankSpawner;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.*;

public final class TankGame extends ApplicationAdapter {

    private static final float TANK_MOVEMENT_DURATION = 0.4f;
    private static final int RANDOM_TANK_COUNT = 3;

    private final LevelSource levelSource;
    private final List<Tank> tanks = new ArrayList<>();
    private final List<ControlStrategy> controls = new ArrayList<>();
    private GameRenderer renderer;

    public TankGame(LevelSource levelSource) {
        this.levelSource = levelSource;
    }

    @Override
    public void create() {
        Level level = levelSource.load();
        GameField field = new GameField(level.getWidth(), level.getHeight(), level.getTrees());

        Tank player = new Tank(field, level.getTankStart(), TANK_MOVEMENT_DURATION);
        field.addObstacle(player);
        tanks.add(player);

        KeyboardHandler keyboard = new KeyboardHandler(Gdx.input::isKeyPressed);
        bindMovement(keyboard, player, UP, W, Direction.UP);
        bindMovement(keyboard, player, LEFT, A, Direction.LEFT);
        bindMovement(keyboard, player, DOWN, S, Direction.DOWN);
        bindMovement(keyboard, player, RIGHT, D, Direction.RIGHT);
        controls.add(keyboard);

        Random random = new Random();
        for (GridPoint2 start : new RandomTankSpawner(random).generate(level, field, RANDOM_TANK_COUNT)) {
            Tank tank = new Tank(field, start, TANK_MOVEMENT_DURATION);
            field.addObstacle(tank);
            tanks.add(tank);
            controls.add(new RandomActionStrategy(Arrays.asList(
                    new MoveAction(tank, Direction.UP),
                    new MoveAction(tank, Direction.LEFT),
                    new MoveAction(tank, Direction.DOWN),
                    new MoveAction(tank, Direction.RIGHT)
            ), random, TANK_MOVEMENT_DURATION));
        }

        renderer = new GameRenderer(Arrays.asList(
                new TankRenderer(tanks),
                new TreeRenderer(level.getTrees())
        ));
    }

    private void bindMovement(KeyboardHandler keyboard, Tank tank, int key, int alternativeKey, Direction direction) {
        Action action = new MoveAction(tank, direction);
        keyboard.bind(key, action);
        keyboard.bind(alternativeKey, action);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        for (Tank tank : tanks) {
            tank.update(deltaTime);
        }
        for (ControlStrategy control : controls) {
            control.update(deltaTime);
        }
        renderer.render();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
