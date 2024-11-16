package byog.Core;

/**
 * Toward.java
 * This enum represents the directions a player or object can move in the game.
 * Each direction is associated with a change in x and y coordinates.
 */

public enum Toward {
    W(0, 1),    // Move upward
    S(0, -1),   // Move downward
    A(-1, 0),   // Move left
    D(1, 0),    // Move right
    STAY(0, 0); // Stay in the current position

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    Toward(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private final int x;
    private final int y;
}
