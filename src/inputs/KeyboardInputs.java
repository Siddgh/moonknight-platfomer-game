package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import constants.PlayerConstants;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_UP):
            case (KeyEvent.VK_SPACE):
            case (KeyEvent.VK_W):
                gamePanel.getGame().getPlayer().setUp(true);
                break;
            case (KeyEvent.VK_DOWN):
            case (KeyEvent.VK_S):
                gamePanel.getGame().getPlayer().setDown(true);
                break;
            case (KeyEvent.VK_LEFT):
            case (KeyEvent.VK_A):
                gamePanel.getGame().getPlayer().setLeft(true);
                break;
            case (KeyEvent.VK_RIGHT):
            case (KeyEvent.VK_D):
                gamePanel.getGame().getPlayer().setRight(true);
                break;
            case (KeyEvent.VK_ENTER):
                gamePanel.getGame().getPlayer().setPlayerAttacking(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_UP):
            case (KeyEvent.VK_SPACE):
            case (KeyEvent.VK_W):
                gamePanel.getGame().getPlayer().setUp(false);
                break;
            case (KeyEvent.VK_DOWN):
            case (KeyEvent.VK_S):
                gamePanel.getGame().getPlayer().setDown(false);
                break;
            case (KeyEvent.VK_LEFT):
            case (KeyEvent.VK_A):
                gamePanel.getGame().getPlayer().setLeft(false);
                break;
            case (KeyEvent.VK_RIGHT):
            case (KeyEvent.VK_D):
                gamePanel.getGame().getPlayer().setRight(false);
                break;
        }
    }

}
