package com.sid.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sid.constants.CharacterBits;
import com.sid.constants.GameConstants;
import com.sid.screens.StartScreen;

public class SkullEnemy extends Enemy {

    private float stateTime;
    private Animation<TextureRegion> skullIdle;

    public SkullEnemy(StartScreen screen, float posX, float posY, Vector2 speed) {
        super(screen, posX, posY, speed);
        stateTime = 0;
        skullIdle = new Animation(0.1f, screen.getAtlas().findRegions("skull"));
        setBounds(getX(), getY(), 48 / GameConstants.PIXELS_PER_METER, 48 / GameConstants.PIXELS_PER_METER);
        setRegion(skullIdle.getKeyFrame(stateTime, true));
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 4);
        setRegion(skullIdle.getKeyFrame(stateTime, true));

        body.setLinearVelocity(speed);
    }

    @Override
    protected void createEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / GameConstants.PIXELS_PER_METER);

        fixtureDef.filter.categoryBits = CharacterBits.ENEMY_BIT;
        fixtureDef.filter.maskBits = CharacterBits.GROUND_BIT | CharacterBits.PLAYER_BIT;

        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape leftEdge = new EdgeShape();
        leftEdge.set(new Vector2(-30 / GameConstants.PIXELS_PER_METER, -20 / GameConstants.PIXELS_PER_METER), new Vector2(-30 / GameConstants.PIXELS_PER_METER, 20 / GameConstants.PIXELS_PER_METER));
        fixtureDef.shape = leftEdge;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape rightEdge = new EdgeShape();
        rightEdge.set(new Vector2(30 / GameConstants.PIXELS_PER_METER, -20 / GameConstants.PIXELS_PER_METER), new Vector2(30 / GameConstants.PIXELS_PER_METER, 20 / GameConstants.PIXELS_PER_METER));
        fixtureDef.shape = rightEdge;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);
    }
}
