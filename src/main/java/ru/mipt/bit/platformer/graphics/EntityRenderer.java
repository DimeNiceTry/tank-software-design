package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface EntityRenderer {

    void render(Batch batch, TileMovement tileMovement);
}
