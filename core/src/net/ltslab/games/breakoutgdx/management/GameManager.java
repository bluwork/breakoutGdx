/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.management;

import com.badlogic.gdx.physics.box2d.Body;
import net.ltslab.games.breakoutgdx.BreakoutRLGame;
import net.ltslab.games.breakoutgdx.actor.Paddle;
import net.ltslab.games.breakoutgdx.util.GameUtils;

public class GameManager {

    private static final String TAG = "GameManager";
    private BreakoutRLGame game;
    private Paddle paddle;
    private GameManager() {}

    private static GameManager instance;

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void setGame(BreakoutRLGame game) {
        this.game = game;
    }
    public void setPaddle(Paddle paddle) {this.paddle = paddle;}

    public BreakoutRLGame getGame() {
        if (game == null) {
            throw (new UnsupportedOperationException("GameManager must set reference to Game Class"));
        }
        return game;
    }

    public void gameOver(boolean win) {
        getGame().onGameOver(win);
    }

    public void updateScore(int amount) {
        getGame().updateScore(amount);
    }

    public void removeBody(Body body) {
        getGame().getRemovedBodies().add(body);
    }

    public void updatePaddleInput(byte lastAction) {

        switch (lastAction) {
            case 0:
                paddle.stop();
                //GameUtils.print(TAG, "Paddle stop.");
                break;
            case 1:
                paddle.moveLeft();
                //GameUtils.print(TAG, "Paddle move left.");
                break;
            case 2:
                paddle.moveRight();
                //GameUtils.print(TAG, "Paddle move right.");
                break;
            default:
                break;
        }

    }
}
