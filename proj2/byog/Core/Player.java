package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Player implements Serializable {
    private int x;
    private int y;

    Player(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    Player(TETile[][] world, long seed) {
        Random random = new Random(seed);
        this.x = RandomUtils.uniform(random, 0, world.length - 1);
        this.y = RandomUtils.uniform(random, 0, world[0].length - 1);
        while (world[x][y] != Tileset.FLOOR) {
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

    public void drawOnWorld(TETile[][] world) {
        world[x][y] = Tileset.PLAYER;
    }

    public void move(World world, Toward direction) {
        int newX = x + direction.x;
        int newY = y + direction.y;
        if (world.teTiles[newX][newY] == Tileset.FLOOR) {
            world.reset();
            x = newX;
            y = newY;
            drawOnWorld(world.teTiles);
        }
    }
}
