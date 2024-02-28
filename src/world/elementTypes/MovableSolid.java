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

        if (targetCell instanceof Air) {
            moveTo(x, y + 1);
        } else {
            lookDiagonally((Math.random() * 2) > 0.5 ? -1 : 1, true);
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
