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
import net.ltslab.games.breakoutgdx.helper.BodyData;
import net.ltslab.games.breakoutgdx.helper.CollisionListener;
import net.ltslab.games.breakoutgdx.helper.PhysicsHelper;
import net.ltslab.games.breakoutgdx.trainer.SendStateReceiveAction;

import java.io.IOException;

public class BreakoutRLGame extends ApplicationAdapter {

    private Stage stage;
    private Stage hudStage;

    // Training elements
    private SendStateReceiveAction sendStateReceiveAction;
    private boolean training = true;

    private GameState gameState = GameState.WAITING;

    // Physics elements
    Box2DDebugRenderer debugRenderer;

    public World world;
    private Array<Body> removedBodies;
    private boolean renderPhysics = true;

    // UI elements
    Label scoreText;

    @Override
    public void create() {

        initialize();

        level = new Level(world, stage);

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
        world.setContactListener(new CollisionListener());

        debugRenderer = new Box2DDebugRenderer();

        // Initialize stages - one for game and one for HUD - UI elements
        stage = new Stage(new ExtendViewport(Const.CAMERA_WIDTH, Const.CAMERA_HEIGHT));
        hudStage = new Stage(new ExtendViewport(Const.CAMERA_WIDTH * Const.SCALE, Const.CAMERA_HEIGHT * Const.SCALE));

    }

    private boolean started;

    @Override
    public void render() {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        hudStage.act();
        hudStage.draw();

        if (removedBodies.size > 0) {
            clearBodies();
        }

        if (gameState == GameState.WAITING) {

            if (!started && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                gameState = GameState.PLAYING;
                start();
            }
        }

        if (gameState == GameState.PLAYING) {

            if (training && started) {
                sendStateReceiveAction.update();
            }

            PhysicsHelper.updatePhysicsStep(world, Gdx.graphics.getDeltaTime());

            if (renderPhysics) {
                stage.getBatch().begin();
                debugRenderer.render(world, stage.getCamera().combined);
                stage.getBatch().end();
            }
        } else if (gameState == GameState.RESETING) {
            resetGame();
        }

    }


    private void clearBodies() {

        if (!world.isLocked()) {
            for (Body b : removedBodies) {
                if (!world.isLocked()) {
                    removedBodies.removeValue(b, true);
                    if (b.getUserData() != null) {
                        BodyData data = (BodyData) b.getUserData();
                        if (data.getActor() != null) {
                            if (data.getName().equals("Brick")) {
                                Brick br = (Brick) data.getActor();
                                if (br.hasHolder()) {
                                    br.getHolder().removeValue(br, true);
                                }
                            }
                            data.getActor().remove();
                        }
                    }
                    world.destroyBody(b);
                }
            }
        }
    }

    public void updateScore(int amount) {
        score += amount;
        scoreText.setText(score);
    }

    @Override
    public void dispose() {
        stage.dispose();
        hudStage.dispose();
        world.dispose();
    }


    public void start() {
        started = true;

        level.start();

    }


    private int score;
    private Level level;


    public void onGameOver() {
        gameState = GameState.RESETING;
    }

    private void resetGame() {
        started = false;
        System.out.println("World is " + (world.isLocked() ? "locked" : "not locked") + ".");

        if (!training) {
            //showWinLose();
        }

        started = false;
        level.reset();
        //uploadAndResetScore();
        score = 0;
        scoreText.setText(score);

        gameState = GameState.WAITING;
    }

    private void showWinLose() {
        throw (new UnsupportedOperationException("Implement Win Lose method"));
    }

    private void uploadAndResetScore() {
        throw (new UnsupportedOperationException("Implement Upload And Reset method"));
    }

    public Array<Body> getRemovedBodies() {
        return removedBodies;
    }

    public enum GameState {
        PLAYING,
        RESETING,
        WAITING
    }
}
