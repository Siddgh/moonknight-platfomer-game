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
import com.sid.constants.FontConstants;
import com.sid.constants.GameConstants;
import com.sid.constants.PathsContants;
import com.sid.constants.UIConstants;
import com.sid.database.HighScoreDataModel;
import com.sid.database.HighScoreDatabase;
import com.sid.scene.GameHUD;

import java.sql.SQLException;
import java.util.List;

/**
 * This class is responsible for displaying the GameOver Screen when the Player dies.
 * It also lets the player start the game again.
 */
public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;

    private String username;

    private boolean isGameOver;

    HighScoreDatabase highScoreDatabase;

    public GameOverScreen(final Game game, GameHUD score, final String username, boolean isGameOver) {
        this.game = game;
        this.username = username;

        // Here, similar to the Welcome Screen, I'm setting up my Viewport and Stage
        viewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MainGameClass) game).batch);
        Gdx.input.setInputProcessor(stage);

        // Fetching the values from JSON to use them as a prompt in the UI Elements later on
        JsonValue jsonValue = getParsedJson();

        // Connecting to the SQLLite Database and Writing the Highscore and Username to the database
        HighScoreDatabase highScoreDatabase = connectAndSaveDataToDatabase(score);

        // Designing the UI Elements and their postions.
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel;
        if (isGameOver) {
            gameOverLabel = new Label(jsonValue.getString("message_fail"), UIConstants.getLabelStyleForSizeAndColor(41, Color.WHITE));
        } else {
            gameOverLabel = new Label(jsonValue.getString("message_pass"), UIConstants.getLabelStyleForSizeAndColor(41, Color.WHITE));
        }

        Label scoreTitleLabel = new Label(jsonValue.getString("score"), UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE));
        Label scoreLabel = new Label(String.format("%03d", (int) score.timer), UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = FontConstants.getBitmapFontFromFonts(21, FontConstants.REGGAEONE);
        style.fontColor = Color.BLACK;

        TextButton btnPlay = new TextButton("Play", style);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (username.trim().length() > 0) {
                    // Restart the game
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
        table.add(new Label("HIGH SCORES", UIConstants.getLabelStyleForSizeAndColor(36, Color.WHITE))).colspan(3).padTop(30);
        table.row();
        table.add(new Label(jsonValue.getString("rank"), UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE))).pad(10);
        table.add(new Label(jsonValue.getString("username"), UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE))).pad(10);
        table.add(new Label(jsonValue.getString("score").toUpperCase(), UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE))).pad(10);
        table.row();
        // I'm loading the top 5 high scores - people who survived the game the most.
        // I do a fetch from the SQLLite Database to get the most recent top performers in the game.
        try {
            List<HighScoreDataModel> highScoreData = highScoreDatabase.getHighScores();
            int counter = 1;
            for (int i = 0; i < highScoreData.size(); i++) {
                if (counter < 6) {
                    table.add(new Label(Integer.toString(counter), UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE))).pad(10);
                    table.add(new Label(highScoreData.get(i).getUsername(), UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE))).pad(10);
                    table.add(new Label(Integer.toString(highScoreData.get(i).getScore()), UIConstants.getLabelStyleForSizeAndColor(21, Color.WHITE))).pad(10);
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

    // This is a wrapper method which calls the methods to create a connection with a SQLite Database.
    private HighScoreDatabase connectAndSaveDataToDatabase(GameHUD score) {
        highScoreDatabase = new HighScoreDatabase();
        try {
            highScoreDatabase.connect();
            highScoreDatabase.saveHighscore(new HighScoreDataModel(username, (int) score.timer));
        } catch (Exception e) {
            System.out.println("Failed to Connect to Database");
        }
        return highScoreDatabase;
    }

    private JsonValue getParsedJson() {
        // I wanted to cover JSON handling as part of my project.
        // Which is why I added all the message prompts that are to be displayed on this screen in a JSON.
        // So here I'm using a JSONReader object to read and parse a json file
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonValue = jsonReader.parse(Gdx.files.internal(PathsContants.GAME_OVER_JSON));
        return jsonValue;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(179 / 255f, 185 / 255f, 209 / 255f, 1);
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
        try {
            highScoreDatabase.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
