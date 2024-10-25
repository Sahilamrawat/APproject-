package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Catapult {
    private Texture texture;
    private float x, y; // Position of the catapult on the screen
    private float width, height; // Width and height of the catapult texture
    private float initialWidth, initialHeight; // Store initial dimensions

    public Catapult(String texturePath) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.initialWidth = texture.getWidth();
        this.initialHeight = texture.getHeight();

        // Set initial position and size as a percentage of screen dimensions
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public Texture getTexture() {
        return texture;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void resize(float screenWidth, float screenHeight) {
        // Adjust position based on the new screen dimensions
        this.x = screenWidth * 0.1f; // Keep 10% from the left
        this.y = screenHeight * 0.1f; // Keep 10% from the bottom

        // Scale the catapult texture based on the screen size
        float scale = Math.min(screenWidth / Gdx.graphics.getWidth(), screenHeight / Gdx.graphics.getHeight());
        this.width = initialWidth * scale;
        this.height = initialHeight * scale;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void dispose() {
        texture.dispose();
    }

    // Optionally, add methods to perform actions related to the catapult
    public void launchBird() {
        System.out.println("Catapult launches a bird!");
        // Add your launching logic here
    }
}
