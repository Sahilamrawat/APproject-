package com.angrybird.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Arrays;

public class Gameplay implements Screen {
    private Vector2 startPosition = new Vector2(); // Starting position of the catapult (on touchDown)
    private Vector2 endPosition = new Vector2();   // End position of the catapult (on touchDragged)
    private Vector2 velocity = new Vector2();      // Initial velocity of the bird when released

    private Array<Image> trajectoryDots;
    private Texture dotTexture;
    private static Bird currentBird; // To track the bird on the catapult
    private Vector2 birdPosition;
    private float dotSpacing = 20; // Space between dots
    private Stage stage;
    private Skin skin;
    private Game game;
    private Image backgroundImage;
    private Label pointsLabel;
    private float catapultX = 100;  // X position of the catapult
    private float catapultY = 100;
    private int points = 1200;
    private Screen previousScreen;
    private ImageTextButton settingsButton;
    private ImageTextButton backButton;
    private boolean isPaused = false;

    private Texture settingTexture;
    private Texture backTexture;
    private Image catapultImage;
    private static ArrayList<Bird> birds = new ArrayList<Bird>();
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
                ((Game)Gdx.app.getApplicationListener()).setScreen(new Pause(game, Gameplay.this));
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
        birds.add(new redbird("redbird.png", 50, 50));
        birds.add(new blackbird("blackbird.png", 50, 50));
        birds.add(new bluebird("bluebird.png", 40, 40));
        birds.add(new yellowbird("yellowbird.png", 40, 40));


        loadNextBird();



        addBlocksToStage();
        createBirdsAndCatapultTable();
        TextButton winButton = new TextButton("Win", skin);
        TextButton loseButton = new TextButton("Lose", skin);

        dotTexture = new Texture(Gdx.files.internal("dot.png"));

        // Initialize the trajectory dots array
        trajectoryDots = new Array<>();

        // Add input listener for tracking mouse events


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
                ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen(game, null,new MainMenu(),Gameplay.this));
            }
        });



        addHoverEffect(buttonImage2, settingsButton);
        stage.addActor(settingsButton);

// Position lose button
        loseButton.setPosition(screenWidth / 2f + buttonSpacing / 2, buttonHeight + 10);
        loseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoseScreen(game, null,new MainMenu(),Gameplay.this));
            }
        });
// Add buttons to the stage
        stage.addActor(winButton);
        stage.addActor(loseButton);




    }
    private boolean isCatapultClicked(float x, float y) {
        return x >= catapultX && x <= catapultX + catapultImage.getWidth() && y >= catapultY && y <= catapultY + catapultImage.getHeight();
    }
    private void launchBird() {
        // Log the current bird status for debugging
        Gdx.app.log("Gameplay", "currentBird: " + currentBird);

        // Check if currentBird is null and return if it is
        if (currentBird == null) {
            Gdx.app.log("Gameplay", "Error: currentBird is null, unable to launch the bird.");
            loadNextBird(); // Load the next bird if current one is null
            return; // Exit the method
        }

        // Set the initial position of the bird (catapult position)
        birdPosition.set(catapultX, catapultY);

        // Check if velocity is set correctly
        if (velocity == null) {
            Gdx.app.log("Gameplay", "Error: velocity is null, unable to launch the bird.");
            return; // Prevent launch if velocity is not set
        }

        // Calculate the initial velocity as a vector (bird's movement speed)
        Vector2 initialVelocity = new Vector2(velocity.x, velocity.y);

        // Animate the bird's movement based on the initial velocity
        animateBirdMovement(initialVelocity);
    }


    private void animateBirdMovement(Vector2 initialVelocity) {
        float gravity = -9.8f; // Gravity force
        float timeStep = 0.1f; // Small time steps for smooth animation
        Vector2 currentPosition = new Vector2(catapultX, catapultY);
        Vector2 currentVelocity = new Vector2(initialVelocity);

        // Update position of the bird over time (animation loop)
        Timer.schedule(new Timer.Task() {
            float time = 0;

            @Override
            public void run() {
                // Calculate the new position
                time += timeStep;

                // Update velocity and position based on physics equations
                float dx = currentVelocity.x * time;
                float dy = currentVelocity.y * time + 1f * gravity * time * time;

                currentPosition.set(catapultX + dx, catapultY + dy); // Apply movement
                currentBird = birds.get(0);
                // Move the bird to the updated position
                currentBird.setPosition(currentPosition.x, currentPosition.y);

                // Stop the animation if the bird hits the ground
                if (currentPosition.y <= 0 || currentPosition.x < 0 || currentPosition.x > Gdx.graphics.getWidth()) {
                    this.cancel(); // Stop the animation
                }
            }
        }, 0, timeStep);
    }
    private void loadNextBird() {
        if (birds != null && birds.size() > 0) {
            currentBird = birds.get(0); // Get the first bird
            birdPosition = new Vector2(catapultX, catapultY); // Position near the catapult
            birds.remove(0); // Remove the bird from the array
            currentBird.setPosition(birdPosition.x, birdPosition.y); // Set the position of the new bird
            stage.addActor((Actor) currentBird); // Add the bird to the stage
        } else {
            Gdx.app.log("Gameplay", "Birds left: " + birds.size());
        }
    }
    private void updateTrajectory(Vector2 startPosition, Vector2 endPosition) {
        clearTrajectory(); // Remove old trajectory dots

        // Calculate initial velocity vector (invert direction for +x trajectory)
        Vector2 velocity = new Vector2(startPosition.x - endPosition.x, startPosition.y - endPosition.y);

        // Physics constants (adjust based on your game)
        float gravity = -9.8f; // Gravity (adjust as per your game's scale)

        // Determine total flight time using quadratic motion equation
        float totalFlightTime = (-velocity.y - (float) Math.sqrt(velocity.y * velocity.y - 2 * gravity * startPosition.y)) / gravity;
        totalFlightTime = Math.max(0, totalFlightTime); // Ensure time is non-negative

        // Interval for placing dots
        float timeStep = totalFlightTime / 15; // Divide total time into 30 intervals

        // Generate trajectory points
        for (int i = 0; i < 30; i++) {
            float time = i * timeStep;

            // Update position based on physics equations
            float dx = velocity.x * time;
            float dy = velocity.y * time + 0.5f * gravity * time * time;
            Vector2 position = new Vector2(startPosition.x + dx, startPosition.y + dy);

            // Add a dot to the stage if within bounds
            if (position.y > 0 && position.x > 0 && position.x < Gdx.graphics.getWidth()) {
                Image dot = new Image(new TextureRegionDrawable(new TextureRegion(dotTexture)));
                dot.setSize(5, 5); // Adjust dot size
                dot.setPosition(position.x, position.y);
                trajectoryDots.add(dot);
                stage.addActor(dot);
            }
        }
    }





    private void clearTrajectory() {
        for (Image dot : trajectoryDots) {
            dot.remove(); // Remove dot from stage
        }
        trajectoryDots.clear(); // Clear array
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
        for (int i = birds.size() - 1; i >= 0; i--) {
            Bird bird = birds.get(i);
            birdsAndCatapultTable.add((Actor) bird).bottom();
        }

        // Initialize and add the catapult
        Catapult catapult = new Catapult("catapult.png");
        catapultImage = new Image(new TextureRegionDrawable(new TextureRegion(catapult.getTexture())));
        stage.addListener(new InputListener() {
            private Vector2 startPosition = new Vector2();
            private Vector2 endPosition = new Vector2();
            private boolean isCatapultSelected = false; // Flag to track if catapult is selected

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Check if the click is within the catapult area
                if (isCatapultClicked(x, y)) {
                    isCatapultSelected = true; // Catapult is selected
                    startPosition.set(x, y); // Save the starting position
                    trajectoryDots.clear(); // Clear previous trajectory
                }
                return isCatapultSelected; // Only proceed if the catapult is selected
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isCatapultSelected) {
                    endPosition.set(x, y); // Update the end position as the mouse drags
                    updateTrajectory(startPosition, endPosition); // Show trajectory path
                    if (currentBird != null) {
                        currentBird.setPosition(endPosition.x, endPosition.y); // Update bird position while dragging
                    }
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isCatapultSelected) {
                    isCatapultSelected = false; // Catapult selection ended
                    velocity.set(startPosition.x - endPosition.x, startPosition.y - endPosition.y); // Set velocity based on drag

                    // Ensure sufficient speed for the bird (e.g., minimum speed threshold)
                    if (velocity.len() < 50) { // If the velocity is too low, increase it
                        velocity.set(velocity.nor().scl(50)); // Normalize and scale to minimum speed
                    }

                    if (currentBird != null) {
                        launchBird();
                        clearTrajectory(); // Clear the trajectory
                        currentBird = null; // Remove reference to the bird after launch
                    }
                }
            }
        });
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
        blockTable.add(stoneTable).expandX().bottom();
        blockTable.row();

        // Create two wood tables for two columns of wood blocks
        Table Wood = new Table();
        Wood.setSize(200, 100);
        Table woodTable1 = new Table();
        Table woodTable2 = new Table();
        woodTable1.setSize(50, 100);
        woodTable2.setSize(50, 100);


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
            stage.act();
        } else {
            stage.act(delta); // Update the stage
        }

        // Draw the stage
        stage.draw();
    }
    public void restartGame() {
        points = 0; // Reset points
        pointsLabel.setText("Points: " + points); // Update label
        System.out.println("Game Restarted");
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
