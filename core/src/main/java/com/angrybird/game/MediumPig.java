package com.angrybird.game;

import com.badlogic.gdx.math.Vector2;

public class MediumPig extends Pig {
    private static final float DEFAULT_HEALTH = 150;
    private static final float WIDTH = 50;
    private static final float HEIGHT = 50;
    private static final String TEXTURE_PATH = "medium_pig.png";


    public MediumPig(String texturePath, boolean isDestroyed, Vector2 positions, float health, String pigType) {
        super(texturePath, isDestroyed, positions, health, pigType);
    }
}
