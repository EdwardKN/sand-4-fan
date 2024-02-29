package main;

import world.Element;

import java.util.Arrays;
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

    public static int convertToCol(int[] col) {
        return (col[0] << 24) | (col[1] << 16) | (col[2] << 8) | col[3];
    }

    public static <T> void shuffleArray(T[] array) {
        int index;
        T temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static <T> T[] push(T[] arr, T item) {
        T[] tmp = Arrays.copyOf(arr, arr.length + 1);
        tmp[tmp.length - 1] = item;
        return tmp;
    }

    public static boolean detectCollision(double x, double y, double w, double h, double x2, double y2, double w2, double h2) {
        double[] convertedR1 = rectangleConverter(x, y, w, h);
        double[] convertedR2 = rectangleConverter(x2, y2, w2, h2);

        x = convertedR1[0];
        y = convertedR1[1];
        w = convertedR1[2];
        h = convertedR1[3];
        x2 = convertedR2[0];
        y2 = convertedR2[1];
        w2 = convertedR2[2];
        h2 = convertedR2[3];
        return x + w > x2 && x < x2 + w2 && y + h > y2 && y < y2 + h2;
    }


    public static double[] rectangleConverter(double x, double y, double w, double h) {
        if (w < 0) {
            x += w;
            w = Math.abs(w);
        }
        if (h < 0) {
            y += h;
            h = Math.abs(h);
        }
        return new double[]{x, y, w, h};
    }
}
