package net.ltslab.games.breakoutgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.ltslab.games.breakoutgdx.BreakoutRLGame;

public class DesktopLauncher {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 144 ;
        //* 2;
        config.height = 144 ;
         //* 2;
        new LwjglApplication(new BreakoutRLGame(), config);
    }
}
