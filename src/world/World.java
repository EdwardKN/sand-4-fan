package world;

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
        chunks.forEach(
                (coord, chunk) -> {
                    if (chunk.shouldStep) {
                        chunk.updateElements();
                    }
                    chunk.shiftShouldStepAndReset();
                }
        );
    }

    public static void createNewChunk(World world, int x, int y) {
        Chunk newChunk = new Chunk(x, y, world);
        world.chunks.put(x + "," + y, newChunk);
        newChunk.initializeChunk(world.perlin);
    }

    public static int getChunkCoord(int c) {
        return (int) ((c - (c < 0 ? -1 : 0)) / CHUNKSIZE) + (c < 0 ? -1 : 0);
    }

    public static Element getElementAtCell(World world, int x, int y) {
        int chunkX = getChunkCoord(x);
        int chunkY = getChunkCoord(y);

        int elementX = relativeElementCoordinate(x);
        int elementY = relativeElementCoordinate(y);

        int elementCoordinateValue = elementCoordinate(elementX, elementY);

        return world.chunks.get(chunkX + "," + chunkY).elements[elementCoordinateValue];
    }

}
