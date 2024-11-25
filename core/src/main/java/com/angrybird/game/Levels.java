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

import java.util.ArrayList;

public class Levels implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private Image backgroundImage;
    private static Game game;
    public static ArrayList<BaseBird>savedBrids=new ArrayList<>();
    public static ArrayList<Pig>savedPigs=new ArrayList<>();
    public static ArrayList<Material>savedMaterials=new ArrayList<>();

    public static ArrayList<Screen> LevelStates=new ArrayList<>();

    public static ArrayList<LevelData> levels = new ArrayList<>() {{
        add(new LevelData("Level 1", "l1_image.png", true, 1, false, new Level_1(game, null)));
        add(new LevelData("Level 2", "lock.png", false, 2, false, new Level_2(game, null)));
        add(new LevelData("Level 3", "lock.png", false, 3, false, new Level_3(game, null)));
    }}; // ArrayList to manage levels
    private ImageTextButton backButton;
    private Texture backTexture;

    public Levels(Game game) {
        Levels.game = game;

    }

    public Levels() {

    }

    private void updateLevels() {
        for (LevelData levelData : levels) {
            // Update texture based on the unlocked state
            if (levelData.isUnlocked&&levelData.LevelNo==1) {
                levelData.texturePath = "l1_image.png"; // Change to the actual unlocked image
            }else if(levelData.isUnlocked&&levelData.LevelNo==2){
                levelData.texturePath = "l2_image.png";
            } else if(levelData.isUnlocked&&levelData.LevelNo==3){
                levelData.texturePath = "l3_image.png";
            }else {
                levelData.texturePath = "lock.png"; // Change to the actual locked image
            }
        }
    }

    @Override
    public void show() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load textures
        backTexture = new Texture(Gdx.files.internal("back.png"));
        Texture backgroundTexture = new Texture(Gdx.files.internal("background1.jpeg"));
        backgroundImage = new Image(backgroundTexture);
        stage.addActor(backgroundImage);

        // Load skin and atlas
        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        // Create main table
        table = new Table();
        table.setFillParent(true);

        // Add heading
        Label heading = new Label("SELECT LEVEL", skin, "title1");
        heading.setFontScale(2);
        table.add(heading).padBottom(30);
        table.row();

        updateLevels();

        // Add levels grid
        Table levelsTable = new Table();
        float buttonSize = 100;

        for (LevelData levelData : levels) {
            Texture texture;
            if (levelData.isUnlocked&&levelData.LevelNo==1) {
                levelData.texturePath = "l1_image.png"; // Change to the actual unlocked image
                texture=new Texture(Gdx.files.internal(levelData.texturePath));
            }else if(levelData.isUnlocked&&levelData.LevelNo==2){
                levelData.texturePath = "l2_image.png";
                texture=new Texture(Gdx.files.internal(levelData.texturePath));
            } else if(levelData.isUnlocked&&levelData.LevelNo==3){
                levelData.texturePath = "l3_image.png";
                texture=new Texture(Gdx.files.internal(levelData.texturePath));
            }else {
                levelData.texturePath = "lock.png"; // Change to the actual locked image
                texture=new Texture(Gdx.files.internal(levelData.texturePath));
            }
            ImageTextButton levelButton = createImageTextButton(texture, buttonSize, buttonSize);

            if (levelData.isUnlocked) {
                levelButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(levelData.screen);
                        LevelStates.add(levelData.screen);
                    }
                });
            } else {
                levelButton.setDisabled(true);
            }

            addHoverEffect(levelButton);
            levelsTable.add(levelButton).pad(10);

//            if (levelsTable.getCells().size() % 3 == 0) { // 3 levels per row
//                levelsTable.row();
//            }
        }

        table.add(levelsTable).center().padBottom(30);
        table.row();

        // Back button
        Texture backTexture=new Texture(Gdx.files.internal("back.png"));
        backButton = createImageTextButton(backTexture, 70, 70);
        backButton.setPosition(50, 50);
        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });

        addHoverEffect(backButton);
        stage.addActor(table);
    }

    private ImageTextButton createImageTextButton(Texture texturePath, float width, float height) {
        ImageTextButton button = new ImageTextButton("", skin);
        Image buttonImage = new Image(texturePath);
        buttonImage.setScaling(Scaling.fill);
        button.add(buttonImage).size(width, height).expand().fill();
        return button;
    }

    private void addHoverEffect(final ImageTextButton button) {
        // Extract the Image from the button
        final Image buttonImage = (Image) button.getChildren().get(0);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; // return true to handle the event
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                System.out.println("Button hovered");
                button.setTransform(true); // Enable button transformations
                button.setScale(1.1f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.setScale(1f);
            }
        });
    }




    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateLevels();
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
        backTexture.dispose();
    }

    // Inner class to manage level data
    public static class LevelData {
        String name;
        String texturePath;
        boolean isUnlocked;
        boolean isCompleted;
        Screen screen;
        int LevelNo;


        public LevelData(String name, String texturePath, boolean isUnlocked,int LevelNo,boolean isCompleted, Screen screen) {
            this.name = name;
            this.texturePath = texturePath;
            this.LevelNo=LevelNo;
            this.isUnlocked = isUnlocked;
            this.isCompleted=isCompleted;
            this.screen = screen;
        }
    }
}
