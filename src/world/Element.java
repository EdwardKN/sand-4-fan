package world;

import world.elementTypes.Air;

import java.util.Arrays;

import static main.Utils.*;

public class Element {
    public int x, y, chunkX, chunkY, relativeX, relativeY;
    public int[] col;

    public Chunk chunk;

    public World world;

    int drawCol;

    public boolean movable = false;

    public double velY = 1;

    public double accY = 0.2;

    public Element(int x, int y, int[] col, Chunk chunk) {
        this.x = x;
        this.y = y;

        this.col = col;

        this.drawCol = convertToCol(col);

        this.chunk = chunk;

        this.world = chunk.world;

        calculateRelativeXY();
    }

    public void moveTo(int newX, int newY) {
        int newChunkX = getChunkCoord(newX);
        int newChunkY = getChunkCoord(newY);

        int elementX = relativeX;
        int elementY = relativeY;

        int newElementX = relativeElementCoordinate(newX);
        int newElementY = relativeElementCoordinate(newY);

        Chunk newChunk = world.chunks.get(newChunkX + "," + newChunkY);
        Chunk oldChunk = chunk;

        Element elementOnNewPos = newChunk.elements[elementCoordinate(newElementX, newElementY)];

        if (world.chunks.get(chunkX + "," + chunkY) == null) {
            world.createNewChunk(chunkX, chunkY);
        }
        if (world.chunks.get(newChunkX + "," + newChunkY) == null) {
            world.createNewChunk(newChunkX, newChunkY);
        }

        elementOnNewPos.x = x;
        elementOnNewPos.y = y;
        oldChunk.elements[elementCoordinate(elementX, elementY)] = elementOnNewPos;
        elementOnNewPos.calculateRelativeXY();

        x = newX;
        y = newY;

        newChunk.elements[elementCoordinate(newElementX, newElementY)] = this;
        calculateRelativeXY();

        newChunk.shouldStepNextFrame = true;
        oldChunk.shouldStepNextFrame = true;

        if (world.chunks.get((chunkX + 1) + "," + chunkY) == null) {
            world.createNewChunk(chunkX + 1, chunkY);
        }
        if (world.chunks.get((chunkX - 1) + "," + chunkY) == null) {
            world.createNewChunk(chunkX - 1, chunkY);
        }
        if (world.chunks.get(chunkX + "," + (chunkY + 1)) == null) {
            world.createNewChunk(chunkX, chunkY + 1);
        }
        if (world.chunks.get(chunkX + "," + (chunkY - 1)) == null) {
            world.createNewChunk(chunkX, chunkY - 1);
        }
        world.chunks.get((chunkX + 1) + "," + chunkY).shouldStepNextFrame = true;
        world.chunks.get((chunkX - 1) + "," + chunkY).shouldStepNextFrame = true;
        world.chunks.get(chunkX + "," + (chunkY + 1)).shouldStepNextFrame = true;
        world.chunks.get(chunkX + "," + (chunkY - 1)).shouldStepNextFrame = true;

    }

    public void calculateRelativeXY() {
        relativeX = relativeElementCoordinate(x);
        relativeY = relativeElementCoordinate(y);

        chunkX = getChunkCoord(x);
        chunkY = getChunkCoord(y);

        chunk = world.chunks.get(chunkX + "," + chunkY);
    }

    public void convertToParticle(double velX, double velY) {
        if (chunk != null) {
            world.particles.add(new Particle(x, y, col, velX, velY, accY, this, world));
            chunk.elements[elementCoordinate(relativeX, relativeY)] = new Air(x, y, chunk);
        }
    }


    public void step() {

    }
}

