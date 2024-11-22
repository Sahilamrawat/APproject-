package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class StoneMaterial extends Material {

    public StoneMaterial(String texturePath, boolean isDestroyed, Vector2 positions, float health, String materialType) {
        super(texturePath, isDestroyed, positions, health, materialType);
    }
}
