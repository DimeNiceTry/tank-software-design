package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.GameField;
import ru.mipt.bit.platformer.GameFieldListener;
import ru.mipt.bit.platformer.GameObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.getSingleLayer;

public final class GameRenderer implements Disposable, GameFieldListener {

    private final Map<GameObject, EntityRenderer> entities = new LinkedHashMap<>();
    private final GameField field;
    private final Function<GameObject, EntityRenderer> rendererFactory;
    private final Batch batch;
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TileMovement tileMovement;

    public GameRenderer(GameField field, TiledMap level, Interpolation interpolation,
                        Function<GameObject, EntityRenderer> rendererFactory) {
        this.field = field;
        this.level = level;
        this.rendererFactory = rendererFactory;
        batch = new SpriteBatch();

        TiledMapTileLayer groundLayer = getSingleLayer(level);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        tileMovement = new TileMovement(
                groundLayer,
                interpolation
        );
        field.addListener(this);
    }

    @Override
    public void objectAdded(GameObject object) {
        entities.put(object, rendererFactory.apply(object));
    }

    @Override
    public void objectRemoved(GameObject object) {
        entities.remove(object);
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        levelRenderer.render();

        batch.begin();
        for (EntityRenderer entity : entities.values()) {
            entity.render(batch, tileMovement);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        field.removeListener(this);
        entities.clear();
        level.dispose();
        batch.dispose();
    }
}
