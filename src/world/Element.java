package world;

import java.awt.*;

import static world.Chunk.*;
import static world.Chunk.relativeElementCoordinate;
import static world.World.*;

public class Element {
    int x, y, chunkX, chunkY, relativeX, relativeY;
    int[] col;

    Chunk chunk;

    World world;

    int drawCol;

    public Element(int x, int y, int[] col, Chunk chunk) {
        this.x = x;
        this.y = y;

        this.col = col;

        this.drawCol = (col[0] << 24) | (col[1] << 16) | (col[2] << 8) | col[3];

        this.chunk = chunk;

        this.world = chunk.world;

        calculateRelativeXY();
    }

    public void moveTo(int x, int y) {
        int newChunkX = getChunkCoord(x);
        int newChunkY = getChunkCoord(y);

        int elementX = relativeX;
        int elementY = relativeY;

        int newElementX = relativeElementCoordinate(x);
        int newElementY = relativeElementCoordinate(y);

        Chunk newChunk = world.chunks.get(newChunkX + "," + newChunkY);
        Chunk oldChunk = chunk;

        Element elementOnNewPos = newChunk.elements[elementCoordinate(newElementX, newElementY)];

        if (world.chunks.get(chunkX + "," + chunkY) == null) {
            createNewChunk(world, chunkX, chunkY);
        }
        if (world.chunks.get(newChunkX + "," + newChunkY) == null) {
            createNewChunk(world, newChunkX, newChunkY);
        }

        elementOnNewPos.x = x;
        elementOnNewPos.y = y;
        chunk.elements[elementCoordinate(elementX, elementY)] = elementOnNewPos;
        elementOnNewPos.calculateRelativeXY();

        this.x = x;
        this.y = y;

        newChunk.elements[elementCoordinate(newElementX, newElementY)] = this;

        newChunk.shouldStepNextFrame = true;
        oldChunk.shouldStepNextFrame = true;

        if (world.chunks.get((chunkX + 1) + "," + chunkY) == null) {
            createNewChunk(world, chunkX + 1, chunkY);
        }
        if (world.chunks.get((chunkX - 1) + "," + chunkY) == null) {
            createNewChunk(world, chunkX - 1, chunkY);
        }
        if (world.chunks.get(chunkX + "," + (chunkY + 1)) == null) {
            createNewChunk(world, chunkX, chunkY + 1);
        }
        if (world.chunks.get(chunkX + "," + (chunkY - 1)) == null) {
            createNewChunk(world, chunkX, chunkY - 1);
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

        chunk = chunk.world.chunks.get(chunkX + "," + chunkY);
    }


}

