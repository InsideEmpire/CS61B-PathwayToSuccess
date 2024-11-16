package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * RoomGenerator.java
 * This class is responsible for generating rooms
 * and tunnels in a tile-based game world.
 * It uses randomization to create diverse layouts
 * while ensuring proper placement of rooms and tunnels.
 */

public class RoomGenerator implements Serializable {
    private int WIDTH; // World width
    private int HEIGHT; // World height
    private int maxRooms; // Maximum number of rooms to generate
    private Random random; // Random number generator

    // Constructor to initialize the room generator with world dimensions and seed
    public RoomGenerator(int width, int height, long seed) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.random = new Random(seed);
        // Randomize the maximum number of rooms
        this.maxRooms = RandomUtils.uniform(random, 20, 25);
    }

    // Generates rooms and tunnels within the given world
    public List<Room> generateRooms(TETile[][] world) {
        List<Room> rooms = new ArrayList<>();
        List<Room> tunnels = new ArrayList<>();

        for (int i = 0; i < maxRooms; i++) {
            int roomWidth = RandomUtils.uniform(random, 5, 9); // Random room width
            int roomHeight = RandomUtils.uniform(random, 5, 9); // Random room height
            int roomX = RandomUtils.uniform(random, 0, WIDTH - roomWidth - 1); // Random x-coordinate
            int roomY = RandomUtils.uniform(random, 0, HEIGHT - roomHeight - 1); // Random y-coordinate

            Room newRoom = new Room(roomX, roomY, roomWidth, roomHeight, world);

            if (newRoom.ifValid(world) && !isOverlapping(newRoom, rooms)) {
                newRoom.toDrawOn(world); // Draw the room if valid and not overlapping
                rooms.add(newRoom);

                if (rooms.size() > 1) {
                    // Connect the current room to the previous room
                    tunnels.addAll(connectRooms(rooms.get(rooms.size() - 2), newRoom, world));
                }
            } else {
                i -= 1; // Retry if room placement is invalid
            }
        }

        // Add tunnels to the list of rooms
        rooms.addAll(tunnels);
        return rooms;
    }

    // Checks if the new room overlaps with any existing rooms
    private boolean isOverlapping(Room newRoom, List<Room> rooms) {
        for (Room room : rooms) {
            if (newRoom.ifInside(room)) {
                return true; // Overlap detected
            }
        }
        return false; // No overlap
    }

    // Connects two rooms with tunnels and returns the tunnels created
    private List<Room> connectRooms(Room roomA, Room roomB, TETile[][] world) {
        int centerAx = roomA.x + roomA.width / 2; // Center x of room A
        int centerAy = roomA.y + roomA.height / 2; // Center y of room A
        int centerBx = roomB.x + roomB.width / 2; // Center x of room B
        int centerBy = roomB.y + roomB.height / 2; // Center y of room B
        List<Room> twoTunnels = new ArrayList<>();

        if (random.nextBoolean()) {
            // Horizontal first, then vertical
            twoTunnels.add(drawHorizontalTunnel(centerAx, centerBx, centerAy, world));
            twoTunnels.add(drawVerticalTunnel(centerAy, centerBy, centerBx, world));
        } else {
            // Vertical first, then horizontal
            twoTunnels.add(drawVerticalTunnel(centerAy, centerBy, centerAx, world));
            twoTunnels.add(drawHorizontalTunnel(centerAx, centerBx, centerBy, world));
        }
        return twoTunnels;
    }

    // Draws a horizontal tunnel and returns the created room
    private Room drawHorizontalTunnel(int x1, int x2, int y, TETile[][] world) {
        Room hallway = new Room(Math.min(x1, x2) - 1, y - 1, Math.abs(x1 - x2) + 3, 3, world);
        hallway.toDrawOn(world); // Draw the tunnel
        return hallway;
    }

    // Draws a vertical tunnel and returns the created room
    private Room drawVerticalTunnel(int y1, int y2, int x, TETile[][] world) {
        Room hallway = new Room(x - 1, Math.min(y1, y2) - 1, 3, Math.abs(y1 - y2) + 3, world);
        hallway.toDrawOn(world); // Draw the tunnel
        return hallway;
    }

}
