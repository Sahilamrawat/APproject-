package com.angrybird.game;

import com.badlogic.gdx.math.Vector2;

public class WoodMaterial extends Material {


    public WoodMaterial(String texturePath, boolean isDestroyed, Vector2 positions, float damage, String materialType, int points) {
        super(texturePath, isDestroyed, positions, damage, materialType, points);
    }
}
