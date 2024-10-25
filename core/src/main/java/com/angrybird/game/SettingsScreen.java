package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;
    private Gameplay gameplayScreen; // Reference to Gameplay screen
    private Image backgroundImage;
    private Table table; // Table to arrange buttons and background

    public SettingsScreen(Game game, Gameplay gameplayScreen) {
        this.game = game;
        this.gameplayScreen = gameplayScreen; // Store reference to the Gameplay screen
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        // Create a semi-transparent overlay (background color for overlay effect)
        Table overlayTable = new Table();
        overlayTable.setFillParent(true);
        overlayTable.setColor(0, 0, 0, 0.7f); // Black color with 70% transparency
        stage.addActor(overlayTable); // Adding semi-transparent background overlay

        // Load and set partial background image for the settings menu area only
        backgroundImage = new Image(new Texture(Gdx.files.internal("Settingbackground.jpg")));
        backgroundImage.setSize(400, 600); // Set a smaller size for the menu area

        // Create buttons for settings options
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton restartButton = new TextButton("Restart", skin);
        TextButton exitButton = new TextButton("Exit Game", skin);
        TextButton saveButton = new TextButton("Save Game", skin);
        TextButton mainMenuButton = new TextButton("Main Menu", skin);

        // Create a table to hold the buttons and background image
        table = new Table();

        table.setSize(400, 600); // Adjust to fit buttons

        // Center the table based on the current screen dimensions
        table.setPosition(
            Gdx.graphics.getWidth() / 2 - table.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - table.getHeight() / 2
        );

        // Add the buttons to the table
        table.add(new Label("ANGRY BIRD", skin,"title")).padBottom(30).row();
        table.add(resumeButton).padBottom(10).row();
        table.add(restartButton).padBottom(10).row();
        table.add(saveButton).padBottom(10).row();
        table.add(mainMenuButton).padBottom(10).row();
        table.add(exitButton).padBottom(10).row();

        stage.addActor(table); // Add the table with buttons to the stage

        // Add listeners for each button
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.setPaused(false); // Resume gameplay
                ((Game)Gdx.app.getApplicationListener()).setScreen(gameplayScreen); // Go back to gameplay
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.restartGame(); // Call the restart method
                gameplayScreen.setPaused(false); // Resume gameplay after restart
                ((Game)Gdx.app.getApplicationListener()).setScreen(gameplayScreen); // Go back to gameplay
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the game
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Implement save game logic
                System.out.println("Game Saved");
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.setPaused(false); // Resume gameplay before going back
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu()); // Go back to gameplay
            }
        });
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 0); // Keep transparent
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the underlying gameplay screen (as if it's paused and visible)
        gameplayScreen.render(delta); // Render gameplay behind the overlay

        // Draw the stage with the settings menu
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // Recalculate and set the position of the table when resizing
        table.setPosition(
            width / 2 - table.getWidth() / 2,
            height / 2 - table.getHeight() / 2
        );
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
