package net.ltslab.games.breakoutgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.ltslab.games.breakoutgdx.BreakoutRLGame;
import net.ltslab.games.breakoutgdx.Const;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Const.CAMERA_WIDTH;
		config.height = Const.CAMERA_HEIGHT;
		new LwjglApplication(new BreakoutRLGame(), config);
	}
}
