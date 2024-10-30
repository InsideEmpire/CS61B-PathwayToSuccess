package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Room {
    int x;
    int y;
    int width;
    int height;
    TETile[][] world;

    public Room(int roomX, int roomY, int roomWidth, int roomHeight, TETile[][] world) {
        x = roomX;
        y = roomY;
        width = roomWidth;
        height = roomHeight;
        this.world =world;
    }

    public void RoomOf(TETile[][] world) {
        this.world = world;
    }

    public boolean ifValid(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        return (x + width <= numXTiles) && (y + height <= numYTiles);
    }

    public boolean ifInside(Room room) {
        return (x < room.x + room.width && x + width > room.x &&
                y < room.y + room.height && y + height > room.y);
    }

    private void drawWallOn(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        for (int i = x; i < x + width; i += 1) {
            for (int j = y; j < y + height; j += 1) {
                if (world[i][j] != Tileset.FLOOR) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }

    /**
     * then we call this method to set the inside area to be FLOOR.
     */
    private void drawFloorOn(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        for (int i = x + 1; i < x + width - 1; i += 1) {
            for (int j = y + 1; j < y + height - 1; j += 1) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    public void toDrawOn(TETile[][] world) {
        // fist call drawWall
        drawWallOn(world);
        // then call drawFloor
        drawFloorOn(world);
    }
}
