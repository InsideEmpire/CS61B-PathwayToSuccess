package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int TILE_SIZE = 16;
    private long SEED = 0L;
    private boolean isPlaying = false;
    private World world;
    private String COMMAND;
    private boolean hasCommand = false;
    private boolean needToLoad = false;


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
        playing(world.getTeTiles());
    }

    public TETile[][] playWithInputString(String input) {

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
        return world.getTeTiles();
    }

    public void playing(TETile[][] teTiles) {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        while (isPlaying) {
            ter.renderFrame(world.getTeTiles());
            displayGameUI();
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
        world.move(keyboardMove());
    }

    public void movePlayer(Toward toward) {
        // TODO: java.lang.NullPointerException
        try {
            world.move(toward);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
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
                        while (isPlaying) {
                            if (StdDraw.hasNextKeyTyped()) {
                                input = StdDraw.nextKeyTyped();
                                if (input == 'Q' || input == 'q') {
                                    System.out.println("成功保存退出");
                                    quitAndSaving();  // 保存并退出
                                }
                            }
                        }
                        return Toward.STAY;
                    default:
                        return Toward.STAY;
                }
            }
            return Toward.STAY;
        }
    }

    public void quitAndSaving() {
        String currentDir = null;
        try {
            currentDir = System.getProperty("user.dir");
        } catch (SecurityException e) {
            // 处理安全异常，例如记录错误日志或使用默认目录
            System.err.println("无法访问工作目录: " + e.getMessage());
            currentDir = ".";  // 使用默认目录
        }

        String filePath = currentDir + "/World.ser";  // 在当前工作目录下保存文件

        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath);
            java.io.ObjectOutput oos = new java.io.ObjectOutputStream(fos);
            oos.writeObject(world);
            isPlaying = false;
            oos.close();
            fos.close();
        } catch (SecurityException e) {
            System.err.println("安全异常: " + e.getMessage());
        } catch (java.io.FileNotFoundException e) {
            System.err.println("文件未找到: " + e.getMessage());
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        String currentDir = null;
        try {
            currentDir = System.getProperty("user.dir");
        } catch (SecurityException e) {
            // 处理安全异常，例如记录错误日志或使用默认目录
            System.err.println("无法访问工作目录: " + e.getMessage());
            currentDir = ".";  // 使用默认目录
        }
        String filePath = currentDir + "/World.ser";  // 读取当前工作目录中的文件

        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(filePath);
            java.io.ObjectInput ois = new java.io.ObjectInputStream(fis);
            // TODO: load 之后没法正确加载world，出现java.lang.NullPointerException
            world = (World) ois.readObject();
            if (world == null) {
                throw new RuntimeException(new NullPointerException("world does not exist"));
            }
            if (world.getPlayer() == null) {
                throw new RuntimeException(new NullPointerException("player does not exist"));
            }
            System.out.println(world);
            world.resetLoad();
            isPlaying = true;
            ois.close();
            fis.close();
        } catch (SecurityException e) {
            System.err.println("安全异常: " + e.getMessage());
        } catch (java.io.FileNotFoundException e) {
            System.err.println("文件未找到: " + e.getMessage());
        } catch (ClassNotFoundException | java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String cursorPointing(TETile[][] teTiles) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int xPos = (int) x;
        int yPos = (int) y;
        return teTiles[xPos][yPos].description();
    }

    private String cursorPointing() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        return "x: " + String.valueOf(x) + " y: " + String.valueOf(x);
    }

    private void displayGameUI() {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(10, HEIGHT - 2, cursorPointing(world.getTeTiles()));
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
                    if (index.get() + 1 < input.length()
                            && (input.charAt(index.get() + 1) == 'Q'
                            || input.charAt(index.get() + 1) == 'q')) {
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

            if (index.get() < input.length()
                    && (input.charAt(index.get()) == 'S'
                    || input.charAt(index.get()) == 's')) {
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
