package com.sid.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sid.screens.StartScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected StartScreen screen;
    public Body body;

    public Vector2 speed;

    public Enemy(StartScreen screen, float posX, float posY, Vector2 speed) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(posX, posY);
        createEnemy();
        this.speed = speed;
    }

    protected abstract void createEnemy();

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
