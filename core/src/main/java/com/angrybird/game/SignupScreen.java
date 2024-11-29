package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;
import java.util.Map;

public class SignupScreen extends MainMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Image overlayImage;
    private Game game;
    private Screen firstScreen;
    private Image backgroundImage;

    // UI components for signup
    TextField usernameField;
    TextField emailField;
    TextField passwordField;
    TextField ageField;
    ImageTextButton signupButton;
    private Texture signupTexture;
    private Image buttonImage1;

    public SignupScreen(Game game, Screen firstScreen) {
        this.game = game;
        this.firstScreen = firstScreen;
    }

    public SignupScreen() {

    }

    @Override
    public void show() {
        // Initialize stage and set input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);


        signupTexture = new Texture(Gdx.files.internal("signup.png"));

        backgroundImage = new Image(new Texture(Gdx.files.internal("background2.jpg")));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0); // Position at the bottom-left corner
        stage.addActor(backgroundImage);  // Add background image        // Create overlay for signup
        overlayImage = new Image(new Texture(Gdx.files.internal("profilebackground.png")));
        overlayImage.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2,
            (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2);
        stage.addActor(overlayImage);

        // Create a table for layout and align it with the overlay
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Username, email, and password fields
        usernameField = new TextField("", skin);
        emailField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        ageField = new TextField("", skin);
        ageField.setMessageText("Enter Age");

// Sign-up button
        signupButton = createImageTextButton(signupTexture, 80, 80);
        signupButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String ageText = ageField.getText();

                // Validate input
                if (username.isEmpty() || password.isEmpty() || ageText.isEmpty()) {
                    Dialog errorDialog = new Dialog("Error", skin);
                    errorDialog.text("All fields must be filled!");
                    errorDialog.button("OK");
                    errorDialog.show(stage);
                    return;
                }

                // Validate age
                try {
                    playerAge = Integer.parseInt(ageText);
                    if (playerAge < 0 || playerAge > 120) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    Dialog errorDialog = new Dialog("Error", skin);
                    errorDialog.text("Age must be a valid number between 0 and 120!");
                    errorDialog.button("OK");
                    errorDialog.show(stage);
                    return;
                }

                // Save player details
                playerName = username;
                playerPassword = password;

                // Use a map for JSON serialization
                Map<String, String> userData = new HashMap<>();
                userData.put("playerName", playerName);
                userData.put("playerPassword", playerPassword);
                userData.put("playerAge", String.valueOf(playerAge));

                // Configure Json for clean output
                Json json = new Json();
                json.setOutputType(JsonWriter.OutputType.json); // Standard JSON output

                // Write to user.json
                FileHandle file = Gdx.files.local("user.json");
                file.writeString(json.toJson(userData), false);

                System.out.println("User registered: " + playerName + ", Age: " + playerAge);

                // Redirect to Main Menu
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });


        // Add components to table
        table.add(new Label("Username:", skin)).padBottom(10);
        table.add(usernameField).width(200).padBottom(10).row();
        table.add(new Label("Email:", skin)).padBottom(10);
        table.add(emailField).width(200).padBottom(10).row();
        table.add(new Label("Password:", skin)).padBottom(10);
        table.add(passwordField).width(200).padBottom(20).row();
        table.add(new Label("Age", skin)).padBottom(10);
        table.add(ageField).width(200).padBottom(20).row();
        table.add(signupButton).colspan(2).center();

        // Add table to the stage
        stage.addActor(table);
        addHoverEffect(buttonImage1, signupButton);
    }
    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        if (texture == signupTexture) {
            buttonImage1 = buttonImage;
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
        Gdx.gl.glClearColor(0, 0, 0, 0.5f); // Semi-transparent black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        overlayImage.setSize(width / 2, height / 2);
        overlayImage.setPosition((width - overlayImage.getWidth()) / 2,
            (height - overlayImage.getHeight()) / 2);
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
