package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int ROOMNUM = 10;

    private Position player;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] finalWorldFrame = null;
        drawStartUI();
        char cmd = getChar();
        if (cmd == 'n') {
            finalWorldFrame = newGame();
            ter.renderFrame(finalWorldFrame);
        } else if (cmd == 'l') {
            loadGame();
        } else {
            System.exit(0);
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        String cmd = input.toLowerCase();
        TETile[][] finalWorldFrame = null;
        if (cmd.charAt(0) == 'n') {
            finalWorldFrame = newGame(cmd);
        } else if (cmd.charAt(0) == 'l') {
            finalWorldFrame = loadGame(cmd);
        } else if (cmd.charAt(0) == 'q') {
            System.exit(0);
        }
        return finalWorldFrame;
    }

    private TETile[][] newGame(String input) {
        int startIndex = input.indexOf('s');
        long seed = Long.parseLong(input.substring(1, startIndex));

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initializeWorld(world);

        Random r = new Random(seed);

        List<Room> rooms = generateRooms(world, r);
        generateHallways(world, r);
        for (Room room : rooms) {
            room.connect(world, r);
        }
        removeDeadHeads(world);
        addWallsAndDoor(world, r);

        play(world, input.substring(startIndex + 1));

        return world;
    }

    private TETile[][] newGame() {
        long seed = getSeed();
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initializeWorld(world);

        Random r = new Random(seed);

        List<Room> rooms = generateRooms(world, r);
        generateHallways(world, r);
        for (Room room : rooms) {
            room.connect(world, r);
        }
        removeDeadHeads(world);
        addWallsAndDoor(world, r);

        play(world);

        return world;
    }

    private TETile[][] loadGame(String input) {
        TETile[][] finalWorldFrame = getSavedGame();
        play(finalWorldFrame, input.substring(1));
        return finalWorldFrame;
    }

    private void loadGame() {
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] finalWorldFrame = getSavedGame();
        ter.renderFrame(finalWorldFrame);
        play(finalWorldFrame);
    }

    private void saveGame(TETile[][] world, String a) {
        File f = new File("savefile.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f));
            os.writeObject(world);
            os.writeObject(player);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void saveGame(TETile[][] world) {
        File f = new File("savefile.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f));
            os.writeObject(world);
            os.writeObject(player);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private TETile[][] getSavedGame() {
        TETile[][] world = null;
        File f = new File("savefile.txt");
        try {
            ObjectInputStream os = new ObjectInputStream(new FileInputStream(f));
            world = (TETile[][]) os.readObject();
            player = (Position) os.readObject();
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
            System.exit(0);
        }
        return world;
    }


    private void play(TETile[][] world, String playString) {
        for (int i = 0; i < playString.length(); i++) {
            switch (playString.charAt(i)) {
                case 'w':
                    player.playerUp(world);
                    break;
                case 's':
                    player.playerDown(world);
                    break;
                case 'a':
                    player.playerLeft(world);
                    break;
                case 'd':
                    player.playerRight(world);
                    break;
                case ':':
                    if (i + 1 < playString.length() && playString.charAt(i + 1) == 'q') {
                        saveGame(world);
                        return;
                    }
                    break;
                default:
            }
        }
    }

    private void play(TETile[][] world) {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char cmd = Character.toLowerCase(StdDraw.nextKeyTyped());
            switch (cmd) {
                case 'w':
                    player.playerUp(world);
                    ter.renderFrame(world);
                    break;
                case 's':
                    player.playerDown(world);
                    ter.renderFrame(world);
                    break;
                case 'a':
                    player.playerLeft(world);
                    ter.renderFrame(world);
                    break;
                case 'd':
                    player.playerRight(world);
                    ter.renderFrame(world);
                    break;
                case ':':
                    while (true) {
                        if (!StdDraw.hasNextKeyTyped()) {
                            continue;
                        }
                        cmd = Character.toLowerCase(StdDraw.nextKeyTyped());
                        if (cmd == 'q') {
                            saveGame(world);
                            System.exit(0);
                        } else {
                            break;
                        }
                    }
                    break;
                default:
            }
        }
    }

    private void initializeWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        for (int x = 1; x < WIDTH - 1; x += 2) {
            for (int y = 1; y < HEIGHT - 1; y += 2) {
                world[x][y] = Tileset.SAND;
            }
        }
    }

    private List<Room> generateRooms(TETile[][] world, Random r) {
        List<Room> rooms = new ArrayList<>();
        int num = 0;
        while (num < ROOMNUM) {
            Room newRoom = new Room(r, WIDTH, HEIGHT);
            if (newRoom.isLegal(rooms)) {
                rooms.add(newRoom);
                newRoom.draw(world);
                num++;
            }
        }
        return rooms;
    }

    private void generateHallways(TETile[][] world, Random r) {
        Position startPoint = randomStartPoint(world, r);
        Stack<Position> stack = new Stack<>();
        stack.push(startPoint);
        while (!stack.isEmpty()) {
            Position cur = stack.peek();
            stack.pop();
            cur.spread(stack, world, r);
        }
    }

    private void removeDeadHeads(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j].equals(Tileset.SAND)) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }

        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    Position pos = new Position(i, j);
                    if (pos.isDeadHead(world)) {
                        world[i][j] = Tileset.NOTHING;
                        flag = true;
                    }
                }
            }
        }
    }

    private void addWallsAndDoor(TETile[][] world, Random r) {
        List<Position> doorCandidates = new ArrayList<>();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position pos = new Position(i, j);
                if (pos.isWall(world)) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position pos = new Position(i, j);
                if (pos.isDoorCandidate(world)) {
                    doorCandidates.add(pos);
                }
            }
        }

        int option = RandomUtils.uniform(r, 0, doorCandidates.size());
        int X = doorCandidates.get(option).getX();
        int Y = doorCandidates.get(option).getY();
        world[X][Y] = Tileset.LOCKED_DOOR;

        for (Position p : doorCandidates.get(option).getAroundPos(world, false)) {
            if (world[p.getX()][p.getY()].equals(Tileset.FLOOR)) {
                world[p.getX()][p.getY()] = Tileset.PLAYER;
                player = new Position(p.getX(), p.getY());
            }
        }
    }

    private Position randomStartPoint(TETile[][] world, Random r) {
        int option = RandomUtils.uniform(r, 0, 4);
        int X = 1, Y = 1;
        switch (option) {
            case 0:
                X = randomNum(r, 1, WIDTH - 1);
                break;
            case 1:
                Y = randomNum(r, 1, HEIGHT - 1);
                break;
            case 2:
                X = WIDTH - 1;
                Y = randomNum(r, 1, HEIGHT - 1);
                break;
            default:
                X = randomNum(r, 1, WIDTH - 1);
                Y = HEIGHT - 1;
                break;
        }
        world[X][Y] = Tileset.FLOOR;
        return new Position(X, Y);
    }

    private int randomNum(Random r, int lowBound, int upBound) {
        int x = RandomUtils.uniform(r, lowBound, upBound);
        if (x % 2 == 0) {
            if (RandomUtils.bernoulli(r)) {
                return x + 1;
            } else {
                return x - 1;
            }
        }
        return x;
    }

    private void drawStartUI() {
        StdDraw.setCanvasSize(WIDTH * 16,  HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 4 * 3, "CS61B: THE GAME");
        Font smallfont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallfont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Quit (Q)");

        StdDraw.show();
    }

    private char getChar() {
        char cmd;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            cmd = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (cmd == 'n' || cmd == 'l' || cmd == 'q') {
                break;
            }
        }
        return cmd;
    }

    private long getSeed() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 1, "Please enter a random seed: ");
        StdDraw.show();

        String inputs = "";
        char input;
        while (true) {
            StdDraw.clear(Color.BLACK);
            font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 1, "Please enter a random seed: ");

            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            input = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (input == 's') {
                break;
            }
            if (!Character.isDigit(input)) {
                continue;
            }
            inputs += input;

            font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, inputs);
            StdDraw.show();
        }
        return Long.parseLong(inputs);
    }

}
