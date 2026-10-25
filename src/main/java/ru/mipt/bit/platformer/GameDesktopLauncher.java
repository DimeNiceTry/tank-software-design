package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.model.LevelSource;
import ru.mipt.bit.platformer.model.RandomLevelSource;
import ru.mipt.bit.platformer.model.TextLevelSource;

import java.util.Random;

public final class GameDesktopLauncher {

    public static void main(String[] args) {
        if (args.length > 1 || (args.length == 1
                && !"file".equals(args[0]) && !"random".equals(args[0]))) {
            throw new IllegalArgumentException("Usage: [file|random]");
        }
        LevelSource levelSource = args.length == 1 && "random".equals(args[0])
                ? new RandomLevelSource(10, 8, 15, new Random())
                : new TextLevelSource(() -> Gdx.files.internal("level.txt").readString("UTF-8"));

        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);

        new Lwjgl3Application(new TankGame(levelSource), config);
    }
}
