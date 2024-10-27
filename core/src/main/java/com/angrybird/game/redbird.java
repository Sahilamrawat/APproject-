package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class redbird extends Actor implements Bird {
    private Texture texture;
    private float initialWidth, initialHeight; // Store initial dimensions

    public redbird(String texturePath, float initialWidth, float initialHeight) {
        // Load the texture
        texture = new Texture(Gdx.files.internal(texturePath));
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        setSize(initialWidth, initialHeight);
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
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void performAction() {
        System.out.println("RedBird is launching!"); 
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

 
    public void resize(float screenWidth, float screenHeight) {
        setPosition(screenWidth * 0.1f, screenHeight * 0.1f); 
        float scale = Math.min(screenWidth / Gdx.graphics.getWidth(), screenHeight / Gdx.graphics.getHeight());
        setSize(initialWidth * scale, initialHeight * scale);
    }
}
