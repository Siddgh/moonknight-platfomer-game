package com.sid.levelcreator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.sid.characters.Enemy;
import com.sid.characters.Player;
import com.sid.constants.CharacterBits;
import jdk.jpackage.internal.Log;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int categoryBits = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        System.out.println(categoryBits);
        switch (categoryBits) {
            case CharacterBits.ENEMY_BIT | CharacterBits.GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == CharacterBits.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).reverseSpeed(true, false);
                } else {
                    ((Enemy) fixtureB.getUserData()).reverseSpeed(true, false);
                }
                break;
            case CharacterBits.PLAYER_BIT | CharacterBits.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == CharacterBits.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).struck();
                } else {
                    ((Player) fixtureB.getUserData()).struck();
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
