package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Gameplay implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;
    private Image backgroundImage;
    private Label pointsLabel; // Label to display points
    private int points = 10;    // Points variable
    private Screen previousScreen; // Store reference to the previous screen
    private ImageTextButton settingsButton; // Class-level variable for the settings button
    private boolean isPaused = false;

    private Texture settingTexture;

    public Gameplay(Game game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen; // Set the previous screen
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        settingTexture = new Texture(Gdx.files.internal("settingButton.png"));

        // Create and add the background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("gameplayBackground.jpg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Set size to full screen
        stage.addActor(backgroundImage);  // Add the background image first, so it's behind everything

        // Create a label to display points in the top-left corner
        pointsLabel = new Label("Points: " + points, skin, "title");
        pointsLabel.setPosition(20, Gdx.graphics.getHeight() - 50); // Set position to top-left
        stage.addActor(pointsLabel);

        // Create a TextButtonStyle using the skin
        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle();
        buttonStyle.up = skin.getDrawable("button"); // Set the up drawable from your skin
        buttonStyle.down = skin.getDrawable("button-down"); // Set the down drawable from your skin
        buttonStyle.font = skin.getFont("button"); // Set the font from your skin
        buttonStyle.fontColor = skin.getColor("button"); // Set the font color from your skin

        // Create the settings button using ImageTextButton
        settingsButton = new ImageTextButton("", buttonStyle); // Set the text to an empty string

        // Create an Image for the settings texture with a larger size
        Image settingsImage = new Image(settingTexture);
        settingsImage.setSize(100, 100); // Set size for the image
        settingsImage.setScaling(Scaling.fill); // Ensure the image fills the space without distortion

        settingsButton.add(settingsImage).expand().fill(); // Add the image to the button
        settingsButton.setSize(60, 60); // Set the button size to be square
        updateSettingsButtonPosition();  // Initially set the position
        stage.addActor(settingsButton);

        // Create Back button at the bottom-left corner
        TextButton backButton = new TextButton("Back", skin);
        backButton.setPosition(20, 20); // Positioning the back button
        stage.addActor(backButton);

     // Array of circular bird images (ensure these images are circular and in your assets)
        Bird[] birds = new Bird[]{
        	    new redbird(50, 92),  // Specify position for redbird
        	    new yellowbird(110, 102), // Specify position for yellowbird
        	    new bluebird(170, 97), // Specify position for bluebird
        	    new blackbird(230, 102) // Specify position for blackbird
        };

        	// Add each bird actor to the stage
        for (Bird bird : birds) {
            if (bird instanceof Actor) {
                stage.addActor((Actor) bird);
            }
        }

        
        // Add listener to Settings button to navigate to the Settings screen
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Pause the gameplay and navigate to the SettingsScreen
                setPaused(true); // Pause the gameplay
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsScreen(game, Gameplay.this)); // Show settings screen
            }
        });

        // Add listener to Back button to navigate back to the previous screen
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Navigate back to the previous screen
                ((Game) Gdx.app.getApplicationListener()).setScreen(previousScreen);
            }
        });
        
     // Create an instance of the Catapult
        Catapult catapult = new Catapult("catapult.png", 250, 102); // Example position at (100, 100)

        // Create an Image to display the catapult (or use an ImageButton if you want it to be clickable)
        TextureRegionDrawable catapultDrawable = new TextureRegionDrawable(new TextureRegion(catapult.getTexture()));
        Image catapultImage = new Image(catapultDrawable);

        // Set the position of the catapult image on the stage based on its coordinates
        catapultImage.setPosition(catapult.getX(), catapult.getY());
        catapultImage.setSize(100, 100); // Set the size as required

        // Add listener if you want to handle clicks on the catapult (optional)
        catapultImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Catapult clicked!");
                catapult.launchBird(); // Call the catapult action method
            }
        });

        // Add the catapult to the stage
        stage.addActor(catapultImage);

    }
    
 // Method to update positions based on screen size
    

    // Method to update the points label
    private void increasePoints(int amount) {
        points += amount;
        pointsLabel.setText("Points: " + points); // Update the label's text
    }

    // Method to update the Settings button position (top-right corner)
    private void updateSettingsButtonPosition() {
        settingsButton.setPosition(Gdx.graphics.getWidth() - settingsButton.getWidth() - 20,
            Gdx.graphics.getHeight() - settingsButton.getHeight() - 20);
    }

    public void restartGame() {
        points = 0; // Reset points
        pointsLabel.setText("Points: " + points); // Update label
        // Any other logic to reset game state can be added here
        System.out.println("Game Restarted"); // Log restart action
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // If the game is paused, do not update gameplay logic
        if (isPaused) {
            stage.act(); // Allow the stage to act (process input for the settings screen)
        } else {
            // Normal rendering and update logic
            stage.act(delta); // Update the stage
        }

        // Draw the stage
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        backgroundImage.setSize(width, height); // Update background size when screen is resized
        updateSettingsButtonPosition();  // Update the settings button position when resized
        pointsLabel.setPosition(20, height - 50); // Update the points label position when resized
     // Update the bird buttons positions when resized
        float buttonStartY = height - 90; // Start position below the points label
        int buttonIndex = 0;

        // Loop through actors to find and update ImageButtons (excluding the settings button)
        for (Actor actor : stage.getActors()) {
            if (actor instanceof ImageButton && actor != settingsButton) {
                ImageButton birdButton = (ImageButton) actor;
                // Update the position of each button dynamically based on the screen height
                birdButton.setPosition(20, buttonStartY - buttonIndex * 70);
                buttonIndex++;
            }
        }
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused; // Set the pause state
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
        backgroundImage.remove();  // Remove the background image
        settingTexture.dispose(); // Dispose of the texture to avoid memory leaks
    }
}
