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
import net.ltslab.games.breakoutgdx.helper.BodyData;

public class Brick extends Actor {

    private boolean destroyed;
    private TextureRegion image;
    public boolean inCollision;
    private Body body;
    private World world;

    public Brick (World world) {
        image = new TextureRegion(new Texture(Gdx.files.internal("brick.png")));
        setBounds(0, 0, image.getRegionWidth()/Const.SCALE, image.getRegionHeight()/Const.SCALE);
        setDestroyed(false);
        this.world = world;
    }

    public void createBody(Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);
        BodyData data = new BodyData("Brick", this);
        body.setUserData(data);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(getWidth()/2, getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 0.5f;

        body.createFixture(fixtureDef);

        rectangle.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(image, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        update();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
    private void update() {

        Vector2 position = body.getPosition();

        setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        setPosition(position.x - getWidth() / 2, position.y - getHeight() / 2);

    }

}
