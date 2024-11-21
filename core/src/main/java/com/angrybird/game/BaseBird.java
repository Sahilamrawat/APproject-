package com.angrybird.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class BaseBird extends Actor implements Bird {
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

    public float getDamage() {
        return damage;
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

