package net.ltslab.games.breakoutgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.ltslab.games.breakoutgdx.BreakoutRLGame;
import net.ltslab.games.breakoutgdx.util.Const;

public class DesktopLauncher {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Const.DESKTOP_WIDTH;
        config.height = Const.DESKTOP_HEIGHT;
        config.resizable = false;
        new LwjglApplication(new BreakoutRLGame(), config);
    }
}
