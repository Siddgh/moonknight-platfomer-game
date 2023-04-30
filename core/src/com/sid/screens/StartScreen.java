package com.sid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sid.MainGameClass;
import com.sid.characters.Player;
import com.sid.constants.GameConstants;
import com.sid.levelcreator.ElementDetection;
import com.sid.levelcreator.WorldContactListener;
import com.sid.scene.GameHUD;

import java.util.Comparator;

public class StartScreen implements Screen {

    private MainGameClass game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;

    private GameHUD gameHUD;
    private TmxMapLoader mapLoader;
    private TiledMap levelMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private float deltaTime;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Player player;

    private TextureAtlas atlas;

    public StartScreen(MainGameClass game) {

        atlas = new TextureAtlas("characters/MainPlayer.atlas");

        this.game = game;
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(GameConstants.GAME_WIDTH / GameConstants.PIXELS_PER_METER, GameConstants.GAME_HEIGHT / GameConstants.PIXELS_PER_METER, gameCamera);
        gameHUD = new GameHUD(game.batch);

        mapLoader = new TmxMapLoader();
        levelMap = mapLoader.load("level1/level1.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(levelMap, 1 / GameConstants.PIXELS_PER_METER);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -120 / GameConstants.PIXELS_PER_METER), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        new ElementDetection(world, levelMap);

        player = new Player(world, this);

        world.setContactListener(new WorldContactListener());
    }


    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void update(float deltaTime) {
        handleKeyboardEvents(deltaTime);

        world.step(1 / 60f, 6, 2);

        player.update(deltaTime);

        gameCamera.position.x = player.body.getPosition().x;

        gameCamera.update();
        orthogonalTiledMapRenderer.setView(gameCamera);
    }

    private void handleKeyboardEvents(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.body.applyLinearImpulse(new Vector2(0,  4.0f), player.body.getWorldCenter(), true);
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.W)) && (player.body.getLinearVelocity().x <= 1)) {
            player.body.applyLinearImpulse(new Vector2(1.0f, 0), player.body.getWorldCenter(), true);
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) && (player.body.getLinearVelocity().x >= -1)) {
            player.body.applyLinearImpulse(new Vector2(-1.0f, 0), player.body.getWorldCenter(), true);
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        orthogonalTiledMapRenderer.render();

        box2DDebugRenderer.render(world, gameCamera.combined);

        game.batch.setProjectionMatrix(gameCamera.combined);

        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();


        game.batch.setProjectionMatrix(gameHUD.stage.getCamera().combined);
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
        world.dispose();
        orthogonalTiledMapRenderer.dispose();
        levelMap.dispose();
        box2DDebugRenderer.dispose();
        gameHUD.dispose();
    }
}
