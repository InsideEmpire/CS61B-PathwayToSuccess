package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int TILE_SIZE = 16;
    public long SEED = 0L;
    private boolean isPlaying = false;
    private World world;
    private String COMMAND;
    private boolean hasCommand = false;
    private boolean needToLoad = false;

    public void main(String[] args) {
        if (args.length != 0) {
            TETile[][] world = playWithInputString(args[0]);
        }
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        displayMainMenu();
        stringAnalysis(keyboardInput());

        if (needToLoad) {
            load();
        } else {
            world = new World(WIDTH, HEIGHT, SEED);
            isPlaying = true;
        }

//        player = new Player(teTiles, SEED);
        playing(world.teTiles);
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same teTiles should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * teTiles. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same teTiles back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the teTiles
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the teTiles that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        stringAnalysis(input);
        if (needToLoad) {
            load();
        } else {
            world = new World(WIDTH, HEIGHT, SEED);
            isPlaying = true;
        }
        if (hasCommand) {
            useCommand();
        }
        return world.teTiles;
    }

    public void playing(TETile[][] teTiles) {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        while (isPlaying) {
            ter.renderFrame(world.teTiles);
            displayGameUI(world.teTiles);
            movePlayer();
        }
        if (!isPlaying) {
            System.out.println("isPlaying已更改为false");
            StdDraw.clear();
            StdDraw.show();
            System.exit(0);
        }
    }


    public void movePlayer() {
        world.player.move(world, keyboardMove());
    }

    public void movePlayer(Toward toward) {
        world.player.move(world, toward);
    }

    public void useCommand() {
        for (int i = 0; i < COMMAND.length(); i++) {
            if (COMMAND.charAt(i) == 'Q') {
                quitAndSaving();
            }
            // world has been initialised
            switch (COMMAND.charAt(i)) {
                case 'W':
                case 'w':
                    movePlayer(Toward.W);
                    break;
                case 'S':
                case 's':
                    movePlayer(Toward.S);
                    break;
                case 'A':
                case 'a':
                    movePlayer(Toward.A);
                    break;
                case 'D':
                case 'd':
                    movePlayer(Toward.D);
                    break;
                default:
                    movePlayer(Toward.STAY);
                    break;
            }
        }
    }

    public Toward keyboardMove() {
        char input;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                input = StdDraw.nextKeyTyped();
                switch (input) {
                    case 'W':
                    case 'w':
                        return Toward.W;
                    case 'S':
                    case 's':
                        return Toward.S;
                    case 'A':
                    case 'a':
                        return Toward.A;
                    case 'D':
                    case 'd':
                        return Toward.D;
                    case ':':
                        // 处理 ':' 按键，进行保存并退出
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                input = StdDraw.nextKeyTyped();
                                if (input == 'Q' || input == 'q') {
                                    System.out.println("成功保存退出");
                                    quitAndSaving();  // 保存并退出
                                    return Toward.STAY;  // 退出循环后返回一个值
                                }
                            }
                        }
//                        break;
                    default:
                        return Toward.STAY;
                }
            }
            return Toward.STAY;
        }
    }

    public void quitAndSaving() {
        try (ObjectOutput oos = new ObjectOutputStream(new FileOutputStream("World.ser"))) {
            oos.writeObject(world);
            isPlaying = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try (ObjectInput ois = new ObjectInputStream(new FileInputStream("World.ser"))) {
            world = (World) ois.readObject();
            System.out.println(world);
            world.resetLoad();
            isPlaying = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String cursorPointing(TETile[][] world) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int xPos = (int) x;
        int yPos = (int) y;
        return world[xPos][yPos].description();
    }

    private String cursorPointing() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        return "x: " + String.valueOf(x) + " y: " + String.valueOf(x);
    }

    private void displayGameUI(TETile[][] world) {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(10, HEIGHT - 2, cursorPointing(world));
        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
    }

    private void displayMainMenu() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        StdDraw.setCanvasSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.text(midWidth, midHeight + 10, "CS61B: THE GAME");

        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight, "New Game (N)");
        StdDraw.text(midWidth, midHeight - 2, "Load Game (L)");
        StdDraw.text(midWidth, midHeight - 4, "Quit (Q)");
        StdDraw.show();
    }

    private void stringAnalysis(String input) {
        long seed = -1;
        StringBuilder seedAndCommand = new StringBuilder();
        AtomicInteger index = new AtomicInteger(0);
        // Handle load command
        if (input.toLowerCase().contains("l")) {
            needToLoad = true;
            if (input.length() > 1) {
                index.addAndGet(1);
                COMMAND = commandAnalysis(input, index).toString();
            }
            return;
        }


        seedAndCommand.append(seedAnalysis(input, index));

        seedAndCommand.append(commandAnalysis(input, index));

        SEED = Long.parseLong(seedAndCommand.toString().split(" ")[0]);

        if (hasCommand) {
            COMMAND = seedAndCommand.toString().split(" ")[1];
        }
    }

    private StringBuilder commandAnalysis(String input, AtomicInteger index) {
        StringBuilder commandBuilder = new StringBuilder();
        while (index.get() < input.length()) {
            char command = input.charAt(index.get());
            hasCommand = true;
            switch (command) {
                case 'W':
                case 'w':
                    commandBuilder.append("W");
                    break;
                case 'S':
                case 's':
                    commandBuilder.append("S");
                    break;
                case 'A':
                case 'a':
                    commandBuilder.append("A");
                    break;
                case 'D':
                case 'd':
                    commandBuilder.append("D");
                    break;
                case ':':
                    if (index.get() + 1 < input.length() && (input.charAt(index.get() + 1) == 'Q' || input.charAt(index.get() + 1) == 'q')) {
                        commandBuilder.append("Q");
                    }
                    break;
                default:
                    index.addAndGet(1);
                    break;
            }
            index.addAndGet(1);
        }

        return commandBuilder;
    }

    private StringBuilder seedAnalysis(String input, AtomicInteger index) {
        StringBuilder seedBuilder = new StringBuilder();
        index.addAndGet(1);
        // Parse the initial seed if the input starts with 'N' or 'n'
        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            // Extract numerical seed value
            while (index.get() < input.length() && Character.isDigit(input.charAt(index.get()))) {
                seedBuilder.append(input.charAt(index.get()));
                index.addAndGet(1);
            }

            if (index.get() < input.length() && (input.charAt(index.get()) == 'S' || input.charAt(index.get()) == 's')) {
                index.addAndGet(1); // Skip 'S' or 's' after the seed
                seedBuilder.append(" ");
            }
        }
        return seedBuilder;
    }

    private String keyboardInput() {
        StringBuilder command = new StringBuilder();
        char input;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            input = StdDraw.nextKeyTyped();
            command.append(input);
            if (command.charAt(command.length() - 1) == 'l'
                    || command.charAt(command.length() - 1) == 'L') {
                isPlaying = true;
                break;
            }
            if (command.charAt(command.length() - 1) == 's'
                    || command.charAt(command.length() - 1) == 'S') {
                break;
            }
        }

        System.out.println(command);
        return command.toString();
    }
}
