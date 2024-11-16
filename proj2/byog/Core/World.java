package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.List;

/**
 * World.java
 * This class represents the game world, which includes the map, rooms, tunnels, player, and outdoor exit.
 * It provides methods to generate the world, reset it, and move the player.
 */

public class World implements Serializable {
    private int width;
    private int height;
    private long seed;
    private List<Room> roomsAndTunnels;
    private transient TETile[][] teTiles;
    private RoomGenerator generator;
    private Player player;
    private Outdoor outdoor;

    // Getter for the player object
    public Player getPlayer() {
        return player;
    }

    // Getter for the world tiles
    public TETile[][] getTeTiles() {
        return teTiles;
    }

    // Constructor initializes the world and generates its components
    World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;

        // Initialize all tiles to NOTHING
        teTiles = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                teTiles[x][y] = Tileset.NOTHING;
            }
        }

        // Generate outdoor exit point
        outdoor = new Outdoor(seed);

        // Generate rooms and tunnels
        generator = new RoomGenerator(width, height, seed);
        roomsAndTunnels = generator.generateRooms(teTiles);

        // Place the player randomly
        player = new Player(teTiles, seed);

        // Set the outdoor exit point on the map
        outdoor.generateRandomOutdoor(teTiles);
    }

    // Resets the world, redrawing rooms, tunnels, and the outdoor exit
    public void reset() {
        Room.toDrawOn(teTiles, roomsAndTunnels);
        teTiles[outdoor.getX()][outdoor.getY()] = Tileset.UNLOCKED_DOOR;
    }

    // Resets the world for loading, reinitializing the tile array and redrawing components
    public void resetLoad() {
        teTiles = new TETile[width][height]; // Reinitialize the tile array
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                teTiles[x][y] = Tileset.NOTHING;
            }
        }
        Room.toDrawOn(teTiles, roomsAndTunnels);
        teTiles[outdoor.getX()][outdoor.getY()] = Tileset.UNLOCKED_DOOR;
    }

    // String representation of the world for debugging
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

    // Moves the player in the specified direction
    public void move(Toward direction) {
        player.move(this, direction);
    }
}
