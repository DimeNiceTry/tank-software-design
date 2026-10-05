package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

public interface EntityRenderer extends Disposable {

    void render(Batch batch, TileMovement tileMovement);
}
