package com.angrybird.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.json.JSONObject;

public class BaseBird extends Actor implements Bird,JsonSerializable {
    private Body body;
    private Texture texture;
    private Image image;
    private float initialWidth, initialHeight;
    private Sprite sprite;
    private World world; // Reference to Box2D world
    private String texturePath;
    private Sprite birdSprite;
    public boolean isLaunched;
    public float damage;
    Vector2 positions;
    public String birdType;
    private float radius;
    private float density;
    private float friction;
    private float restitution;
    private float angularDamping;
    private float spriteWidth;
    private float spriteHeight;

    public float getDamage() {
        return damage;
    }
    public void setBirdProperties() {
        this.radius = 1f;
        this.density = 2.5f;
        this.friction = 1f;
        this.restitution = 0.1f;
        this.angularDamping = 0.7f;
        this.spriteWidth = 2f;
        this.spriteHeight = 2f;
    }
    // Constructor to initialize the bird texture and physics body
    public BaseBird(String texturePath, boolean isLaunched, Vector2 positions,float damage,String birdType) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.birdType=birdType;

        this.texturePath = texturePath;
        this.isLaunched = isLaunched;
        this.positions = positions; // Store the initial position
        this.damage=damage;
    }


    public String getTexturePath() {
        return texturePath;
    }

    public Image getImage() {
        return image;
    }

    public float getRadius() {
        return radius;
    }

    public float getDensity() {
        return density;
    }

    public float getFriction() {
        return friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public float getSpriteWidth() {
        return spriteWidth;
    }

    public float getSpriteHeight() {
        return spriteHeight;
    }

    public BaseBird() {
    }

    public float getInitialWidth() {
        return initialWidth;
    }

    public String getBirdType() {
        return birdType;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("image", texture);
        json.put("isActive", isLaunched);
        JSONObject positionJson = new JSONObject();
        positionJson.put("x", positions.x);
        positionJson.put("y", positions.y);
        json.put("position", positionJson);
        json.put("power", damage);
        json.put("type", birdType);
        json.put("radius", radius);
        json.put("density", density);
        json.put("friction", friction);
        json.put("restitution", restitution);
        json.put("angularDamping", angularDamping);
        json.put("SpriteWidth", spriteWidth);
        json.put("SpriteHeight", spriteHeight);
        return json;
    }
//    @Override
//    public String toString() {
//        return String.format("{\n" +
//                "  \"image\": \"%s\",\n" +
//                "  \"isActive\": %b,\n" +
//                "  \"position\": {\"x\": %.2f, \"y\": %.2f},\n" +
//                "  \"power\": %.2f,\n" +
//                "  \"type\": \"%s\",\n" +
//                "  \"radius\": %.2f,\n" +
//                "  \"density\": %.2f,\n" +
//                "  \"friction\": %.2f,\n" +
//                "  \"restitution\": %.2f,\n" +
//                "  \"angularDamping\": %.2f,\n" +
//                "  \"SpriteWidth\": %.2f,\n" +
//                "  \"SpriteHeight\": %.2f\n"+
//                "}",
//            texture, isLaunched, positions.x, positions.y, damage, birdType, radius, density, friction, restitution, angularDamping,spriteWidth,spriteHeight);
//    }
    public void setLaunched(boolean launched) {
        isLaunched = launched;
    }
    // Create Box2D body and fixture for the bird

    public Sprite getSprite() {
        return sprite;
    }

    // Setter for body
    public void setBody(Body body) {
        this.body = body;
    }

    // Getter for body

    @Override
    public void update(float deltaTime) {
        // Update the position based on Box2D physics
        image.setPosition(body.getPosition().x - image.getWidth() / 2, body.getPosition().y - image.getHeight() / 2);
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void performAction() {

    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    @Override
    public void resize(float screenWidth, float screenHeight) {
        setPosition(screenWidth * 0.1f, screenHeight * 0.1f);
        float scale = Math.min(screenWidth / Gdx.graphics.getWidth(), screenHeight / Gdx.graphics.getHeight());
        setSize(initialWidth * scale, initialHeight * scale);
    }

    @Override
    public void draw(SpriteBatch batch, float delta) {
        update(delta); // Update the bird's position based on physics
        image.draw(batch, 1); // Draw the bird image
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    // Abstract method for each bird to define its own action (e.g., launching)

}

