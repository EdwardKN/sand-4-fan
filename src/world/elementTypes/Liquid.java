package world.elementTypes;

import world.Chunk;
import world.Element;

public class Liquid extends Element {

    public double flowChance = 1;
    public int dispersionRate = 5;

    public Liquid(int x, int y, int[] col, Chunk chunk) {
        super(x, y, col, chunk);
        movable = true;
    }

    @Override
    public void step() {
        Element targetCell = world.getElementAtCell(x, y + 1);

        if (targetCell instanceof Air) {
            lookVertically();
        } else if (Math.random() < flowChance) {
            lookHorizontally((Math.random() * 2) > 0.5 ? -1 : 1, true);
        }
    }

    public void lookVertically() {
        int maxDir = 0;
        for (int i = 1; i < (int) velY + 1; i++) {
            Element targetCell = world.getElementAtCell(x, y + i);
            if (targetCell instanceof Air) {
                maxDir = i;
            } else {
                break;
            }
        }
        if (maxDir != 0) {
            this.velY += accY;

            this.moveTo(this.x, this.y + maxDir);
        }
    }

    public void lookHorizontally(int dir, boolean first) {
        int maxLeft = 0;
        int maxRight = 0;
        boolean leftMaxed = false;
        boolean rightMaxed = false;
        int maxAmount = dispersionRate;
        for (int i = 1; i < maxAmount; i++) {
            Element targetCell1 = world.getElementAtCell(x + i, y);
            Element targetCell2 = world.getElementAtCell(x - i, y);
            if (!rightMaxed) {
                if (targetCell1 instanceof Air) {
                    maxRight = i;
                } else {
                    rightMaxed = true;
                }
            }
            if (!leftMaxed) {
                if (targetCell2 instanceof Air) {
                    maxLeft = i;
                } else {
                    leftMaxed = true;
                }
            }
            if (leftMaxed && rightMaxed) {
                break;
            }
        }
        if (maxLeft != 0 || maxRight != 0) {
            if (maxLeft > maxRight) {
                moveTo(x - maxLeft, y);
            } else if (maxRight > maxLeft) {
                moveTo(x + maxRight, y);
            } else {
                moveTo(x + maxRight * ((Math.random() * 2) > 0.5 ? -1 : 1), y);
            }
        }
    }
}
