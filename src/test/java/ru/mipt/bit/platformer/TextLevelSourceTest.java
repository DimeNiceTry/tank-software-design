package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.TextLevelSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TextLevelSourceTest {

    @Test
    public void readsTreesAndTankFromTopToBottom() {
        Level level = new TextLevelSource(() -> "_T_\nX__\n__T\n").load();

        assertEquals(3, level.getWidth());
        assertEquals(3, level.getHeight());
        assertEquals(new GridPoint2(0, 1), level.getTankStart());
        assertEquals(2, level.getTrees().size());
        assertTrue(level.getTrees().get(0).occupies(new GridPoint2(1, 2)));
        assertTrue(level.getTrees().get(1).occupies(new GridPoint2(2, 0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsUnequalRowWidths() {
        new TextLevelSource(() -> "X_\n_\n").load();
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMissingTank() {
        new TextLevelSource(() -> "__\nT_\n").load();
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMultipleTanks() {
        new TextLevelSource(() -> "X_\n_X\n").load();
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsUnknownCells() {
        new TextLevelSource(() -> "X?\n__\n").load();
    }
}
