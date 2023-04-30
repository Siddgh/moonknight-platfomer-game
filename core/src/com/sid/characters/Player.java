package com.sid.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.sid.constants.GameConstants;
import com.sid.screens.StartScreen;

public class Player extends Sprite {
    public enum State {STANDING, RUNNING, JUMPING, ATTACKING, DYING, FALLING}

    public State currState, previousState;
    public World world;
    public Body body;

    private Animation<TextureRegion> playerRun, playerJump, playerStand;

    private float stateTimer;
    private boolean isRunningRight;

    public Player(World world, StartScreen screen) {
        super(screen.getAtlas().findRegion("idle"));
        this.world = world;

        currState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;

        playerRun = new Animation(0.1f, screen.getAtlas().findRegions("run"));
        playerJump = new Animation(0.1f, screen.getAtlas().findRegions("highjump"));
        playerStand = new Animation(0.1f, screen.getAtlas().findRegions("idle"));


        createPlayer();
        setBounds(0, 0, 102 / GameConstants.PIXELS_PER_METER, 102 / GameConstants.PIXELS_PER_METER);
        setRegion(playerStand.getKeyFrame(stateTimer));
    }

    public void update(float deltaTime) {
        setPosition(body.getPosition().x - getWidth() / 4, body.getPosition().y - getHeight() / 4);
        setRegion(getFrame(deltaTime));
    }

    public TextureRegion getFrame(float deltaTime) {
        currState = getState();
        TextureRegion region;
        switch (currState) {
            case JUMPING:
            case FALLING:
                region = playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = playerStand.getKeyFrame(stateTimer, true);
                break;
        }


        if ((body.getLinearVelocity().x < 0 || !isRunningRight) && !region.isFlipX()) {
            region.flip(true, false);
            isRunningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || isRunningRight) && region.isFlipX()) {
            region.flip(true, false);
            isRunningRight = true;
        }

        stateTimer = currState == previousState ? stateTimer + deltaTime : 0;
        previousState = currState;
        return region;
    }

    public State getState() {
        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }


    private void createPlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / GameConstants.PIXELS_PER_METER, 120 / GameConstants.PIXELS_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / GameConstants.PIXELS_PER_METER);

        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);


        EdgeShape leftEdge = new EdgeShape();
        leftEdge.set(new Vector2(-2 / GameConstants.PIXELS_PER_METER, 30 / GameConstants.PIXELS_PER_METER), new Vector2(-2 / GameConstants.PIXELS_PER_METER, 30 / GameConstants.PIXELS_PER_METER));
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData("Left");

    }
}
