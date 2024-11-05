package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;

public class TestDemo {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    // 172898 2354170
    private static final int SEED = 2354170;

    public static void main(String[] args) {
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
    }
}
