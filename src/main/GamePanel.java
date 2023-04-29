package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import constants.PlayerConstants;
import constants.ResourceConstants;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {

    private Game game;

    // GamePanel constructor registers keyboard & mouse movemenets
    public GamePanel(Game game) {
        this.game = game;
        addKeyListener(new KeyboardInputs(this));
        setJPanelSize();
        MouseInputs mouseInputs = new MouseInputs();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    // paintComponenet runs everytime the panel has to be rerendered.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return game;
    }

    public void setJPanelSize() {
        Dimension dimension = new Dimension(1280, 800);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
    }

    public void updateGame() {

    }
}
