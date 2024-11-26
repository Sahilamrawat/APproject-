package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadGame extends Levels implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private ScrollPane scrollPane;
    private ImageTextButton backButton,LoadGame;
    private Texture backTexture,LoadTexture;
    private Game game;
    private SpriteBatch batch;

    private Screen levelScreen;
    private Image buttonImage1,buttonImage2;

    private Image backgroundImage;

    public LoadGame(Game game, Screen levelScreen) {
        this.game = game;
        this.levelScreen = levelScreen;
    }

    public static int getSavedGamesCount(String filePath) {
        StringBuilder jsonData = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            // Check if the file is empty (i.e., no content in the first line)
            if (line == null || line.trim().isEmpty()) {
                System.out.println("The data file is empty. Returning 0 saved games.");
                return 0;  // Return 0 if the file is empty
            }

            // If not empty, append the content to the StringBuilder
            jsonData.append(line);
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse JSON and get "savedGamesCount"
        JSONObject jsonObject = new JSONObject(jsonData.toString());
        return jsonObject.getInt("savedGamesCount");
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backTexture = new Texture(Gdx.files.internal("back.png"));
        LoadTexture = new Texture(Gdx.files.internal("loadGame.png"));

        // Load the background texture
        Texture backgroundTexture = new Texture(Gdx.files.internal("background1.jpeg"));
        backgroundImage = new Image(backgroundTexture);

        batch = new SpriteBatch();
        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        stage.addActor(backgroundImage);

        // Create table and scrollable pane
        table = new Table(skin);
        table.setFillParent(true);

        // Add heading
        Label heading = new Label("SELECT A GAME TO LOAD", skin,"title");
        heading.setFontScale(2);
        table.add(heading).padBottom(30);
        table.row();

        // Create a vertical list of buttons for saved games
        Table gamesTable = new Table();

        int count=getSavedGamesCount("data.json");
        System.out.println(count);
        for (int i = 0; i < count; i++) {
//            final String savedGame = savedGames.get(i);
            LoadGame =createImageTextButton(LoadTexture,70,70);
            addHoverEffect(buttonImage2, LoadGame);

            int index=i;
            LoadGame.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    loadGame("Load GAME", index); // Load the selected game

                }
            });

            gamesTable.add(LoadGame).width(300).padBottom(10);
            gamesTable.row();
        }


        // Make the games table scrollable
        scrollPane = new ScrollPane(gamesTable, skin);
        scrollPane.setFadeScrollBars(false); // Disable fading scrollbars
        scrollPane.setScrollingDisabled(true, false); // Only vertical scrolling
        table.add(scrollPane).width(400).height(300).padBottom(5);
        table.row();

        // Back button as ImageTextButton to return to the Main Menu
        backButton =createImageTextButton(backTexture,70,70);

        backButton.pad(10);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                gameplayScreen.setPaused(false); // Resume gameplay
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu()); // Go back to gameplay
            }
        });

        table.add(backButton).padBottom(20);
        addHoverEffect(buttonImage1, backButton);

        stage.addActor(table);
    }
    private ImageTextButton createImageTextButton(Texture texture, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texture);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        if (texture ==backTexture) {
            buttonImage1 = buttonImage;
        }else if (texture ==LoadTexture) {
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

    private void loadGame(String gameName,int index) {
        // Logic to load the game, for example:
        System.out.println("Loading " + gameName + "...");
        ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGamePlay(game, LoadGame.this,index));
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
    }
}
