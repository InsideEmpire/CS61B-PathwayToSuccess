package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {
    int x;
    int y;
    int width;
    int height;
    transient TETile[][] world;

    public Room(int roomX, int roomY, int roomWidth, int roomHeight, TETile[][] inputWorld) {
        x = roomX;
        y = roomY;
        width = roomWidth;
        height = roomHeight;
        this.world = inputWorld;
    }

    public void roomOf(TETile[][] inputWorld) {
        this.world = inputWorld;
    }

    public boolean ifValid(TETile[][] inputWorld) {
        int numXTiles = inputWorld.length;
        int numYTiles = inputWorld[0].length;
        return (x + width <= numXTiles) && (y + height <= numYTiles);
    }

    public boolean ifInside(Room room) {
        return (x < room.x + room.width && x + width > room.x 
                && y < room.y + room.height && y + height > room.y);
    }

    private void drawWallOn(TETile[][] inputWorld) {
        int numXTiles = inputWorld.length;
        int numYTiles = inputWorld[0].length;
        for (int i = x; i < x + width; i += 1) {
            for (int j = y; j < y + height; j += 1) {
                if (inputWorld[i][j] != Tileset.FLOOR) {
                    inputWorld[i][j] = Tileset.WALL;
                }
            }
        }
    }

    /**
     * then we call this method to set the inside area to be FLOOR.
     */
    private void drawFloorOn(TETile[][] inputWorld) {
        int numXTiles = inputWorld.length;
        int numYTiles = inputWorld[0].length;
        for (int i = x + 1; i < x + width - 1; i += 1) {
            for (int j = y + 1; j < y + height - 1; j += 1) {
                inputWorld[i][j] = Tileset.FLOOR;
            }
        }
    }

    public void toDrawOn(TETile[][] inputWorld) {
        // fist call drawWall
        drawWallOn(inputWorld);
        // then call drawFloor
        drawFloorOn(inputWorld);
    }

    public static void toDrawOn(TETile[][] inputWorld, List<Room> rooms) {
        for (Room room : rooms) {
            room.toDrawOn(inputWorld);
        }
    }
}
