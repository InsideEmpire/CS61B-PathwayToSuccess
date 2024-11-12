package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.*;
import java.util.List;

import static byog.Core.Game.keyboardMove;

public class World implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    static List<Room> roomsAndTunnels;
    static TETile[][] world;
    static RoomGenerator generator;
    static Player player;

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

        player = new Player(world, seed);

        return world;
    }

    public static TETile[][] reset() {
        Room.toDrawOn(world, roomsAndTunnels);
        world[Outdoor.x][Outdoor.y] = Tileset.UNLOCKED_DOOR;
        return world;
    }

//    public static void quitAndSaving() {
//        try (ObjectOutput oos = new ObjectOutputStream(new FileOutputStream("byog/Core/data/World.ser"))) {
//            oos.writeObject(roomsAndTunnels);
//            oos.writeObject(generator);
//            oos.writeObject(player);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static TETile[][] load() {
//        try (ObjectInput ois = new ObjectInputStream(new FileInputStream("byog/Core/data/World.ser"))) {
//            roomsAndTunnels = (List<Room>) ois.readObject();
//            generator = (RoomGenerator) ois.readObject();
//            player = (Player) ois.readObject();
//            world = reset();
//            player.drawOnWorld(world);
//            return world;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static void movePlayer(TETile[][] world) {
        player.move(world, keyboardMove());
    }

}
