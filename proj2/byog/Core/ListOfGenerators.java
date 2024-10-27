package byog.Core;

import byog.TileEngine.TETile;

import java.util.Random;

/**
 * a list of many generators, first one sets the beginning area and the second follows (which means it generates from
 * the inside of the previous one)
 */
public class ListOfGenerators {
    private int numberOfRooms;
    private int numberOfHallways;
    private Generator[] rooms;
    private Generator[] hallways;

    public ListOfGenerators(TETile[][] world) {
        numberOfRooms = RandomUtils.uniform(new Random(), 10);
        numberOfHallways = RandomUtils.uniform(new Random(), numberOfRooms + 1, numberOfRooms + 5);
        rooms = new Generator[numberOfRooms];
        hallways = new Generator[numberOfHallways];

        Generator lastOne = null;
        for (int i = 0, j = 0; i < numberOfRooms || j < numberOfHallways;) {
            if (i < numberOfRooms) {
                rooms[i] = Generator.generateRandom(world, lastOne, true);
                rooms[i].toDrawOnWorld(world);
                lastOne = rooms[i];
                i += 1;
                if (j < numberOfHallways) {
                    hallways[j] = Generator.generateRandom(world, lastOne, false);
                    hallways[j].toDrawOnWorld(world);
                    lastOne = hallways[j];
                    j += 1;
                }
            }
            if (RandomUtils.uniform(new Random(), 2) == 1 && j < numberOfHallways) {
                hallways[j] = Generator.generateRandom(world, lastOne, true);
                hallways[j].toDrawOnWorld(world);
                lastOne = hallways[j];
                j += 1;
            }
        }
    }

}
