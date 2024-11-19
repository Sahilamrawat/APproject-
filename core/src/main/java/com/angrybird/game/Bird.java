package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public interface Bird {
    Texture getTexture();
    void performAction();

    void setPosition(float x, float y);

    void resize(float screenWidth, float screenHeight);
    void update(float deltaTime);

    Body getBody();



    void draw(SpriteBatch batch, float delta);
    float getX();
    float getY();
    float getWidth();
    float getHeight();

    void dispose();


}
