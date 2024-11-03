package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;

public class World {
    public static TETile[][] initialise(int WIDTH, int HEIGHT, long SEED) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        RoomGenerator generator = new RoomGenerator(WIDTH, HEIGHT, SEED);
        List<Room> rooms = generator.generateRooms(world);

        ter.renderFrame(world);

        return world;
    }
}
