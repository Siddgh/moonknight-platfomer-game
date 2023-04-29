package com.sid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sid.MainGameClass;
import com.sid.constants.GameConstants;
import com.sid.scene.GameHUD;

public class StartScreen implements Screen {

    private MainGameClass game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;

    private GameHUD gameHUD;
    private TmxMapLoader mapLoader;
    private TiledMap levelMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private float deltaTime;

    public StartScreen(MainGameClass game) {
        this.game = game;
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, gameCamera);
        gameHUD = new GameHUD(game.batch);

        mapLoader = new TmxMapLoader();
        levelMap = mapLoader.load("level1/level1.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(levelMap);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

    }

    public void update(float deltaTime) {
        handleKeyboardEvents(deltaTime);
        gameCamera.update();
        orthogonalTiledMapRenderer.setView(gameCamera);
    }

    private void handleKeyboardEvents(float deltaTime) {
        if (Gdx.input.isTouched())
            gameCamera.position.x += 100 * deltaTime;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gameHUD.stage.getCamera().combined);

        orthogonalTiledMapRenderer.render();
        gameHUD.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        System.out.printf("%d x %d\n", width, height);
        gameViewport.update(width, height);
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
