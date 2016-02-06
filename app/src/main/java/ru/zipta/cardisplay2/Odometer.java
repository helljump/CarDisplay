package ru.zipta.cardisplay2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Odometer extends Actor {

    private final TextureAtlas.AtlasRegion bg;
    private final Sprite i1;
    private final TextureRegion i2;
    private final TextureRegion i3;

    public Odometer(float x, float y){
        setPosition(x, y);
        bg = Assets.instance.indicators_atlas.findRegion("odo");

        i1 = new Sprite(Assets.instance.icons[0][0]);
        i1.setPosition(getX()+245, getY()+120);

        i2 = Assets.instance.icons[0][1];
        i3 = Assets.instance.icons[0][2];
    }

    public void draw(Batch batch, float parentAlpha){
        batch.draw(bg, getX(), getY());
        i1.draw(batch, 0.1f);
        batch.draw(i2, getX()+245, getY()+200);
        batch.draw(i3, getX()+245, getY()+280);
        super.draw(batch, parentAlpha);
    }
}
