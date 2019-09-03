/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.trainer;

import java.io.Serializable;

public class StepData implements Serializable {
    private byte[] lastFrame;
    private int reward;
    private boolean done;
    private String info;

    public byte[] getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(byte[] frame) {
        this.lastFrame = frame;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Reward: " + reward + " GameOver: " + (isDone()? " yes ": " no ");
    }
}