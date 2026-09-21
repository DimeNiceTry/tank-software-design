package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;

import java.util.Collections;

public final class TankGame extends ApplicationAdapter {

    private Tank tank;
    private KeyboardHandler keyboardHandler;
    private GameRenderer renderer;

    @Override
    public void create() {
        Tree tree = new Tree(new GridPoint2(1, 3));
        GameField field = new GameField(
                Collections.singletonList(tree)
        );

        tank = new Tank(field, new GridPoint2(1, 1));
        keyboardHandler = new KeyboardHandler(
                tank,
                Gdx.input::isKeyPressed
        );
        renderer = new GameRenderer(tank, tree);
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
