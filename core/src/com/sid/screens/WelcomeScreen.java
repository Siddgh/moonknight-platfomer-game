package com.sid.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sid.MainGameClass;
import com.sid.constants.FontConstants;
import com.sid.constants.GameConstants;
import com.sid.constants.UIConstants;

/***
 * This Class holds the logic for displaying WelcomeScreen which has 1 primary task that is to get the username for the player.
 * Username is then used across the app to track the highscore associated with the user.
 */
public class WelcomeScreen implements Screen {
    private Game game;
    private Viewport viewport;
    private Stage stage;
    private TextField usernameField;

    /**
     * The Constructor takes care of defining the overall UI to be displayed for the welcome screen.
     *
     * @param game
     */
    public WelcomeScreen(final Game game) {
        this.game = game;

        //we initialise the Viewport of the Welcome Screen to the static Game Width and Height which is stored in the GameConstants file.
        // Viewport also needs a OrthographicCamera() which is at its default configuration.
        viewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, new OrthographicCamera());

        //Stage is where we add all our UI Elements to, for them to be rendered
        stage = new Stage(viewport, ((MainGameClass) game).batch);

        //InputProcessor is passed to stage which indicates that all the UI elements inside the stage can interact with the US.
        // We require this since we are adding a textfield which needs to intereact with the user to get the Username
        Gdx.input.setInputProcessor(stage);

        TextButton btnPlay = new TextButton(UIConstants.PLAY_PROMPT, getTextButtonStyle());

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (usernameField.getText().trim().length() > 0) {
                    //We start the game only when the user has provided a username.
                    game.setScreen(new StartScreen((MainGameClass) game, usernameField.getText()));
                }
            }
        });


        usernameField = new TextField("", getTextFieldStyle());
        usernameField.setAlignment(Align.center);
        usernameField.setMessageText(UIConstants.USERNAME_PROMPT);


        // This block defines how our UI Elements are going to be layed out.
        // We add each element inside a table and use .row() method to add breaks in between them.
        // Here we add a Label, a Textfield and a Button.
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(new Label(UIConstants.WELCOME_PROMPT, UIConstants.getLabelStyleForSizeAndColor(48, Color.WHITE))).expandX().padBottom(30);
        table.row();
        table.add(new Label("Enter your username to get started", UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE))).expandX();
        table.row();
        table.add(usernameField).expandX();
        table.row();
        table.add(btnPlay).padTop(30).expandX();

        stage.addActor(table);

    }

    // This is a utility method that creates a style for the "Play" button.
    // The reason its inside this class and not inside a Global Constants file is because in the entire code this method is only used once.
    private TextButton.TextButtonStyle getTextButtonStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = FontConstants.getBitmapFontFromFonts(21, FontConstants.REGGAEONE);
        style.fontColor = Color.WHITE;
        return style;
    }

    // This is a utility method that creates a style for the Textfield.
    private TextField.TextFieldStyle getTextFieldStyle() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = FontConstants.getBitmapFontFromFonts(21, FontConstants.REGGAEONE);
        textFieldStyle.fontColor = Color.WHITE;
        return textFieldStyle;
    }
 
    @Override
    public void show() {

    }

    // Render method runs constantly as part of the game loop.
    @Override
    public void render(float delta) {
        //Here we are clearing the screen and giving black as the background color for the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // We then draw the elements inside the stage.
        stage.draw();
        stage.act();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
