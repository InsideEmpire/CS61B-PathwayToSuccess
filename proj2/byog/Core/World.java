package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;

public class World {
    static List<Room> roomsAndTunnels;
    static TETile[][] world;
    static RoomGenerator generator;

    public static TETile[][] initialise(int width, int height, long seed) {

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT

        // initialize tiles
        world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        generator = new RoomGenerator(width, height, seed);
        roomsAndTunnels = generator.generateRooms(world);

        return world;
    }

    public static TETile[][] reset() {
        Room.toDrawOn(world, roomsAndTunnels);
        world[Outdoor.x][Outdoor.y] = Tileset.UNLOCKED_DOOR;
        return world;
    }

}
