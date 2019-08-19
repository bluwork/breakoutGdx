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
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import net.ltslab.games.breakoutgdx.helper.CollisionListener;
import net.ltslab.games.breakoutgdx.helper.PhysicsHelper;
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
	private int brickColumns = 6;
	private int brickRows = 5;

	private ArrayList<Brick> bricks;

	Box2DDebugRenderer debugRenderer;

	public World world;

	private Array<Body> removedBodies;

	@Override
	public void create () {

		Box2D.init();

		removedBodies = new Array<>();

		debugRenderer= new Box2DDebugRenderer();
		world  = new World(new Vector2(0, 0), false);
		world.setContactListener(new CollisionListener(removedBodies));

		bricks = new ArrayList<>();

		stage = new Stage(new ExtendViewport(Const.CAMERA_WIDTH , Const.CAMERA_HEIGHT));

		addBricks();

		Paddle paddle = new Paddle(world);
		paddle.createBody(new Vector2(Const.CAMERA_WIDTH/2 - (paddle.getWidth()/2)/Const.SCALE, 0));


		stage.addActor(paddle);

		ball = new PlayerBall(paddle, world);
		ball.createBody(new Vector2(Const.CAMERA_WIDTH/2 - ball.getWidth()/2, paddle.getHeight()));

		stage.addActor(ball);

		try {
			sendStateReceiveAction = new SendStateReceiveAction();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ball.setBricks(bricks);

		PhysicsHelper.createWorldBorders(world);

		start();

	}



	@Override
	public void render () {


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

		if (removedBodies.size > 0) {
			clearBodies();
		}

		PhysicsHelper.updatePhysicsStep(world, Gdx.graphics.getDeltaTime());

//		stage.getBatch().begin();
//		debugRenderer.render(world, stage.getCamera().combined);
//		stage.getBatch().end();

	}

	private void clearBodies() {
		for (Body b : removedBodies) {
			if (!world.isLocked()) {
				removedBodies.removeValue(b, true);
				world.destroyBody(b);
			}
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

	private static final float HEIGHT_OFFSET = Const.CAMERA_HEIGHT * .6f;


	private void addBricks() {
		for (int i = 0; i < brickRows; i++) {
			for (int j = 0; j < brickColumns; j++) {
				Brick brick = new Brick(world);
				bricks.add(brick);
				brick.createBody(new Vector2(i * (Const.CAMERA_WIDTH * 3/4)/brickColumns + brick.getWidth()/2  , j * (Const.CAMERA_HEIGHT/3)/brickRows - brick.getHeight()/2+ HEIGHT_OFFSET));
				stage.addActor(brick);

			}
		}
	}

}
