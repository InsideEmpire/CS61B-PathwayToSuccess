package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.List;

/**
 * This file defines the Room class,
 * which represents rectangular rooms in a tile-based game world.
 * It includes methods for drawing rooms and checking room properties.
 */

public class Room implements Serializable {
    int x; // X-coordinate of the room's top-left corner
    int y; // Y-coordinate of the room's top-left corner
    int width; // Width of the room
    int height; // Height of the room
    // Reference to the world grid, not serialized
    transient TETile[][] world;

    // Constructor to initialize room properties and link it to a world
    public Room(int roomX, int roomY, int roomWidth, int roomHeight, TETile[][] inputWorld) {
        x = roomX;
        y = roomY;
        width = roomWidth;
        height = roomHeight;
        this.world = inputWorld;
    }

    // Updates the world grid for the room
    public void roomOf(TETile[][] inputWorld) {
        this.world = inputWorld;
    }

    // Checks if the room fits within the world bounds
    public boolean ifValid(TETile[][] inputWorld) {
        int numXTiles = inputWorld.length;
        int numYTiles = inputWorld[0].length;
        return (x + width <= numXTiles) && (y + height <= numYTiles);
    }

    // Checks if this room overlaps with another room
    public boolean ifInside(Room room) {
        return (x < room.x + room.width && x + width > room.x
                && y < room.y + room.height && y + height > room.y);
    }

    // Draws the walls of the room in the world grid
    private void drawWallOn(TETile[][] inputWorld) {
        int numXTiles = inputWorld.length;
        int numYTiles = inputWorld[0].length;
        for (int i = x; i < x + width; i += 1) {
            for (int j = y; j < y + height; j += 1) {
                if (inputWorld[i][j] != Tileset.FLOOR) {
                    // Place wall if not floor
                    inputWorld[i][j] = Tileset.WALL;
                }
            }
        }
    }

    // Fills the interior of the room with floor tiles
    private void drawFloorOn(TETile[][] inputWorld) {
        int numXTiles = inputWorld.length;
        int numYTiles = inputWorld[0].length;
        for (int i = x + 1; i < x + width - 1; i += 1) {
            for (int j = y + 1; j < y + height - 1; j += 1) {
                inputWorld[i][j] = Tileset.FLOOR;
            }
        }
    }

    // Draws the room (walls and floor) onto the world grid
    public void toDrawOn(TETile[][] inputWorld) {
        // Draw the walls first
        drawWallOn(inputWorld);
        // Fill the interior with floor tiles
        drawFloorOn(inputWorld);
    }

    // Draws multiple rooms onto the world grid
    public static void toDrawOn(TETile[][] inputWorld, List<Room> rooms) {
        for (Room room : rooms) {
            room.toDrawOn(inputWorld);
        }
    }
}
