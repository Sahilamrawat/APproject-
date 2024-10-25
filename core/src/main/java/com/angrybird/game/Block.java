package com.angrybird.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

public class Block extends Image {
    private Material material;

    public Block(Material material, float width, float height) {
        super(new Texture(material.getTexturePath())); // Use the texture path from the Material class
        this.material = material;

        // Set the size of the block
        setSize(width, height);
        setScaling(Scaling.fill); // Ensure the texture fills the given size
    }

    public Material getMaterial() {
        return material; // Returns the material object
    }

    public int getStrength() {
        return material.getStrength();
    }
}
