package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class WinScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;
    private Gameplay gameplayScreen;

    // Constructor that takes both the game and gameplay screen
    public WinScreen(Game game, Gameplay gameplayScreen) {
        this.game = game;
        this.gameplayScreen = gameplayScreen;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"));

        // Display win message
        Label winLabel = new Label("You Win!", skin, "title");
        winLabel.setPosition(Gdx.graphics.getWidth() / 2f - winLabel.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f + 50);
        stage.addActor(winLabel);

        // Create a button to go back to the main menu or restart
        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.setSize(150, 50);
        mainMenuButton.setPosition(Gdx.graphics.getWidth() / 2f - mainMenuButton.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - 50);

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Pass the gameplayScreen when returning to MainMenu
                game.setScreen(new MainMenu(game, gameplayScreen));
            }
        });
        stage.addActor(mainMenuButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
