package world;

import perlin.PerlinGenerator;
import world.elementTypes.Air;
import world.elementTypes.Solid;

import java.awt.image.BufferedImage;
import java.util.*;

import static main.Utils.elementCoordinate;
import static main.Utils.shuffleArray;

public class Chunk {
    public int x, y;

    public World world;

    public boolean shouldStep = true;
    public boolean shouldStepNextFrame = false;

    public boolean hasUpdatedSinceImageBufferChange = true;

    public static final int CHUNKSIZE = 32;

    public BufferedImage imageBuffer = new BufferedImage(CHUNKSIZE, CHUNKSIZE, BufferedImage.TYPE_INT_ARGB);
    public Element[] elements = new Element[CHUNKSIZE * CHUNKSIZE];

    public Chunk(int x, int y, World world) {
        this.x = x;
        this.y = y;
        this.world = world;
    }

    public void updateImageBuffer() {
        hasUpdatedSinceImageBufferChange = false;
        for (int x = 0; x < CHUNKSIZE; x++) {
            for (int y = 0; y < CHUNKSIZE; y++) {
                imageBuffer.setRGB(x, y, elements[elementCoordinate(x, y)].drawCol);
            }
        }
    }

    public void initializeChunk(PerlinGenerator perlin) {
        for (int x = 0; x < CHUNKSIZE; x++) {
            for (int y = 0; y < CHUNKSIZE; y++) {
                double noise = perlin.getLayeredPerlinNoise(this.x * CHUNKSIZE + x, this.y * CHUNKSIZE + y, new int[]{100, 40}, new int[]{5, 1});
                if (noise > 0.5) {
                    elements[elementCoordinate(x, y)] = new Air(x, y, new int[]{255, 255, 255, 255}, this);
                } else {
                    elements[elementCoordinate(x, y)] = new Solid(x, y, new int[]{255, 0, 0, 0}, this);
                }
            }
        }

    }

    public void updateElements() {
        hasUpdatedSinceImageBufferChange = true;

        Element[] shuffledElements = Arrays.copyOf(elements, elements.length);
        shuffleArray(shuffledElements);


        for (int i = 0; i < Math.pow(CHUNKSIZE, 2); i++) {
            if (shuffledElements[i].movable) {
                shuffledElements[i].step();
            }
        }

    }

    public void shiftShouldStepAndReset() {
        shouldStep = shouldStepNextFrame;
        shouldStepNextFrame = false;
    }


}

