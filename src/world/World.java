package world;

import java.util.Dictionary;
import java.util.Hashtable;

import perlin.PerlinGenerator;

import static world.Chunk.CHUNKSIZE;


public class World {

    public Dictionary<String, Chunk> chunks = new Hashtable<>();

    static final int seed = 0;

    public PerlinGenerator perlin = new PerlinGenerator(seed);

    public void initializeWorld() {
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 10; y++) {
                createNewChunk(this, x, y);
            }
        }
    }

    public static void createNewChunk(World world, int x, int y) {
        Chunk newChunk = new Chunk(x, y, world);
        world.chunks.put(x + "," + y, newChunk);
        newChunk.initializeChunk(world.perlin);
    }

    public static int getChunkCoord(int c) {
        return (int) ((c - (c < 0 ? -1 : 0)) / CHUNKSIZE) + (c < 0 ? -1 : 0);
    }

}
