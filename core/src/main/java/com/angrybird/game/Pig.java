package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

public class Pig extends Actor {

    private Texture texture;
    private String pigType;
    private float health;
    private String texturePath;

    public boolean isDestroyed;
    Vector2 positions;

    public String getPigType() {
        return pigType;
    }

    public Pig(String texturePath, boolean isDestroyed, Vector2 positions, float health, String pigType) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.pigType=pigType;
        this.texturePath = texturePath;
        this.isDestroyed =isDestroyed;
        this.health=health;
        this.positions = positions; // Store the initial position
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }


    public String getTexturePath() {
        return texturePath;
    }

}
