package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;


public class Room {
    private final Position bottomLeft;
    private final int width;
    private final int height;
    private final int maxWidth = 7;
    private final int maxHeight = 7;
    private final int minWidth = 3;
    private final int minHeight = 3;
    private final int maxConnectNum = 3;

    public Room(Random r, int worldWidth, int worldHeight) {
        width = randomNum(r, minWidth, maxWidth);
        height = randomNum(r, minHeight, maxHeight);
        int X = randomNum(r, 1, worldWidth - width - 1);
        int Y = randomNum(r, 1, worldHeight - height - 1);
        bottomLeft = new Position(X, Y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getCentralPos() {
        return new Position(bottomLeft.getX() + width / 2, bottomLeft.getY() + height / 2);
    }

    public boolean isLegal(List<Room> rooms) {
        if (rooms.isEmpty()) {
            return true;
        }
        for (Room r : rooms) {
            if (isOverLapped(r)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverLapped(Room other) {
        Position centralPos1 = getCentralPos();
        Position centralPos2 = other.getCentralPos();
        int diffX = Math.abs(centralPos1.getX() - centralPos2.getX());
        int diffY = Math.abs(centralPos1.getY() - centralPos2.getY());
        boolean x = diffX <= (width / 2 + other.getWidth() / 2);
        boolean y = diffY <= (height / 2 + other.getHeight() / 2);
        return x && y;
    }

    public void draw(TETile[][] world) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[bottomLeft.getX() + i][bottomLeft.getY() + j] = Tileset.FLOOR;
            }
        }
    }

    public void connect(TETile[][] world, Random r) {
        int worldWidth = world.length;
        int worldHeight = world[0].length;
        List<Position> candidates = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            int X = bottomLeft.getX() + i;
            int Y1 = bottomLeft.getY();
            int Y2 = bottomLeft.getY() + height - 1;
            if (Y1 - 2 >= 0 && world[X][Y1 - 2].equals(Tileset.FLOOR)) {
                candidates.add(new Position(X, Y1));
            }
            if (Y2 + 2 < worldHeight && world[X][Y2 + 2].equals(Tileset.FLOOR)) {
                candidates.add(new Position(X, Y2));
            }
        }
        for (int i = 0; i < height; i++) {
            int Y = bottomLeft.getY() + i;
            int X1 = bottomLeft.getX();
            int X2 = bottomLeft.getX() + width - 1;
            if (X1 - 2 >= 0 && world[X1 - 2][Y].equals(Tileset.FLOOR)) {
                candidates.add(new Position(X1, Y));
            }
            if (X2 + 2 < worldWidth && world[X2 + 2][Y].equals(Tileset.FLOOR)) {
                candidates.add(new Position(X2, Y));
            }
        }

        if (candidates.size() == 0) {
            return;
        }

        int connectNum = RandomUtils.uniform(r, 1, maxConnectNum);
        for (int i = 0; i < connectNum; i++) {
            int option = RandomUtils.uniform(r, 0, candidates.size());
            int X = candidates.get(option).getX();
            int Y = candidates.get(option).getY();
            if (X == bottomLeft.getX()) {
                world[X - 1][Y] = Tileset.FLOOR;
            } else if (X == bottomLeft.getX() + width - 1) {
                world[X + 1][Y] = Tileset.FLOOR;
            } else if (Y == bottomLeft.getY()) {
                world[X][Y - 1] = Tileset.FLOOR;
            } else if (Y == bottomLeft.getY() + height - 1) {
                world[X][Y + 1] = Tileset.FLOOR;
            }
        }
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
}
