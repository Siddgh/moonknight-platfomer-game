package com.sid.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sid.constants.FontConstants;
import com.sid.constants.GameConstants;

/**
 * This class is responsbile to draw the survival time over the game at all times while playing the game.
 */
public class GameHUD implements Disposable {
    public Stage stage;
    private Viewport viewport;

    public float timer;
    private float timeCount;
    Label timerLabel, timerTitleLabel;

    private boolean shouldUpdateTimer = true;

    // This is very similar to the WelcomeScreen or the GameOverScreens
    public GameHUD(SpriteBatch spriteBatch) {
        timer = 0;

        this.viewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(this.viewport, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timerTitleLabel = new Label("Survival Time",
                new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(18, FontConstants.REGGAEONE), Color.WHITE));

        timerLabel = new Label(String.format("%03d", (int) timer), new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(24, FontConstants.REGGAEONE), Color.WHITE));

        table.add(timerTitleLabel)
                .expandX()
                .align(Align.topRight)
                .padTop(20)
                .padRight(20);

        table.row();
        table.add(timerLabel).expandX().align(Align.topRight).padTop(5).padRight(20);

        stage.addActor(table);
    }

    //Here I constantly update the timer for Survival time by 1 millisecond
    public void update(float deltaTime) {
        if (shouldUpdateTimer) {
            timeCount += deltaTime;
            if (timeCount >= 1) {
                timer += 1;
                timerLabel.setText(String.format("%03d", (int) timer));
                timeCount = 0;
            }
        }
    }

    public void setShouldUpdateTimer(boolean shouldUpdateTimer) {
        this.shouldUpdateTimer = shouldUpdateTimer;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
