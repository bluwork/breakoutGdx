/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import net.ltslab.games.breakoutgdx.actor.Ball;
import net.ltslab.games.breakoutgdx.actor.Brick;
import net.ltslab.games.breakoutgdx.actor.Paddle;
import net.ltslab.games.breakoutgdx.helper.PhysicsHelper;
import net.ltslab.games.breakoutgdx.management.GameManager;
import net.ltslab.games.breakoutgdx.util.Const;

public class Level {

    private World world;
    private Stage stage;
    private Paddle paddle;
    private Ball ball;

    private Array<Brick> bricks;



    public Level(World world, Stage stage) {

        this.world = world;
        this.stage = stage;

        PhysicsHelper.createWorldBorders(world);

        addBricks();
        addPaddle();
        addBall();
    }

    private static final float HEIGHT_OFFSET = Const.CAMERA_HEIGHT * .6f;

    private void addBricks() {
        if (bricks == null) {
            bricks = new Array<>();
        }
        for (int i = 0; i < Const.BRICK_ROWS; i++) {
            for (int j = 0; j < Const.BRICK_COLUMNS; j++) {
                Brick brick = new Brick(world);
                bricks.add(brick);
                brick.setHolder(bricks);
                brick.createBody(new Vector2(i * (Const.CAMERA_WIDTH * 3 / 4) / Const.BRICK_COLUMNS + brick.getWidth() / 2, j * (Const.CAMERA_HEIGHT / 3) / Const.BRICK_ROWS - brick.getHeight() / 2 + HEIGHT_OFFSET));
                stage.addActor(brick);
            }
        }
    }

    private void addPaddle() {
        paddle = new Paddle(world);
        paddle.createBody(new Vector2(Const.CAMERA_WIDTH / 2 - (paddle.getWidth() / 2) / Const.SCALE, paddle.getHeight() / 2));
        stage.addActor(paddle);
        GameManager.getInstance().setPaddle(paddle);
    }

    private void addBall() {
        ball = new Ball(paddle, world);
        ball.createBody(new Vector2(Const.CAMERA_WIDTH / 2 - (ball.getWidth() / 2) / Const.SCALE, paddle.getHeight() + ball.getHeight() / 2));
        stage.addActor(ball);
    }

    public void start() {
        ball.start();
    }

    public void reset() {
        clearBricks();
        ball.stopAndReset();
        paddle.stopAndReset();
        addBricks();
    }

    private void clearBricks() {
        for(Brick b : bricks) {
            GameManager.getInstance().removeBody(b.getBody());
        }
    }
}
