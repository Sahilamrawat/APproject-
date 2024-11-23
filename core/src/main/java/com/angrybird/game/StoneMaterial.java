package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class StoneMaterial extends Material {


    public StoneMaterial(String texturePath, boolean isDestroyed, Vector2 positions, float damage, String materialType, int points) {
        super(texturePath, isDestroyed, positions, damage, materialType, points);
    }
}
