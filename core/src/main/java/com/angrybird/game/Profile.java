package com.angrybird.game;

import aurelienribon.tweenengine.Tween;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Profile implements Screen {
    private Stage stage;
    private Skin skin;
    private Image backgroundImage;
    private Game game;
    private Screen mainMenuScreen;

    // Labels for player information
    private Label playerNameLabel;
    private Label playerLevelLabel;
    private Label playerAgeLabel;
    private Table table;

    private ImageTextButton updateButton, backButton;
    private Texture updateTexture, backTexture;

    private Image buttonImage1, buttonImage2;
    // Overlay image
    private Image overlayImage;

    public Profile(Game game, Screen mainMenuScreen) {
        this.game = game;
        this.mainMenuScreen = mainMenuScreen;
    }

    @Override
    public void show() {
        // Set up the stage and input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        updateTexture = new Texture(Gdx.files.internal("update.png"));
        backTexture = new Texture(Gdx.files.internal("back.png"));


        // Load and set the background image
        backgroundImage = new Image(new Texture(Gdx.files.internal("background2.jpg")));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0); // Position at the bottom-left corner
        stage.addActor(backgroundImage);

        // Initialize overlay image
        overlayImage = new Image(new Texture(Gdx.files.internal("profilebackground.png")));
//
        float reducedWidth = Gdx.graphics.getWidth() / 6;
        float height = Gdx.graphics.getHeight() / 2 + 100;

        overlayImage.setSize(reducedWidth, height); // Set the size with the new width
        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2,
                                 (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2); // Center it


        stage.addActor(overlayImage);

        table = new Table();
        table.setFillParent(true);


        updateButton = createImageTextButton(updateTexture, 80, 80);
        // Exit button
        backButton = createImageTextButton(backTexture, 80, 80);




        playerNameLabel = new Label("Player Name: Mavrick", skin,"button");
        playerLevelLabel = new Label("Player Level: 1", skin,"button");
        playerAgeLabel = new Label("Player Age: 21", skin,"button");

        // Add heading label and player information to the table
        table.add(new Label("PROFILE", skin,"title1")).padBottom(30).row();
        table.add(playerNameLabel).padBottom(10).row();
        table.add(playerLevelLabel).padBottom(10).row();
        table.add(playerAgeLabel).padBottom(10).row();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition back to main menu
                ((Game) Gdx.app.getApplicationListener()).setScreen(mainMenuScreen);
            }
        });

        updateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic to update player profile fields
                playerNameLabel.setText("Player Name: Updated Name");
                playerLevelLabel.setText("Player Level: Updated Level");
                playerAgeLabel.setText("Player Age: Updated Age");
                System.out.println("Profile updated");
            }
        });
        table.add(updateButton).padTop(20).row();
        table.add(backButton).padTop(10).row();
        stage.addActor(table);
        addHoverEffect(buttonImage1, updateButton);
        addHoverEffect(buttonImage2, backButton);
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
    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        if (texture == updateTexture) {
            buttonImage1 = buttonImage;
        } else if (texture == backTexture) {
            buttonImage2 = buttonImage;
        }

        return button;

    }

    @Override
    public void render(float delta) {
        // Clear the screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the profile overlay
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // Update background image size to match the new screen dimensions
        backgroundImage.setSize(width, height);
        overlayImage.setSize(width / 2-30, height / 2+80);
        overlayImage.setPosition((width - overlayImage.getWidth()) / 2, (height - overlayImage.getHeight()) / 2);
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
