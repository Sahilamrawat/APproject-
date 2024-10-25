package com.angrybird.game;

public abstract class Material {
    private String texturePath;
    private int strength;

    public Material(String texturePath, int strength) {
        this.texturePath = texturePath;
        this.strength = strength;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getStrength() {
        return strength;
    }
}
