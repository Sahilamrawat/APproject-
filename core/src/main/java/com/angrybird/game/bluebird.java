package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class bluebird extends BaseBird {
    private Texture texture;
    private float initialWidth, initialHeight; // Store initial dimensions
    public bluebird(String texturePath, boolean isLaunched, Vector2 positions) {
        super(texturePath, isLaunched,positions);
    }
    @Override
    public void performAction() {
        // Implement blue bird's specific action (e.g., splitting into three)
        System.out.println("BlueBird is splitting!");
        // Example: logic for splitting into multiple projectiles
    }


    @Override
    public Texture getTexture() {
        return texture;
    }



    @Override
    public void dispose() {
        // Dispose of the texture to free memory
        if (texture != null) {
            texture.dispose();
        }
    }

    // Method to resize the bird based on screen size
    public void resize(float screenWidth, float screenHeight) {
        // Adjust position and size based on the new screen dimensions
        setPosition(screenWidth * 0.1f, screenHeight * 0.1f); // Keep 10% from the left and bottom

        // Optionally scale the bird texture based on the screen size
        float scale = Math.min(screenWidth / Gdx.graphics.getWidth(), screenHeight / Gdx.graphics.getHeight());
        setSize(initialWidth * scale, initialHeight * scale);
    }
}
