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
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Levels implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton backButton;
    private Game game;
    private Image backgroundImage; // Image actor for background
    private boolean[] levelUnlocked;

    // Locked icon texture
    private Texture lockedIconTexture;

    public Levels(Game game) {
        this.game = game;
        // Initialize the unlock state (Level 1 is unlocked, others are locked)
        levelUnlocked = new boolean[20];
        levelUnlocked[0] = true; // Unlock Level 1
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load the background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("background1.jpeg"));
        backgroundImage = new Image(backgroundTexture);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        // Load the locked icon texture
        lockedIconTexture = new Texture(Gdx.files.internal("locked_icon.png")); // Ensure you have this image in your assets

        // Add background image to the stage first so it appears behind other UI elements
        stage.addActor(backgroundImage);

        // Create the main table
        table = new Table();
        table.setFillParent(true);

        // Add heading
        Label heading = new Label("SELECT LEVEL", skin,"title");
        heading.setFontScale(2);
        table.add(heading).padBottom(30);
        table.row();

        // Create a grid for level buttons
        Table levelsTable = new Table();
        int levelsPerRow = 4; // Number of levels per row
        float buttonSize = 100; // Set a fixed size for buttons

        for (int i = 0; i < 3; i++) { // Iterate through all 20 levels
            final int level = i + 1; // Level number (1 to 20)
            TextButton playButton = new TextButton("", skin); // Empty text initially

            // If the level is locked, use the locked icon
            if (!levelUnlocked[i]) {
                playButton.add(new Image(lockedIconTexture)); // Add locked icon
                playButton.setDisabled(true); // Disable the button for locked levels
            } else {
                playButton.setText("" + level); // Set the level number as text
                playButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Switch to the Gameplay screen when the level is clicked
                        game.setScreen(new Gameplay(game, Levels.this));
                    }
                });
            }

            // Set the button size
            playButton.setSize(buttonSize, buttonSize); // Use fixed button size

            // Scale label font for better visibility
            playButton.getLabel().setFontScale(3f);

            // Add the button to the levels table
            levelsTable.add(playButton).size(buttonSize, buttonSize).pad(10); // Set fixed size with padding

            // Move to the next row after a specified number of levels
            if ((i + 1) % levelsPerRow == 0) {
                levelsTable.row(); // Create a new row after every `levelsPerRow` buttons
            }
        }

        // Add levels table to the main table
        table.add(levelsTable).center().padBottom(30);
        table.row();

        // Back button to return to the Main Menu
        backButton = new TextButton("Back", skin);
        backButton.pad(10);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });

        table.add(backButton).padBottom(20);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        lockedIconTexture.dispose(); // Dispose of the locked icon texture
        // backgroundImage.dispose(); // Dispose of the background texture
    }
}
