package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Catapult {
    private Texture texture;
    private float x, y; // Position of the catapult on the screen

    public Catapult(String texturePath, float x, float y) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.x = x;
        this.y = y;
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

    // Optionally, add methods to perform actions related to the catapult
    public void launchBird() {
        System.out.println("Catapult launches a bird!");
        // Add your launching logic here
    }
}

