package main;

import Characters.Player;

import java.awt.*;

public class Game implements Runnable {

    private final int FPS = 120;
    private final int UPS = 100;
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameLoopThread;

    private Player player;

    public Game() {
        createCharacterObjects();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void createCharacterObjects() {
        player = new Player(200, 200);
    }

    // startGameLoop would trigger an infinite loop that rerenders the game panel
    // every 120 FPS
    private void startGameLoop() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    private void updateGame() {
        player.updatePlayer();
    }

    public void render(Graphics g) {
        player.renderPlayer(g);
    }

    public Player getPlayer() {
        return player;
    }

    /*
     * Game Loop would run on 120 Frames per second which is defined through the FPS
     * variable
     */
    @Override
    public void run() {

        // timePerFrameInNanoSeconds calculates the time required for each frame based
        // on the FPS
        double timePerFrameInNanoSeconds = 1000000000.0 / FPS;
        double timePerUpdateInNanoSeconds = 1000000000.0 / UPS;

        // Initialize variables to keep track of the time and frame count
        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastFrameCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {

            // Get the current time and check if enough time has passed since the last frame
            // was rendered
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdateInNanoSeconds;
            deltaF += (currentTime - previousTime) / timePerFrameInNanoSeconds;

            previousTime = currentTime;
            if (deltaU >= 1) {
                updateGame();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++; // increment the frames count for FPS Counter
                deltaF--;
            }


//            if (timeNow - previousFrame >= timePerFrameInNanoSeconds) {
//                gamePanel.repaint();
//                previousFrame = timeNow;
//                frames++; // increment the frames count for FPS Counter
//            }

            // Runs every second after the previous frame count is updated
            if (System.currentTimeMillis() - lastFrameCheck >= 1000) {
                lastFrameCheck = System.currentTimeMillis();
                System.out.println("FPS Count :- " + frames + " | UPS + " + updates);
                frames = 0; // reset previous frame count
                updates = 0;
            }
        }
    }

    public void outOfFocus() {
        player.reset();
    }
}
