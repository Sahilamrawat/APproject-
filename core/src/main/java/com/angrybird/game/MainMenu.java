package com.angrybird.game;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import tween.ActorAccessor;

public class MainMenu implements Screen {
    private Game game;
    private Skin skin;
    private Stage stage;
    private Table table;
    private TextButton buttonPlay, buttonExit, buttonLoad, profile;
    private Label heading;
    private BitmapFont white, black;
    private TextureAtlas atlas;

    private Image backgroundImage;
    private TweenManager tweenManager;

    @Override
    public void show() {
        // Load the texture atlas and skin
        stage = new Stage(new ScreenViewport());

        // Load the background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("background.jpg"));
        backgroundImage = new Image(backgroundTexture);

        // Initially set the alpha of the background image to 0 (invisible)
        backgroundImage.getColor().a = 0f;

        stage.addActor(backgroundImage); // Add the background image to the stage first

        // Load the texture atlas and skin
        atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        table = new Table(skin);
        table.setFillParent(true);

        // Create buttons
        buttonExit = new TextButton("EXIT", skin);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonExit.pad(15);

        buttonPlay = new TextButton("PLAY", skin);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Levels((Game) Gdx.app.getApplicationListener()));
            }
        });
        buttonPlay.pad(15);

        buttonLoad = new TextButton("LOAD GAME", skin);
        buttonLoad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGame(game));
            }
        });
        buttonLoad.pad(15);

        profile = new TextButton("PROFILE", skin);
        profile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Profile(game, MainMenu.this));
            }
        });
        profile.pad(15);

        heading = new Label("MAIN MENU", skin,"title");
        heading.setFontScale(2);

        // Set up the table layout
        table.add(heading).colspan(2); // Center the heading across two columns
        table.row().padBottom(60);

        // Add buttons to the table
        table.add(buttonLoad).left(); // Load Game button at the top left
        table.add(profile).right(); // Profile button at the top right
        table.row().padBottom(20);
        table.add(buttonPlay).colspan(2).center(); // Center Play button
        table.row();
        table.add(buttonExit).colspan(2).center(); // Center Exit button

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        // Animate the heading's color
        Timeline.createSequence().beginSequence()
            .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 0, 1))
            .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 0))
            .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 0))
            .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 0))
            .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 1))
            .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 1))
            .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 1))
            .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 0))
            .end().repeat(Tween.INFINITY, .5f).start(tweenManager);

        // Animate fade-in for buttons
        Timeline.createSequence().beginSequence()
            .push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(buttonLoad, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(profile, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
            .push(Tween.from(heading, ActorAccessor.ALPHA, 0.25f).target(0))
            .push(Tween.to(buttonPlay, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(buttonLoad, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(profile, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(buttonExit, ActorAccessor.ALPHA, 0.25f).target(1))
            .end().start(tweenManager);

        // Animate fade-in for the background image after a 0.5-second delay
        Tween.to(backgroundImage, ActorAccessor.ALPHA, 1f)
            .target(1) // Fully visible
            .delay(0.1f)
            .start(tweenManager);
    }


    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tweenManager.update(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // Resize background to fit the new screen size
        backgroundImage.setSize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        stage.dispose();
    }
}
