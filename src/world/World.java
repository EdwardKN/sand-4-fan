package world;

import java.util.*;
import java.util.stream.Stream;

import entity.Player;
import perlin.PerlinGenerator;

import static main.GamePanel.*;
import static main.Utils.*;


public class World {

    public Map<String, Chunk> chunks = new Hashtable<>();

    ArrayList<Particle> particles = new ArrayList<Particle>();
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
        Particle[] filteredParticles = particles.stream().filter(e -> detectCollision(e.x, e.y, 1, 1, player.x, player.y, STANDARDX * RENDERSCALE, STANDARDY * RENDERSCALE)).toArray(Particle[]::new);

        for (Particle particle : filteredParticles) {
            particle.updatePos();
        }

        Particle[] otherParticles = particles.stream().filter(e -> !detectCollision(e.x, e.y, 1, 1, player.x, player.y, STANDARDX * RENDERSCALE, STANDARDY * RENDERSCALE)).toArray(Particle[]::new);

        for (Particle particle : otherParticles) {
            particle.convertToElement();
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

        Chunk chunk = chunks.get(chunkX + "," + chunkY);

        if (chunk == null) {
            createNewChunk(chunkX, chunkY);
            return null;
        }

        return chunk.elements[elementCoordinateValue];
    }

}
