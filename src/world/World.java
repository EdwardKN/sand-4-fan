package world;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import perlin.PerlinGenerator;

import static main.Utils.*;


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


    public Element getElementAtCell(int x, int y) {
        int chunkX = getChunkCoord(x);
        int chunkY = getChunkCoord(y);

        int elementX = relativeElementCoordinate(x);
        int elementY = relativeElementCoordinate(y);

        int elementCoordinateValue = elementCoordinate(elementX, elementY);

        return chunks.get(chunkX + "," + chunkY).elements[elementCoordinateValue];
    }

}
