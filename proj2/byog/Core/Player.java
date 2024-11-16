package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

/**
 * Player.java
 * This class represents the player in the game,
 * including their position and movement logic.
 * It also provides methods for rendering the player
 * on the world grid and controlling movement.
 */

public class Player implements Serializable {
    private int x; // Player's x-coordinate
    private int y; // Player's y-coordinate

    // Constructor to initialize the player with a specific position
    Player(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    // Constructor to place the player randomly on a floor tile in the world
    Player(TETile[][] world, long seed) {
        Random random = new Random(seed);
        this.x = RandomUtils.uniform(random, 0, world.length - 1);
        this.y = RandomUtils.uniform(random, 0, world[0].length - 1);
        while (world[x][y] != Tileset.FLOOR) {
            // Ensure the position is a valid floor tile
            if (random.nextBoolean()) {
                x = (x + random.nextInt(world.length)) % world.length;
            } else {
                y = (y + random.nextInt(world[0].length)) % world[0].length;
            }
        }
        if (world[x][y] == Tileset.FLOOR) {
            drawOnWorld(world);
        }
    }

    // Draws the player on the world grid at the current position
    public void drawOnWorld(TETile[][] world) {
        world[x][y] = Tileset.PLAYER;
    }

    // Moves the player in a specified direction if the target tile is valid
    public void move(World world, Toward direction) {
        int newX = x + direction.getX();
        int newY = y + direction.getY();
        if (world.getTeTiles()[newX][newY] == Tileset.FLOOR) {
            // Check if the target tile is a floor
            // Reset the world to erase the current player position
            world.reset();
            x = newX;
            y = newY;
            drawOnWorld(world.getTeTiles());
        }
    }
}
