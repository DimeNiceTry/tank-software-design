package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.getSingleLayer;

public final class GameRenderer implements Disposable {

    private final List<EntityRenderer> entities;
    private final Batch batch;
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TileMovement tileMovement;

    public GameRenderer(Collection<? extends EntityRenderer> entities) {
        this.entities = new ArrayList<>(entities);
        batch = new SpriteBatch();
        level = new TmxMapLoader().load("level.tmx");

        TiledMapTileLayer groundLayer = getSingleLayer(level);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        tileMovement = new TileMovement(
                groundLayer,
                Interpolation.smooth
        );
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        levelRenderer.render();

        batch.begin();
        for (EntityRenderer entity : entities) {
            entity.render(batch, tileMovement);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        for (EntityRenderer entity : entities) {
            entity.dispose();
        }
        level.dispose();
        batch.dispose();
    }
}
