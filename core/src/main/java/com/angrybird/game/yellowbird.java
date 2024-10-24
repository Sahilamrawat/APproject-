package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class yellowbird extends Actor implements Bird {
    private Texture texture;

    public yellowbird(float x,float y) {
        // Load the texture
        texture = new Texture(Gdx.files.internal("yellowbird.png"));

        // Set the size for the bird based on the desired width and height
        float birdWidth = 50; // Set desired width
        float birdHeight = 50; // Set desired height

        // Set size and bounds for the bird actor
        setSize(birdWidth, birdHeight);
        setPosition(x,y);

        // Add listener for touch or click interactions
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
        System.out.println("BlueBird is launching!");
        // Add specific action logic here
    }

    public Texture getTexture() {
        return texture;
    }
}
