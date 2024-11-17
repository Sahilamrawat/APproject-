package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Catapult {
    private Texture texture;
    private float x, y;
    private float width, height;
    private float initialWidth, initialHeight;

    public Catapult(String texturePath) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.initialWidth = texture.getWidth();
        this.initialHeight = texture.getHeight();

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
        this.x = screenWidth * 0.1f;
        this.y = screenHeight * 0.1f;


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


    public void launchBird() {
        System.out.println("Catapult launches a bird!");

    }
}
