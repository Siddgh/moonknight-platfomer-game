package com.sid.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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

import javax.swing.text.View;

public class WelcomeScreen implements Screen {
    private Game game;
    private Viewport viewport;
    private Stage stage;

    private TextField usernameField;

    public WelcomeScreen(final Game game) {
        this.game = game;
        viewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MainGameClass) game).batch);
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle fontTitle = new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(48, FontConstants.REGGAEONE), Color.WHITE);
        Label.LabelStyle fontSubTitle = new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(21, FontConstants.REGGAEONE), Color.WHITE);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = FontConstants.getBitmapFontFromFonts(21, FontConstants.REGGAEONE);
        style.fontColor = Color.GREEN;

        TextButton btnPlay = new TextButton("Play", style);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (usernameField.getText().trim().length() > 0) {
                    game.setScreen(new StartScreen((MainGameClass) game, usernameField.getText()));
                }
            }
        });

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = FontConstants.getBitmapFontFromFonts(21, FontConstants.REGGAEONE);
        textFieldStyle.fontColor = Color.WHITE;

        usernameField = new TextField("", textFieldStyle);
        usernameField.setAlignment(Align.center);
        usernameField.setMessageText("Username...");


        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(new Label("Welcome!", fontTitle)).expandX().padBottom(30);
        table.row();
        table.add(usernameField).expandX();
        table.row();
        table.add(btnPlay).padTop(30).expandX();


        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
