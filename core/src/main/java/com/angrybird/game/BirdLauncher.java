package com.angrybird.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class BirdLauncher {
    private World world;
    private Body birdBody;

    public BirdLauncher(World world) {
        this.world = world;
    }

    public Body createBird(float x, float y, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        birdBody = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1.0f; // Adjust for weight
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.6f; // For bounce effect

        birdBody.createFixture(fixtureDef);
        circleShape.dispose();

        return birdBody;
    }

    public void launchBird(float forceX, float forceY) {
        birdBody.applyLinearImpulse(new Vector2(forceX, forceY), birdBody.getWorldCenter(), true);
    }
}
