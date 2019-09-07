/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.management;

import com.badlogic.gdx.physics.box2d.Body;
import net.ltslab.games.breakoutgdx.BreakoutRLGame;
import net.ltslab.games.breakoutgdx.actor.Paddle;
import net.ltslab.games.breakoutgdx.util.GameUtils;

import java.io.IOException;

public class GameManager {

    private static final String TAG = "GameManager";
    private BreakoutRLGame game;
    private Paddle paddle;

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    private int reward;
    private boolean gameOver;
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

    public void updatePaddleInput(int lastAction) {

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

    public void addToReward(int amount) {
        reward += amount;
    }
    public byte[] getScreenSnapshot() {
        return getGame().getLastSnapshot();
    }

    public long getElapsedTime() {
        return getGame().getElapsedTime();
    }

}
