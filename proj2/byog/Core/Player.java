package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Player {
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
    }
    void drawOnWorld(TETile[][] world) {
        world[x][y] = Tileset.PLAYER;
    }
    
}
