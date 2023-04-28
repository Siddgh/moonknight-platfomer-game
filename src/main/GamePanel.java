package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import constants.ResourceConstants;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GamePanel extends JPanel {

    // setup variables to add direction to the player movement
    // xDelta indicates movement in X axis
    // yDelta indicates movement in Y axis
    private int xDelta = 0, yDelta = 0;
    private BufferedImage playerImage;
    private BufferedImage[] playerIdleAnim;
    private int animTick, animIndex, animSpeed = 12;

    // GamePanel constructor registers keyboard & mouse movemenets
    public GamePanel() {
        addKeyListener(new KeyboardInputs(this));
        setJPanelSize();
        MouseInputs mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

        addImage();
        loadAnimations();

    }

    private void loadAnimations() {
        playerIdleAnim = new BufferedImage[17];
        for (int i = 0; i < playerIdleAnim.length; i++) {
            playerIdleAnim[i] = playerImage.getSubimage(i * 128, 2 * 128, 128, 128);
        }
    }

    private void addImage() {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(ResourceConstants.PLAYER_IDLE_LOCATION);

        try {
            playerImage = ImageIO.read(inputStream);
            inputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void updateXDelta(int value) {
        this.xDelta += value;
        repaint();
    }

    public void updateYDelta(int value) {
        this.yDelta += value;
        repaint();
    }

    public void setRectPosition(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
        repaint();
    }

    // paintComponenet runs everytime the panel has to be rerendered.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateAnimationTicker();
        g.drawImage(playerIdleAnim[animIndex], xDelta, yDelta, 128, 128, null);

    }

    private void updateAnimationTicker() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= playerIdleAnim.length) {
                animIndex = 0;
            }
        }
    }

    public void setJPanelSize() {
        Dimension dimension = new Dimension(1280, 800);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
    }
}
