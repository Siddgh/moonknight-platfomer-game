package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
                gamePanel.updateYDelta(-50);
                break;
            case (KeyEvent.VK_DOWN):
            case (KeyEvent.VK_S):
                gamePanel.updateYDelta(50);
                break;
            case (KeyEvent.VK_LEFT):
            case (KeyEvent.VK_A):
                gamePanel.updateXDelta(-50);
                break;
            case (KeyEvent.VK_RIGHT):
            case (KeyEvent.VK_D):
                gamePanel.updateXDelta(50);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
