/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Paddle extends Actor {

    TextureRegion image;
    private Body body;
    private World world;

    public Paddle(World world) {
        image = new TextureRegion(new Texture(Gdx.files.internal("paddle.png")));
        setBounds(0, 0, image.getRegionWidth()/Const.SCALE, image.getRegionHeight()/Const.SCALE);
        this.world = world;
    }

    public void createBody(Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(getWidth()/2, getHeight()/2);

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
        handleInput();
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

    private float leftRightSpeed = 3f;

    private void handleInput() {
        if (Gdx.input.isKeyPressed( Input.Keys.LEFT)) {
            body.setLinearVelocity(- leftRightSpeed, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.setLinearVelocity(leftRightSpeed, 0);
        } else {
            // Stop moving in the X and Y direction
            body.setLinearVelocity(0, 0);
        }
    }

    private void update() {

        Vector2 position = body.getPosition();

        setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        setPosition(position.x - getWidth() / 2, position.y - getHeight() / 2);

    }
}
