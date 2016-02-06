package ru.zipta.cardisplay2;

import android.content.Context;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CarDisplay extends Game {

    public final static int VIEWPORT_WIDTH = 1280;
    public final static int VIEWPORT_HEIGHT = 480;
	private MenuScreen screen;
	private Context cntx;

	public CarDisplay(Context cntx){
		super();
		this.cntx = cntx;
	}

	@Override
    public void create() {
		Assets.instance.init(new AssetManager());
		screen = new MenuScreen();
		setScreen(screen);
	}

	@Override
	public void dispose() {
		Assets.instance.dispose();
		super.dispose();
	}

}
