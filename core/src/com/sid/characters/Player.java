package com.sid.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.sid.constants.CharacterBits;
import com.sid.constants.GameConstants;
import com.sid.screens.GameOverScreen;
import com.sid.screens.StartScreen;

/**
 * This class include the entire logic for how the player works in the game.
 * 1. It is responsible for defining the Player
 * 2. Loading its spirite and animating them as per player's action
 * 3. Check the direction in the which the player should run
 * 4. Player movements like Run, Jump, Stand etc
 */
public class Player extends Sprite {

    public enum State {STANDING, RUNNING, JUMPING, DEAD, FALLING}

    public State currState, previousState;
    public World world;
    public Body body;

    private Animation<TextureRegion> playerRun, playerJump, playerStand, playerDead;

    private float stateTimer;
    private boolean isRunningRight;

    boolean isPlayerDead;

    private StartScreen screen;

    public Player(StartScreen screen) {
        super(screen.getAtlas().findRegion("idle"));
        this.screen = screen;
        this.world = screen.getWorld();

        // I first initialise some default values.
        currState = State.STANDING; // Current Action that player is performing
        previousState = State.STANDING; // Previous Action that player was performing. This is mostly to find out if the player is falling after a jump or if the player is Dying
        stateTimer = 0; // How long is the player doing a particular action
        isRunningRight = true; // Is the player running towards right or left?

        // Here I load the individual player animations from the spiritesheets in the asset folder
        playerRun = new Animation(0.1f, screen.getAtlas().findRegions("run"));
        playerJump = new Animation(0.1f, screen.getAtlas().findRegions("highjump"));
        playerStand = new Animation(0.1f, screen.getAtlas().findRegions("idle"));
        playerDead = new Animation(0.1f, screen.getAtlas().findRegions("death"));

        createPlayer();

        // Making sure the camera always follows the player and that the player is always in the center.
        setBounds(0, 0, 102 / GameConstants.PIXELS_PER_METER, 102 / GameConstants.PIXELS_PER_METER);
        setOriginCenter();
        setRegion(playerStand.getKeyFrame(stateTimer));
    }

    public void update(float deltaTime) {
        handleKeyboardEvents();
        // Update Players position
        setPosition(body.getPosition().x - getWidth() / 3, body.getPosition().y - getHeight() / 4);
        setRegion(getFrame(deltaTime));
    }

    public void finishedGame() {
        screen.game.setScreen(new GameOverScreen(screen.game, screen.gameHUD, screen.username, false));
    }

    // This method is responsible to start an animation depending on Players current state
    public TextureRegion getFrame(float deltaTime) {
        currState = getState(); // I get the current state from the getState() method

        TextureRegion region;

        // I check which is the state and start the animation accordingly.
        switch (currState) {
            case DEAD:
                region = playerDead.getKeyFrame(stateTimer);
                break;
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

        // Further on, I check if the player is running towards left or right, depending on that
        // I flip the player spritie to make it seems like the player was running in that direction
        if ((body.getLinearVelocity().x < 0 || !isRunningRight) && !region.isFlipX()) {
            region.flip(true, false);
            isRunningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || isRunningRight) && region.isFlipX()) {
            region.flip(true, false);
            isRunningRight = true;
        }

        // I update my timer and make my currState my previous state
        stateTimer = currState == previousState ? stateTimer + deltaTime : 0;
        previousState = currState;

        return region;
    }

    // This method returns the current state the players in.
    public State getState() {
        if (isPlayerDead) {
            return State.DEAD;
        } else if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            screen.gameHUD.setShouldUpdateTimer(true);
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            screen.gameHUD.setShouldUpdateTimer(true);
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0) {
            screen.gameHUD.setShouldUpdateTimer(true);
            return State.RUNNING;
        } else {
            screen.gameHUD.setShouldUpdateTimer(false);
            return State.STANDING;
        }
    }

    // This method is used to define the player and make it interactable with the other elements in the game.
    private void createPlayer() {
        // I first start with defining the overall body configurations for the Player and give its initial start position.
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / GameConstants.PIXELS_PER_METER, 120 / GameConstants.PIXELS_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Since my player would be moving, I choose DynamicBody here.
        body = world.createBody(bodyDef); // I create the body for my player.

        // Now I need to make a fixture that can be tagged with my player and can be used to detect collisions.
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // Instead of a rectable or a polgon, here I've used a CircleShape.
        shape.setRadius(10 / GameConstants.PIXELS_PER_METER);

        // I further add PlayerBit as category bits and update the bits that the player can intereact with.
        fixtureDef.filter.categoryBits = CharacterBits.PLAYER_BIT;
        fixtureDef.filter.maskBits = CharacterBits.GROUND_BIT | CharacterBits.ENEMY_BIT | CharacterBits.FINISH_BIT; // This means player can interact with S

        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);


        // Once fixture is created, I also define two edges on both the sides, left and right.
        // This way I can find out if the player touches the enemy from either left or right side.
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

    // This method handles the input keys.
    // Currently configured input keys are below
    // awsd, up left top right bottom arrow
    private void handleKeyboardEvents() {
        // If the user is dead, I don't proceed at all
        if (currState != State.DEAD) {
            // If the user is not dead and if the pressed key is Up or Jump or W, player jumps
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                switch (getState()) {
                    case STANDING:
                    case RUNNING:
                        this.body.applyLinearImpulse(new Vector2(0, 3.5f), this.body.getWorldCenter(), true);
                        break;
                }
            }

            // Logic to make player move right
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.W)) && (this.body.getLinearVelocity().x <= 5)) {
                this.body.applyLinearImpulse(new Vector2(6.0f, 0), this.body.getWorldCenter(), true);
            }

            // Logic to make playe
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) && (this.body.getLinearVelocity().x >= -5)) {
                this.body.applyLinearImpulse(new Vector2(-6.0f, 0), this.body.getWorldCenter(), true);
            }
        }

    }

    public boolean isPlayerDead() {
        return isPlayerDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void struck() {
        isPlayerDead = true;
    }


}
