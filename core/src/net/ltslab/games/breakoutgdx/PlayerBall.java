/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import net.ltslab.games.breakoutgdx.helper.BodyData;
import org.graalvm.compiler.asm.sparc.SPARCAssembler;

import java.util.ArrayList;

public class PlayerBall extends Actor {

    TextureRegion ballImage;
    private boolean canAct;
    private float speed = 100;

    private World world;

    private Paddle paddle;

    private Body body;

    private Vector2 initialPosition;

    public PlayerBall (Paddle paddle, World world) {

        ballImage = new TextureRegion(new Texture(Gdx.files.internal("ball.png")));
        setBounds(0, 0, ballImage.getRegionWidth()/Const.SCALE, ballImage.getRegionHeight()/Const.SCALE);
        this.paddle = paddle;
        this.world = world;


    }

    public void createBody(Vector2 position) {
        initialPosition = position;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        body = world.createBody(bodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.3f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef);
        BodyData data = new BodyData("Ball", this);
        body.setUserData(data);
        circleShape.dispose();
        update();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(ballImage, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void start() {
        canAct = true;
        body.setLinearVelocity(5 , 5);
    }

    public void stopAndReset() {
        canAct = false;
        //body.setTransform(initialPosition, body.getAngle());
        update();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (canAct) {
            update();
        }
    }

    private void update() {


        Vector2 position  = body.getPosition();

        setRotation(body.getAngle()* MathUtils.radiansToDegrees);

        setPosition(position.x - getWidth()/2, position.y - getHeight()/2);

    }


}
