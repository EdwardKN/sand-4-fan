package main;

import world.Element;

import java.util.Random;

import static world.Chunk.CHUNKSIZE;

public class Utils {
    public static int getChunkCoord(int c) {
        return ((c - (c < 0 ? -1 : 0)) / CHUNKSIZE) + (c < 0 ? -1 : 0);
    }

    public static int elementCoordinate(int x, int y) {
        return y * CHUNKSIZE + x;
    }

    public static int relativeElementCoordinate(int c) {
        return ((c % CHUNKSIZE) + CHUNKSIZE) % CHUNKSIZE;
    }

    public static void shuffleArray(Element[] array) {
        int index;
        Element temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
