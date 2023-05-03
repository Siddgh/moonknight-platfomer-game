package com.sid;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sid.screens.WelcomeScreen;

/***
 * This is the main class of the project
 * This class I've kept as light as possible.
 * This class declares an object of SpriteBatch
 * The same spritebatch object is then used accross all the files
 * As per LibGDX Documentation it is recommended to stick to a single SpriteBatch to keep the performance of the game optimized.
 * ***/
public class MainGameClass extends Game {

    public SpriteBatch batch;

    // On Create is called everytime the app runs.
    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new WelcomeScreen(this)); //here we take the user to the Welcome Screen to get the username.
    }

    @Override
    public void render() {
        super.render();
    }
}
