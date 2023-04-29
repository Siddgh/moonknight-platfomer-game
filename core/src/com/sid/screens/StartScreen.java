package com.sid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    public StartScreen(MainGameClass game) {
        this.game = game;
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, gameCamera);
        gameHUD = new GameHUD(game.batch);

        mapLoader = new TmxMapLoader();
        levelMap = mapLoader.load("level1/level1.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(levelMap);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : levelMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);
            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }


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


        orthogonalTiledMapRenderer.render();

        box2DDebugRenderer.render(world, gameCamera.combined);


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

    }
}
