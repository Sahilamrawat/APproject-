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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Level_3 implements Screen {
    private Vector2 startPosition = new Vector2(); // Starting position of the catapult (on touchDown)
    private Vector2 endPosition = new Vector2();   // End position of the catapult (on touchDragged)

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Array<Image> trajectoryDots;
    private Texture dotTexture;

    private Stage stage;

    private boolean isBirdLaunched = false;

    public int LevelNo=3;
    private Skin skin;
    private Game game;

    private Label pointsLabel;

    private int points;
    private Screen previousScreen;
    private ImageTextButton settingsButton;
    private ImageTextButton backButton;
    private boolean isPaused = false;
    private Stage pauseStage;
    private Texture settingTexture;
    private Texture backTexture;
    private Image catapultImage;
    private ArrayList<BaseBird> birds;
    private ArrayList<Pig> pigs;
    private ArrayList<Material> Materials;

    private Image buttonImage1, buttonImage2;

    public ArrayList<Body> BirdBodies;
    public ArrayList<Body> pigsBodies;
    public ArrayList<Body> materialBodies;


    private Array<Body> tmpBodies=new Array<Body>();
    private Body BoxBody,BirdBody,PigBody;
    private Sprite backgroundSprite;
    private static boolean birdsInitialized = false;
    private ImageTextButton resumeButton, exitButton, restartButton, saveButton,menuButton;
    private Texture resumeTexture, exitTexture, restartTexture, saveTexture,menuTexture;
    private Image button_image_1, button_image_2, buttonImage3, buttonImage4,buttonImage5,overlayImage;



    public Level_3(Game game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
    }

    public Level_3() {
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
        points = 0;
        // Combine InputController and Stage using InputMultiplexer
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(inputController);
        Gdx.input.setInputProcessor(inputMultiplexer);

        TextureAtlas atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        settingTexture = new Texture(Gdx.files.internal("pause.png"));

        settingsButton=createImageTextButton(settingTexture,100,100);

        backgroundSprite = new Sprite(new Texture("gameplayBackground.jpg"));
        backgroundSprite.setSize(1920f/50f*2f, 1080f/50f*2f);
        backgroundSprite.setOrigin(backgroundSprite.getWidth()/2,backgroundSprite.getHeight()/2);
        backgroundSprite.setPosition(-38,-20.5f);


        pointsLabel = new Label("Points: " + points, skin, "title");
        pointsLabel.setPosition(20, Gdx.graphics.getHeight() - 30);
        stage.addActor(pointsLabel);


        updateSettingsButtonPosition();

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createPauseMenu();
                isPaused=true;
                isBirdLaunched=false;
//                LevelStates.get(LevelNo-1).render(deltatime);

                Gdx.input.setInputProcessor(pauseStage); // Switch input to pauseStage
                pauseStage.addAction(Actions.fadeIn(0.5f));

                birdsInitialized=false;
                System.out.println("Game Status "+isPaused);

            }
        });
        System.out.println("Game Status "+isPaused);
        addHoverEffect(buttonImage2, settingsButton);
        stage.addActor(settingsButton);











// Create an array of texture paths for the birds
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        if(!birdsInitialized) {
            birds = new ArrayList<BaseBird>();
            BirdBodies=new ArrayList<Body>();

            BaseBird redbird = new BaseBird("redbird.png", false, new Vector2(-25, -14.5f),27,"redBird");
            BaseBird bluebird = new BaseBird("bluebird.png", false, new Vector2(-24, -14.5f),20,"blueBird");
            BaseBird yellowbird = new BaseBird("yellowbird.png", false, new Vector2(-23, -14.5f),25,"blackBird");
            BaseBird blackbird = new BaseBird("blackbird.png", false, new Vector2(-22, -14.5f),30,"yellowBird");
            birds.add(redbird);

            birds.add(bluebird);
            birds.add(yellowbird);
            birds.add(blackbird);

            // Loop through the birds and create their bodies and sprites
            for (BaseBird bird : birds) {

                System.out.println("before launching" + bird.isLaunched());
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                bodyDef.position.set(bird.positions);

                CircleShape shape = new CircleShape();
                shape.setRadius(1f);


                fixtureDef.shape = shape;
                fixtureDef.density = 5f;
                fixtureDef.friction = 1f;
                fixtureDef.restitution = 0.1f;

                Body birdBody = world.createBody(bodyDef);
                birdBody.createFixture(fixtureDef);
                bird.setBody(birdBody);
                Sprite birdSprite = new Sprite(new Texture(bird.getTexturePath()));
                birdSprite.setSize(2, 2);
                birdSprite.setOrigin(birdSprite.getWidth() / 2, birdSprite.getHeight() / 2);
                birdBody.setUserData(birdSprite);
                birdBody.setAngularDamping(0.7f);
                BirdBodies.add(birdBody);
                // Optionally dispose of the shape when done
                shape.dispose();
            }
            birdsInitialized = true;
        }

        //pig

        pigs = new ArrayList<Pig>();
        pigsBodies=new ArrayList<Body>();

        Pig smallPig1 = new Pig("small_pig.png", false, new Vector2(13.10f,-14.5f),10,"small",500);
        Pig smallPig2 = new Pig("small_pig.png", false, new Vector2(21.30f,-14.5f),10,"small",500);
        Pig mediumPig = new Pig("medium_pig.png", false, new Vector2(17.10f,-5),20,"medium",1000);
        Pig bigPig = new Pig("big_pig.png", false, new Vector2(17.10f,-14.5f),30,"big",2000);

        Pig smallPig3 = new Pig("small_pig.png", false, new Vector2(13.10f,-7f),10,"small",500);
        Pig smallPig4 = new Pig("small_pig.png", false, new Vector2(21.50f,-7f),10,"small",500);

        Pig mediumPig1 = new Pig("medium_pig.png", false, new Vector2(14.3f,-1),20,"medium",1000);
        Pig mediumPig2 = new Pig("medium_pig.png", false, new Vector2(19.10f,-1),20,"medium",1000);

        Pig bigPig1 = new Pig("big_pig.png", false, new Vector2(17.10f,4),30,"big",2000);
        Pig smallPig5 = new Pig("small_pig.png", false, new Vector2(14.10f,4),24,"small",500);
        Pig smallPig6 = new Pig("small_pig.png", false, new Vector2(22.30f,4),24,"small",500);


        Pig smallPig7 = new Pig("small_pig.png", false, new Vector2(11.10f,4),10,"small",500);
        Pig smallPig8 = new Pig("small_pig.png", false, new Vector2(24.30f,4),10,"small",500);

        pigs.add(smallPig1);
        pigs.add(smallPig2);
        pigs.add(smallPig3);
        pigs.add(smallPig4);

        pigs.add(smallPig5);
        pigs.add(smallPig6);


        pigs.add(smallPig7);
        pigs.add(smallPig8);



        pigs.add(mediumPig);

        pigs.add(mediumPig1);
        pigs.add(mediumPig2);

        pigs.add(bigPig);
        pigs.add(bigPig1);


        // Loop through the birds and create their bodies and sprites
        for (Pig pig : pigs) {

            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(pig.positions);

            CircleShape shape = new CircleShape();
            if(pig.getPigType().equalsIgnoreCase("small")){
                shape.setRadius(0.7f);
                fixtureDef.shape = shape;
                fixtureDef.density = 5f;
                fixtureDef.friction = 1f;
                fixtureDef.restitution = 0f;
            }else if(pig.getPigType().equalsIgnoreCase("medium")){
                shape.setRadius(1f);
                fixtureDef.shape = shape;
                fixtureDef.density = 5f;
                fixtureDef.friction = 1f;
                fixtureDef.restitution = 0f;
            }else if(pig.getPigType().equalsIgnoreCase("big")){
                shape.setRadius(1.1f);
                fixtureDef.shape = shape;
                fixtureDef.density = 5f;
                fixtureDef.friction = 1f;
                fixtureDef.restitution = 0f;
            }





            Body pigBody = world.createBody(bodyDef);
            pigBody.createFixture(fixtureDef);

            Sprite pigSprite = new Sprite(new Texture(pig.getTexturePath()));
            if(pig.getPigType().equalsIgnoreCase("small")){
                pigSprite.setSize(2, 1.5f);
                pigSprite.setOrigin(pigSprite.getWidth() / 2, pigSprite.getHeight() / 2);
                pigBody.setUserData(pigSprite);
                pigBody.setAngularDamping(3f);
//                pigBody.setGravityScale(5f);
                pigsBodies.add(pigBody);
            }else if(pig.getPigType().equalsIgnoreCase("medium")){
                pigSprite.setSize(2, 2);
                pigSprite.setOrigin(pigSprite.getWidth() / 2, pigSprite.getHeight() / 2);
                pigBody.setUserData(pigSprite);
                pigBody.setAngularDamping(7f);
//                pigBody.setGravityScale(5f);
                pigsBodies.add(pigBody);
            }else if(pig.getPigType().equalsIgnoreCase("big")){
                pigSprite.setSize(4, 3);
                pigSprite.setOrigin(pigSprite.getWidth() / 2, pigSprite.getHeight() / 2);
                pigBody.setUserData(pigSprite);
                pigBody.setAngularDamping(20f);
//                pigBody.setGravityScale(5f);
                pigsBodies.add(pigBody);
            }





            // Optionally dispose of the shape when done
            shape.dispose();
        }


        //material

        Materials = new ArrayList<Material>();
        materialBodies=new ArrayList<Body>();

        Material woodBlock = new Material("wood_block.png", false, new Vector2(14.4f,-11.5f),10,"wood",100);
        Material StoneBlock = new Material("stone_block.png", false, new Vector2(17.10f,-8),20,"stone",200);
//        Material StoneBlock1 = new Material("stone_block.png", false, new Vector2(14.8f,18),10,"stone1");
        Material IceBlock = new Material("ice_block.png", false, new Vector2(17.10f,-6),20,"ice",150);
        Material woodBlock1 = new Material("wood_block.png", false, new Vector2(19.6f,-11f),10,"wood",100);
        Material woodBlock2 = new Material("wood_block.png", false, new Vector2(11.4f,-11.5f),10,"wood",100);
        Material woodBlock3 = new Material("wood_block.png", false, new Vector2(22.6f,-11.5f),10,"wood",100);
        Material StoneBlock1 = new Material("stone_block.png", false, new Vector2(17.10f,-2),20,"stone",200);
        Material IceBlock1 = new Material("ice_block.png", false, new Vector2(17.10f,-1),20,"ice",150);
        Material StoneBlock2 = new Material("stone_block.png", false, new Vector2(17.10f,1f),20,"stone",200);
        Material woodBlock4 = new Material("wood_block.png", false, new Vector2(11.4f,-6),10,"wood",100);
        Material woodBlock5 = new Material("wood_block.png", false, new Vector2(22.6f,-6),10,"wood",100);


        Material woodBlock6 = new Material("wood_block.png", false, new Vector2(14.4f,-6f),10,"wood",100);
        Material woodBlock7 = new Material("wood_block.png", false, new Vector2(19.8f,-7f),10,"wood",100);

        Material IceBlock2 = new Material("ice_block.png", false, new Vector2(12.10f,-1),20,"ice",150);
        Material IceBlock3 = new Material("ice_block.png", false, new Vector2(22.10f,-1),20,"ice",150);

        Material woodBlock8 = new Material("wood_block.png", false, new Vector2(14.4f,4f),10,"wood",100);
        Material woodBlock9 = new Material("wood_block.png", false, new Vector2(19.6f,4f),10,"wood",100);

        Material woodBlock10 = new Material("wood_block.png", false, new Vector2(11.4f,4),10,"wood",100);
        Material woodBlock11 = new Material("wood_block.png", false, new Vector2(22.6f,4),10,"wood",100);
//
        Material StoneBlock3 = new Material("stone_block.png", false, new Vector2(17.10f,9f),20,"stone",200);

        Materials.add(woodBlock);
        Materials.add(woodBlock4);
        Materials.add(woodBlock5);

        Materials.add(woodBlock6);
        Materials.add(woodBlock7);

        Materials.add(woodBlock8);
        Materials.add(woodBlock9);

        Materials.add(woodBlock10);
        Materials.add(woodBlock11);

        Materials.add(StoneBlock);
        Materials.add(StoneBlock1);
        Materials.add(StoneBlock2);

        Materials.add(StoneBlock3);



        Materials.add(woodBlock2);
        Materials.add(woodBlock3);
        Materials.add(IceBlock);
        Materials.add(IceBlock1);
        Materials.add(IceBlock2);
        Materials.add(IceBlock3);

        Materials.add(woodBlock1);


        // Loop through the birds and create their bodies and sprites
        for (Material material : Materials) {

            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(material.positions);

            PolygonShape shape=new PolygonShape();
            if(material.getMaterialType().equalsIgnoreCase("wood")){

                shape.setAsBox(.6f,3);
                //fixture
                fixtureDef.shape=shape;
                fixtureDef.friction=1f;
                fixtureDef.restitution=0f;
                fixtureDef.density=5;
            }else if(material.getMaterialType().equalsIgnoreCase("stone")){
                shape.setAsBox(10,0.5f);
                //fixture
                fixtureDef.shape=shape;
                fixtureDef.friction=1f;
                fixtureDef.restitution=0f;
                fixtureDef.density=5;

            }
            else if(material.getMaterialType().equalsIgnoreCase("ice")){
                shape.setAsBox(1.2f,1.2f);
                //fixture
                fixtureDef.shape=shape;
                fixtureDef.friction=1f;
                fixtureDef.restitution=-10f;
                fixtureDef.density=5;
            }





            Body MaterialBody = world.createBody(bodyDef);
            MaterialBody.createFixture(fixtureDef);

            Sprite materialSprite = new Sprite(new Texture(material.getTexturePath()));
            if(material.getMaterialType().equalsIgnoreCase("wood")){
                materialSprite.setSize(1, 6.2f);
                materialSprite.setOrigin(materialSprite.getWidth() / 2, materialSprite.getHeight() / 2);
                MaterialBody.setUserData(materialSprite);
//                MaterialBody.setGravityScale(6f);
                MaterialBody.setAngularDamping(0.7f);
                materialBodies.add(MaterialBody);
            }else if(material.getMaterialType().equalsIgnoreCase("stone")){
                materialSprite.setSize(20f, 1f);
                materialSprite.setOrigin(materialSprite.getWidth() / 2, materialSprite.getHeight() / 2);
                MaterialBody.setUserData(materialSprite);
                MaterialBody.setAngularDamping(0.7f);
//                MaterialBody.setGravityScale(6f);
                materialBodies.add(MaterialBody);

            }else if(material.getMaterialType().equalsIgnoreCase("ice")){
                materialSprite.setSize(2.4f, 2.4f);
                materialSprite.setOrigin(materialSprite.getWidth() / 2, materialSprite.getHeight() / 2);
                MaterialBody.setUserData(materialSprite);
                MaterialBody.setAngularDamping(0.7f);
//                MaterialBody.setGravityScale(3f);
                materialBodies.add(MaterialBody);
            }





            // Optionally dispose of the shape when done
            shape.dispose();
        }









        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Retrieve the bodies associated with the fixtures
                Body bodyA = fixtureA.getBody();
                Body bodyB = fixtureB.getBody();
                if (isBirdBody(bodyA) && isPigBody(bodyB)) {
                    BaseBird bird = getBirdFromBody(bodyA);
                    Pig pig = getPigFromBody(bodyB);
                    if (bird != null && pig != null) {
                        handleCollision(pig,bird);
                    }
                } else if (isBirdBody(bodyB) && isPigBody(bodyA)) {
                    BaseBird bird = getBirdFromBody(bodyB);
                    Pig pig = getPigFromBody(bodyA);
                    if (bird != null && pig != null) {
                        handleCollision(pig,bird);
                    }
                }
                if (isBirdLaunched) {
                    if (isMaterialBody(bodyA) && isBirdBody(bodyB)) {
                        Material material = getMaterialFromBody(bodyA);
                        BaseBird bird = getBirdFromBody(bodyB);
                        if (material != null && bird != null) {
                            handleBirdMaterialCollision(bird, material);
                        }
                    } else if (isMaterialBody(bodyB) && isBirdBody(bodyA)) {
                        Material material = getMaterialFromBody(bodyB);
                        BaseBird bird = getBirdFromBody(bodyA);
                        if (material != null && bird != null) {
                            handleBirdMaterialCollision(bird, material);
                        }
                    }
                }
                if (isBirdLaunched) {
                    if (isMaterialBody(bodyA) && isPigBody(bodyB)) {
                        Material material = getMaterialFromBody(bodyA);
                        Pig pig = getPigFromBody(bodyB);
                        if (material != null && pig != null) {
                            handleMaterialCollision(pig, material);
                        }
                    } else if (isMaterialBody(bodyB) && isPigBody(bodyA)) {
                        Material material = getMaterialFromBody(bodyB);
                        Pig pig = getPigFromBody(bodyA);
                        if (material != null && pig != null) {
                            handleMaterialCollision(pig, material);
                        }
                    }
                }


            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

//        ground body
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,-14.5f);

        ChainShape groundShape=new ChainShape();
        groundShape.createChain(new Vector2[]{new Vector2(-50,0),new Vector2(50,0)});

        //fixture definition
        fixtureDef.shape=groundShape;
        fixtureDef.friction=.5f;
        fixtureDef.restitution=0;
        world.createBody(bodyDef).createFixture(fixtureDef);
        groundShape.dispose();


        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(40,-14.5f);

        ChainShape groundShape1=new ChainShape();
        groundShape1.createChain(new Vector2[]{new Vector2(0,-300),new Vector2(0,300)});

        //fixture definition
        fixtureDef.shape=groundShape1;
        fixtureDef.friction=.5f;
        fixtureDef.restitution=0;
        world.createBody(bodyDef).createFixture(fixtureDef);
        groundShape1.dispose();



        TextButton winButton = new TextButton("Win", skin);
        TextButton loseButton = new TextButton("Lose", skin);



        dotTexture = new Texture(Gdx.files.internal("dot.png"));

        // Initialize the trajectory dots array
        trajectoryDots = new Array<>();

        // Add input listener for tracking mouse events
//        Texture catapultTexture = new Texture(Gdx.files.internal("catapult.png"));

//        catapultImage.setPosition(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.26f);
//        catapultImage.setSize(100, 100); // Adjust as needed
//        stage.addActor(catapultImage);


        //catapult
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(-16,-13.3f);

        PolygonShape shape4=new PolygonShape();
        shape4.setAsBox(1f,1.2f);



        //fixture
        fixtureDef.shape=shape4;
        fixtureDef.friction=.75f;
        fixtureDef.restitution=.1f;
        fixtureDef.density=5;

        BoxBody=world.createBody(bodyDef);
        BoxBody.createFixture(fixtureDef);

        Sprite catapultSprite = new Sprite(new Texture("catapult.png"));

        catapultSprite.setSize(3, 4);
        catapultSprite.setOrigin(catapultSprite.getWidth() / 2, catapultSprite.getHeight() / 2);
//        BoxBody.setUserData(catapultSprite);
        BoxBody.setAngularDamping(0.7f);
        catapultImage = new Image(catapultSprite);
//        catapultImage.setSize(-16,-8);

        stage.addActor(catapultImage);


        shape4.dispose();

        positionClosestBird();
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
                float scaleFactor = 200f; // Change this to control sensitivity
                float forceX = dragX * scaleFactor;
                float forceY = dragY * scaleFactor;

                // Apply the calculated force to the bird's body in the physics world
                for(int i=BirdBodies.size()-1;i>=0;i--) {
                    if(!birds.get(i).isLaunched()) {
                        birds.get(i).setLaunched(true);
                        BirdBodies.get(i).applyForceToCenter(forceX, forceY, true);
                        //                    BirdBodies.remove(i);
                        positionClosestBird();
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




        addHoverEffect(buttonImage2, settingsButton);
        stage.addActor(settingsButton);






    }
    private void createPauseMenu() {
        pauseStage = new Stage(new ScreenViewport());

        // Overlay background
        overlayImage = new Image(new Texture(Gdx.files.internal("profilebackground.png")));
        overlayImage.setSize(Gdx.graphics.getWidth() / 2f - 390, Gdx.graphics.getHeight() / 2f + 140);
        overlayImage.setPosition((Gdx.graphics.getWidth() - overlayImage.getWidth()) / 2,
            (Gdx.graphics.getHeight() - overlayImage.getHeight()) / 2);
        pauseStage.addActor(overlayImage);

        // Pause menu buttons
        resumeTexture = new Texture(Gdx.files.internal("resume.png"));
        exitTexture = new Texture(Gdx.files.internal("exit3.png"));
        restartTexture = new Texture(Gdx.files.internal("restart.png"));
        saveTexture = new Texture(Gdx.files.internal("save.png"));
        menuTexture = new Texture(Gdx.files.internal("menu1.png"));

        resumeButton = createImageTextButton(resumeTexture, 80, 80);
        exitButton = createImageTextButton(exitTexture, 80, 80);
        restartButton = createImageTextButton(restartTexture, 80, 80);
        saveButton = createImageTextButton(saveTexture, 90, 90);
        menuButton = createImageTextButton(menuTexture, 80, 80);



        addHoverEffect(button_image_2,restartButton);
        addHoverEffect(button_image_1,resumeButton);
        addHoverEffect(buttonImage3,saveButton);
        addHoverEffect(buttonImage4,menuButton);
        addHoverEffect(buttonImage5,exitButton);
        // Table to organize buttons
        Table table = new Table();
        table.setSize(200, 650);
        table.setPosition(
            Gdx.graphics.getWidth() / 2 - table.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - table.getHeight() / 2
        );

        table.add(new Label("ANGRY BIRD", skin, "title")).padBottom(20).row();
        table.add(resumeButton).padBottom(5).row();
        table.add(restartButton).padBottom(5).row();
        table.add(saveButton).padBottom(5).row();
        table.add(menuButton).padBottom(5).row();
        table.add(exitButton).padBottom(5);

        pauseStage.addActor(table);

        // Button listeners
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(stage); // Switch back to main stage
                pauseStage.addAction(Actions.fadeOut(0.5f)); // Fade out pause menu
                isPaused=false;
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new Level_3(game,null)); // Restart level
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit game
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Game Saved"); // Implement save logic
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu()); // Go to main menu
            }
        });
    }
    private boolean isBirdBody(Body body) {
        return BirdBodies.contains(body);
    }

    private boolean isPigBody(Body body) {
        return pigsBodies.contains(body);
    }

    private boolean isMaterialBody(Body body) {
        return materialBodies.contains(body);
    }
    private BaseBird getBirdFromBody(Body body) {
        int index = BirdBodies.indexOf(body);
        return index != -1 ? birds.get(index) : null;
    }

    private Pig getPigFromBody(Body body) {
        int index = pigsBodies.indexOf(body);
        return index != -1 ? pigs.get(index) : null;
    }

    private Material getMaterialFromBody(Body body) {
        int index = materialBodies.indexOf(body);
        return index != -1 ? Materials.get(index) : null;
    }

    public void handleBirdMaterialCollision(BaseBird bird, Material material) {
        float damage = calculateDamage(bird);
        material.damage(damage);
    }
    private void handleCollision(Pig pig, BaseBird bird) {
        float damage = calculateDamage(bird);
        System.out.println(bird.birdType+": is my bird type");

        pig.damage(damage);
        System.out.println(pig.getPigType()+" recieved this much damage "+ bird.getDamage()+" current health "+pig.getHealth());
        pig.setCollided(true);

    }
    private void handleMaterialCollision(Pig pig, Material material) {
        float damage = calculateMaterialDamage(material);
//        System.out.println(bird.birdType+": is my bird type");
        material.damage(damage);
        pig.damage(damage);
        System.out.println(pig.getPigType()+" recieved this much damage "+ material.getDamage()+" current health "+pig.getHealth());
        pig.setMaterialCollided(true);

    }

    private float calculateDamage(BaseBird bird) {
        return bird.getDamage();
    }
    private float calculateMaterialDamage(Material material) {
        return material.getDamage();
    }


    private void positionClosestBird() {
        if (!birds.isEmpty() && !BirdBodies.isEmpty()) {
            // Find the first bird that is not launched
            for (int i =birds.size()-1; i >=0; i--) {
                if (!birds.get(i).isLaunched()) {
                    // Get the top position of the catapult
                    Vector2 catapultTop = new Vector2(
                        BoxBody.getPosition().x,
                        BoxBody.getPosition().y + 2 // Half the height of the catapult (Box2D units)
                    );

                    // Set the bird's body position to the top of the catapult
                    BirdBodies.get(i).setTransform(catapultTop, 0);

                    // Optionally, adjust the sprite position as well for rendering
                    Sprite birdSprite = (Sprite) BirdBodies.get(i).getUserData();
                    birdSprite.setPosition(
                        catapultTop.x - birdSprite.getWidth() / 2,
                        catapultTop.y - birdSprite.getHeight() / 2
                    );

                    // Break after positioning one bird
                    break;
                }
            }
        }
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

        }else if (texture == resumeTexture) {
            button_image_1 = buttonImage;
        }
        else if (texture == restartTexture) {
            button_image_2 = buttonImage;
        }
        else if (texture == saveTexture) {
            buttonImage3 = buttonImage;
        }
        else if (texture == menuTexture) {
            buttonImage4 = buttonImage;
        }
        else if (texture == exitTexture) {
            buttonImage5 = buttonImage;
        }

        return button;

    }


    private void updateBackButtonPosition() {
        backButton.setPosition(Gdx.graphics.getWidth() - backButton.getWidth() - 20,
            Gdx.graphics.getHeight() - backButton.getHeight() - 20);
    }








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

        if (!isPaused) {
            // Gameplay rendering logic
            debugRenderer.render(world, camera.combined);
            camera.update();
            world.step(1 / 60f, 8, 3);

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // Draw the background
            backgroundSprite.draw(batch);

            // Update and draw all sprites in the world
            world.getBodies(tmpBodies);
            for (Body body : tmpBodies) {
                if (body.getUserData() != null && body.getUserData() instanceof Sprite) {
                    Sprite sprite = (Sprite) body.getUserData();
                    sprite.setPosition(
                        body.getPosition().x - sprite.getWidth() / 2,
                        body.getPosition().y - sprite.getHeight() / 2
                    );
                    sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                    sprite.draw(batch);
                }
            }

            // Update points
            pointsLabel.setText("Points: " + points);

            // Handle birds logic
            for (int i = BirdBodies.size() - 1; i >= 0; i--) {
                if (isBodyStopped(BirdBodies.get(i)) && birds.get(i).isLaunched()) {
                    isBirdLaunched = true;
                    world.destroyBody(BirdBodies.get(i));
                    BirdBodies.remove(i);
                    break;
                }
            }

            // Handle pigs logic
            for (int i = pigsBodies.size() - 1; i >= 0; i--) {
                if (pigs.get(i).isDestroyed) {
                    points += pigs.get(i).gamePoints;
                    world.destroyBody(pigsBodies.get(i));
                    pigsBodies.remove(i);
                    pigs.remove(i);
                    break;
                }
            }

            // Handle materials logic
            for (int i = materialBodies.size() - 1; i >= 0; i--) {
                if (Materials.get(i).isDestroyed) {
                    points += Materials.get(i).points;
                    world.destroyBody(materialBodies.get(i));
                    materialBodies.remove(i);
                    Materials.remove(i);
                }
            }

            // Check win condition
            if (pigsBodies.isEmpty()) {
                isBirdLaunched = false;
                birdsInitialized = false;
                for (int i = 0; i < Levels.levels.size(); i++) {
                    if (Levels.levels.get(i).LevelNo == LevelNo) {
                        Levels.levels.get(i).isCompleted = true;
                        if (i + 1 < Levels.levels.size()) {
                            Levels.levels.get(i + 1).isUnlocked = true;
                        }
                    }
                }
                ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen(game, new Level_3(), null, points, 3));
            }

            // Check lose condition
            if (BirdBodies.isEmpty() && !pigsBodies.isEmpty()) {
                isBirdLaunched = false;
                birdsInitialized = false;
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoseScreen(game, new Level_3(), points, 3));
            }

            batch.end();
            Vector2 bodyPosition = BoxBody.getPosition();
            catapultImage.setPosition(
                171.5f*3f,52.5f*3
            );
            catapultImage.setSize(85,100);
            stage.act(delta);
            stage.draw();
        } else {
            // Pause menu rendering logic
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // Draw only the background
            backgroundSprite.draw(batch);
            batch.end();

            // Draw the pause menu
            pauseStage.act(delta);
            pauseStage.draw();
        }
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


        debugRenderer.dispose();
        stage.dispose();
        skin.dispose();
        settingTexture.dispose();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
