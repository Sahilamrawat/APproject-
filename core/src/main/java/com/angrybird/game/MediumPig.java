package com.angrybird.game;

public class MediumPig extends Pig {
    private static final float DEFAULT_HEALTH = 150;
    private static final float WIDTH = 50;
    private static final float HEIGHT = 50;
    private static final String TEXTURE_PATH = "medium_pig.png";

    public MediumPig() {
        super(TEXTURE_PATH, DEFAULT_HEALTH, WIDTH, HEIGHT);
    }
}
