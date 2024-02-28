package world;

import java.awt.*;

import static world.Chunk.*;
import static world.Chunk.relativeElementCoordinate;
import static world.World.*;

public class Element {
    public int x, y, chunkX, chunkY, relativeX, relativeY;
    public int[] col;

    public Chunk chunk;

    public World world;

    int drawCol;

    public boolean movable = false;

    public Element(int x, int y, int[] col, Chunk chunk) {
        this.x = x;
        this.y = y;

        this.col = col;

        this.drawCol = (col[0] << 24) | (col[1] << 16) | (col[2] << 8) | col[3];

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


    public void step() {

    }
}

