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
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
    private Label pointsLabel;
    private int points = 10;
    private Screen previousScreen;
    private ImageTextButton settingsButton;
    private ImageTextButton backButton;
    private boolean isPaused = false;

    private Texture settingTexture;
    private Texture backTexture;
    private Image catapultImage;
    private Bird[] birds;
    private BigPig bigPig;
    private MediumPig mediumPig;
    private SmallPig smallPig1;
    private SmallPig smallPig2;
    private Image buttonImage1, buttonImage2;

    public Gameplay(Game game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
    }

    public Gameplay() {
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        settingTexture = new Texture(Gdx.files.internal("pause.png"));
        backTexture = new Texture(Gdx.files.internal("back.png"));
        settingsButton=createImageTextButton(settingTexture,100,100);
        backButton=createImageTextButton(backTexture,50,50);
        Texture backgroundTexture = new Texture(Gdx.files.internal("gameplayBackground.jpg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);

        pointsLabel = new Label("Points: " + points, skin, "title");
        pointsLabel.setPosition(20, Gdx.graphics.getHeight() - 30);
        stage.addActor(pointsLabel);


        updateSettingsButtonPosition();

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new Pause(game, Gameplay.this)); // Your desired action
            }
        });
        addHoverEffect(buttonImage2, settingsButton);
        stage.addActor(settingsButton);


        updateBackButtonPosition();
        backButton.setSize(70, 70);

// Position the button
        backButton.setPosition(50, 50);
        stage.addActor(backButton);

// Add ClickListener for the back button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(previousScreen); // Action to go back
            }
        });
        addHoverEffect(buttonImage1, backButton);

        // Initialize birds
        birds = new Bird[]{
            new blackbird("blackbird.png", 50, 50),
            new bluebird("bluebird.png", 40, 40),
            new yellowbird("yellowbird.png", 40, 40),
            new redbird("redbird.png", 50, 50)
        };




        addBlocksToStage();
        createBirdsAndCatapultTable();
        TextButton winButton = new TextButton("Win", skin);
        TextButton loseButton = new TextButton("Lose", skin);


// Position both buttons at the bottom center
        float buttonWidth = 100;
        float buttonHeight = 50;
        winButton.setSize(buttonWidth, buttonHeight);
        loseButton.setSize(buttonWidth, buttonHeight);

// Calculate positions to center the buttons at the bottom
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonSpacing = 20;

// Position win button
        winButton.setPosition(screenWidth / 2f - buttonWidth - buttonSpacing / 2, buttonHeight + 10);
        winButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen(game, null,new MainMenu()));
            }
        });



        addHoverEffect(buttonImage2, settingsButton);
        stage.addActor(settingsButton);

// Position lose button
        loseButton.setPosition(screenWidth / 2f + buttonSpacing / 2, buttonHeight + 10);
        loseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoseScreen(game, null,new MainMenu()));
            }
        });
// Add buttons to the stage
        stage.addActor(winButton);
        stage.addActor(loseButton);




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
        if (texture == backTexture) {
            buttonImage1 = buttonImage;
        } else if (texture == settingTexture) {
            buttonImage2 = buttonImage;

        }

        return button;

    }


    private void updateBackButtonPosition() {
        backButton.setPosition(Gdx.graphics.getWidth() - backButton.getWidth() - 20,
            Gdx.graphics.getHeight() - backButton.getHeight() - 20);
    }


    private void createBirdsAndCatapultTable() {
        // Create a main table for birds and the catapult
        Table birdsAndCatapultTable = new Table();

        birdsAndCatapultTable.setName("birdsAndCatapultTable");

        // Set size based on screen dimensions
        float width =400;// 80% of screen width
        float height = 100; // Fixed height
        birdsAndCatapultTable.setSize(width, height);

        // Position it at the bottom of the screen, with some padding
        birdsAndCatapultTable.setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.15f - birdsAndCatapultTable.getHeight() * 0.15f); // 10% from left and 20 pixels from bottom
        stage.addActor(birdsAndCatapultTable);

        // Add birds to the table
        for (Bird bird : birds) {
            birdsAndCatapultTable.add((Actor) bird).bottom();
        }

        // Initialize and add the catapult
        Catapult catapult = new Catapult("catapult.png");
        catapultImage = new Image(new TextureRegionDrawable(new TextureRegion(catapult.getTexture())));
        catapultImage.setSize(100, 100);
        birdsAndCatapultTable.add(catapultImage).size(100, 100);
    }


    private void addBlocksToStage() {
        Material woodMaterial = new WoodMaterial();
        Material stoneMaterial = new StoneMaterial();
        Material iceMaterial = new IceMaterial();

        // Create a main table for blocks
        Table blockTable = new Table();


        blockTable.setName("blockTable");
        blockTable.setSize(200, 200);
        blockTable.setPosition(Gdx.graphics.getWidth() - blockTable.getWidth() - 90, Gdx.graphics.getHeight() * 0.15f - blockTable.getHeight() * 0.15f);


        Table zeroFloor=new Table();

        zeroFloor.setSize(200,50);
        Pig mediumPig = new MediumPig();
        zeroFloor.add(mediumPig).size(50,50);
        blockTable.add(zeroFloor);
        blockTable.row();
        // Create a dedicated table for the ice block
        Table pigTable1=new Table();
        pigTable1.setSize(50,50);
        Pig smallPig1 = new SmallPig();
        pigTable1.add(smallPig1);


        Table firstFloor=new Table();
        firstFloor.setSize(200,50);

        Table iceTable = new Table();
        iceTable.setSize(50, 50);
        Block iceBlock = new Block(iceMaterial, 50, 50);


        iceTable.add(iceBlock).center();
        Table pigTable2=new Table();
        Pig smallPig2 = new SmallPig();
        pigTable2.add(smallPig2);
//
        firstFloor.add(pigTable1).size(50,50).bottom();
        firstFloor.add(iceTable).size(70, 70).bottom(); // Add ice table to blockTable
        firstFloor.add(pigTable2).size(50,50).bottom();

        blockTable.add(firstFloor);
        blockTable.row(); // Move to a new row for the stone block

        // Create a dedicated table for the stone block
        Table stoneTable = new Table();
        stoneTable.setSize(100, 40);
        Block stoneBlock = new Block(stoneMaterial, 60, 40);
        stoneTable.add(stoneBlock).center().size(60, 40);
        blockTable.add(stoneTable).expandX().bottom(); // Center the stone table in the blockTable
        blockTable.row(); // Move to a new row for the wood blocks

        // Create two wood tables for two columns of wood blocks
        Table Wood = new Table();
        Wood.setSize(200, 100);
        Table woodTable1 = new Table();
        Table woodTable2 = new Table();
        woodTable1.setSize(50, 100);
        woodTable2.setSize(50, 100);

        // Add wood blocks in a column format within woodTable1 and woodTable2
        for (int i = 0; i < 3; i++) {
            Block woodBlock1 = new Block(woodMaterial, 30, 30);
            woodTable1.add(woodBlock1).size(30, 30);
            woodTable1.row(); // Move to the next row after each block


            Block woodBlock2 = new Block(woodMaterial, 30, 30);
            woodTable2.add(woodBlock2).size(30, 30);
            woodTable2.row();

        }

        // Add both woodTables to the main blockTable as separate columns
        Wood.add(woodTable1).uniform().bottom().size(50, 100).padRight(30);
        bigPig = new BigPig();
        bigPig.setPosition(1100, 90);
        Wood.add(bigPig);
        Wood.add(woodTable2).uniform().bottom().size(50, 100).padLeft(30);
        blockTable.add(Wood).bottom();
        stage.addActor(blockTable);
    }



    private void updateSettingsButtonPosition() {
        settingsButton.setPosition(Gdx.graphics.getWidth() - settingsButton.getWidth() - 70,
            Gdx.graphics.getHeight() - settingsButton.getHeight() - 50);
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        backgroundImage.setSize(width, height);
        pointsLabel.setPosition(20, height - 100);
        updateSettingsButtonPosition();


        // Example bird positions
        // Update the birdsAndCatapultTable position
        updateBirdsAndCatapultTablePosition(width, height);

        // Resize blocks if necessary
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Block) {
                Block block = (Block) actor;
                block.setSize(50, 50);
            }
        }

        // Update blockTable position after resizing
        updateBlockTablePosition(width, height);
    }

    private void updateBirdsAndCatapultTablePosition(int width, int height) {
        Actor birdsAndCatapultTable = stage.getRoot().findActor("birdsAndCatapultTable");
        if (birdsAndCatapultTable != null) {
            // Position it at the bottom of the screen, with some padding
            birdsAndCatapultTable.setPosition(width * 0.05f, height * 0.15f - birdsAndCatapultTable.getHeight() * 0.15f); // 10% from left and 20 pixels from bottom
        }
    }


    // Method to update the blockTable position dynamically
    private void updateBlockTablePosition(int width, int height) {
        Actor blockTable = stage.getRoot().findActor("blockTable");
        if (blockTable != null) {
            blockTable.setPosition(width - blockTable.getWidth() - 130, height * 0.20f - blockTable.getHeight() * 0.20f);
        }
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
    public void restartGame() {
        points = 0; // Reset points
        pointsLabel.setText("Points: " + points); // Update label
        // Any other logic to reset game state can be added here
        System.out.println("Game Restarted"); // Log restart action
    }


    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void hide() {
        // Cleanup resources if necessary
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        settingTexture.dispose();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
