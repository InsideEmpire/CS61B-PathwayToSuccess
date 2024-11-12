package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoomGenerator implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int WIDTH;
    private int HEIGHT;
    private int maxRooms;
    private Random random;

    public RoomGenerator(int width, int height, long seed) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.random = new Random(seed);
        this.maxRooms = RandomUtils.uniform(random, 10, 15);
    }

    public List<Room> generateRooms(TETile[][] world) {
        List<Room> rooms = new ArrayList<>();
        List<Room> tunnels = new ArrayList<>();

        for (int i = 0; i < maxRooms; i++) {
            int roomWidth = RandomUtils.uniform(random, 4, 10); // 房间宽度范围
            int roomHeight = RandomUtils.uniform(random, 4, 10); // 房间高度范围
            int roomX = RandomUtils.uniform(random, 0, WIDTH - roomWidth - 1);
            int roomY = RandomUtils.uniform(random, 0, HEIGHT - roomHeight - 1);


            Room newRoom = new Room(roomX, roomY, roomWidth, roomHeight, world);

            if (newRoom.ifValid(world) && !isOverlapping(newRoom, rooms)) {
                //newRoom.RoomOf(teTiles); useless
                newRoom.toDrawOn(world);
                rooms.add(newRoom);

                if (rooms.size() > 1) {
                    tunnels.addAll(connectRooms(rooms.get(rooms.size() - 2), newRoom, world));
                }
            } else {
                i -= 1;
            }
        }
//        generateRandomOutdoor(world);
        rooms.addAll(tunnels);
        return rooms;
    }

    private boolean isOverlapping(Room newRoom, List<Room> rooms) {
        for (Room room : rooms) {
            if (newRoom.ifInside(room)) {
                return true;
            }
        }
        return false;
    }

    private List<Room> connectRooms(Room roomA, Room roomB, TETile[][] world) {
        int centerAx = roomA.x + roomA.width / 2;
        int centerAy = roomA.y + roomA.height / 2;
        int centerBx = roomB.x + roomB.width / 2;
        int centerBy = roomB.y + roomB.height / 2;
        List<Room> twoTunnels = new ArrayList<>();

        if (random.nextBoolean()) {
            twoTunnels.add(drawHorizontalTunnel(centerAx, centerBx, centerAy, world));
            twoTunnels.add(drawVerticalTunnel(centerAy, centerBy, centerBx, world));
        } else {
            twoTunnels.add(drawVerticalTunnel(centerAy, centerBy, centerAx, world));
            twoTunnels.add(drawHorizontalTunnel(centerAx, centerBx, centerBy, world));
        }
        return twoTunnels;
    }

    private Room drawHorizontalTunnel(int x1, int x2, int y, TETile[][] world) {
        Room hallway = new Room(Math.min(x1, x2) - 1, y - 1, Math.abs(x1 - x2) + 3, 3, world);
        hallway.toDrawOn(world);
        return hallway;
    }

    private Room drawVerticalTunnel(int y1, int y2, int x, TETile[][] world) {
        Room hallway = new Room(x - 1, Math.min(y1, y2) - 1, 3, Math.abs(y1 - y2) + 3, world);
        hallway.toDrawOn(world);
        return hallway;
    }

//    private void generateRandomOutdoor(TETile[][] world) {
//        boolean hasOutdoor = false;
//        int x = random.nextInt(world.length);
//        int y = random.nextInt(world[0].length);
//        while (world[x][y] != Tileset.WALL) {
//            if (random.nextBoolean()) {
//                x = (x + random.nextInt(world.length)) % world.length;
//            } else {
//                y = (y + random.nextInt(world[0].length)) % world[0].length;
//            }
//        }
//        if (world[x][y] == Tileset.WALL) {
//            world[x][y] = Tileset.UNLOCKED_DOOR;
//            Outdoor.x = x;
//            Outdoor.y = y;
//        }
//    }
}
