package com.angrybird.game;

import com.badlogic.gdx.math.Vector2;

public class SmallPig extends Pig {
    private static final float DEFAULT_HEALTH = 25;
    private static final float WIDTH = 40;
    private static final float HEIGHT = 40;
    private static final String TEXTURE_PATH = "small_pig.png";


    public SmallPig(String texturePath, boolean isDestroyed, Vector2 positions, float health, String pigType, int gamePoints) {
        super(texturePath, isDestroyed, positions, health, pigType, gamePoints);
    }
}
