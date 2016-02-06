package ru.zipta.cardisplay2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Disposable;


class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getSimpleName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;
    public TextureAtlas indicators_atlas;
    public Texture background;
    public TextureRegion[][] icons;
    public BitmapFont lcd_font;

    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter titleParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        titleParams.fontFileName = "LCD-N___.TTF";
        titleParams.fontParameters.size = 35;
        titleParams.fontParameters.characters = "0123456789";
        assetManager.load("lcd.ttf", BitmapFont.class, titleParams);

        assetManager.load("sprites.atlas", TextureAtlas.class);
        assetManager.load("back.png", Texture.class);

        assetManager.load("icons.png", Texture.class);

        assetManager.finishLoading();
        update();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public float getProgress(){
        return assetManager.getProgress();
    }

    public boolean update(){
        if(!assetManager.update()){
            return false;
        }
        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        indicators_atlas = assetManager.get("sprites.atlas", TextureAtlas.class);
        background = assetManager.get("back.png", Texture.class);
        Texture egg = assetManager.get("icons.png", Texture.class);
        icons = TextureRegion.split(egg, 63, 63);
        lcd_font = assetManager.get("lcd.ttf", BitmapFont.class);

        return true;
    }

}
