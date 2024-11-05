package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;

public class World {
    public static TETile[][] initialise(int width, int height, long seed) {

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT

        // initialize tiles
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        RoomGenerator generator = new RoomGenerator(width, height, seed);
        List<Room> rooms = generator.generateRooms(world);

        return world;
    }
}
