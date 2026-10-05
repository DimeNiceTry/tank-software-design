package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;

import java.util.Arrays;

import static com.badlogic.gdx.Input.Keys.*;

public final class TankGame extends ApplicationAdapter {

    private static final float TANK_MOVEMENT_DURATION = 0.4f;

    private Tank tank;
    private KeyboardHandler keyboardHandler;
    private GameRenderer renderer;

    @Override
    public void create() {
        Tree tree = new Tree(new GridPoint2(1, 3));
        GameField field = new GameField(Arrays.asList(tree));

        tank = new Tank(field, new GridPoint2(1, 1), TANK_MOVEMENT_DURATION);
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
                new TreeRenderer(tree)
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
