package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class blackbird extends BaseBird {
    private Texture texture;
    private boolean isLaunched; // Store initial dimensions
    Vector2 positions;

    public blackbird(String texturePath, boolean isLaunched, Vector2 positions, float damage, String birdType) {
        super(texturePath, isLaunched, positions, damage, birdType);
    }


    @Override
    public void performAction() {
        // Implement black bird's specific action
        System.out.println("BlackBird is launching with explosive power!");
        // Example: apply an explosive effect (customize as needed)
    }




    @Override
    public Texture getTexture() {
        return texture;
    }



    @Override
    public void dispose() {
        texture.dispose(); // Dispose of the texture to free resources
    }
}
