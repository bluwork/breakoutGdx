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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.ltslab.games.breakoutgdx.actor.Brick;
import net.ltslab.games.breakoutgdx.helper.BodyData;
import net.ltslab.games.breakoutgdx.helper.CollisionListener;
import net.ltslab.games.breakoutgdx.helper.PhysicsHelper;
import net.ltslab.games.breakoutgdx.management.GameManager;
import net.ltslab.games.breakoutgdx.trainer.Communicator;
import net.ltslab.games.breakoutgdx.util.Const;
import net.ltslab.games.breakoutgdx.util.GameUtils;

import java.io.IOException;

public class BreakoutRLGame extends ApplicationAdapter {

    private static final String TAG = BreakoutRLGame.class.getSimpleName();
    private Stage stage;
    private Stage hudStage;

    // Training elements
    private Communicator communicator;
    private boolean training = true;

    private GameState gameState = GameState.WAITING;

    // Physics elements
    Box2DDebugRenderer debugRenderer;

    public World world;
    private Array<Body> removedBodies;
    private boolean renderPhysics = true;

    private long startTime;


    // UI elements
    Label scoreText;
    private byte[] lastSnapshot;

    @Override
    public void create() {

        initialize();

        level = new Level(world, stage, !training);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        scoreText = new Label("0", labelStyle);
        scoreText.setAlignment(Align.top);
        Table table = new Table();
        table.setFillParent(true);
        if (!training) {
            table.add(scoreText);
        }

        hudStage.addActor(table);
        communicator = new Communicator();
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
        if (training) {
            //stage.getBatch().setShader(GrayscaleShader.grayscaleShader);
        }

        hudStage = new Stage(new ExtendViewport(Const.CAMERA_WIDTH * Const.SCALE, Const.CAMERA_HEIGHT * Const.SCALE));

    }

    @Override
    public void render() {

        if (!training) {
            Gdx.gl.glClearColor(.5f, .5f, .5f, 1);

        } else {
            Gdx.gl.glClearColor(0, 0, 1, 1);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        hudStage.act();
        hudStage.draw();

        if (removedBodies.size > 0) {
            clearBodies();
        }

        if (gameState == GameState.WAITING) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !training) {
                gameState = GameState.PLAYING;
                start();
            }
        }

        if (gameState == GameState.PLAYING) {

            PhysicsHelper.updatePhysicsStep(world, Gdx.graphics.getDeltaTime());
            if ((System.currentTimeMillis() - startTime) > 30000) {
                try {
                    onGameOver(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startTime = System.currentTimeMillis();

            }

            if (renderPhysics
                    && !training
            ) {
                stage.getBatch().begin();
                debugRenderer.render(world, stage.getCamera().combined);
                stage.getBatch().end();
            }

        }
        if (gameState == GameState.RESETTING) {
            resetGame();
        }

        lastSnapshot = GameUtils.takeNoAlphaSnapshot();

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
        GameManager.getInstance().setReward(score);
    }

    @Override
    public void dispose() {
        stage.dispose();
        hudStage.dispose();
        world.dispose();
    }

    public void start() {

        //GameUtils.print(TAG, "Start called");
        GameManager.getInstance().setGameOver(false);
        GameManager.getInstance().setReward(0);
        startTime = System.currentTimeMillis();
        level.start();
        gameState = GameState.PLAYING;


    }

    private int score;
    private Level level;
    private int bestScore;


    public void onGameOver(boolean win) throws IOException {

        if (win) {
            GameManager.getInstance().addToReward(10);
        } else {
            GameManager.getInstance().setReward(-1);
        }
        if (score > bestScore) {
            bestScore = score;
        }

        GameManager.getInstance().setGameOver(true);
        gameState = GameState.RESETTING;


    }

    public long getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    private void resetGame() {

        //GameUtils.print(TAG, "World is " + (world.isLocked() ? "locked" : "not locked") + ".");
        level.reset();
        score = 0;
        scoreText.setText(score);
        gameState = GameState.WAITING;
    }

    public Array<Body> getRemovedBodies() {
        return removedBodies;
    }

    public byte[] getLastSnapshot() {
        return lastSnapshot;
    }

    public enum GameState {
        PLAYING,
        RESETTING,
        WAITING
    }
}
