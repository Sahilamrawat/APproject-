package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;

public interface Bird {
    Texture getTexture();
    void performAction();

    void setPosition(float x, float y); // Sets the position of the bird

    void resize(float screenWidth, float screenHeight);

    float getX(); // Get the x position
    float getY(); // Get the y position
    float getWidth(); // Get the width of the bird
    float getHeight(); // Get the height of the bird

    void dispose();
}
