package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class blackbird extends Actor implements Bird {
    private Texture texture;
    private float initialWidth, initialHeight; // Store initial dimensions

    public blackbird(String texturePath, float initialWidth, float initialHeight) {
        // Load the texture
        texture = new Texture(Gdx.files.internal(texturePath));
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;

        setSize(initialWidth, initialHeight);

        // Set initial position
        setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.1f); 

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                performAction(); // Execute the bird's specific action
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Draw the bird texture with its set size
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void performAction() {
        System.out.println("Blackbird is launching!");
        // Add specific action logic here
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void resize(float screenWidth, float screenHeight) {
       
        setPosition(screenWidth * 0.1f, screenHeight * 0.1f); // Keep 10% from the left and bottom

        // Optionally scale the bird texture based on the screen size
        float scale = Math.min(screenWidth / Gdx.graphics.getWidth(), screenHeight / Gdx.graphics.getHeight());
        setSize(initialWidth * scale, initialHeight * scale);
    }

    @Override
    public float getX() {
        return super.getX(); // Use Actor's getX
    }

    @Override
    public float getY() {
        return super.getY(); // Use Actor's getY
    }

    @Override
    public float getWidth() {
        return super.getWidth(); // Use Actor's getWidth
    }

    @Override
    public float getHeight() {
        return super.getHeight(); // Use Actor's getHeight
    }

    @Override
    public void dispose() {
        texture.dispose(); // Dispose of the texture to free resources
    }
}
