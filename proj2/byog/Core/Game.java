package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int TILE_SIZE = 16;
    public long SEED = 0L;
    private static boolean isPlaying = false;
    private World world;

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
        SEED = stringAnalise(keyboardInput());

        if (SEED >= 0) {
            world = new World(WIDTH, HEIGHT, SEED);
            isPlaying = true;
        } else if (SEED == -2){
            load();
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

        SEED = stringAnalise(input);
        world = new World(WIDTH, HEIGHT, SEED);
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
                                    isPlaying = false;
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

    public  void quitAndSaving() {
        try (ObjectOutput oos = new ObjectOutputStream(new FileOutputStream("byog/Core/data/World.ser"))) {
            oos.writeObject(world);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  void load() {
        try (ObjectInput ois = new ObjectInputStream(new FileInputStream("byog/Core/data/World.ser"))) {
            world = (World) ois.readObject();
            System.out.println(world);
            world.resetLoad();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String cursorPointing(TETile[][] world) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int xPos = (int) x;
        int yPos = (int) y;
        return world[xPos][yPos].description();
    }
    private static String cursorPointing() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        return "x: " + String.valueOf(x) + " y: " + String.valueOf(x);
    }

    private static void displayGameUI(TETile[][] world) {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(10, HEIGHT - 2, cursorPointing(world));
        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
    }

    private static void displayMainMenu() {
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

    private static long stringAnalise(String input) {
        if (input.equals("l")
                || input.equals("L")
                && isPlaying) {
            return -2;
        }
//        boolean startNew = false;
        if (input.length() < 3) {
            return -1;
        }
        char c = input.charAt(0);
        if (c == 'N' || c == 'n') {
            StringBuilder seed = new StringBuilder();
            int index = 1;
            while (index < input.length()
                    && input.charAt(index) >= '0'
                    && input.charAt(index) <= '9') {
                seed.append(input.charAt(index));
                index += 1;
            }
            if (input.charAt(index) == 'S' || input.charAt(index) == 's') {
                return Long.parseLong(seed.toString());
            }
        }
        return -1;
    }


    private static String keyboardInput() {
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
