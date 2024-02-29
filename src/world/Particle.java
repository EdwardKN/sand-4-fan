package world;

import world.elementTypes.Air;

import java.util.Arrays;
import java.util.stream.Stream;

import static main.Utils.*;
import static world.Chunk.CHUNKSIZE;

public class Particle {

    double x, y, velX, velY, accY;
    int[] col;

    int drawX, drawY, oldDrawX, oldDrawY, drawCol;

    Element element;

    World world;

    public Particle(int x, int y, int[] col, double startVelX, double startVelY, double accY, Element element, World world) {
        this.x = x;
        this.y = y;
        this.drawX = x;
        this.drawY = y;
        this.col = new int[]{255, 200, 200, 200};

        this.velX = startVelX;
        this.velY = startVelY;

        this.accY = accY;

        this.drawCol = convertToCol(col);

        this.world = world;

        this.element = element;
    }

    public void updatePos() {
        velY += accY;

        x += velX;
        y += velY;

        drawX = (int) x;
        drawY = (int) y;

        if (drawX != oldDrawX || drawY != oldDrawY) {
            oldDrawX = drawX;
            oldDrawY = drawY;

            int chunkX = getChunkCoord(drawX);
            int chunkY = getChunkCoord(drawY);

            Chunk chunk = world.chunks.get(chunkX + "," + chunkY);

            chunk.hasUpdatedSinceImageBufferChange = true;

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

            if (!(world.getElementAtCell(drawX, drawY) instanceof Air)) {
                x -= this.velX;
                y -= this.velY;
                drawX = (int) x;
                drawY = (int) y;

                while (!(world.getElementAtCell(drawX, drawY) instanceof Air)) {
                    y--;
                    drawY = (int) y;
                }

                convertToElement();
            }
        }
    }

    public void convertToElement() {
        int chunkX = getChunkCoord(drawX);
        int chunkY = getChunkCoord(drawY);

        int elementX = relativeElementCoordinate(drawX);
        int elementY = relativeElementCoordinate(drawY);

        element.x = drawX;
        element.y = drawY;

        element.calculateRelativeXY();

        Chunk chunk = world.chunks.get(chunkX + "," + chunkY);

        chunk.hasUpdatedSinceImageBufferChange = true;
        chunk.shouldStepNextFrame = true;

        chunk.elements[elementCoordinate(elementX, elementY)] = element;

        world.particles.remove(this);
    }
}
