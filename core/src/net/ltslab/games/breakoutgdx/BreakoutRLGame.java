/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import net.ltslab.games.breakoutgdx.trainer.SendStateReceiveAction;

import java.io.IOException;
import java.util.ArrayList;

public class BreakoutRLGame extends ApplicationAdapter {

	private Stage stage;
	private int skipFrames;
	private int totalSend;
	private SendStateReceiveAction sendStateReceiveAction;
	private boolean stopSending;
	private PlayerBall ball;
	private int brickColumns = 2;
	private int brickRows = 2;

	private ArrayList<Brick> bricks;

	public World world;

	@Override
	public void create () {

		Box2D.init();
		world  = new World(new Vector2(0, 0), true);

		bricks = new ArrayList<>();

		stage = new Stage(new StretchViewport(Const.CAMERA_WIDTH, Const.CAMERA_HEIGHT));
		Paddle paddle = new Paddle();
		paddle.setPosition(Const.CAMERA_WIDTH/2 - paddle.getWidth()/2, 0);

		Group brickLayout = new Group();

		brickLayout.setHeight(brickRows * brickHeight);
		brickLayout.setWidth(brickColumns * brickWidth);
		brickLayout.setColor(Color.BLUE);

		brickLayout.setPosition(Const.CAMERA_WIDTH/2 - brickLayout.getWidth()/2 + (Const.CAMERA_WIDTH - brickLayout.getWidth()) /2, Const.CAMERA_HEIGHT * 2 /3);

		addBricks(brickLayout);

		stage.addActor(brickLayout);

		stage.addActor(paddle);

		ball = new PlayerBall(paddle);
		ball.setPosition(Const.CAMERA_WIDTH/2 - ball.getWidth()/2, paddle.getHeight());
		stage.addActor(ball);

		try {
			sendStateReceiveAction = new SendStateReceiveAction();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ball.setBricks(bricks);
		start();

	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		if (skipFrames >= 4) {
			if (!stopSending) {
				takeScreenShot();
			}
			skipFrames = 0;
		} else {
			skipFrames++;
		}

	}

	private void update(float delta) {
		updatePhysics(delta);
	}


	private float accumulator = 0;

	private void updatePhysics(float deltaTime) {
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= Const.TIME_STEP) {
			world.step(Const.TIME_STEP, Const.VELOCITY_ITERATIONS, Const.POSITION_ITERATIONS);
			accumulator -= Const.TIME_STEP;
		}
	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}

	private void takeScreenShot() {
		totalSend ++;
		if (totalSend > 15) {
			stopSending = true;
			try {
				sendStateReceiveAction.stop();
				System.out.println("Stop sending.");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		byte [] pixels = ScreenUtils.getFrameBufferPixels(true);

		// this loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
		for(int i = 4; i < pixels.length; i += 4) {
			pixels[i - 1] = (byte) 255;
		}

        try {
            sendStateReceiveAction.sendState(pixels);
            System.out.println("State was send.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Pixels size: " + pixels.length);

		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		PixmapIO.writePNG(new FileHandle(Gdx.files.getLocalStoragePath() + "mypixmap.png"), pixmap);
		pixmap.dispose();



	}

	public void start() {
		ball.start();
	}

	int brickWidth = (int)Const.CAMERA_WIDTH/ brickColumns;
	int brickHeight = 40;

	private void addBricks(Group layout) {
		for (int i = 0; i < brickRows; i++) {
			for (int j = 0; j < brickColumns; j++) {
				Brick brick = new Brick();
				bricks.add(brick);
				brick.setPosition(i * brickWidth, j * brickHeight);
				System.out.println(brick.getX() + " " + brick.getY());
				layout.addActor(brick);


			}

		}
	}

}
