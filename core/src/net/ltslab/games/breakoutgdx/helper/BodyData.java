/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.helper;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class BodyData {
    private String name;
    private Actor actor;

    public BodyData(String name, Actor actor) {
        this.name = name;
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }


    public String getName() {
        return name;
    }

}
