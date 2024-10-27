package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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

public class WinScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Image backgroundImage;
    private Game game;
    private Screen nextLevelScreen;
    private Screen mainMenuScreen;
    private Gameplay gameplayScreen;

    // Labels for win information
    private Label winTitleLabel;
    private Label scoreLabel;
    private Label bonusLabel;

    private Table table;
    private ImageTextButton nextButton, mainMenuButton,restartButton;
    private Texture nextTexture, mainMenuTexture,restartTexture;

    private Image buttonImage1, buttonImage2,buttonImage3;
    private Image overlayImage,birdImage;

    public WinScreen(Game game, Screen nextLevelScreen, Screen mainMenuScreen,Gameplay gameplayScreen) {
        this.game = game;
        this.nextLevelScreen = nextLevelScreen;
        this.mainMenuScreen = mainMenuScreen;
        this.gameplayScreen = gameplayScreen;
    }




    @Override
    public void show() {
        // Set up the stage and input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        nextTexture = new Texture(Gdx.files.internal("next.png"));
        mainMenuTexture = new Texture(Gdx.files.internal("mainmenu.png"));
        restartTexture = new Texture(Gdx.files.internal("restartt.png"));

        // Create a semi-transparent black Pixmap for the center overlay
        backgroundImage = new Image(new Texture(Gdx.files.internal("gameplayBackground.jpg")));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0); // Position at the bottom-left corner
        stage.addActor(backgroundImage);  // Add background image

        overlayImage = new Image(new Texture(Gdx.files.internal("winBackground.png")));

        float reducedWidth = Gdx.graphics.getWidth() / 2-70; // You can change 6 to another divisor for your desired width
        float height = Gdx.graphics.getHeight() / 2 + 100; // Keep height as desired

        overlayImage.setSize(reducedWidth, height); // Set the size with the new width
        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2,
            (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2); // Center it


        stage.addActor(overlayImage); // Add overlay image

        // Create table for layout
        table = new Table();
        table.setFillParent(true);

        nextButton = createImageTextButton(nextTexture, 80, 80);
        nextButton.setDisabled(true);
        mainMenuButton = createImageTextButton(mainMenuTexture, 80, 80);
        restartButton = createImageTextButton(restartTexture, 80, 80);

        winTitleLabel = new Label("YOU WON ! ! !", skin, "title1");
        scoreLabel = new Label("Score: 1200", skin, "button");
        bonusLabel = new Label("Bonus: 200", skin, "button");

        // Add heading label and information to the table

        table.add(winTitleLabel).padBottom(30).row();
        table.add(scoreLabel).padBottom(10).row();
        table.add(bonusLabel).padBottom(5).row();

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to next level screen
                ((Game) Gdx.app.getApplicationListener()).setScreen(nextLevelScreen);
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to main menu screen
                ((Game) Gdx.app.getApplicationListener()).setScreen(mainMenuScreen);
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

        table.add(nextButton).padTop(5).row();
        table.add(mainMenuButton).padTop(10).row();
        table.add(restartButton).padTop(5).row();
        stage.addActor(table);

        addHoverEffect(buttonImage1, nextButton);
        addHoverEffect(buttonImage2, mainMenuButton);
        addHoverEffect(buttonImage3, restartButton);
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
        if (texture == nextTexture) {
            buttonImage1 = buttonImage;
        } else if (texture == mainMenuTexture) {
            buttonImage2 = buttonImage;
        }else if (texture == restartTexture) {
            buttonImage3 = buttonImage;
        }

        return button;
    }

    @Override
    public void render(float delta) {
        // Clear the screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the win screen overlay
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        overlayImage.setSize(width / 2-200, height / 2+80);
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
