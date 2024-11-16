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
    public static final int HEIGHT = 40;
    private static final int TILE_SIZE = 16;

    private World world;

    private long SEED = 0L;
    private String COMMAND;

    private boolean isPlaying = false; // Flag to check if the game is ongoing
    private boolean hasCommand = false; // Flag to check if there is a command to process
    private boolean needToLoad = false; // Flag to check if a saved game needs to be loaded


    // Starts the game with keyboard input, displaying the main menu and processing user commands
    public void playWithKeyboard() {
        displayMainMenu();  // Show the main menu
        stringAnalysis(keyboardInput()); // Analyze the keyboard input to set up the game

        // Load saved game if needed, or initialize a new game
        if (needToLoad) {
            load();
        } else {
            world = new World(WIDTH, HEIGHT, SEED);
            isPlaying = true;
        }
        playing();  // Begin the game loop
    }

    // Starts the game with an input string, processing the seed and commands
    public TETile[][] playWithInputString(String input) {
        stringAnalysis(input); // Analyze the input string to set up the game
        if (needToLoad) {
            load();
        } else {
            world = new World(WIDTH, HEIGHT, SEED);
            isPlaying = true;
        }
        if (hasCommand) {
            useCommand();  // Process the commands if available
        }
        return world.getTeTiles();  // Return the tiles for rendering
    }

    // Main game loop that handles rendering and player movement
    public void playing() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        while (isPlaying) {
            ter.renderFrame(world.getTeTiles());  // Render the current state of the world
            displayGameUI();  // Display the game UI (e.g., coordinates)
            movePlayer();  // Move the player based on keyboard input
        }
        if (!isPlaying) {
            StdDraw.clear();  // Clear the screen
            StdDraw.show();   // Update the display
            System.exit(0);   // Exit the game
        }
    }

    // Handles player movement using keyboard input
    public void movePlayer() {
        world.move(keyboardMove());
    }

    // Handles player movement using a given direction
    public void movePlayer(Toward toward) {
        if (world == null) {
            throw new RuntimeException(new NullPointerException("world is null"));
        } else {
            world.move(toward);
        }
    }

    // Processes the commands provided by the user (e.g., movement or quitting)
    public void useCommand() {
        for (int i = 0; i < COMMAND.length(); i++) {
            if (COMMAND.charAt(i) == 'Q') {
                quitAndSaving();  // Quit the game and save
            }
            // Process movement commands
            switch (COMMAND.charAt(i)) {
                case 'W':
                case 'w':
                    movePlayer(Toward.W);  // Move up
                    break;
                case 'S':
                case 's':
                    movePlayer(Toward.S);  // Move down
                    break;
                case 'A':
                case 'a':
                    movePlayer(Toward.A);  // Move left
                    break;
                case 'D':
                case 'd':
                    movePlayer(Toward.D);  // Move right
                    break;
                default:
                    movePlayer(Toward.STAY);  // Stay in place if the command is invalid
                    break;
            }
        }
    }

    // Listens for keyboard input to move the player
    public Toward keyboardMove() {
        char input;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                input = StdDraw.nextKeyTyped();  // Get the next key typed by the user
                switch (input) {
                    case 'W':
                    case 'w':
                        return Toward.W;  // Move up
                    case 'S':
                    case 's':
                        return Toward.S;  // Move down
                    case 'A':
                    case 'a':
                        return Toward.A;  // Move left
                    case 'D':
                    case 'd':
                        return Toward.D;  // Move right
                    case ':':
                        // Handle ':' key for saving and quitting
                        while (isPlaying) {
                            if (StdDraw.hasNextKeyTyped()) {
                                input = StdDraw.nextKeyTyped();
                                if (input == 'Q' || input == 'q') {
                                    System.out.println("成功保存退出");  // Print message indicating saving and quitting
                                    quitAndSaving();  // Save and quit
                                }
                            }
                        }
                        return Toward.STAY;  // Stay in place if no valid input
                    default:
                        return Toward.STAY;  // Stay in place for any other input
                }
            }
            return Toward.STAY;  // Default to staying in place
        }
    }

    // Saves the current game state to a file
    public void quitAndSaving() {
        try {
            java.io.ObjectOutputStream out =
                    new java.io.ObjectOutputStream(new java.io.FileOutputStream("savefile.txt"));
            out.writeObject(world);  // Save the world object to a file
            out.close();
            isPlaying = false;  // Stop the game loop
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // Loads a saved game state from a file
    public void load() {
        try {
            java.io.ObjectInputStream in =
                    new java.io.ObjectInputStream(new java.io.FileInputStream("savefile.txt"));
            world = (World) in.readObject();  // Load the world object from the file
            world.resetLoad();  // Reset the world after loading
            isPlaying = true;  // Start the game
            in.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Returns the description of the tile the cursor is pointing to
    private String cursorPointing(TETile[][] teTiles) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int xPos = (int) x;
        int yPos = (int) y;
        return teTiles[xPos][yPos].description();
    }

    // Returns the cursor's current coordinates
    private String cursorPointing() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        return "x: " + String.valueOf(x) + " y: " + String.valueOf(y);
    }

    // Displays the game's user interface, including the cursor's position
    private void displayGameUI() {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(10, HEIGHT - 2, cursorPointing(world.getTeTiles()));
        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));  // Reset font size
    }

    // Displays the main menu at the start of the game
    private void displayMainMenu() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        StdDraw.setCanvasSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);  // Set canvas size
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);  // Clear the screen with black color
        StdDraw.enableDoubleBuffering();  // Enable double buffering for smoother rendering
        StdDraw.text(midWidth, midHeight + 10, "CS61B: THE GAME");

        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight, "New Game (N)");
        StdDraw.text(midWidth, midHeight - 2, "Load Game (L)");
        StdDraw.text(midWidth, midHeight - 4, "Quit (Q)");
        StdDraw.show();
    }

    // Analyzes the input string to extract seed and command information
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

        // Extract seed and command from the input string
        seedAndCommand.append(seedAnalysis(input, index));
        seedAndCommand.append(commandAnalysis(input, index));

        // Parse the seed and command
        SEED = Long.parseLong(seedAndCommand.toString().split(" ")[0]);

        // If there is a command, store it
        if (hasCommand) {
            COMMAND = seedAndCommand.toString().split(" ")[1];
        }
    }

    // Analyzes and extracts the command characters (W, A, S, D, Q) from the input string
    private StringBuilder commandAnalysis(String input, AtomicInteger index) {
        StringBuilder commandBuilder = new StringBuilder();
        while (index.get() < input.length()) {
            char command = input.charAt(index.get());
            hasCommand = true;

            // Append corresponding commands to the commandBuilder
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
                    // Special case for 'Q' (quit command)
                    if (index.get() + 1 < input.length()
                            && (input.charAt(index.get() + 1) == 'Q'
                            || input.charAt(index.get() + 1) == 'q')) {
                        commandBuilder.append("Q");
                    }
                    break;
                default:
                    index.addAndGet(1);  // Skip invalid characters
                    break;
            }
            index.addAndGet(1);  // Move to the next character
        }

        return commandBuilder;
    }

    // Extracts the seed from the input string (after 'N' or 'n') and returns it as a StringBuilder
    private StringBuilder seedAnalysis(String input, AtomicInteger index) {
        StringBuilder seedBuilder = new StringBuilder();
        index.addAndGet(1);  // Skip the 'N' or 'n' character

        // Parse the numerical seed if the input starts with 'N' or 'n'
        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            // Extract digits for the seed value
            while (index.get() < input.length() && Character.isDigit(input.charAt(index.get()))) {
                seedBuilder.append(input.charAt(index.get()));
                index.addAndGet(1);
            }

            // Skip 'S' or 's' if found after the seed value
            if (index.get() < input.length()
                    && (input.charAt(index.get()) == 'S' || input.charAt(index.get()) == 's')) {
                index.addAndGet(1);  // Skip 'S' or 's'
                seedBuilder.append(" ");
            }
        }
        return seedBuilder;
    }

    // Handles user keyboard input for commands like 'L' or 'S'
    private String keyboardInput() {
        StringBuilder command = new StringBuilder();
        char input;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            input = StdDraw.nextKeyTyped();
            command.append(input);

            // Check for specific commands to trigger actions
            if (command.charAt(command.length() - 1) == 'l' || command.charAt(command.length() - 1) == 'L') {
                isPlaying = true;  // Start the game if 'L' is pressed
                break;
            }
            if (command.charAt(command.length() - 1) == 's' || command.charAt(command.length() - 1) == 'S') {
                break;  // End the input collection if 'S' is pressed
            }
        }

        // Print the collected command for debugging
        System.out.println(command);
        return command.toString();  // Return the collected command as a string
    }
}