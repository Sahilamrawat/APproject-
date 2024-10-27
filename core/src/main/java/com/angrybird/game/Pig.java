package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

public class Pig extends Image {
    private float health;
    private boolean defeated;

    public Pig(String texturePath, float health, float width, float height) {
        super(new Texture(texturePath));
        this.health = health;
        this.defeated = false;

        setSize(width, height);
        setScaling(Scaling.fill); 
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public void takeDamage(float damage) {
        if (defeated) return;
        health -= damage;
    }
}
