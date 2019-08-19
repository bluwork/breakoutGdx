/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.helper;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class CollisionListener implements ContactListener {

    private Array<Body> removedBodies;

    public CollisionListener(Array<Body> removedBodies) {
        this.removedBodies = removedBodies;
    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (a.getUserData() != null && b.getUserData() != null) {
            BodyData aData = (BodyData) a.getUserData();
            BodyData bData = (BodyData) b.getUserData();
            if (aData.getName().equals("Brick")) {
                aData.getActor().remove();
                removedBodies.add(a);

            } else if (bData.getName().equals("Brick")) {
                bData.getActor().remove();
                removedBodies.add(b);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
