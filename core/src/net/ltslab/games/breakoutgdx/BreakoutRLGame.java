package net.ltslab.games.breakoutgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BreakoutRLGame extends ApplicationAdapter {

	private Stage stage;
	
	@Override
	public void create () {
		stage = new Stage();
		stage.addActor(new PlayerBall());
		stage.addActor(new Paddle());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}
}
