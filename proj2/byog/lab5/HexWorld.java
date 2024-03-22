package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static void addBackground(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static void addHexagon(TETile[][] tiles, int w, int h, int n) {
        int tileNum = RANDOM.nextInt(11);
        for (int i = 0; i < n; i++) {
            for (int j = -i; j < n + i; j++) {
                tiles[w + j][h + i] = randomTile(tileNum);
                tiles[w + j][2 * n - i - 1 + h] = randomTile(tileNum);
            }
        }
    }

    private static TETile randomTile(int tileNum) {
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.FLOOR;
            case 5: return Tileset.LOCKED_DOOR;
            case 6: return Tileset.PLAYER;
            case 7: return Tileset.SAND;
            case 8: return Tileset.TREE;
            case 9: return Tileset.UNLOCKED_DOOR;
            case 10: return Tileset.WATER;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] worldTiles = new TETile[WIDTH][HEIGHT];

        int x = 25, y = 10;
        addBackground(worldTiles);
        addHexagon(worldTiles, x, y, 3);
        addHexagon(worldTiles, x, y + 24, 3);
        for (int i = 3; i < 24; i += 3) {
            if (i % 6 != 0) {
                addHexagon(worldTiles, x + 5, y + i, 3);
                addHexagon(worldTiles, x - 5, y + i, 3);
            } else {
                addHexagon(worldTiles, x - 10, y + i, 3);
                addHexagon(worldTiles, x, y + i, 3);
                addHexagon(worldTiles, x + 10, y + i, 3);
            }
        }
        ter.renderFrame(worldTiles);
    }
}
