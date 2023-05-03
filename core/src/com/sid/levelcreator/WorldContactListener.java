package com.sid.levelcreator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.sid.characters.Enemy;
import com.sid.characters.Player;
import com.sid.constants.CharacterBits;
import jdk.jpackage.internal.Log;

/**
 * This class is responsible for handling Collisions and performing actions when collision action is detected.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        //Since I've stored individual categoryBits for my ground, player and enemy
        // When there is a collision, I'm easily able to check whith two elements had the collision.
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int categoryBits = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (categoryBits) {
            // Handle Collision between Enemy and Ground
            case CharacterBits.ENEMY_BIT | CharacterBits.GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == CharacterBits.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).reverseSpeed(true, false);
                } else {
                    ((Enemy) fixtureB.getUserData()).reverseSpeed(true, false);
                }
                break;
            //Handle Collision between Player and Enemy, here I'm just marking the player as dead
            case CharacterBits.PLAYER_BIT | CharacterBits.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == CharacterBits.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).struck();
                } else {
                    ((Player) fixtureB.getUserData()).struck();
                }
                break;
                //Handle Collision between Player and a Game Finish Checkpoint.
            case CharacterBits.PLAYER_BIT | CharacterBits.FINISH_BIT:
                if (fixtureA.getFilterData().categoryBits == CharacterBits.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).finishedGame();
                } else {
                    ((Player) fixtureB.getUserData()).finishedGame();
                }
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
