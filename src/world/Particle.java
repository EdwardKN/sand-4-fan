package world;

import static main.Utils.convertToCol;
import static main.Utils.getChunkCoord;

public class Particle {

    double x, y, velX, velY, accY;
    int[] col;

    int drawX, drawY, oldDrawX, oldDrawY, drawCol;

    World world;

    public Particle(int x, int y, int[] col, double startVelX, double startVelY, double accY, World world) {
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
        }
    }
}
