/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import net.ltslab.games.breakoutgdx.util.Const;
import net.ltslab.games.breakoutgdx.util.GameUtils;

public class Paddle extends Actor {

    TextureRegion image;
    private Body body;
    private World world;
    private Vector2 initialPosition;
    private boolean updateInput;

    public Paddle(World world, boolean updateInput) {
        image = new TextureRegion(new Texture(Gdx.files.internal("paddle.png")));
        setBounds(0, 0, image.getRegionWidth() / Const.SCALE, image.getRegionHeight() / Const.SCALE);
        this.world = world;
        this.updateInput = updateInput;
    }

    public void createBody(Vector2 position) {
        initialPosition = position;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(getWidth() / 2, getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        body.createFixture(fixtureDef);

        rectangle.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (updateInput) {
            handleInput();
        }
        update();
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(image, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    private float movingSpeed = 10f;

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight();
        } else {
            stop();
        }
    }

    public void moveLeft() {
        //GameUtils.print("Paddle", "Moving Left.");
        currentDir = Direction.LEFT;

    }

    public void moveRight() {
        //GameUtils.print("Paddle", "Moving Rigth.");
        currentDir = Direction.RIGHT;

    }

    public void stop() {
        //GameUtils.print("Paddle", "Not moving.");
        currentDir = Direction.STOP;

    }

    private Direction currentDir = Direction.STOP;

    private void update() {

        if (currentDir == Direction.LEFT) {
            body.setLinearVelocity(-movingSpeed, 0);
        } else if (currentDir == Direction.RIGHT) {
            body.setLinearVelocity(movingSpeed, 0);
        } else {
            body.setLinearVelocity(0, 0);
        }

        Vector2 position = body.getPosition();

        setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        setPosition(position.x - getWidth() / 2, position.y - getHeight() / 2);

    }

    public void stopAndReset() {
        body.setTransform(initialPosition, body.getAngle());
        update();
    }

    public enum Direction {
        LEFT, RIGHT, STOP
    }
}
