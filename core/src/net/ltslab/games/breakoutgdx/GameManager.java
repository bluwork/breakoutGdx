/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx;

public class GameManager {

    private BreakoutRLGame game;
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

    public BreakoutRLGame getGame() {
        if (game == null) {
            throw (new UnsupportedOperationException("GameManager must set reference to Game Class"));
        }
        return game;
    }

    public void gameOver() {
        getGame().onGameOver();
    }

    public void updateScore(int amount) {

    }
}
