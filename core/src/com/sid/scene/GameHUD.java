package com.sid.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sid.constants.FontConstants;
import com.sid.constants.GameConstants;

public class GameHUD {
    public Stage stage;
    private Viewport viewport;

    private Integer bountyEarned;
    private Label bountyEarnedTitleLable;
    private Label bountyEarnedLabel;

    public GameHUD(SpriteBatch spriteBatch) {
        this.bountyEarned = 0;

        this.viewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(this.viewport, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        bountyEarnedTitleLable = new Label("Bounty Earned",
                new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(18, FontConstants.REGGAEONE), Color.WHITE));

        bountyEarnedLabel = new Label(String.format("%d", bountyEarned),
                new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(24, FontConstants.REGGAEONE), Color.WHITE));

        table.add(bountyEarnedTitleLable)
                .expandX()
                .align(Align.topRight)
                .padTop(20)
                .padRight(20);

        table.row();
        table.add(bountyEarnedLabel).expandX().align(Align.topRight).padTop(20).padRight(20);

        stage.addActor(table);
    }

}
