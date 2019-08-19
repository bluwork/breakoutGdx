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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Paddle extends Actor {

    TextureRegion image;
    public Rectangle rectangle;

    public Paddle() {
        image = new TextureRegion(new Texture(Gdx.files.internal("paddle.png")));
        setBounds(image.getRegionX(), image.getRegionY(), image.getRegionWidth(), image.getRegionHeight());
        rectangle = new Rectangle(0, 0, getWidth(),getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        handleInput();
        rectangle.x = getX();
        rectangle.y = getY();
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(image, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                Gdx.app.log("Paddle", "Left");
            setPosition(getX() - 10, getY());

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                Gdx.app.log("Paddle", "Right");
            setPosition(getX() + 10, getY());
        }
    }
}
