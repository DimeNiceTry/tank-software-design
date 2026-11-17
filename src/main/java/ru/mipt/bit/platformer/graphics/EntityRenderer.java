package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

/** Infrastructure-internal rendering contract; its libGDX types make it unsuitable as a core port. */
public interface EntityRenderer {

    void render(Batch batch, TileMovement tileMovement);
}
