package constants;

public class PlayerConstants {
    public static class PlayerDirections {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerActions{
        public static final int STANDING = 0;
        public static final int RUNNING = 1;
        public static final int JUMPING = 2;
        public static final int ATTACKING = 3;
        public static final int DYING = 4;

        public static int getTotalSpritesCountFromPlayerAction(int playerAction) {
            switch (playerAction) {
                case STANDING:
                    return 17;
                case RUNNING:
                    return 6;
                case JUMPING:
                    return 12;
                case ATTACKING:
                    return 7;
                case DYING:
                    return 9;
                default:
                    return 1;
            }
        }
    }

}
