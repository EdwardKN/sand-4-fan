package element;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Chunk {
    public int x, y;

    public boolean shouldStep = true;
    public boolean shouldStepNextFrame = false;

    public boolean hasUpdatedSinceImageBufferChange = true;

    static final int CHUNKSIZE = 32;

    public BufferedImage imageBuffer = new BufferedImage(CHUNKSIZE, CHUNKSIZE, BufferedImage.TYPE_INT_ARGB);
    public Element[] elements = new Element[CHUNKSIZE * CHUNKSIZE];

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updateImageBuffer() {
        hasUpdatedSinceImageBufferChange = false;
        for (int x = 0; x < CHUNKSIZE; x++) {
            for (int y = 0; y < CHUNKSIZE; y++) {
                imageBuffer.setRGB(x, y, elements[elementCoordinate(x, y)].drawCol);
            }
        }
    }

    public void initializeChunk() {
        for (int x = 0; x < CHUNKSIZE; x++) {
            for (int y = 0; y < CHUNKSIZE; y++) {
                elements[elementCoordinate(x, y)] = new Element(x, y, new int[]{255, (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)});
            }
        }

    }

    public int elementCoordinate(int x, int y) {
        return y * CHUNKSIZE + x;
    }

}

