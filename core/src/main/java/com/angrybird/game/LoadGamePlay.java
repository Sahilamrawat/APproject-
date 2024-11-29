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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class LoadGamePlay extends Levels implements Screen {
    private Vector2 startPosition = new Vector2(); // Starting position of the catapult (on touchDown)
    private Vector2 endPosition = new Vector2();   // End position of the catapult (on touchDragged)

    private OrthographicCamera camera;
    private Stage pauseStage;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Array<Image> trajectoryDots;
    private Texture dotTexture;
    public static int LevelNo;

    private Stage stage;

    private static boolean isBirdLaunched = false;

    //    public Levels level=new Levels();
    Levels levels=new Levels();

    private Skin skin;
    private Game game;

    private Label pointsLabel;

    private static int points;
    private Screen previousScreen;
    private ImageTextButton settingsButton;
    private ImageTextButton backButton;
    private boolean isPaused = false;

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



    public int index;

    private ArrayList<Vector2> birdBodyPositions;
    private ArrayList<Vector2> pigBodyPositions;
    private ArrayList<Vector2> materialBodyPositions;


    private Array<Body> tmpBodies=new Array<Body>();
    private Body BoxBody,BirdBody,PigBody;
    private Sprite backgroundSprite;
    private static boolean birdsInitialized = false;

    private ImageTextButton resumeButton, exitButton, restartButton, saveButton,menuButton;
    private Texture resumeTexture, exitTexture, restartTexture, saveTexture,menuTexture;
    private Image button_image_1, button_image_2, buttonImage3, buttonImage4,buttonImage5,overlayImage;
    private static final String FILE_NAME = "data.json";
    public LoadGamePlay(ArrayList<BaseBird> birds, ArrayList<Pig> pigs, ArrayList<Material> materials,
                   ArrayList<Body> birdBodies, ArrayList<Body> pigsBodies, ArrayList<Body> materialBodies) {
        this.birds = birds;
        this.pigs = pigs;
        this.Materials = materials;

        this.birdBodyPositions = extractPositions(birdBodies);
        this.pigBodyPositions = extractPositions(pigsBodies);
        this.materialBodyPositions = extractPositions(materialBodies);
    }
    private ArrayList<Vector2> extractPositions(ArrayList<Body> bodies) {
        ArrayList<Vector2> positions = new ArrayList<>();
        for (Body body : bodies) {
            positions.add(body.getPosition());
        }
        return positions;
    }

    public LoadGamePlay(Game game, Screen previousScreen,int index) {
        super(game);
        this.game = game;
        this.index=index;
        this.previousScreen = previousScreen;
        loadData(index);

    }

    public LoadGamePlay() {

    }



    public void loadData(int gameIndex) {
        System.out.println("DATA IS GETTING LOADED");

        // Open the data file
        File file = new File("data.json");
        if (file.length() == 0) {
            System.out.println("The data file is empty. Nothing to load.");
            return;  // Exit the method if the file is empty
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("data.json"))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            // Parse the JSON content
            JSONObject jsonObject = new JSONObject(jsonContent.toString());

            // Check if player exists
            if (!jsonObject.has(playerName)) {
                System.out.println("Player with name " + playerName + " not found in the data.");
                return;  // Exit if the player data doesn't exist
            }

            // Get the player's data
            JSONObject playerData = jsonObject.getJSONObject(playerName);

            // Check the saved games count
            int savedGamesCount = playerData.getInt("savedGamesCount");
            if (gameIndex < 0 || gameIndex >= savedGamesCount) {
                System.out.println("Invalid game index. Please choose a valid game.");
                return;
            }

            // Get the selected game data
            JSONArray gamesArray = playerData.getJSONArray("games");
            JSONObject selectedGame = gamesArray.getJSONObject(gameIndex);

            // Load the selected game's data
            LevelNo = selectedGame.getInt("level");
            points = selectedGame.getInt("points");

            // Deserialize birds
            JSONArray birdsArray = selectedGame.getJSONArray("birds");
            birds = new ArrayList<>();
            for (int i = 0; i < birdsArray.length(); i++) {
                JSONObject bird = birdsArray.getJSONObject(i);
                birds.add(new BaseBird(
                    bird.getString("image"),
                    bird.getBoolean("isActive"),
                    new Vector2((float) bird.getJSONObject("position").getDouble("x"),
                        (float) bird.getJSONObject("position").getDouble("y")),
                    (float) bird.getDouble("power"),
                    bird.getString("type")
                ));
            }

            // Deserialize pigs
            JSONArray pigsArray = selectedGame.getJSONArray("pigs");
            pigs = new ArrayList<>();
            for (int i = 0; i < pigsArray.length(); i++) {
                JSONObject pig = pigsArray.getJSONObject(i);
                pigs.add(new Pig(
                    pig.getString("image"),
                    pig.getBoolean("isActive"),
                    new Vector2((float) pig.getJSONObject("position").getDouble("x"),
                        (float) pig.getJSONObject("position").getDouble("y")),
                    (float) pig.getDouble("health"),
                    pig.getString("type"),
                    pig.getInt("score")
                ));
            }

            // Deserialize materials
            JSONArray materialsArray = selectedGame.getJSONArray("materials");
            Materials = new ArrayList<>();
            for (int i = 0; i < materialsArray.length(); i++) {
                JSONObject material = materialsArray.getJSONObject(i);
                Materials.add(new Material(
                    material.getString("image"),
                    material.getBoolean("isActive"),
                    new Vector2((float) material.getJSONObject("position").getDouble("x"),
                        (float) material.getJSONObject("position").getDouble("y")),
                    (float) material.getDouble("damage"),
                    material.getString("type"),
                    material.getInt("score")
                ));
            }

            System.out.println("Game loaded successfully for player: " + playerName + " and game index: " + gameIndex);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Error parsing JSON data: " + e.getMessage());
        }
    }






    @Override
    public void show() {
        isBirdLaunched=false;
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






        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        if(!birdsInitialized) {
//            birds = new ArrayList<BaseBird>();
            BirdBodies=new ArrayList<Body>();

            // Loop through the birds and create their bodies and sprites
            for (BaseBird bird : birds) {

                bird.setBirdProperties();
                System.out.println("before launching" + bird.isLaunched());
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                bodyDef.position.set(bird.positions);

                CircleShape shape = new CircleShape();
                shape.setRadius(bird.getRadius());


                fixtureDef.shape = shape;
                fixtureDef.density = bird.getDensity();
                fixtureDef.friction = bird.getFriction();
                fixtureDef.restitution = bird.getRestitution();

                Body birdBody = world.createBody(bodyDef);
                birdBody.createFixture(fixtureDef);
                bird.setBody(birdBody);
                Sprite birdSprite = new Sprite(new Texture(bird.getTexturePath()));
                birdSprite.setSize(bird.getSpriteWidth(), bird.getSpriteHeight());
                birdSprite.setOrigin(birdSprite.getWidth() / 2, birdSprite.getHeight() / 2);
                birdBody.setUserData(birdSprite);
                birdBody.setAngularDamping(bird.getAngularDamping());
                BirdBodies.add(birdBody);
                bird.setBirdProperties();
                // Optionally dispose of the shape when done
                shape.dispose();
            }
            birdsInitialized = true;
        }

        //pig

//        pigs = new ArrayList<Pig>();
        pigsBodies=new ArrayList<Body>();




        // Loop through the birds and create their bodies and sprites
        for (Pig pig : pigs) {
            pig.setPigProperties();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(pig.positions);

            CircleShape shape = new CircleShape();
            if(pig.getPigType().equalsIgnoreCase("small")){
                shape.setRadius(pig.getRadius());
                fixtureDef.shape = shape;
                fixtureDef.density = pig.getDensity();
                fixtureDef.friction = pig.getFriction();
                fixtureDef.restitution = pig.getRestitution();
            }else if(pig.getPigType().equalsIgnoreCase("medium")){
                shape.setRadius(pig.getRadius());
                fixtureDef.shape = shape;
                fixtureDef.density = pig.getDensity();
                fixtureDef.friction = pig.getFriction();
                fixtureDef.restitution = pig.getRestitution();
            }else if(pig.getPigType().equalsIgnoreCase("big")){
                shape.setRadius(pig.getRadius());
                fixtureDef.shape = shape;
                fixtureDef.density = pig.getDensity();
                fixtureDef.friction = pig.getFriction();
                fixtureDef.restitution = pig.getRestitution();
            }





            Body pigBody = world.createBody(bodyDef);
            pigBody.createFixture(fixtureDef);

            Sprite pigSprite = new Sprite(new Texture(pig.getTexturePath()));
            if(pig.getPigType().equalsIgnoreCase("small")){
                pigSprite.setSize(pig.getSpriteWidth(), pig.getSpriteHeight());
                pigSprite.setOrigin(pigSprite.getWidth() / 2, pigSprite.getHeight() / 2);
                pigBody.setUserData(pigSprite);
                pigBody.setAngularDamping(pig.getAngularDamping());
                pigsBodies.add(pigBody);
                pig.setPigProperties();
            }else if(pig.getPigType().equalsIgnoreCase("medium")){
                pigSprite.setSize(pig.getSpriteWidth(), pig.getSpriteHeight());
                pigSprite.setOrigin(pigSprite.getWidth() / 2, pigSprite.getHeight() / 2);
                pigBody.setUserData(pigSprite);
                pigBody.setAngularDamping(pig.getAngularDamping());
                pigsBodies.add(pigBody);
                pig.setPigProperties();
            }else if(pig.getPigType().equalsIgnoreCase("big")){
                pigSprite.setSize(pig.getSpriteWidth(), pig.getSpriteHeight());
                pigSprite.setOrigin(pigSprite.getWidth() / 2, pigSprite.getHeight() / 2);
                pigBody.setUserData(pigSprite);
                pigBody.setAngularDamping(pig.getAngularDamping());
                pigsBodies.add(pigBody);
                pig.setPigProperties();
            }





            // Optionally dispose of the shape when done
            shape.dispose();
        }


        //material

//        Materials = new ArrayList<Material>();
        materialBodies=new ArrayList<Body>();




        // Loop through the birds and create their bodies and sprites
        for (Material material : Materials) {
            material.setMaterialProperties();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(material.positions);

            PolygonShape shape=new PolygonShape();
            if(material.getMaterialType().equalsIgnoreCase("wood")){

                shape.setAsBox(material.getWidth(),material.getHeight());
                //fixture
                fixtureDef.shape=shape;
                fixtureDef.friction=material.getFriction();
                fixtureDef.restitution=material.getRestitution();
                fixtureDef.density=material.getDensity();

            }else if(material.getMaterialType().equalsIgnoreCase("stone")){
                shape.setAsBox(material.getWidth(),material.getHeight());
                //fixture
                fixtureDef.shape=shape;
                fixtureDef.friction=material.getFriction();
                fixtureDef.restitution=material.getRestitution();
                fixtureDef.density=material.getDensity();
            }else if(material.getMaterialType().equalsIgnoreCase("ice")){
                shape.setAsBox(material.getWidth(),material.getHeight());
                //fixture
                fixtureDef.shape=shape;
                fixtureDef.friction=material.getFriction();
                fixtureDef.restitution=material.getRestitution();
                fixtureDef.density=material.getDensity();
            }





            Body MaterialBody = world.createBody(bodyDef);
            MaterialBody.createFixture(fixtureDef);

            Sprite materialSprite = new Sprite(new Texture(material.getTexturePath()));
            if(material.getMaterialType().equalsIgnoreCase("wood")){
                materialSprite.setSize(material.getSpriteWidth(), material.getSpriteHeight());
                materialSprite.setOrigin(materialSprite.getWidth() / 2, materialSprite.getHeight() / 2);
                MaterialBody.setUserData(materialSprite);
                MaterialBody.setAngularDamping(material.getAngularDamping());
                materialBodies.add(MaterialBody);
                material.setMaterialProperties();
            }else if(material.getMaterialType().equalsIgnoreCase("stone")){
                materialSprite.setSize(material.getSpriteWidth(), material.getSpriteHeight());
                materialSprite.setOrigin(materialSprite.getWidth() / 2, materialSprite.getHeight() / 2);
                MaterialBody.setUserData(materialSprite);
                MaterialBody.setAngularDamping(material.getAngularDamping());
                materialBodies.add(MaterialBody);
                material.setMaterialProperties();
            }else if(material.getMaterialType().equalsIgnoreCase("ice")){
                materialSprite.setSize(material.getSpriteWidth(), material.getSpriteHeight());
                materialSprite.setOrigin(materialSprite.getWidth() / 2, materialSprite.getHeight() / 2);
                MaterialBody.setUserData(materialSprite);
                MaterialBody.setAngularDamping(material.getAngularDamping());
                materialBodies.add(MaterialBody);
                material.setMaterialProperties();
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
        groundShape.createChain(new Vector2[]{new Vector2(-300,0),new Vector2(300,0)});

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



        dotTexture = new Texture(Gdx.files.internal("d3.png"));

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
                float scaleFactor = 100f; // Change this to control sensitivity
                float forceX = dragX * scaleFactor-15f;
                float forceY = dragY * scaleFactor-15f;

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

// Position lose button

// Add buttons to the stage





    }





    public static void saveGame(String levelName, ArrayList<BaseBird> birds, ArrayList<Pig> pigs, ArrayList<Material> materials, int points) {
        try {
            // Read existing data from the file (if exists)
            StringBuilder jsonContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("data.json"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
            } catch (FileNotFoundException e) {
                // File doesn't exist, we will create a new one
            }

            // Initialize JSON Object
            JSONObject jsonObject;

            if (jsonContent.length() > 0) {
                // If content exists, parse it
                jsonObject = new JSONObject(jsonContent.toString());
            } else {
                // If file is empty, initialize a new JSON object
                jsonObject = new JSONObject();
                jsonObject.put("savedGamesCount", 0);
                jsonObject.put("games", new JSONArray());
            }

            // Get the saved games count and increment it
            int savedGamesCount = jsonObject.getInt("savedGamesCount");
            jsonObject.put("savedGamesCount", savedGamesCount + 1);
            // Create the new game data
            JSONObject savedGame = new JSONObject();
            savedGame.put("level", levelName);
            savedGame.put("points", points);
            savedGame.put("birds", listToJsonArray(birds));  // Use updated helper method
            savedGame.put("pigs", listToJsonArray(pigs));    // Use updated helper method
            savedGame.put("materials", listToJsonArray(materials));  // Use updated helper method

            // Add the saved game to the games array
            JSONArray gamesArray = jsonObject.getJSONArray("games");
            gamesArray.put(savedGame);
            jsonObject.put("games", gamesArray);

            // Write the updated content to the file
            try (FileWriter writer = new FileWriter("data.json")) {
                writer.write(jsonObject.toString(4));  // Pretty print with indentation of 4 spaces
                System.out.println("Game saved successfully in data.json");
            }

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    // Updated helper method to convert ArrayList to JSONArray
    private static JSONArray listToJsonArray(ArrayList<?> list) {
        JSONArray jsonArray = new JSONArray();
        for (Object obj : list) {
            if (obj instanceof JsonSerializable) {
                jsonArray.put(((JsonSerializable) obj).toJson());
            } else {
                throw new IllegalArgumentException("Object does not implement JsonSerializable: " + obj);
            }
        }
        return jsonArray;
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
                if (LevelNo == 1) {
                    isBirdLaunched=false;
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new Level_1(game, null)); // Restart level
                }else if(LevelNo==2){
                    isBirdLaunched=false;
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new Level_2(game, null));
                } else if (LevelNo==3) {
                    isBirdLaunched=false;
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new Level_3(game, null));
                }
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
                saveGame(String.valueOf(LevelNo),birds,pigs,Materials,points);
                savedGames.add("Load Game "+counter++);
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
                dot.setSize(10, 10); // Adjust dot size
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
                    birds.remove(i);
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
                if(LevelNo==1){
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen(game, new Level_1(), new Level_2(), points, 1));
                }else if(LevelNo==2){
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen(game, new Level_2(), new Level_3(), points, 2));
                }else{
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen(game, new Level_3(), null, points, 3));
                }

            }

            // Check lose condition
            if (BirdBodies.isEmpty() && !pigsBodies.isEmpty()) {
                isBirdLaunched = false;
                birdsInitialized = false;
                if(LevelNo==1){
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LoseScreen(game, new Level_1(), points, 1));
                }else if(LevelNo==2){
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LoseScreen(game, new Level_2(), points, 2));
                }else{
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LoseScreen(game, new Level_3(), points, 3));
                }
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


    // Render the catapult


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


        debugRenderer.dispose();
        stage.dispose();
        skin.dispose();
        settingTexture.dispose();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

}
