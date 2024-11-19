package com.angrybird.game;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import tween.SpriteAccessor;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    private SpriteBatch batch;
    private Sprite splash;
    private TweenManager tweenManager;
    private Texture backgroundTexture; // Texture for the background
    private Sprite backgroundSprite;
    @Override
    public void show() {
        // Initialize the SpriteBatch
        batch = new SpriteBatch();

        // Initialize the TweenManager for animations
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        // Load the splash texture
        Texture splashTexture = new Texture("AngryBird.png");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Adjust splash size to screen
        splash.setAlpha(0); // Start with transparent splash image

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 2).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Gameplay());
            }
        }).start(tweenManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tweenManager.update(delta);
        batch.begin();


        splash.draw(batch);
        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        float desiredWidth = 1280f;
        float desiredHeight = 320f;
        splash.setSize(desiredWidth, desiredHeight);

        // Calculate the center position based on the new size of the splash image
        float centerX = (width - splash.getWidth()) / 2;
        float centerY = (height - splash.getHeight()) / 2;

        splash.setPosition(centerX, centerY);
    }



    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        batch.dispose();
        splash.getTexture().dispose();

    }
}
