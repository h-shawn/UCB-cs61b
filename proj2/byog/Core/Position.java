package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Position implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int X;
    private int Y;

    public Position(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void playerUp(TETile[][] world) {
        if (world[X][Y + 1].equals(Tileset.FLOOR)) {
            world[X][Y] = Tileset.FLOOR;
            Y = Y + 1;
            world[X][Y] = Tileset.PLAYER;
        }
    }

    public void playerDown(TETile[][] world) {
        if (world[X][Y - 1].equals(Tileset.FLOOR)) {
            world[X][Y] = Tileset.FLOOR;
            Y = Y - 1;
            world[X][Y] = Tileset.PLAYER;
        }
    }

    public void playerLeft(TETile[][] world) {
        if (world[X - 1][Y].equals(Tileset.FLOOR)) {
            world[X][Y] = Tileset.FLOOR;
            X = X - 1;
            world[X][Y] = Tileset.PLAYER;
        }
    }

    public void playerRight(TETile[][] world) {
        if (world[X + 1][Y].equals(Tileset.FLOOR)) {
            world[X][Y] = Tileset.FLOOR;
            X = X + 1;
            world[X][Y] = Tileset.PLAYER;
        }
    }

    public void spread(Stack<Position> stack, TETile[][] world, Random r) {
        int worldWidth = world.length;
        int worldHeight = world[0].length;

        Position[] candidates = new Position[4];

        if (X + 2 < worldWidth && world[X + 2][Y].equals(Tileset.SAND)) {
            world[X + 1][Y] = Tileset.FLOOR;
            world[X + 2][Y] = Tileset.FLOOR;
            candidates[0] = new Position(X + 2, Y);
        }
        if (X - 2 >= 0 && world[X - 2][Y].equals(Tileset.SAND)) {
            world[X - 1][Y] = Tileset.FLOOR;
            world[X - 2][Y] = Tileset.FLOOR;
            candidates[1] = new Position(X - 2, Y);
        }
        if (Y + 2 < worldHeight && world[X][Y + 2].equals(Tileset.SAND)) {
            world[X][Y + 1] = Tileset.FLOOR;
            world[X][Y + 2] = Tileset.FLOOR;
            candidates[2] = new Position(X, Y + 2);
        }
        if (Y - 2 >= 0 && world[X][Y - 2].equals(Tileset.SAND)) {
            world[X][Y - 1] = Tileset.FLOOR;
            world[X][Y - 2] = Tileset.FLOOR;
            candidates[3] = new Position(X, Y - 2);
        }

        RandomUtils.shuffle(r, candidates);
        for (int i = 0; i < 4; i++) {
            if (candidates[i] != null) {
                stack.push(candidates[i]);
            }
        }
    }

    public boolean isDeadHead(TETile[][] world) {
        if (!world[X][Y].equals(Tileset.FLOOR)) {
            return false;
        }

        List<Position> posList = getAroundPos(world, false);
        int num = 0;
        for (Position p : posList) {
            if (world[p.getX()][p.getY()].equals(Tileset.NOTHING)) {
                num++;
            }
        }
        return num == posList.size() - 1;
    }

    public boolean isWall(TETile[][] world) {
        if (world[X][Y].equals(Tileset.FLOOR)) {
            return false;
        }
        for (Position pos : getAroundPos(world, true)) {
            if (world[pos.getX()][pos.getY()].equals(Tileset.FLOOR)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDoorCandidate(TETile[][] world) {
        int worldWidth = world.length;
        int worldHeight = world[0].length;

        if (!world[X][Y].equals(Tileset.WALL)) {
            return false;
        }

        if (X - 1 >= 0 && X + 1 < worldWidth) {
            boolean flag1 = world[X - 1][Y].equals(Tileset.FLOOR) && world[X + 1][Y].equals(Tileset.NOTHING);
            boolean flag2 = world[X + 1][Y].equals(Tileset.FLOOR) && world[X - 1][Y].equals(Tileset.NOTHING);
            return flag1 || flag2;
        }
        if (Y - 1 >= 0 && Y + 1 < worldHeight) {
            boolean flag1 = world[X][Y - 1].equals(Tileset.FLOOR) && world[X][Y + 1].equals(Tileset.NOTHING);
            boolean flag2 = world[X][Y + 1].equals(Tileset.FLOOR) && world[X][Y - 1].equals(Tileset.NOTHING);
            return flag1 || flag2;
        }
        return false;
    }


    public List<Position> getAroundPos(TETile[][] world, Boolean isAll) {
        int worldWidth = world.length;
        int worldHeight = world[0].length;
        List<Position> posList = new ArrayList<>();
        if (X + 1 < worldWidth) {
            posList.add(new Position(X + 1, Y));
        }
        if (X - 1 >= 0) {
            posList.add(new Position(X - 1, Y));
        }
        if (Y + 1 < worldHeight) {
            posList.add(new Position(X, Y + 1));
        }
        if (Y - 1 >= 0) {
            posList.add(new Position(X, Y - 1));
        }
        if (isAll) {
            if (X + 1 < worldWidth && Y + 1 < worldHeight) {
                posList.add(new Position(X + 1, Y + 1));
            }
            if (X - 1 >= 0 && Y + 1 < worldHeight) {
                posList.add(new Position(X - 1, Y + 1));
            }
            if (X - 1 >= 0 && Y - 1 >= 0) {
                posList.add(new Position(X - 1, Y - 1));
            }
            if (X + 1 < worldWidth && Y - 1 >= 0) {
                posList.add(new Position(X + 1, Y - 1));
            }
        }
        return posList;
    }
}
