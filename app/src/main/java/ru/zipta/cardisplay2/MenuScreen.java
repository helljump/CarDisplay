package ru.zipta.cardisplay2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

class MenuScreen implements Screen {

    private static final String TAG = MenuScreen.class.getSimpleName();

    private Stage bgstage;
    private Speedometer speedometer;
    private Tachometer tachometer;
    private EventBus eventBus = EventBus.getDefault();
    private BitmapFont font;
    private String log = "";

    @Override
    public void render(float deltaTime) {
        bgstage.act(deltaTime);
        bgstage.draw();
        final Batch b = bgstage.getBatch();
        b.begin();
        font.draw(b, log, 10, 10);
        b.end();
    }

    @Override
    public void resize(int width, int height) {
        bgstage.getViewport().update(width, height);
    }

    @Override
    public void show() {
        font = new BitmapFont();
        font.setColor(Color.RED);
        bgstage = new BackStage(new FitViewport(CarDisplay.VIEWPORT_WIDTH, CarDisplay.VIEWPORT_HEIGHT));
        Gdx.input.setInputProcessor(bgstage);
        speedometer = new Speedometer(55, 0);
        bgstage.addActor(speedometer);
        tachometer = new Tachometer(785, 0);
        bgstage.addActor(tachometer);
        bgstage.addActor(new Odometer(365, 0));
        eventBus.register(this);
    }

    @Override
    public void hide() {
        eventBus.unregister(this);
        bgstage.dispose();
        font.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resume() {

    }


    public void onEventMainThread(AndroidLauncher.MessageEvent event) {
        final String egg;
        try {
            egg = new String(event.data, "UTF-8");
            log = egg;
            Gdx.app.debug(TAG, "" + egg);
            JSONObject spam = new JSONObject(egg);
            speedometer.setValue(spam.getInt("0"));
            tachometer.setValue(spam.getInt("1"));
        } catch (JSONException | UnsupportedEncodingException e) {
            Gdx.app.debug(TAG, "" + e);
        }
    }

}


