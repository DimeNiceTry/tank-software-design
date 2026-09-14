package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private GameField field;
    private Tank tank;

    @Override
    public void create() {
        batch = new SpriteBatch();
        field = new GameField(batch);
        tank = new Tank(field, new GridPoint2(1, 1));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        handleInput();
        tank.update(Gdx.graphics.getDeltaTime());

        field.renderLevel();

        batch.begin();
        tank.render(batch);
        field.renderObstacles(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        tank.dispose();
        field.dispose();
        batch.dispose();
    }

    private void handleInput() {
        moveIfPressed(Direction.UP, UP, W);
        moveIfPressed(Direction.LEFT, LEFT, A);
        moveIfPressed(Direction.DOWN, DOWN, S);
        moveIfPressed(Direction.RIGHT, RIGHT, D);
    }

    private void moveIfPressed(
            Direction direction,
            int firstKey,
            int secondKey
    ) {
        if (Gdx.input.isKeyPressed(firstKey)
                || Gdx.input.isKeyPressed(secondKey)) {
            tank.move(direction);
        }
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
