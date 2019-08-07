package net.ltslab.games.breakoutgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Brick extends Actor {

    private Vector2 position;

    private boolean destroyed;

    private TextureRegion image;

    public Brick (Vector2 position) {
        this.position = position;
        image = new TextureRegion(new Texture(Gdx.files.internal("brick.png")));
        setBounds(image.getRegionX(), image.getRegionY(), image.getRegionWidth(), image.getRegionHeight());
        setDestroyed(false);

    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
