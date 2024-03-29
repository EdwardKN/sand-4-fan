package world;

import perlin.PerlinGenerator;
import world.elementTypes.Air;
import world.elementTypes.Solid;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Stream;

import static main.Utils.*;

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
        Particle[] filteredParticles = world.particles.stream().filter(e -> detectCollision(e.drawX, e.drawY, 1, 1, x * CHUNKSIZE, y * CHUNKSIZE, CHUNKSIZE, CHUNKSIZE)).toArray(Particle[]::new);

        for (Particle particle : filteredParticles) {

            int particleX = relativeElementCoordinate(particle.drawX);
            int particleY = relativeElementCoordinate(particle.drawY);

            imageBuffer.setRGB(particleX, particleY, particle.drawCol);

        }
    }

    public void initializeChunk(PerlinGenerator perlin) {
        for (int x = 0; x < CHUNKSIZE; x++) {
            for (int y = 0; y < CHUNKSIZE; y++) {
                double noise = perlin.getLayeredPerlinNoise(this.x * CHUNKSIZE + x, this.y * CHUNKSIZE + y, new int[]{100, 40}, new int[]{5, 1});
                if (noise > 0.5) {
                    elements[elementCoordinate(x, y)] = new Air(x, y, this);
                } else {
                    elements[elementCoordinate(x, y)] = new Solid(x, y, new int[]{255, 0, 0, 0}, this);
                }
            }
        }

    }

    public void updateElements() {
        hasUpdatedSinceImageBufferChange = true;

        Element[] elementsCopy = Arrays.copyOf(elements, elements.length);

        Element[] filteredElements = Stream.of(elementsCopy).filter(e -> e.movable).toArray(Element[]::new);
        shuffleArray(filteredElements);

        for (Element element : filteredElements) {
            if (element.movable) {
                element.step();
            }
        }

    }

    public void shiftShouldStepAndReset() {
        shouldStep = shouldStepNextFrame;
        shouldStepNextFrame = false;
    }


}

