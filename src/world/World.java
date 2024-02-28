package world;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import perlin.PerlinGenerator;

import static world.Chunk.*;


public class World {

    public Map<String, Chunk> chunks = new Hashtable<>();

    static final int seed = 0;

    public PerlinGenerator perlin = new PerlinGenerator(seed);

    public void updateChunks() {
        Collection<Chunk> values = chunks.values();
        Chunk[] targetArray = values.toArray(new Chunk[0]);

        for (Chunk chunk : targetArray) {
            if (chunk.shouldStep) {
                chunk.updateElements();
            }
            chunk.shiftShouldStepAndReset();
        }
    }

    public void createNewChunk(int x, int y) {
        Chunk newChunk = new Chunk(x, y, this);
        chunks.put(x + "," + y, newChunk);
        newChunk.initializeChunk(perlin);
    }

    public static int getChunkCoord(int c) {
        return (int) ((c - (c < 0 ? -1 : 0)) / CHUNKSIZE) + (c < 0 ? -1 : 0);
    }

    public Element getElementAtCell(int x, int y) {
        int chunkX = getChunkCoord(x);
        int chunkY = getChunkCoord(y);

        int elementX = relativeElementCoordinate(x);
        int elementY = relativeElementCoordinate(y);

        int elementCoordinateValue = elementCoordinate(elementX, elementY);

        return chunks.get(chunkX + "," + chunkY).elements[elementCoordinateValue];
    }

}
