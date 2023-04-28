package main;

public class Game implements Runnable {

    private final int FPS = 120;

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameLoopThread;

    public Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    // startGameLoop would trigger an infinite loop that rerenders the game panel
    // every 120 FPS
    private void startGameLoop() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
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

        // Initialize variables to keep track of the time and frame count
        long previousFrame = System.nanoTime(), timeNow = System.nanoTime();

        int frames = 0;
        long lastFrameCheck = System.currentTimeMillis();

        while (true) {

            // Get the current time and check if enough time has passed since the last frame
            // was rendered
            timeNow = System.nanoTime();
            if (timeNow - previousFrame >= timePerFrameInNanoSeconds) {
                gamePanel.repaint();
                previousFrame = timeNow;
                frames++; // increment the frames count for FPS Counter
            }

            // Runs every second after the previous frame count is updated
            if (System.currentTimeMillis() - lastFrameCheck >= 1000) {
                lastFrameCheck = System.currentTimeMillis();
                System.out.println("FPS Count :- " + frames);
                frames = 0; // reset previous frame count
            }
        }
    }
}
