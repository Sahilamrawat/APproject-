package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Bird {
    Texture getTexture();
    void performAction();

    void setPosition(float x, float y);

    void resize(float screenWidth, float screenHeight);

    float getX();
    float getY();
    float getWidth();
    float getHeight();

    void dispose();


}
