package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class yellowbird extends BaseBird {
    private Texture texture;
    private boolean isLaunched; // Store initial dimensions
    Vector2 positions;

    public yellowbird(String texturePath, boolean isLaunched, Vector2 positions, float damage, String birdType) {
        super(texturePath, isLaunched, positions, damage, birdType);
    }


    @Override
    public void performAction() {
        // Implement yellow bird's specific action (e.g., speed boost)
        System.out.println("YellowBird is accelerating!");
        // Example: apply an acceleration to the Box2D body
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

}
