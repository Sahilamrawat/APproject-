package com.angrybird.game;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

        // Load and set the background image
        backgroundImage = new Image(new Texture(Gdx.files.internal("background.jpg")));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0); // Position at the bottom-left corner
        stage.addActor(backgroundImage);  // Add background image

        // Initialize overlay image
        overlayImage = new Image(new Texture(Gdx.files.internal("profile.jpeg")));
        overlayImage.setColor(1, 1, 1, 0); // Initially invisible
        overlayImage.setSize(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2); // Set size
        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2, (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2); // Center it
        stage.addActor(overlayImage); // Add overlay image

        // Create table for layout
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create labels for Player Name, Player Level, Player Age
        playerNameLabel = new Label("Player Name: Mavrick", skin,"button");
        playerLevelLabel = new Label("Player Level: 1", skin,"button");
        playerAgeLabel = new Label("Player Age: 21", skin,"button");

        // Add heading label and player information to the table
        table.add(new Label("PROFILE", skin,"title1")).padBottom(30).row();
        table.add(playerNameLabel).padBottom(10).row();
        table.add(playerLevelLabel).padBottom(10).row();
        table.add(playerAgeLabel).padBottom(10).row();

        // Create Update Profile button
        TextButton updateProfileButton = new TextButton("Update Profile", skin);
        updateProfileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic to update player profile fields
                playerNameLabel.setText("Player Name: Updated Name");
                playerLevelLabel.setText("Player Level: Updated Level");
                playerAgeLabel.setText("Player Age: Updated Age");
                System.out.println("Profile updated");
            }
        });
        table.add(updateProfileButton).padTop(20).row();

        // Create Back button to go back to the Main Menu screen
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition back to main menu
                ((Game) Gdx.app.getApplicationListener()).setScreen(mainMenuScreen);
            }
        });
        table.add(backButton).padTop(10).row();

        // Fade in overlay when the Profile screen is shown

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
        overlayImage.setSize(width / 2, height / 2);
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
//        backgroundImage.getTexture().dispose(); // Dispose background texture
//        overlayImage.getTexture().dispose(); // Dispose overlay texture
    }
}
