package ru.zipta.cardisplay2;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


public class Speedometer extends Meter{

    private final TextureAtlas.AtlasRegion bg;
    private final Sprite arrow;
    private final BitmapFont font;
    private String speed;

    public Speedometer(float x, float y){
        super();
        setPosition(x, y);
        bg = Assets.instance.indicators_atlas.findRegion("speed");
        arrow = new Sprite(Assets.instance.indicators_atlas.findRegion("arrow"));
        arrow.setPosition(getX() + 179, getY() + 210);
        arrow.setOrigin(39, 39);
        font = Assets.instance.lcd_font;
        setBounds(x, y, bg.originalWidth, bg.originalHeight);
        currentValue = 0;
        value = 200;
        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stateMachine.changeState(MeterState.GROW);
                return false;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void setValue(int speed){
        value = speed;
    }

    public void draw(Batch batch, float parentAlpha){
        batch.draw(bg, getX(), getY());
        arrow.setRotation((float) -1.36 * currentValue + 136);
        arrow.draw(batch);
        font.draw(batch, String.format("%03d", (int)currentValue), getX()+190, getY() + 115);
        super.draw(batch, parentAlpha);
    }

}
