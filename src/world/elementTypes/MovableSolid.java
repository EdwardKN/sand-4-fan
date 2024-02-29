package world.elementTypes;

import world.Chunk;
import world.Element;

public class MovableSolid extends Solid {

    public MovableSolid(int x, int y, int[] col, Chunk chunk) {
        super(x, y, col, chunk);
        movable = true;
    }

    @Override
    public void step() {
        Element targetCell = world.getElementAtCell(x, y + 1);

        if (targetCell instanceof Air || targetCell instanceof Liquid) {
            lookVertically();
        } else {
            lookDiagonally((Math.random() * 2) > 0.5 ? -1 : 1, true);
        }
    }

    public void lookVertically() {
        int maxDir = 0;
        for (int i = 1; i < (int) velY + 1; i++) {
            Element targetCell = world.getElementAtCell(x, y + i);
            if (targetCell instanceof Air || targetCell instanceof Liquid) {
                maxDir = i;
            } else {
                break;
            }
        }
        if (maxDir != 0) {
            Element targetCell = world.getElementAtCell(x, y + maxDir);

            this.velY += accY;
            if (targetCell instanceof Liquid) this.velY = Math.min(this.velY, 1);

            this.moveTo(this.x, this.y + maxDir);
        }
    }

    public void lookDiagonally(int dir, boolean first) {
        Element targetCell = world.getElementAtCell(x + dir, y + 1);

        if (targetCell instanceof Air) {
            moveTo(this.x + dir, this.y + 1);
        } else if (first) {
            this.lookDiagonally(-dir, false);
        }
    }
}
