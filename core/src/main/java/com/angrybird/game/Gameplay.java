package com.angrybird.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
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
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Arrays;

public class Gameplay implements Screen {
    private Vector2 startPosition = new Vector2(); // Starting position of the catapult (on touchDown)
    private Vector2 endPosition = new Vector2();   // End position of the catapult (on touchDragged)
    private Vector2 velocity = new Vector2();      // Initial velocity of the bird when released
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
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
    private static ArrayList<BaseBird> birds = new ArrayList<BaseBird>();
    private BigPig bigPig;
    private MediumPig mediumPig;
    private SmallPig smallPig1;
    private SmallPig smallPig2;
    private Image buttonImage1, buttonImage2;
    private BirdLauncher birdLauncher;
    private Body launchedBird;

    public static ArrayList<Body> BirdBodies=new ArrayList<Body>();
    private BaseBird redbird,bluebird,yellowbird,blackbird;
//    private Texture groundTexture;
    private SpriteBatch spriteBatch;
    private Sprite boxSprite;
    private Sprite birdSprite;
    private Array<Body> tmpBodies=new Array<Body>();
    private Body BoxBody,BirdBody;
    private boolean launched1,launched2,launched3,launched4=false;

    public Gameplay(Game game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
    }

    public Gameplay() {
    }

    @Override
    public void show() {
        world = new World(new Vector2(0, -9.8f), true); // Gravity vector (-9.8f for downward gravity)
        camera = new OrthographicCamera();
//        viewport = new FitViewport(800, 480, camera); // Adjust dimensions as needed
//        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        batch = new SpriteBatch();



        // Create the ground body
//        createGround();

        debugRenderer = new Box2DDebugRenderer();

        // Create ground
//        createGround();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(null); // Reset any previous input processor

        // Initialize the InputController
        InputController inputController = new InputController() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.ESCAPE:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new Levels(game));
                        break;
                    case Input.Keys.W:
                        // Add functionality for W key
                        break;
                    case Input.Keys.A:
                        // Add functionality for A key
                        break;
                    case Input.Keys.D:
                        // Add functionality for D key
                        break;
                    // Add more keys as needed
                }
                return true; // Return true to indicate the event was handled
            }
        };

        // Combine InputController and Stage using InputMultiplexer
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(inputController);
        Gdx.input.setInputProcessor(inputMultiplexer);

        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        settingTexture = new Texture(Gdx.files.internal("pause.png"));
        backTexture = new Texture(Gdx.files.internal("back.png"));
        settingsButton=createImageTextButton(settingTexture,100,100);
        backButton=createImageTextButton(backTexture,50,50);
//        Texture backgroundTexture = new Texture(Gdx.files.internal("gameplayBackground.jpg"));
//        backgroundImage = new Image(backgroundTexture);
//        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        stage.addActor(backgroundImage);

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









// Create an array of texture paths for the birds
        String[] texturePaths = {
            "redbird.png",     // Texture for Bird 1
            "bluebird.png",    // Texture for Bird 2
            "yellowbird.png",  // Texture for Bird 3
            "blackbird.png"    // Texture for Bird 4
        };
        BaseBird redbird=new BaseBird("redbird.png",false,new Vector2(-25, 20));
        BaseBird bluebird=new BaseBird("bluebird.png",false,new Vector2(-24, 20));
        BaseBird yellowbird=new BaseBird("yellowbird.png",false, new Vector2(-23, 20));
        BaseBird blackbird=new BaseBird("blackbird.png",false, new Vector2(-22, 20));
        birds.add(redbird);

        birds.add(bluebird);
        birds.add(yellowbird);
        birds.add(blackbird);
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
// Loop through the birds and create their bodies and sprites
        for (BaseBird bird:birds) {

            System.out.println("before launching"+bird.isLaunched());
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(bird.positions);

            CircleShape shape = new CircleShape();
            shape.setRadius(1f);


            fixtureDef.shape = shape;
            fixtureDef.density = 2.5f;
            fixtureDef.friction = 1f;
            fixtureDef.restitution = 0.1f;

            Body birdBody = world.createBody(bodyDef);
            birdBody.createFixture(fixtureDef);

            Sprite birdSprite = new Sprite(new Texture(bird.getTexturePath()));
            birdSprite.setSize(2, 2);
            birdSprite.setOrigin(birdSprite.getWidth() / 2, birdSprite.getHeight() / 2);
            birdBody.setUserData(birdSprite);
            birdBody.setAngularDamping(0.7f);
            BirdBodies.add(birdBody);
            // Optionally dispose of the shape when done
            shape.dispose();
        }


//        ground body
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,-10f);

        ChainShape groundShape=new ChainShape();
        groundShape.createChain(new Vector2[]{new Vector2(-50,0),new Vector2(50,0)});

        //fixture definition
        fixtureDef.shape=groundShape;
        fixtureDef.friction=.5f;
        fixtureDef.restitution=0;
        world.createBody(bodyDef).createFixture(fixtureDef);
        groundShape.dispose();


        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(2.25f,10);

        PolygonShape shape1=new PolygonShape();
        shape1.setAsBox(.5f,1);



        //fixture
        fixtureDef.shape=shape1;
        fixtureDef.friction=.75f;
        fixtureDef.restitution=.1f;
        fixtureDef.density=5;

        BoxBody=world.createBody(bodyDef);
        BoxBody.createFixture(fixtureDef);
        shape1.dispose();



//        loadNextBird();
//
//

//        addBlocksToStage();
//        createBirdsAndCatapultTable();
        TextButton winButton = new TextButton("Win", skin);
        TextButton loseButton = new TextButton("Lose", skin);



        dotTexture = new Texture(Gdx.files.internal("dot.jpg"));

        // Initialize the trajectory dots array
        trajectoryDots = new Array<>();

        // Add input listener for tracking mouse events
        Texture catapultTexture = new Texture(Gdx.files.internal("catapult.png"));
        catapultImage = new Image(catapultTexture);
        catapultImage.setPosition(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.26f);
        catapultImage.setSize(100, 100); // Adjust as needed
        stage.addActor(catapultImage);

        // Add input listener for the catapult
        catapultImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Check if the touch is within the catapult bounds
                startPosition.set(catapultImage.getX() + x, catapultImage.getY() + catapultImage.getHeight());
                return true; // Consume the event
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                // Calculate the drag direction
                float dragX = catapultImage.getX() + x;

                // Ensure the trajectory is only shown in the positive direction (forward)
                if (dragX < startPosition.x) {
                    // Update the end position and show trajectory
                    endPosition.set(dragX, catapultImage.getY() + y);
                    updateTrajectory(startPosition, endPosition);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                // Clear the trajectory visualization
                clearTrajectory();

                // Calculate the drag vector

                float dragX = startPosition.x - endPosition.x;
                float dragY = startPosition.y - endPosition.y;

                // Scale the drag to calculate force (adjust scale factor as needed)
                float scaleFactor = 80f; // Change this to control sensitivity
                float forceX = dragX * scaleFactor;
                float forceY = dragY * scaleFactor;

                // Apply the calculated force to the bird's body in the physics world
                for(int i=BirdBodies.size()-1;i>=0;i--) {
                    if(!birds.get(i).isLaunched()) {
                        birds.get(i).setLaunched(true);
                        BirdBodies.get(i).applyForceToCenter(forceX, forceY, true);
                        //                    BirdBodies.remove(i);
                        break;
                    }
                }
                for(BaseBird bird:birds){
                    System.out.println("after launching "+bird.isLaunched());
                }




                // Optional: Reset positions or other states if needed
            }
        });

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





    private void updateTrajectory(Vector2 startPosition, Vector2 endPosition) {
        clearTrajectory(); // Remove old trajectory dots

        // Calculate initial velocity vector (invert direction for +x trajectory)
        Vector2 velocity = new Vector2(startPosition.x - endPosition.x, startPosition.y - endPosition.y);

        // Physics constants (adjust based on your game)
        float gravity = -9.8f; // Gravity (adjust as per your game's scale)

        // Adjust the starting position to be above the catapult
        float catapultX = catapultImage.getX();
        float catapultY = catapultImage.getY() + catapultImage.getHeight(); // Start above the catapult
        startPosition.set(catapultX + catapultImage.getWidth() / 2, catapultY);

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




//    private void addBlocksToStage() {
//        Material woodMaterial = new WoodMaterial();
//        Material stoneMaterial = new StoneMaterial();
//        Material iceMaterial = new IceMaterial();
//
//        // Create a main table for blocks
//        Table blockTable = new Table();
//
//
//        blockTable.setName("blockTable");
//        blockTable.setSize(200, 200);
//        blockTable.setPosition(Gdx.graphics.getWidth() - blockTable.getWidth() - 90, Gdx.graphics.getHeight() * 0.15f - blockTable.getHeight() * 0.15f);
//
//
//        Table zeroFloor=new Table();
//
//        zeroFloor.setSize(200,50);
//        Pig mediumPig = new MediumPig();
//        zeroFloor.add(mediumPig).size(50,50);
//        blockTable.add(zeroFloor);
//        blockTable.row();
//        // Create a dedicated table for the ice block
//        Table pigTable1=new Table();
//        pigTable1.setSize(50,50);
//        Pig smallPig1 = new SmallPig();
//        pigTable1.add(smallPig1);
//
//
//        Table firstFloor=new Table();
//        firstFloor.setSize(200,50);
//
//        Table iceTable = new Table();
//        iceTable.setSize(50, 50);
//        Block iceBlock = new Block(iceMaterial, 50, 50);
//
//
//        iceTable.add(iceBlock).center();
//        Table pigTable2=new Table();
//        Pig smallPig2 = new SmallPig();
//        pigTable2.add(smallPig2);
////
//        firstFloor.add(pigTable1).size(50,50).bottom();
//        firstFloor.add(iceTable).size(70, 70).bottom(); // Add ice table to blockTable
//        firstFloor.add(pigTable2).size(50,50).bottom();
//
//        blockTable.add(firstFloor);
//        blockTable.row(); // Move to a new row for the stone block
//
//        // Create a dedicated table for the stone block
//        Table stoneTable = new Table();
//        stoneTable.setSize(100, 40);
//        Block stoneBlock = new Block(stoneMaterial, 60, 40);
//        stoneTable.add(stoneBlock).center().size(60, 40);
//        blockTable.add(stoneTable).expandX().bottom();
//        blockTable.row();
//
//        // Create two wood tables for two columns of wood blocks
//        Table Wood = new Table();
//        Wood.setSize(200, 100);
//        Table woodTable1 = new Table();
//        Table woodTable2 = new Table();
//        woodTable1.setSize(50, 100);
//        woodTable2.setSize(50, 100);
//
//
//        for (int i = 0; i < 3; i++) {
//            Block woodBlock1 = new Block(woodMaterial, 30, 30);
//            woodTable1.add(woodBlock1).size(30, 30);
//            woodTable1.row(); // Move to the next row after each block
//
//
//            Block woodBlock2 = new Block(woodMaterial, 30, 30);
//            woodTable2.add(woodBlock2).size(30, 30);
//            woodTable2.row();
//
//        }
//
//        // Add both woodTables to the main blockTable as separate columns
//        Wood.add(woodTable1).uniform().bottom().size(50, 100).padRight(30);
//        bigPig = new BigPig();
//        bigPig.setPosition(1100, 90);
//        Wood.add(bigPig);
//        Wood.add(woodTable2).uniform().bottom().size(50, 100).padLeft(30);
//        blockTable.add(Wood).bottom();
//        stage.addActor(blockTable);
//    }



    private void updateSettingsButtonPosition() {
        settingsButton.setPosition(Gdx.graphics.getWidth() - settingsButton.getWidth() - 70,
            Gdx.graphics.getHeight() - settingsButton.getHeight() - 50);
    }


    @Override
    public void resize(int width, int height) {
        camera.viewportWidth=width/25;
        camera.viewportHeight=height/25;
        camera.update();
        stage.getViewport().update(width, height, true);
//        backgroundImage.setSize(width, height);
        pointsLabel.setPosition(20, height - 100);
        updateSettingsButtonPosition();


        for (Actor actor : stage.getActors()) {
            if (actor instanceof Block) {
                Block block = (Block) actor;
                block.setSize(50, 50);
            }
        }

    }
    private boolean isBodyStopped(Body body) {
        // Threshold velocity to consider the bird as stopped
        float velocityThreshold = 0.2f;

        // Get linear velocity of the body
        Vector2 velocity = body.getLinearVelocity();

        // Check if both x and y velocity are below the threshold
        return Math.abs(velocity.x) < velocityThreshold && Math.abs(velocity.y) < velocityThreshold;
    }
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        camera.update();
        world.step(1 / 60f, 8, 3);
//        camera.position.set(BirdBody.getPosition().x,BirdBody.getPosition().y,0);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        world.getBodies(tmpBodies);
        for(Body body:tmpBodies){
            if(body.getUserData()!=null&&body.getUserData() instanceof Sprite){
                Sprite sprite=(Sprite)body.getUserData();
                sprite.setPosition(body.getPosition().x-sprite.getWidth()/2,body.getPosition().y-sprite.getHeight()/2);
                sprite.setRotation(body.getAngle()*MathUtils.radiansToDegrees);
                sprite.draw(batch);



            }
        }
        for (int i = BirdBodies.size() - 1; i >= 0; i--) {
            if (isBodyStopped(BirdBodies.get(i)) &&birds.get(i).isLaunched()) {
                System.out.println(i);
                world.destroyBody(BirdBodies.get(i)); // Destroy the bird body
                BirdBodies.remove(i);
                System.out.println(i);


                break; // Exit the loop after destroying
            }
        }


        batch.end();

        debugRenderer.render(world, camera.combined);
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
        batch.dispose();
        world.dispose();
        BirdBodies.clear();

        boxSprite.getTexture().dispose();
        birdSprite.getTexture().dispose();
        debugRenderer.dispose();
        stage.dispose();
        skin.dispose();
        settingTexture.dispose();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
