/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.helper;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.ltslab.games.breakoutgdx.Const;

public class PhysicsHelper {

    private static float accumulator;


    public static void updatePhysicsStep(World world, float delta) {

            float frameTime = Math.min(delta, 0.25f);
            accumulator += frameTime;
            while (accumulator >= Const.TIME_STEP) {
                world.step(Const.TIME_STEP, Const.VELOCITY_ITERATIONS, Const.POSITION_ITERATIONS);

                accumulator -= Const.TIME_STEP;
            }
        }


    public static void createWorldBorders(World world) {
        createCage(Side.N, world);
        createCage(Side.S, world);
        createCage(Side.W, world);
        createCage(Side.E, world);
    }

    private static void createCage(Side side, World world) {
        BodyDef bodyDefinition = new BodyDef();
        Vector2 position = new Vector2();
        PolygonShape box = new PolygonShape();

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.0f;

        switch (side) {
            case N:
                position.y = Const.CAMERA_HEIGHT;
                position.x = Const.CAMERA_WIDTH / 2;
                box.setAsBox(Const.CAMERA_WIDTH / 2, .1f);
                break;
            case S:
                position.y = 0f;
                position.x = Const.CAMERA_WIDTH / 2;
                box.setAsBox(Const.CAMERA_WIDTH / 2, .1f);
                fixtureDef.isSensor = true;

                break;
            case W:
                position.y = Const.CAMERA_HEIGHT / 2;
                position.x = 0;
                box.setAsBox(.1f, Const.CAMERA_HEIGHT / 2);

                break;
            case E:
                position.y = Const.CAMERA_HEIGHT / 2;
                position.x = Const.CAMERA_WIDTH;
                box.setAsBox(.1f, Const.CAMERA_HEIGHT / 2);
                break;
        }

        bodyDefinition.position.set(position);

        Body body = world.createBody(bodyDefinition);

        fixtureDef.shape = box;

        body.createFixture(fixtureDef);

        box.dispose();


    }

    public enum Side {
        N, S, W, E
    }
}

