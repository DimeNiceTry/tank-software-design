package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.TankRenderer;
import ru.mipt.bit.platformer.graphics.TreeRenderer;
import ru.mipt.bit.platformer.input.KeyboardHandler;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelSource;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Arrays;

import static com.badlogic.gdx.Input.Keys.*;

public final class TankGame extends ApplicationAdapter {

    private static final float TANK_MOVEMENT_DURATION = 0.4f;

    private final LevelSource levelSource;
    private Tank tank;
    private KeyboardHandler keyboardHandler;
    private GameRenderer renderer;

    public TankGame(LevelSource levelSource) {
        this.levelSource = levelSource;
    }

    @Override
    public void create() {
        Level level = levelSource.load();
        GameField field = new GameField(level.getWidth(), level.getHeight(), level.getTrees());

        tank = new Tank(field, level.getTankStart(), TANK_MOVEMENT_DURATION);
        keyboardHandler = new KeyboardHandler(Gdx.input::isKeyPressed);
        keyboardHandler.bind(UP, () -> tank.move(Direction.UP));
        keyboardHandler.bind(W, () -> tank.move(Direction.UP));
        keyboardHandler.bind(LEFT, () -> tank.move(Direction.LEFT));
        keyboardHandler.bind(A, () -> tank.move(Direction.LEFT));
        keyboardHandler.bind(DOWN, () -> tank.move(Direction.DOWN));
        keyboardHandler.bind(S, () -> tank.move(Direction.DOWN));
        keyboardHandler.bind(RIGHT, () -> tank.move(Direction.RIGHT));
        keyboardHandler.bind(D, () -> tank.move(Direction.RIGHT));

        renderer = new GameRenderer(Arrays.asList(
                new TankRenderer(tank),
                new TreeRenderer(level.getTrees())
        ));
    }

    @Override
    public void render() {
        keyboardHandler.handleInput();
        tank.update(Gdx.graphics.getDeltaTime());
        renderer.render();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
