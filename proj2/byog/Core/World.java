package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.*;
import java.util.List;

//import static byog.Core.Game.keyboardMove;

public class World implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    int width;
    int height;
    List<Room> roomsAndTunnels;
    transient TETile[][] teTiles;
    RoomGenerator generator;
    Player player;

    World(int width, int height, long seed) {

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        this.width = width;
        this.height = height;
        // initialize tiles
        teTiles = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                teTiles[x][y] = Tileset.NOTHING;
            }
        }

        generator = new RoomGenerator(width, height, seed);
        roomsAndTunnels = generator.generateRooms(teTiles);

        player = new Player(teTiles, seed);

    }

    public TETile[][] initialise(int width, int height, long seed) {

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT

        // initialize tiles
        teTiles = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                teTiles[x][y] = Tileset.NOTHING;
            }
        }

        generator = new RoomGenerator(width, height, seed);
        roomsAndTunnels = generator.generateRooms(teTiles);

        player = new Player(teTiles, seed);

        return teTiles;
    }

    public void reset() {
        Room.toDrawOn(teTiles, roomsAndTunnels);
        teTiles[Outdoor.x][Outdoor.y] = Tileset.UNLOCKED_DOOR;
    }

    public void resetLoad() {
        teTiles = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                teTiles[x][y] = Tileset.NOTHING;
            }
        }
        Room.toDrawOn(teTiles, roomsAndTunnels);
        teTiles[Outdoor.x][Outdoor.y] = Tileset.UNLOCKED_DOOR;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("World {")
                .append("\n  width: ").append(width)
                .append("\n  height: ").append(height)
                .append("\n  roomsAndTunnels: ").append(roomsAndTunnels)
                .append("\n  generator: ").append(generator)
                .append("\n  player: ").append(player)
                .append("\n}");

        return sb.toString();
    }

}
