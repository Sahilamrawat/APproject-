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
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginScreen extends MainMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Image overlayImage;
    private Image backgroundImage;
    private Game game;
    private Screen firstScreen;

    // UI components for login
    private TextField usernameField;
    private TextField passwordField;
    ImageTextButton loginButton;
    ImageTextButton signUpButton;
    private Texture signupTexture;
    private Texture loginTexture;
    private Image buttonImage1,buttonImage2;

    public LoginScreen(Game game, Screen firstScreen) {
        this.game = game;
        this.firstScreen = firstScreen;
    }

    public LoginScreen() {
    }

    @Override
    public void show() {
        // Initialize stage and set input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        loginTexture = new Texture(Gdx.files.internal("login.png"));

        backgroundImage = new Image(new Texture(Gdx.files.internal("background2.jpg")));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0); // Position at the bottom-left corner
        stage.addActor(backgroundImage);  // Add background image
        // Create overlay for login
        overlayImage = new Image(new Texture(Gdx.files.internal("profilebackground.png")));
        overlayImage.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 3);
        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2,
            (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2);
        stage.addActor(overlayImage);

        loginButton = createImageTextButton(loginTexture, 80, 80);
        // Create a table for layout and align it with the overlay
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Username and password fields
        usernameField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

// Login button listener
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String enteredUsername = usernameField.getText();
                String enteredPassword = passwordField.getText();
                loadUserData(usernameField.getText());
                // Replace these with the actual credentials for validation
//                String correctUsername = playerName;
//                String correctPassword = playerPassword;
                System.out.println(playerName);
                // Check if the system has no registered user
                if (playerName == null || playerPassword== null || playerName.isEmpty() || playerPassword.isEmpty()) {
                    Dialog signUpPrompt = new Dialog("No User Found", skin) {
                        @Override
                        protected void result(Object object) {
                            if ((boolean) object) {
                                // Redirect to Sign-Up screen
                                ((Game) Gdx.app.getApplicationListener()).setScreen(new SignupScreen(game,firstScreen));
                            }
                        }
                    };
                    signUpPrompt.text("No user is registered. Would you like to sign up?");
                    signUpPrompt.button("Yes", true);  // Pass true when Yes is clicked
                    signUpPrompt.button("No", false); // Pass false when No is clicked
                    signUpPrompt.show(stage);         // Ensure `stage` is the current Stage
                    return;
                }

                // Validate username and password
                if (enteredUsername.equals(playerName) && enteredPassword.equals(playerPassword)) {
                    // Valid login: proceed to Main Menu
                    System.out.println("Logging in as: " + enteredUsername);
//                    playerName = enteredUsername;
//                    playerPassword = enteredPassword;

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                }else {
                    // Invalid login: show error message
                    Dialog invalidDialog = new Dialog("Invalid Login", skin) {
                        @Override
                        protected void result(Object object) {
                            // Handle dialog close action if needed
                        }
                    };
                    invalidDialog.text("Invalid username or password!");
                    invalidDialog.button("OK");
                    invalidDialog.show(stage); // Ensure `stage` is the current Stage
                }
            }
        });
        signupTexture = new Texture(Gdx.files.internal("signup.png"));

        signUpButton = createImageTextButton(signupTexture, 80, 80);
//        signUpButton.setPosition(loginButton.getX(), loginButton.getY() - loginButton.getHeight() - 10); // Adjust position below loginButton
        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Redirect to Sign-Up screen when Sign Up button is clicked
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SignupScreen(game, firstScreen));
            }
        });

// Add the signUpButton to the stage


        // Add components to table
        table.add(new Label("Username:", skin)).padBottom(10);
        table.add(usernameField).width(200).padBottom(10).row();
        table.add(new Label("Password:", skin)).padBottom(10);
        table.add(passwordField).width(200).padBottom(20).row();
        table.add(loginButton).colspan(2).center().row();
        table.add(signUpButton).colspan(2).center();


        // Add table to the stage
        stage.addActor(table);
        addHoverEffect(buttonImage1, loginButton);
        addHoverEffect(buttonImage2, signUpButton);
    }

    private void loadUserData(String username) {
        FileHandle file = Gdx.files.local("user.json");
        if (file.exists()) {
            Json json = new Json();
            try {
                // Read the JSON file into a JSONArray
                String fileContents = file.readString();
                if (!fileContents.isEmpty()) {
                    JSONArray usersArray = new JSONArray(fileContents);

                    // Loop through the users array and find the user by username
                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userObj = usersArray.getJSONObject(i);
                        String storedUsername = userObj.getString("playerName");

                        // If the username matches, load the user data
                        if (storedUsername.equals(username)) {
                            playerName = storedUsername;
                            playerPassword = userObj.getString("playerPassword");
                            playerAge = userObj.getInt("playerAge");

                            System.out.println("Loaded user: " + playerName + ", Age: " + playerAge);
                            return; // Exit after finding and loading the user
                        }
                    }
                    System.out.println("User not found: " + username);
                }
            } catch (Exception e) {
                System.out.println("Error loading user data: " + e.getMessage());
            }
        } else {
            System.out.println("No saved user data found.");
        }
    }

    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        if (texture == loginTexture) {
            buttonImage1 = buttonImage;
        }else if (texture == signupTexture) {
            buttonImage2 = buttonImage;
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
        overlayImage.setSize(width / 2, height / 3);
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
