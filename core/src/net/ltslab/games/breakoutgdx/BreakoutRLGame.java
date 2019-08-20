/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.ltslab.games.breakoutgdx.helper.CollisionListener;
import net.ltslab.games.breakoutgdx.helper.PhysicsHelper;
import net.ltslab.games.breakoutgdx.trainer.SendStateReceiveAction;

import java.io.IOException;
import java.util.ArrayList;

public class BreakoutRLGame extends ApplicationAdapter {

    private Stage stage;
    private Stage hudStage;

    // Training elements
    private SendStateReceiveAction sendStateReceiveAction;
    private boolean training = true;

    // Level elements
    private PlayerBall ball;
    private Paddle paddle;
    private int brickColumns = 6;
    private int brickRows = 5;
    private Array<Brick> bricks;

    // Physics elements
    Box2DDebugRenderer debugRenderer;

    public World world;
    private Array<Body> removedBodies;
    private boolean physics;
    private boolean renderPhysics = true;

    // UI elements
    Label scoreText;

    @Override
    public void create() {

        initialize();

        PhysicsHelper.createWorldBorders(world);

        addBricks();

        paddle = new Paddle(world);
        paddle.createBody(new Vector2(Const.CAMERA_WIDTH / 2 - (paddle.getWidth() / 2) / Const.SCALE, paddle.getHeight() / 2));
        stage.addActor(paddle);

        ball = new PlayerBall(paddle, world);
        ball.createBody(new Vector2(Const.CAMERA_WIDTH / 2 - (ball.getWidth() / 2) / Const.SCALE, paddle.getHeight() + ball.getHeight() / 2));
        stage.addActor(ball);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        scoreText = new Label("0", labelStyle);
        Table table = new Table();
        table.setFillParent(true);
        table.add(scoreText);

        hudStage.addActor(table);

        try {
            sendStateReceiveAction = new SendStateReceiveAction();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initialize() {

        GameManager.getInstance().setGame(this);

        // Initialize physics
        Box2D.init();

        removedBodies = new Array<>();

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new CollisionListener(removedBodies));

        debugRenderer = new Box2DDebugRenderer();

        // Initialize stages - one for game and one for HUD - UI elements
        stage = new Stage(new ExtendViewport(Const.CAMERA_WIDTH, Const.CAMERA_HEIGHT));
        hudStage = new Stage(new ExtendViewport(Const.CAMERA_WIDTH * Const.SCALE, Const.CAMERA_HEIGHT * Const.SCALE));

    }

    private boolean started;

    @Override
    public void render() {

        if (!started && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            start();
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        if (training && started) {
            sendStateReceiveAction.update();
        }
        hudStage.act();
        hudStage.draw();

        if (removedBodies.size > 0) {
            clearBodies();
        }

        if (physics) {
            PhysicsHelper.updatePhysicsStep(world, Gdx.graphics.getDeltaTime());
        }

        if (renderPhysics) {
            stage.getBatch().begin();
            debugRenderer.render(world, stage.getCamera().combined);
            stage.getBatch().end();
        }
    }

    private static final int BRICK_VALUE = 60;

    private void clearBodies() {

        if (!world.isLocked()) {
            for (Body b : removedBodies) {
                if (!world.isLocked()) {
                    removedBodies.removeValue(b, true);
                    world.destroyBody(b);
                    updateScore(BRICK_VALUE);
                }
            }
        }
    }

    private void updateScore(int amount) {
        score += amount;
        scoreText.setText(score);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


    public void start() {
        started = true;
        physics = true;
        ball.start();
    }

    private static final float HEIGHT_OFFSET = Const.CAMERA_HEIGHT * .6f;

    private void addBricks() {
        if (bricks == null) {
            bricks = new Array<>();
        }
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickColumns; j++) {
                Brick brick = new Brick(world);
                bricks.add(brick);
                brick.createBody(new Vector2(i * (Const.CAMERA_WIDTH * 3 / 4) / brickColumns + brick.getWidth() / 2, j * (Const.CAMERA_HEIGHT / 3) / brickRows - brick.getHeight() / 2 + HEIGHT_OFFSET));
                stage.addActor(brick);
            }
        }
    }

    private int score;


    public void onGameOver() {
        //showWinLose();
        physics = false;
        started = false;
        ball.stopAndReset();
        paddle.stopAndReset();
        //uploadAndResetScore();
        score = 0;
    }

    private void showWinLose() {
        throw (new UnsupportedOperationException("Implement Win Lose method"));
    }

    private void uploadAndResetScore() {
        throw (new UnsupportedOperationException("Implement Upload And Reset method"));
    }

}
