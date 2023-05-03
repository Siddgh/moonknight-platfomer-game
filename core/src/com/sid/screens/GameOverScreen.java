package com.sid.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sid.MainGameClass;
import com.sid.characters.Player;
import com.sid.constants.FontConstants;
import com.sid.constants.GameConstants;
import com.sid.database.HighScoreDataModel;
import com.sid.database.HighScoreDatabase;
import com.sid.scene.GameHUD;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;

    private String username;

    public GameOverScreen(final Game game, GameHUD score, final String username) {
        this.game = game;
        this.username = username;

        viewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MainGameClass) game).batch);
        Gdx.input.setInputProcessor(stage);


        JsonReader jsonReader = new JsonReader();
        JsonValue jsonValue = jsonReader.parse(Gdx.files.internal("GameOver.json"));

        HighScoreDatabase highScoreDatabase = new HighScoreDatabase();
        try {
            highScoreDatabase.connect();
            highScoreDatabase.saveHighscore(new HighScoreDataModel(username, (int) score.timer));
        } catch (Exception e) {
            System.out.println("Failed to Connect to Database");
        }

        Label.LabelStyle fontTitle = new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(48, FontConstants.REGGAEONE), Color.WHITE);
        Label.LabelStyle fontTitle2 = new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(36, FontConstants.REGGAEONE), Color.WHITE);
        Label.LabelStyle fontSubTitle = new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(21, FontConstants.REGGAEONE), Color.GREEN);
        Label.LabelStyle fontSubTitle2 = new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(18, FontConstants.REGGAEONE), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label(jsonValue.getString("message"), fontTitle);
        Label scoreTitleLabel = new Label(jsonValue.getString("score"), fontSubTitle2);
        Label scoreLabel = new Label(String.format("%03d", (int) score.timer), fontSubTitle2);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = FontConstants.getBitmapFontFromFonts(21, FontConstants.REGGAEONE);
        style.fontColor = Color.GREEN;

        TextButton btnPlay = new TextButton("Play", style);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (username.trim().length() > 0) {
                    game.setScreen(new StartScreen((MainGameClass) game, username));
                }
            }
        });

        table.add(gameOverLabel).colspan(3).center();
        table.row();
        table.add(scoreTitleLabel).colspan(3).padTop(20);
        table.row();
        table.add(scoreLabel).colspan(3);
        table.row();
        table.add(new Label("HIGH SCORES", fontTitle2)).colspan(3).padTop(30);
        table.row();
        table.add(new Label(jsonValue.getString("rank"), fontSubTitle2)).pad(10);
        table.add(new Label(jsonValue.getString("username"), fontSubTitle2)).pad(10);
        table.add(new Label(jsonValue.getString("score").toUpperCase(), fontSubTitle2)).pad(10);
        table.row();
        try {
            List<HighScoreDataModel> highScoreData = highScoreDatabase.getHighScores();
            int counter = 1;
            for (int i = 0; i < highScoreData.size(); i++) {
                if (counter < 6) {
                    table.add(new Label(Integer.toString(counter), fontSubTitle2)).pad(10);
                    table.add(new Label(highScoreData.get(i).getUsername(), fontSubTitle2)).pad(10);
                    table.add(new Label(Integer.toString(highScoreData.get(i).getScore()), fontSubTitle2)).pad(10);
                    table.row();
                    counter++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        table.add(btnPlay).colspan(3).padTop(50);
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
