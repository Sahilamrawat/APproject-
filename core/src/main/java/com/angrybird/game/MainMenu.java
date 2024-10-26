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
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputListener;

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
    private ImageTextButton playButton, exitButton, loadButton, profileButton;
    private Texture playTexture, exitTexture, loadTexture, profileTexture;
    private Image buttonImage1, buttonImage2, buttonImage3, buttonImage4;

    private Image backgroundImage;
    private TweenManager tweenManager;

    @Override
    public void show() {
        // Initialize the stage, skin, and atlas
        stage = new Stage(new ScreenViewport());
        atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        // Load textures for each button
        playTexture = new Texture(Gdx.files.internal("play.png"));
        exitTexture = new Texture(Gdx.files.internal("exit.png"));
        loadTexture = new Texture(Gdx.files.internal("load.png"));
        profileTexture = new Texture(Gdx.files.internal("profile.png"));

        // Play button
        playButton = createImageTextButton(playTexture, 250, 250);
        // Exit button
        exitButton = createImageTextButton(exitTexture, 80, 80);
        // Load button
        loadButton = createImageTextButton(loadTexture, 80, 80);
        // Profile button
        profileButton = createImageTextButton(profileTexture, 50, 50);

        // Add listeners for each button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Levels(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGame(game));
            }
        });

        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Profile(game, MainMenu.this));
            }
        });

        // Load the background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("background.jpg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.getColor().a = 0f;
        stage.addActor(backgroundImage);

        // Set up the table layout
        table = new Table(skin);

        table.setFillParent(true);

        // Create the heading
        heading = new Label("Angry Bird !!!", skin, "title1");
        heading.setFontScale(2);

        // Add heading and buttons to the table
        table.add(heading).colspan(2).padBottom(10).center();
        table.row();
        table.add(playButton).colspan(2).center().padBottom(10); // Center Play button
        table.row();
        table.add(loadButton).colspan(2).padBottom(20).center();    // Load Game button on the left
        table.row();
        table.add(exitButton).colspan(2).center(); // Center Exit button

        profileButton.setPosition(
            Gdx.graphics.getWidth() - 100, // 20 pixels from the right edge
            Gdx.graphics.getHeight()  - 100 // 20 pixels from the top edge
        );
        stage.addActor(profileButton);
        stage.addActor(table);

        // Set up input processor
        Gdx.input.setInputProcessor(stage);

        // Initialize tween manager and animations
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        // Animate heading color
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

        // Animate fade-in for buttons and background
        Timeline.createSequence().beginSequence()
            .push(Tween.set(playButton, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(loadButton, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(profileButton, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(exitButton, ActorAccessor.ALPHA).target(0))
            .push(Tween.from(heading, ActorAccessor.ALPHA, 0.25f).target(0))
            .push(Tween.to(playButton, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(loadButton, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(profileButton, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(exitButton, ActorAccessor.ALPHA, 0.25f).target(1))
            .end().start(tweenManager);

        // Background image fade-in
        Tween.to(backgroundImage, ActorAccessor.ALPHA, 1f)
            .target(1)
            .delay(0.1f)
            .start(tweenManager);

        // Add hover effects
        addHoverEffect(buttonImage1, playButton);
        addHoverEffect(buttonImage2, exitButton);
        addHoverEffect(buttonImage3, loadButton);
        addHoverEffect(buttonImage4, profileButton);
    }

    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        if (texture == playTexture) {
            buttonImage1 = buttonImage;
        } else if (texture == exitTexture) {
            buttonImage2 = buttonImage;
        } else if (texture == loadTexture) {
            buttonImage3 = buttonImage;
        } else if (texture == profileTexture) {
            buttonImage4 = buttonImage;
        }

        return button;

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

    private void addHoverEffect(final Image image, final ImageTextButton button) {
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; // return true to handle the event
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Scale up the image on hover
                image.setScale(1.1f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Scale down the image when not hovered
                image.setScale(1f);
            }
        });
    }

    @Override
    public void hide() {
        // Dispose of resources
        stage.dispose();
        playTexture.dispose();
        exitTexture.dispose();
        loadTexture.dispose();
        profileTexture.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
