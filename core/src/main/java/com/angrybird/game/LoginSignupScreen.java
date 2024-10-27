package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoginSignupScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;

    // Buttons for login and signup
    private ImageTextButton loginButton;
    private ImageTextButton signupButton;
    private Texture loginTexture, signupTexture;
    private Image buttonImage1, buttonImage2;
    public LoginSignupScreen(Game game) {
        this.game = game;
    }

    public LoginSignupScreen() {
    }

    @Override
    public void show() {
        // Set up stage and input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        loginTexture = new Texture(Gdx.files.internal("login.png"));
        signupTexture = new Texture(Gdx.files.internal("signup.png"));

        // Background image
        Image backgroundImage = new Image(new Texture(Gdx.files.internal("background2.jpg")));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);

        // Create table for layout and center it
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        loginButton = createImageTextButton(loginTexture, 80, 80);
        // Exit button
        signupButton = createImageTextButton(signupTexture, 80, 80);
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to LoginScreen
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game, LoginSignupScreen.this));
            }
        });

        // Create signup button
        signupButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to SignupScreen
                ((Game)Gdx.app.getApplicationListener()).setScreen(new SignupScreen(game, LoginSignupScreen.this));
            }
        });

        // Add buttons to the table with padding
        table.add(loginButton).padBottom(20).row();
        table.add(signupButton).padTop(20);

        // Add the table to the stage
        stage.addActor(table);
        addHoverEffect(buttonImage1, loginButton);
        addHoverEffect(buttonImage2, signupButton);
    }
    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        if (texture == loginTexture) {
            buttonImage1 = buttonImage;
        } else if (texture == signupTexture) {
            buttonImage2 = buttonImage;
        }

        return button;

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
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
