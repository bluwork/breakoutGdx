/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.helper;

import com.badlogic.gdx.physics.box2d.*;
import net.ltslab.games.breakoutgdx.Const;
import net.ltslab.games.breakoutgdx.GameManager;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if (a.getUserData() != null && b.getUserData() != null) {
            BodyData aData = (BodyData) a.getUserData();
            BodyData bData = (BodyData) b.getUserData();
            if (aData.getName().equals("Brick")) {
                GameManager.getInstance().removeBody(a);
                GameManager.getInstance().updateScore(Const.BRICK_VALUE);

            } else if (bData.getName().equals("Brick")) {
                GameManager.getInstance().removeBody(b);
                GameManager.getInstance().updateScore(Const.BRICK_VALUE);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

       if (contact.getFixtureA().isSensor()) {

            if (contact.getFixtureB().getBody().getUserData() != null) {
                BodyData bData = (BodyData) contact.getFixtureB().getBody().getUserData();
                if (bData.getName().equals("Ball")) {
                    System.out.println("Ball is out. Calling a game over.");
                    GameManager.getInstance().gameOver();

                }
            }
        }
        if (contact.getFixtureB().isSensor()) {
            if (contact.getFixtureA().getBody().getUserData() != null) {
                BodyData aData = (BodyData) contact.getFixtureA().getBody().getUserData();
                if (aData.getName().equals("Ball")) {
                    System.out.println("Ball is out. Calling a game over.");
                    GameManager.getInstance().gameOver();
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
