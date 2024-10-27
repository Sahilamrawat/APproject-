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

public class Pause implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;
    private Gameplay gameplayScreen; // Reference to Gameplay screen
    private Image backgroundImage;
    private ImageTextButton resumeButton, exitButton, restartButton, saveButton,menuButton;
    private Texture resumeTexture, exitTexture, restartTexture, saveTexture,menuTexture;
    private Image buttonImage1, buttonImage2, buttonImage3, buttonImage4,buttonImage5,overlayImage;

    private Table table; // Table to arrange buttons and background

    public Pause(Game game, Gameplay gameplayScreen) {
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
        resumeTexture = new Texture(Gdx.files.internal("resume.png"));
        exitTexture = new Texture(Gdx.files.internal("exit3.png"));
        restartTexture = new Texture(Gdx.files.internal("restart.png"));
        saveTexture = new Texture(Gdx.files.internal("save.png"));
        menuTexture = new Texture(Gdx.files.internal("menu1.png"));

        // Play button
        resumeButton = createImageTextButton(resumeTexture, 80, 80);
        // Exit button
        exitButton = createImageTextButton(exitTexture, 80, 80);
        // Load button
        restartButton = createImageTextButton(restartTexture, 80, 80);
        // Profile button
        saveButton = createImageTextButton(saveTexture, 90, 90);
        menuButton = createImageTextButton(menuTexture, 80, 80);

        overlayImage = new Image(new Texture(Gdx.files.internal("profilebackground.png")));
//        overlayImage.setColor(1, 1, 1, 0); // Initially invisible
//        overlayImage.setSize(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2+100); // Set size
//        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2, (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2); // Center it
        // Setting the size with a reduced width
        float reducedWidth = 100; // You can change 6 to another divisor for your desired width
        float height = 600; // Keep height as desired

        overlayImage.setSize(reducedWidth, height); // Set the size with the new width
        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2,
            (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2); // Center it


        stage.addActor(overlayImage); // Add overlay image




        // Create buttons for settings options


        // Create a table to hold the buttons and background image
        table = new Table();

        table.setSize(200, 650); // Adjust to fit buttons

        // Center the table based on the current screen dimensions
        table.setPosition(
            Gdx.graphics.getWidth() / 2 - table.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - table.getHeight() / 2
        );

        // Add the buttons to the table
        table.add(new Label("ANGRY BIRD", skin,"title")).padBottom(20).row();
        table.add(resumeButton).padBottom(5).row();
        table.add(restartButton).padBottom(5).row();
        table.add(saveButton).padBottom(5).row();
        table.add(menuButton).padBottom(5).row();
        table.add(exitButton).padBottom(5);

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

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.setPaused(false); // Resume gameplay before going back
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu()); // Go back to gameplay
            }
        });
        addHoverEffect(buttonImage1, resumeButton);
        addHoverEffect(buttonImage2, exitButton);
        addHoverEffect(buttonImage3, restartButton);
        addHoverEffect(buttonImage4, saveButton);
        addHoverEffect(buttonImage5, menuButton);
    }

    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        if (texture == resumeTexture) {
            buttonImage1 = buttonImage;
        } else if (texture == exitTexture) {
            buttonImage2 = buttonImage;
        } else if (texture == restartTexture) {
            buttonImage3 = buttonImage;
        } else if (texture == saveTexture) {
            buttonImage4 = buttonImage;
        }
        else if (texture == menuTexture) {
            buttonImage5 = buttonImage;
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
        overlayImage.setSize(width / 2-390, height / 2+140);
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
