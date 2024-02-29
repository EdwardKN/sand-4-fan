package world;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Stream;

import entity.Player;
import perlin.PerlinGenerator;

import static main.GamePanel.*;
import static main.Utils.*;


public class World {

    public Map<String, Chunk> chunks = new Hashtable<>();

    public Particle[] particles = new Particle[0];

    Player player;

    static final int seed = 0;

    public PerlinGenerator perlin = new PerlinGenerator(seed);

    public World(Player player) {
        this.player = player;
    }

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

    public void updateParticles() {
        Particle[] filteredParticles = Stream.of(particles).filter(e -> detectCollision(e.x, e.y, 1, 1, player.x, player.y, STANDARDX * RENDERSCALE, STANDARDY * RENDERSCALE)).toArray(Particle[]::new);

        for (Particle particle : filteredParticles) {
            particle.updatePos();
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
