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
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Levels implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private ImageTextButton backButton;
    private Texture backTexture;
    private Game game;
    private Image backgroundImage; // Image actor for background
    private boolean[] levelUnlocked;

    // Locked icon texture
    private ImageTextButton lockerIconButton;
    private ImageTextButton levelOneButton;
    private Texture lockedIconTexture;
    private Texture levelOneTexture;
    private Image buttonImage1, buttonImage2,buttonImage3;
    public Levels(Game game) {
        this.game = game;
        // Initialize the unlock state (Level 1 is unlocked, others are locked)
        levelUnlocked = new boolean[20];
        levelUnlocked[0] = true; // Unlock Level 1

        // Load the texture for Level 1
        levelOneTexture = new Texture(Gdx.files.internal("l1_image.png")); // Ensure you have this image in your assets
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        backTexture = new Texture(Gdx.files.internal("back.png"));
        // Load the background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("background1.jpeg"));
        backgroundImage = new Image(backgroundTexture);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        // Load the locked icon texture
        lockedIconTexture = new Texture(Gdx.files.internal("lock.png")); // Ensure you have this image in your assets
        levelOneTexture=new Texture(Gdx.files.internal("l1_image.png"));

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

        for (int i = 0; i < 4; i++) { // Iterate through 4 levels (for example)
            final int level = i + 1; // Level number (1 to 4)

            if (level == 1 && levelUnlocked[i]) { // If it's Level 1 and it is unlocked
                levelOneButton = createImageTextButton(levelOneTexture, buttonSize, buttonSize);

                levelOneButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ((Game)Gdx.app.getApplicationListener()).setScreen(new Gameplay(game, Levels.this));
                    }
                });
                levelsTable.add(levelOneButton).pad(10);

                addHoverEffect(buttonImage1, levelOneButton);

            } else { // Locked level case
                lockerIconButton = createImageTextButton(lockedIconTexture, buttonSize, buttonSize);
                lockerIconButton.setDisabled(true);
                levelsTable.add(lockerIconButton).pad(10);
                addHoverEffect(buttonImage2, lockerIconButton);
            }

            if ((i + 1) % levelsPerRow == 0) {
                levelsTable.row(); // Move to the next row after every `levelsPerRow` buttons
            }
        }



        // Add levels table to the main table
        table.add(levelsTable).center().padBottom(30);
        table.row();

        // Back button to return to the Main Menu
        backButton =createImageTextButton(backTexture,70,70);


// Position the button
        backButton.setPosition(50, 50);
        stage.addActor(backButton);

// Add ClickListener for the back button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });
        addHoverEffect(buttonImage3, backButton);
        stage.addActor(table);

    }

    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        if (texture == levelOneTexture) {
            buttonImage1 = buttonImage;
        } else if (texture == lockedIconTexture) {
            buttonImage2 = buttonImage;
        }
        else if (texture ==backTexture) {
            buttonImage3 = buttonImage;
        }

        return button;

    }

    private void updateBackButtonPosition() {
        backButton.setPosition(Gdx.graphics.getWidth() - backButton.getWidth() - 20,
            Gdx.graphics.getHeight() - backButton.getHeight() - 20);
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
