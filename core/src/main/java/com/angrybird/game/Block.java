package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

public class Block extends Image {
    private Material material;
    private float width;
    private float height;

    public Block(Material material, float width, float height) {
        super(new Texture(material.getTexturePath())); // Use the texture path from the Material class
        this.material = material;
        this.width = width;
        this.height = height;

        setSize(width, height);
        setScaling(Scaling.fill); // Ensure the texture fills the given size
    }

    public String getMaterialName() {
        return material.getClass().getSimpleName(); // Returns the class name as the material name
    }

    public int getStrength() {
        return material.getStrength();
    }
}
