package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Material {
    private String texturePath;
    private int strength;
    public boolean isDestroyed;
    public float damage;
    private Sprite sprite;
    private Texture texture;
    private String materialType;
    private Body body;
    public int points;
    private float width;         // Width of the material
    private float height;        // Height of the material
    private float friction;
    private float restitution;
    private float density;

    public void setMaterialProperties() {
        switch (materialType.toLowerCase()) {
            case "wood":
                this.width = 0.5f;
                this.height = 3f;
                break;
            case "stone":
                this.width = 5f;
                this.height = 0.5f;
                break;
            case "ice":
                this.width = 1.2f;
                this.height = 1.2f;
                break;
            default:
                throw new IllegalArgumentException("Unknown material type: " + materialType);
        }
        this.friction = 1f;
        this.restitution = 0f;
        this.density = 5f;
    }
    private boolean collided;
    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    Vector2 positions;
    public Material(String texturePath, boolean isDestroyed, Vector2 positions, float damage, String materialType,int points) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.materialType=materialType;
        this.texturePath = texturePath;
        this.isDestroyed =isDestroyed;
        this.damage=damage;
        this.points=points;
        this.positions = positions; // Store the initial position
    }

    @Override
    public String toString() {
        return String.format("{\n" +
                "  \"image\": \"%s\",\n" +
                "  \"isActive\": %b,\n" +
                "  \"position\": {\"x\": %.2f, \"y\": %.2f},\n" +
                "  \"damage\": %.2f,\n" +
                "  \"type\": \"%s\",\n" +
                "  \"score\": %d,\n" +
                "  \"width\": %.2f,\n" +
                "  \"height\": %.2f,\n" +
                "  \"density\": %.2f,\n" +
                "  \"friction\": %.2f,\n" +
                "  \"restitution\": %.2f\n" +
                "}",
            texturePath, isDestroyed, positions.x, positions.y, damage, materialType, points, width, height, density, friction, restitution);
    }


    public void setDamage(float damage) {
        this.damage = damage;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public String getMaterialType() {
        return materialType;
    }

    public int getStrength() {
        return strength;
    }

    public float getDamage() {
        return this.damage;

    }

    public void damage(float damage) {
        if(this.damage<=damage){
            this.damage-=damage*0.025f;
            this.isDestroyed=true;
        }
    }
}
