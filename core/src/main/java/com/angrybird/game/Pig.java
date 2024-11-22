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
    private boolean collided;
    private boolean materialCollided;

    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }

    public void setMaterialCollided(boolean materialCollided) {
        this.materialCollided = materialCollided;
    }

    public boolean isDestroyed;
    private Sprite sprite;

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    Vector2 positions;
    int gamePoints;

    public String getPigType() {
        return pigType;
    }

    public Pig(String texturePath, boolean isDestroyed, Vector2 positions, float health, String pigType,int gamePoints) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.pigType=pigType;
        this.texturePath = texturePath;
        this.isDestroyed =isDestroyed;
        this.gamePoints=gamePoints;
        this.health=health;
        this.positions = positions; // Store the initial position
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }



    public void damage(float damageAmount) {
        this.health -= damageAmount; // Reduce health by the damage amount
        if (this.health <= 0) {
            this.health = 0;       // Ensure health does not go below zero
            this.isDestroyed = true; // Mark the pig as destroyed
        }
    }
    public String getTexturePath() {
        return texturePath;
    }

    public boolean isDead() {
        if (this.health <= 0) {
            this.health = 0;       // Ensure health does not go below zero
            return true;
        }
        else{
            return false;
        }
    }
}
