package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Material {
    private String texturePath;
    private int strength;
    public boolean isDestroyed;
    public float damage;
    private Sprite sprite;
    private Texture texture;
    private String materialType;

    private boolean collided;
    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    Vector2 positions;
    public Material(String texturePath, boolean isDestroyed, Vector2 positions, float damage, String materialType) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.materialType=materialType;
        this.texturePath = texturePath;
        this.isDestroyed =isDestroyed;
        this.damage=damage;
        this.positions = positions; // Store the initial position
    }

    public String getTexturePath() {
        return texturePath;
    }

    public String getMaterialType() {
        return materialType;
    }

    public int getStrength() {
        return strength;
    }
}
