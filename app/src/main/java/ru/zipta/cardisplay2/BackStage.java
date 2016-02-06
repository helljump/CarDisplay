package ru.zipta.cardisplay2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class BackStage extends Stage {

    private static final String TAG = BackStage.class.getSimpleName();
    private final Texture bg;

    public BackStage(Viewport vp) {
        super(vp);
        bg = Assets.instance.background;
    }

    @Override
    public void draw() {
        Batch b = getBatch();
        b.begin();
        b.draw(bg, 0, 0, getWidth(), getHeight());
        b.end();
        super.draw();
    }
}
