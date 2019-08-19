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

public class Brick extends Actor {

    private boolean destroyed;
    private TextureRegion image;
    public Rectangle rectangle;
    public boolean inCollision;

    public Brick () {
        image = new TextureRegion(new Texture(Gdx.files.internal("brick.png")));
        setBounds(image.getRegionX(), image.getRegionY(), image.getRegionWidth(), image.getRegionHeight());
        setDestroyed(false);
        rectangle = new Rectangle(0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        rectangle.x = localToStageCoordinates(new Vector2(getX(), getY())).x;
        rectangle.y = localToStageCoordinates(new Vector2(getX(), getY())).y;
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(image, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

}
