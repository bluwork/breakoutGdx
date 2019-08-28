/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.stepdata;

import java.io.Serializable;
import java.util.ArrayList;

public class StepData implements Serializable {
    private ArrayList<byte[]> frames;
    private int reward;
    private boolean done;
    private String info;

    public ArrayList<byte[]> getFrames() {
        return frames;
    }

    public void setFrames(ArrayList<byte[]> frames) {
        this.frames = frames;
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
}