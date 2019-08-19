package net.ltslab.games.breakoutgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.ltslab.games.breakoutgdx.BreakoutRLGame;
import net.ltslab.games.breakoutgdx.Const;

public class DesktopLauncher {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 450;
        config.height = 800;
        new LwjglApplication(new BreakoutRLGame(), config);
    }
}
