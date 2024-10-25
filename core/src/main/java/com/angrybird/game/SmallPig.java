package com.angrybird.game;

public class SmallPig extends Pig {
    private static final float DEFAULT_HEALTH = 25;
    private static final float WIDTH = 40;
    private static final float HEIGHT = 40;
    private static final String TEXTURE_PATH = "small_pig.png";

    public SmallPig() {
        super(TEXTURE_PATH, DEFAULT_HEALTH, WIDTH, HEIGHT);
    }
}
