package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class redbird extends BaseBird {
    private Texture texture;
    private boolean isLaunched; // Store initial dimensions
    Vector2 positions;
    public redbird(String texturePath, boolean isLaunched, Vector2 positions) {
        super(texturePath, isLaunched,positions);
    }


    @Override
    public void performAction() {
        // Implement red bird's specific action
        System.out.println("RedBird is launching!");
        // Example: apply an impulse to the Box2D body (you can customize this)
        getBody().applyLinearImpulse(new Vector2(10, 10), getBody().getWorldCenter(), true);
    }



    @Override
    public Texture getTexture() {
        return texture;
    }



    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }


}
