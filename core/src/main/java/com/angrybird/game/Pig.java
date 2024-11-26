package com.angrybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.json.JSONObject;

public class Pig extends Actor implements JsonSerializable {

    private Texture texture;
    private String pigType;
    private float health;
    private String texturePath;
    private boolean collided;
    private boolean materialCollided;
    private float radius;
    private float density;
    private float friction;
    private float restitution;
    private float angularDamping;
    private float spriteWidth;
    private float spriteHeight;


    public Texture getTexture() {
        return texture;
    }

    public boolean isMaterialCollided() {
        return materialCollided;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public Vector2 getPositions() {
        return positions;
    }

    public int getGamePoints() {
        return gamePoints;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("image", texture);
        json.put("isActive", collided);

        // Assuming `positions` is an object with fields `x` and `y`:
        JSONObject positionJson = new JSONObject();
        positionJson.put("x", positions.x);
        positionJson.put("y", positions.y);
        json.put("position", positionJson);

        json.put("health", health);
        json.put("type", pigType);
        json.put("score", gamePoints);
        json.put("radius", radius);
        json.put("density", density);
        json.put("friction", friction);
        json.put("restitution", restitution);
        json.put("SpriteWidth", spriteWidth);
        json.put("SpriteHeight", spriteHeight);

        return json;
    }




    public void setPigProperties() {
        switch (pigType.toLowerCase()) {
            case "small":
                this.radius = 0.7f;
                this.density = 2.5f;
                this.friction = 1f;
                this.restitution = 0.1f;
                this.angularDamping = 3f;
                this.spriteWidth = 2f;
                this.spriteHeight = 1.5f;
                break;
            case "medium":
                this.radius = 1f;
                this.density = 2.5f;
                this.friction = 1f;
                this.restitution = 0.1f;
                this.angularDamping = 3f;
                this.spriteWidth = 2f;
                this.spriteHeight = 2f;
                break;
            case "big":
                this.radius = 1.1f;
                this.density = 2.5f;
                this.friction = 1f;
                this.restitution = 0.1f;
                this.angularDamping = 3f;
                this.spriteWidth = 4f;
                this.spriteHeight = 3f;
                break;
        }
    }
    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }

    public void setMaterialCollided(boolean materialCollided) {
        this.materialCollided = materialCollided;
    }

    public boolean isDestroyed;
    private Sprite sprite;

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public float getRadius() {
        return radius;
    }

    public float getDensity() {
        return density;
    }

    public float getFriction() {
        return friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public float getSpriteWidth() {
        return spriteWidth;
    }

    public float getSpriteHeight() {
        return spriteHeight;
    }

//    @Override
//    public String toString() {
//        return String.format("{\n" +
//                "  \"image\": \"%s\",\n" +
//                "  \"isActive\": %b,\n" +
//                "  \"position\": {\"x\": %.2f, \"y\": %.2f},\n" +
//                "  \"health\": %.2f,\n" +
//                "  \"type\": \"%s\",\n" +
//                "  \"score\": %d,\n" +
//                "  \"radius\": %.2f,\n" +
//                "  \"density\": %.2f,\n" +
//                "  \"friction\": %.2f,\n" +
//                "  \"restitution\": %.2f,\n" +
//                "  \"SpriteWidth\": %.2f,\n" +
//                "  \"SpriteHeight\": %.2f\n"+
//                "}",
//            texture, collided, positions.x, positions.y, health, pigType, gamePoints, radius, density, friction, restitution,spriteWidth,spriteHeight);
//    }


    Vector2 positions;
    int gamePoints;

    public String getPigType() {
        return pigType;
    }

    public Pig(String texturePath, boolean isDestroyed, Vector2 positions, float health, String pigType,int gamePoints) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.pigType=pigType;
        this.texturePath = texturePath;
        this.isDestroyed =isDestroyed;
        this.gamePoints=gamePoints;
        this.health=health;
        this.positions = positions; // Store the initial position
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }



    public void damage(float damageAmount) {
        this.health -= damageAmount; // Reduce health by the damage amount
        if (this.health <= 0) {
            this.health = 0;       // Ensure health does not go below zero
            this.isDestroyed = true; // Mark the pig as destroyed
        }
    }
    public String getTexturePath() {
        return texturePath;
    }

    public boolean isDead() {
        if (this.health <= 0) {
            this.health = 0;       // Ensure health does not go below zero
            return true;
        }
        else{
            return false;
        }
    }
}
