package com.angrybird.game;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import tween.ActorAccessor;

public class MainMenu implements Screen {
    private Game game;
    private Skin skin;
    private Stage stage;
    private Table table;
    private TextButton buttonPlay, buttonExit, buttonLoad, profile;
    private Label heading;
    private BitmapFont white, black;
    private TextureAtlas atlas;
    private ImageTextButton playButton, exitButton, loadButton, profileButton;
    private Level_1 level1Screen;
    private Texture playTexture, exitTexture, loadTexture, profileTexture;
    private Image buttonImage1, buttonImage2, buttonImage3, buttonImage4;





    public static String playerName ;
    public static int playerLevel=1;
    public static int playerAge;
    public static String playerPassword ;




    private static Label playerNameLabel;
//    private String playerName = "Mavrick";  // Default name
    private Image profileImage;



    private Image backgroundImage;
    private TweenManager tweenManager;

    public MainMenu() {
    }

    public MainMenu(Game game, Level_1 level1Screen) {
        this.game = game;
        this.level1Screen = level1Screen;
    }

    @Override
    public void show() {
        // Initialize the stage, skin, and atlas
        stage = new Stage(new ScreenViewport());
        atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        // Load textures for each button
        playTexture = new Texture(Gdx.files.internal("play1.png"));
        exitTexture = new Texture(Gdx.files.internal("exit.png"));
        loadTexture = new Texture(Gdx.files.internal("load.png"));
        profileTexture = new Texture(Gdx.files.internal("profile_icon.png"));


        // Play button
        playButton = createImageTextButton(playTexture, 170, 170);
        // Exit button
        exitButton = createImageTextButton(exitTexture, 120, 120);
        // Load button
        loadButton = createImageTextButton(loadTexture, 120, 120);
        // Profile button
        profileButton = createImageTextButton(profileTexture, 70, 70);

        // Add listeners for each button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Levels(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGame(game, level1Screen));
            }
        });

//        profileImage = new Image(new Texture(Gdx.files.internal("profile_icon.png")));  // Replace with your image
//
//        // Create label with the player's name

//
//        // Create the profile button that holds both the image and the label
//        profileButton = new ImageTextButton("", skin); // Empty text, we'll use the Table inside
//
//        Image profile=new Image(new Texture(Gdx.files.internal("profilebackground.png")));
//
//        // Add an image and player name label inside the button (inside a table)
//        Table profileTable = new Table();
//        profileTable.top();  // Align the table to the top, you can also use bottom() or center() based on your preference
//
//        profileTable.add(profile).expand().fill();  // Use expand() and fill() to make the background image fill the cell
//
//// Create a new row for the profile image and player name label
//        profileTable.row().padTop(20);  // Add padding above the row
//        profileTable.add(profileImage).size(100, 100).padRight(10);  // Profile image size and padding
//        profileTable.add(playerNameLabel).padTop(10);
//        // Make the table inside the button (the entire table is clickable)
//        profileButton.add(profileTable).expand().fill();
//
//        // Add click listener to transition to the Profile screen
        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Profile(game, MainMenu.this));
            }
        });

        // Position the profile button
        profileButton.setPosition(
            Gdx.graphics.getWidth() - 150,
            Gdx.graphics.getHeight() - 100
        );








        // Load the background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("background2.jpg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.getColor().a = 0f;
        stage.addActor(backgroundImage);

        // Set up the table layout
        table = new Table(skin);

        table.setFillParent(true);

        table.add(playButton).colspan(2).center().padBottom(10); // Center Play button
        table.row();
        table.add(loadButton).colspan(2).padBottom(20).center();    // Load Game button on the left
        table.row();
        table.add(exitButton).colspan(2).center(); // Center Exit button
        table.center();
        table.setPosition(Gdx.graphics.getWidth()/64f,Gdx.graphics.getHeight()/64f);
        stage.addActor(profileButton);
        stage.addActor(table);

        // Set up input processor
        Gdx.input.setInputProcessor(stage);

        // Initialize tween manager and animations
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());
//


        // Animate fade-in for buttons and background
        Timeline.createSequence().beginSequence()
            .push(Tween.set(playButton, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(loadButton, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(profileButton, ActorAccessor.ALPHA).target(0))
            .push(Tween.set(exitButton, ActorAccessor.ALPHA).target(0))
            .push(Tween.from(heading, ActorAccessor.ALPHA, 0.25f).target(0))
            .push(Tween.to(playButton, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(loadButton, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(exitButton, ActorAccessor.ALPHA, 0.25f).target(1))
            .push(Tween.to(profileButton, ActorAccessor.ALPHA, 0.25f).target(1))

            .end().start(tweenManager);

        // Background image fade-in
        Tween.to(backgroundImage, ActorAccessor.ALPHA, 0.3f)
            .target(1)
//            .delay(0.1f)
            .start(tweenManager);

        // Add hover effects
        addHoverEffect(buttonImage1, playButton);
        addHoverEffect(buttonImage2, exitButton);
        addHoverEffect(buttonImage3, loadButton);
        addHoverEffect(buttonImage4, profileButton);
    }

    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        // Create the ImageTextButton with the provided texture
        ImageTextButton button = new ImageTextButton("", skin);

        // Create the image for the button
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill); // Ensure the image scales properly
        button.add(buttonImage).size(width, height).expand().fill(); // Add the image to the button

        // Set text inside the button, if the texture is the profile texture
        if (texture == playTexture) {
            buttonImage1 = buttonImage;
        } else if (texture == exitTexture) {
            buttonImage2 = buttonImage;
        } else if (texture == loadTexture) {
            buttonImage3 = buttonImage;
        } else if (texture == profileTexture) {
            // Set the text to player name and ensure it is visible inside the button
            button.getLabel().setText(playerName);  // Set the text of the button to the player's name
            buttonImage4 = buttonImage;

            // Optionally, you can adjust the label position or style (e.g., font size)
            button.getLabel().setFontScale(1.1f);  // You can adjust the font scale as needed
            button.getLabel().setAlignment(Align.center);  // Align text to the center of the button
        }

        return button;
    }


    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tweenManager.update(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // Resize background to fit the new screen size
        backgroundImage.setSize(width, height);
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

                image.setScale(1f);
            }
        });
    }

    @Override
    public void hide() {
        // Dispose of resources
        stage.dispose();
        playTexture.dispose();
        exitTexture.dispose();
        loadTexture.dispose();
        profileTexture.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
