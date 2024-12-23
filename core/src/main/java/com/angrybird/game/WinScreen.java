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

public class WinScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Image backgroundImage;
    private Game game;
    private Screen nextLevelScreen;
    private Screen mainMenuScreen;
    private Screen level1Screen;

    // Labels for win information
    private Label winTitleLabel;
    private Label scoreLabel;
    private Label bonusLabel;

    private Table table;
    private ImageTextButton nextButton, mainMenuButton,restartButton;
    private Texture nextTexture, mainMenuTexture,restartTexture;

    private Image buttonImage1, buttonImage2,buttonImage3;
    private Image overlayImage,birdImage;
    private int points;
    public int level;


    public WinScreen(Game game, Screen level1Screen, Screen nextLevelScreen,int points,int level) {
        this.game = game;
        this.level1Screen = level1Screen;
        this.nextLevelScreen=nextLevelScreen;
        this.points=points;
        this.level=level;
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
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);

        overlayImage = new Image(new Texture(Gdx.files.internal("winBackground.png")));

        float reducedWidth = Gdx.graphics.getWidth() / 2-70;
        float height = Gdx.graphics.getHeight() / 2 + 100;

        overlayImage.setSize(reducedWidth, height); // Set the size with the new width
        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2,
            (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2); // Center it


        stage.addActor(overlayImage);
        table = new Table();
        table.setFillParent(true);

        if(level!=3){
            nextButton = createImageTextButton(nextTexture, 80, 80);
            nextButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Transition to next level screen
                    ((Game) Gdx.app.getApplicationListener()).setScreen(nextLevelScreen);
                }
            });
            table.add(nextButton).padTop(5).row();
            addHoverEffect(buttonImage1, nextButton);
        }


        mainMenuButton = createImageTextButton(mainMenuTexture, 80, 80);
        restartButton = createImageTextButton(restartTexture, 80, 80);

        winTitleLabel = new Label("YOU WON ! ! !", skin, "title1");
        scoreLabel = new Label("Total points", skin, "title");
        scoreLabel.setText("Total points"+points);


        table.add(winTitleLabel).padBottom(30).row();
        table.add(scoreLabel).padBottom(10).row();
        table.add(bonusLabel).padBottom(5).row();



        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to main menu screen
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                level1Screen.restartGame();
//                level1Screen.setPaused(false); // Resume gameplay after restart
                ((Game)Gdx.app.getApplicationListener()).setScreen(level1Screen); // Go back to gameplay
            }
        });


        table.add(mainMenuButton).padTop(10).row();
        table.add(restartButton).padTop(5).row();
        stage.addActor(table);


        addHoverEffect(buttonImage2, mainMenuButton);
        addHoverEffect(buttonImage3, restartButton);
    }


    private void addHoverEffect(final Image image, final ImageTextButton button) {
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
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
