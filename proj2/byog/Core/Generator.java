package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class Generator {
    private int x;
    private int y;
    private int width;
    private int height;

    public Generator() {

    }

    /**
     * @param x The x-coordinate of the bottom-left point of the room or hallway.
     * @param y The y-coordinate of the bottom-left point of the room or hallway.
     * @param width the width on x-coordinate of the room or hallway.
     * @param height the height on y-coordinate of the room or hallway.
     */
    public Generator(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * This method can find if the generator is being generated out of the world we give.
     * @param world The 2D-array of TETile.
     * @return If generator is valid.
     */
    public boolean ifValid(TETile[][] world) {
        if (world == null) {
            throw new NullPointerException("The incoming parameter world do not existed");
        }
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        return (x + width <= numXTiles) && (y + height <= numYTiles);
    }

    public boolean ifInside(Generator last) {
        return (x < last.x + last.width && x + width > last.x &&
                y < last.y + last.height && y + height > last.y);
    }

    public boolean ifConnect(Generator last) {
        return (last.x + last.width - 2 == x + 1) || (last.x + last.width - 2 == x + width - 2) || (last.y + last.height - 2 == y + 1) || (last.y + last.height - 2 == y + height - 2);
    }

    /**
     * we firstly set all tile in our generator to be WALL.
     */
    public void drawWall(TETile[][] world) {
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
    public void drawFloor(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        for (int i = x + 1; i < x + width - 1; i += 1) {
            for (int j = y + 1; j < y + height - 1; j += 1) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    /**
     * use two methods above to draw on our world.
     */
    public void toDrawOnWorld(TETile[][] world) {
        // fist call drawWall
        drawWall(world);
        // then call drawFloor
        drawFloor(world);
    }

    public static Generator generateRandom(TETile[][] world, Generator lastOne, boolean lastIsHallway) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        Generator room = null;
        Generator hallway = null;

        if (lastOne == null) {
            while ((room == null) || !room.ifValid(world)) {
                int newX = RandomUtils.uniform(new Random(), 0, numXTiles);
                int newY = RandomUtils.uniform(new Random(), 0, numYTiles);
                int newWidth = RandomUtils.uniform(new Random(), 3, Math.min(numXTiles - newX, numXTiles - 1) + 3);
                int newHeight = RandomUtils.uniform(new Random(), 3, Math.min(numYTiles - newY, numYTiles - 1) + 3);
                room = new Generator(newX, newY, newWidth, newHeight);
            }
            return room;
        }

        try {
            if (lastIsHallway) {
                while ((room == null) || !room.ifValid(world) || room.ifInside(lastOne) || room.ifConnect(lastOne)) {
                    int newX = RandomUtils.uniform(new Random(), 0, numXTiles);
                    int newY = RandomUtils.uniform(new Random(), 0, numYTiles);
                    int newWidth = RandomUtils.uniform(new Random(), 3, Math.min(numXTiles - newX, numXTiles - 1) + 3);
                    int newHeight = RandomUtils.uniform(new Random(), 3, Math.min(numYTiles - newY, numYTiles - 1) + 3);
                    room = new Generator(newX, newY, newWidth, newHeight);
                }
                return room;
            } else { // last is room
                while ((hallway == null) || !hallway.ifValid(world) || hallway.ifInside(lastOne) || hallway.ifConnect(lastOne)) {
                    int newX = RandomUtils.uniform(new Random(), 0, numXTiles);
                    int newY = RandomUtils.uniform(new Random(), 0, numYTiles);
                    int newWidth;
                    int newHeight;

                    if (RandomUtils.uniform(new Random(), 2) == 0) { // which direction the hallway facing to
                        newWidth = RandomUtils.uniform(new Random(), 3, Math.min(numXTiles - newX, numXTiles - 1) + 3);
                        newHeight = 3;
                    } else {
                        newWidth = 3;
                        newHeight = RandomUtils.uniform(new Random(), 3, Math.min(numYTiles - newY, numYTiles - 1) + 3);
                    }
                    hallway = new Generator(newX, newY, newWidth, newHeight);
                }
                return hallway;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
