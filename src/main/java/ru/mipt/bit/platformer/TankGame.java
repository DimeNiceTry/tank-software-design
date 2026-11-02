package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.BulletRenderer;
import ru.mipt.bit.platformer.graphics.EntityRenderer;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.TankRenderer;
import ru.mipt.bit.platformer.graphics.TreeRenderer;
import ru.mipt.bit.platformer.input.KeyboardHandler;
import ru.mipt.bit.platformer.input.RandomActionStrategy;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.LevelSource;
import ru.mipt.bit.platformer.model.RandomTankSpawner;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.*;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.getSingleLayer;

public final class TankGame extends ApplicationAdapter {

    private static final float TANK_MOVEMENT_DURATION = 0.4f;
    private static final int RANDOM_TANK_COUNT = 3;

    private final LevelSource levelSource;
    private GameLoop gameLoop;
    private GameRenderer renderer;
    private Texture tankTexture;
    private Texture treeTexture;
    private Texture bulletTexture;

    public TankGame(LevelSource levelSource) {
        this.levelSource = levelSource;
    }

    @Override
    public void create() {
        Level level = levelSource.load();
        TiledMap background = new TmxMapLoader().load("level.tmx");
        TiledMapTileLayer ground = getSingleLayer(background);
        try {
            level.requireSize(ground.getWidth(), ground.getHeight());
        } catch (IllegalArgumentException exception) {
            background.dispose();
            throw exception;
        }

        tankTexture = new Texture("images/tank_blue.png");
        treeTexture = new Texture("images/greenTree.png");
        Pixmap bulletImage = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        bulletImage.setColor(Color.GOLD);
        bulletImage.fillCircle(8, 8, 6);
        bulletTexture = new Texture(bulletImage);
        bulletImage.dispose();

        GameField field = new GameField(level.getWidth(), level.getHeight(), Collections.emptyList());
        renderer = new GameRenderer(field, background, Interpolation.smooth, this::createRenderer);
        gameLoop = new GameLoop(field);
        for (Tree tree : level.getTrees()) {
            field.add(tree);
        }

        Tank player = new Tank(field, level.getTankStart(), TANK_MOVEMENT_DURATION);
        field.add(player);

        KeyboardHandler keyboard = new KeyboardHandler(Gdx.input::isKeyPressed);
        bindMovement(keyboard, player, UP, W, Direction.UP);
        bindMovement(keyboard, player, LEFT, A, Direction.LEFT);
        bindMovement(keyboard, player, DOWN, S, Direction.DOWN);
        bindMovement(keyboard, player, RIGHT, D, Direction.RIGHT);
        keyboard.bindOnPress(SPACE, new ShootAction(player));
        gameLoop.control(player, keyboard);

        Random random = new Random();
        for (GridPoint2 start : new RandomTankSpawner(random).generate(level, field, RANDOM_TANK_COUNT)) {
            Tank tank = new Tank(field, start, TANK_MOVEMENT_DURATION);
            field.add(tank);
            gameLoop.control(tank, new RandomActionStrategy(Arrays.asList(
                    new MoveAction(tank, Direction.UP),
                    new MoveAction(tank, Direction.LEFT),
                    new MoveAction(tank, Direction.DOWN),
                    new MoveAction(tank, Direction.RIGHT),
                    new ShootAction(tank)
            ), random, TANK_MOVEMENT_DURATION));
        }
    }

    private EntityRenderer createRenderer(GameObject object) {
        if (object instanceof Tank) {
            return new TankRenderer((Tank) object, new TextureRegion(tankTexture));
        }
        if (object instanceof Tree) {
            return new TreeRenderer((Tree) object, new TextureRegion(treeTexture));
        }
        if (object instanceof Bullet) {
            return new BulletRenderer((Bullet) object, new TextureRegion(bulletTexture));
        }
        throw new IllegalArgumentException("Unknown game object: " + object.getClass());
    }

    private void bindMovement(KeyboardHandler keyboard, Tank tank, int key, int alternativeKey, Direction direction) {
        Action action = new MoveAction(tank, direction);
        keyboard.bind(key, action);
        keyboard.bind(alternativeKey, action);
    }

    @Override
    public void render() {
        gameLoop.update(Gdx.graphics.getDeltaTime());
        renderer.render();
    }

    @Override
    public void dispose() {
        if (renderer != null) renderer.dispose();
        if (tankTexture != null) tankTexture.dispose();
        if (treeTexture != null) treeTexture.dispose();
        if (bulletTexture != null) bulletTexture.dispose();
    }
}
