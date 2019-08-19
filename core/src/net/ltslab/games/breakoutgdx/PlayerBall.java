/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.graalvm.compiler.asm.sparc.SPARCAssembler;

import java.util.ArrayList;

public class PlayerBall extends Actor {

    TextureRegion ballImage;
    private boolean canAct;
    private float speed = 100;
    private Rectangle rectangle;
    private ArrayList<Brick> bricks;


    private Paddle paddle;

    private Vector2 direction = new Vector2();

    public PlayerBall (Paddle paddle) {
        ballImage = new TextureRegion(new Texture(Gdx.files.internal("ball.png")));
        setBounds(ballImage.getRegionX(), ballImage.getRegionY(), ballImage.getRegionWidth(), ballImage.getRegionHeight());
        direction.x = 1;
        direction.y = 1;
        rectangle = new Rectangle(0, 0, getWidth(), getHeight());
        this.paddle = paddle;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        rectangle.x = getX();
        rectangle.y = getY();
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(ballImage, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void start() {
        canAct = true;
    }

    public void stopAndReset() {
        canAct = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (canAct) {
            move(delta);
        }

    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    private void move(float delta) {

        checkCollisions();

        if (getX() > Const.CAMERA_WIDTH - getWidth()) {
            direction.x = -1;
        }
        if (getX() < 0) {
            direction.x = 1;
        }
        if (getY() < 0) {
            direction.y = 1;
        }
        if (getY() > Const.CAMERA_HEIGHT - getHeight()) {
            direction.y = -1;
        }
        direction = direction.nor();
        setPosition(getX() + speed * direction.x * delta, getY() + speed * direction.y * delta);
    }

    private void checkCollisions() {
        if (rectangle.overlaps(paddle.rectangle)) {
            direction.y = 1;
        }
//        for (Brick b : bricks) {
//            if (rectangle.overlaps(b.rectangle)) {
//                if (!b.inCollision) {
//                    b.inCollision = true;
//                    System.out.println("Ball collide with brick");
//                    bricks.remove(b);
//
//                    if (getY() > b.getY()) {
//                        direction.y = 1;
//                    } else {
//                        direction.y = -1;
//                    }
//                    b.remove();
//                    b.inCollision = false;
//                }
//
//            }
//        }

    }
}
