package com.angrybird.game;

import com.badlogic.gdx.math.Vector2;

public class BigPig extends Pig {
    private static final float DEFAULT_HEALTH = 200;
    private static final float WIDTH = 100;
    private static final float HEIGHT = 100;
    private static final String TEXTURE_PATH = "big_pig.png";


    public BigPig(String texturePath, boolean isDestroyed, Vector2 positions, float health, String pigType, int gamePoints) {
        super(texturePath, isDestroyed, positions, health, pigType, gamePoints);
    }
}
