package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

/**
 * Outdoor.java
 * This class represents the outdoor exit point in the game.
 * It includes methods to generate a random outdoor position on the world map.
 */

public class Outdoor implements Serializable {
    private int x;
    private int y;
    private Random random;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    Outdoor(long seed) {
        this.random = new Random(seed);
    }

    void generateRandomOutdoor(TETile[][] world) {
        x = random.nextInt(world.length);
        y = random.nextInt(world[0].length);
        while (world[x][y] != Tileset.WALL) {
            if (random.nextBoolean()) {
                x = (x + random.nextInt(world.length)) % world.length;
            } else {
                y = (y + random.nextInt(world[0].length)) % world[0].length;
            }
        }
        if (world[x][y] == Tileset.WALL) {
            world[x][y] = Tileset.UNLOCKED_DOOR;
        }
    }

}
