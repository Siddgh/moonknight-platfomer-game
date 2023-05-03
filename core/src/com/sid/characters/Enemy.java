package com.sid.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sid.screens.StartScreen;

/**
 * This is an abstract class for creating Enemies
 * My idea was that in the furture if I had to add more enemies in the game,
 * I can have them extend this class that way we would share some common features.
 * */
public abstract class Enemy extends Sprite {
    protected World world;
    protected StartScreen screen;
    public Body body;

    public Vector2 speed;

    // Every enemy would have their own speed, their own unique positions in the game.
    // Enemies can also be moving or non-moving.
    public Enemy(StartScreen screen, float posX, float posY, Vector2 speed) {
        this.world = screen.getWorld();
        this.screen = screen;
        //setting the default poistion of the enemy
        setPosition(posX, posY);
        createEnemy();
        this.speed = speed;
    }

    protected abstract void createEnemy();

    //reverseSpeed method would be used for moving enemies
    //where when the enemy hits a dead end he can automatically start moving to the opposite side.
    public void reverseSpeed(boolean x, boolean y) {
        if (x) {
            speed.x = -speed.x;
        }

        if (y) {
            speed.y = -speed.y;
        }

    }

    public abstract void update(float deltaTime);
}
