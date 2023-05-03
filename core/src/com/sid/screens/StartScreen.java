package com.sid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sid.MainGameClass;
import com.sid.characters.Enemy;
import com.sid.characters.Player;
import com.sid.constants.CharacterBits;
import com.sid.constants.GameConstants;
import com.sid.constants.PathsContants;
import com.sid.levelcreator.ElementDetection;
import com.sid.levelcreator.WorldContactListener;
import com.sid.scene.GameHUD;


public class StartScreen implements Screen {

    public MainGameClass game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;

    public GameHUD gameHUD;
    private TmxMapLoader mapLoader;
    private TiledMap levelMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Player player;

    private TextureAtlas atlas;
    ElementDetection elementDetection, finishDetection;

    public String username;

    /**
     * This file is responsible for the entire game flow, setting up level maps
     *
     * @param game
     * @param username
     */
    public StartScreen(MainGameClass game, String username) {

        // atlas holds the metadata of all my mainplayer spritiesheets
        atlas = new TextureAtlas(PathsContants.MAIN_PLAYER_ATLAS);

        this.game = game;
        this.username = username;

        // Below 3 lines, define the Camera and the View port for our actual game screen.
        // 1. Creates an instance of OrthographicCamera object
        // 2. We Set the size of the Viewport and Scale it as per the Pixel Per Meter.
        // 3. We setup a game HUD object which would constantly show our score.
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(GameConstants.GAME_WIDTH / GameConstants.PIXELS_PER_METER, GameConstants.GAME_HEIGHT / GameConstants.PIXELS_PER_METER, gameCamera);
        gameHUD = new GameHUD(game.batch);

        // Here we setup the Map for the Level.
        // Level is built using Tiled software which creates a .tmx extension
        mapLoader = new TmxMapLoader();
        levelMap = mapLoader.load(PathsContants.LEVEL_MAP);

        // We update the Camera to start display the level and to follow the main player
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(levelMap, 1 / GameConstants.PIXELS_PER_METER);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

        setUpPlatforms();

        // We define an object for the main player class.
        player = new Player(this);
    }

    // This method is responsible for setting up the world and creating platforms in it.
    // Platforms are the ground elements which the Main Player can interact with.
    // Example of interacting with platforms would be something like Player standing on it.
    private void setUpPlatforms() {
        // World takes 2 parameters.
        // First defines the overall Gravity in the Game. Here we have set it to -120.
        // Second is a boolean value that indicates whether the platform elements should be calculating the main players distance constantly.
        world = new World(new Vector2(0, -120 / GameConstants.PIXELS_PER_METER), true);

        // ElementDetection is a class which makes the platform elements interactable.
        elementDetection = new ElementDetection(this);
        finishDetection = new ElementDetection(this);
        // I'm using CharacterBits to detect collisions between my different elements.
        // Here I assign this element the Ground_BIT, since it mostly only covers ground that user can walk on.
        elementDetection.setCategoryFilter(CharacterBits.GROUND_BIT);
        finishDetection.setCategoryFilter(CharacterBits.FINISH_BIT);
        //World Contact Listener is where we actually check which two elements have collided.
        world.setContactListener(new WorldContactListener());
    }


    public TextureAtlas getAtlas() {
        return atlas;
    }

    // Update method indicates that there have been some changes in the game which needs to be updated and synced
    public void update(float deltaTime) {
        world.step(1 / 60f, 6, 2);

        player.update(deltaTime);

        for (Enemy enemy : elementDetection.getSkullEnemyArray()) {
            enemy.update(deltaTime);
        }

        for (Enemy enemy : elementDetection.getDemonEnemyArray()) {
            enemy.update(deltaTime);
        }


        // Check if player is frozen. If he is frozen then it we conclude that the player has died.
        if (player.body.getPosition().y < 0) {
            player.struck();
        }

        gameHUD.update(deltaTime);

        // We only have the camera follow the player if he is not dead.
        if (player.currState != Player.State.DEAD) {
            gameCamera.position.x = player.body.getPosition().x;
        }

        gameCamera.update();
        orthogonalTiledMapRenderer.setView(gameCamera);
    }

    // Check if the player is dead
    public boolean gameOver() {
        if (player.currState == Player.State.DEAD && player.getStateTimer() > 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void show() {

    }

    // Render method is responsible to render all the elements in the game.
    // Here Render element renders the map, the player position, the enemies
    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(179 / 255f, 185 / 255f, 209 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        orthogonalTiledMapRenderer.render();

        //box2DDebugRenderer.render(world, gameCamera.combined);

        game.batch.setProjectionMatrix(gameCamera.combined);

        // Drawing the player onscreen.
        game.batch.begin();
        player.draw(game.batch);

        // Drawing Enemies on screen
        for (Enemy enemy : elementDetection.getSkullEnemyArray()) {
            enemy.draw(game.batch);
        }

        for (Enemy enemy : elementDetection.getDemonEnemyArray()) {
            enemy.draw(game.batch);
        }
        game.batch.end();


        game.batch.setProjectionMatrix(gameHUD.stage.getCamera().combined);
        gameHUD.stage.draw();

        // We constantly check if the Player is dead. If yes we navigate to the GameOver Screen.
        if (gameOver()) {
            game.setScreen(new GameOverScreen(game, gameHUD, username, true));
            dispose();
        }

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

    public TiledMap getLevelMap() {
        return levelMap;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void dispose() {
        world.dispose();
        orthogonalTiledMapRenderer.dispose();
        levelMap.dispose();
        //box2DDebugRenderer.dispose();
        gameHUD.dispose();
    }
}
