package com.angrybird.game;

public class BigPig extends Pig {
    private static final float DEFAULT_HEALTH = 200;
    private static final float WIDTH = 100;
    private static final float HEIGHT = 100;
    private static final String TEXTURE_PATH = "big_pig.png";

    public BigPig() {
        super(TEXTURE_PATH, DEFAULT_HEALTH, WIDTH, HEIGHT);
    }
}
