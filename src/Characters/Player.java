package Characters;

import constants.PlayerConstants;
import constants.ResourceConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Player extends Character {
    private BufferedImage[][] playerAnimations;
    private int animTick, animIndex, animSpeed = 12;
    private int playerAction = PlayerConstants.PlayerActions.STANDING;
    private Boolean isPlayerMoving = false, isPlayerAttacking = false;

    private boolean left, up, right, down;
    private float speed = 2.0f;

    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    public void updatePlayer() {
        updatePlayerPosition();
        updateAnimationTicker();
        setPlayerActionAnimation();
    }

    public void renderPlayer(Graphics g) {
        g.drawImage(playerAnimations[playerAction][animIndex], (int) x, (int) y, 128, 128, null);
    }

    private void updatePlayerPosition() {
        isPlayerMoving = false;

        if (left && !right) {
            x -= speed;
            isPlayerMoving = true;
        } else if (!left && right) {
            x += speed;
            isPlayerMoving = true;
        }

        if (up && !down) {
            y -= speed;
            isPlayerMoving = true;
        } else if (!up && down) {
            y += speed;
            isPlayerMoving = true;
        }

    }


    private void updateAnimationTicker() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= PlayerConstants.PlayerActions.getTotalSpritesCountFromPlayerAction(playerAction)) {
                animIndex = 0;
                isPlayerAttacking = false;
            }
        }
    }


    private void setPlayerActionAnimation() {
        int startAnimation = playerAction;

        if (isPlayerMoving) {
            playerAction = PlayerConstants.PlayerActions.RUNNING;
        } else {
            playerAction = PlayerConstants.PlayerActions.STANDING;
        }

        if (isPlayerAttacking) {
            playerAction = PlayerConstants.PlayerActions.ATTACKING;
        }

        if (startAnimation != playerAction) {
            resetAnimationTicker();
        }

    }

    private void resetAnimationTicker() {
        animTick = 0;
        animIndex = 0;
    }

    private BufferedImage getPlayerImage() {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(ResourceConstants.PLAYER_IDLE_LOCATION);

        try {
            BufferedImage playerImage = ImageIO.read(inputStream);
            inputStream.close();
            return playerImage;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private void loadAnimations() {
        playerAnimations = new BufferedImage[5][17];
        for (int i = 0; i < playerAnimations.length; i++) {
            for (int j = 0; j < playerAnimations[i].length; j++) {
                playerAnimations[i][j] = Objects.requireNonNull(getPlayerImage()).getSubimage(j * 128, i * 128, 128, 128);
            }
        }
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void reset() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public Boolean getPlayerAttacking() {
        return isPlayerAttacking;
    }

    public void setPlayerAttacking(Boolean playerAttacking) {
        isPlayerAttacking = playerAttacking;
    }
}
