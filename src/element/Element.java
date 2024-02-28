package element;

import java.awt.*;

public class Element {
    int x, y;
    int[] col;

    int drawCol;

    public Element(int x, int y, int[] col) {
        this.x = x;
        this.y = y;

        this.col = col;

        this.drawCol = (col[0] << 24) | (col[1] << 16) | (col[2] << 8) | col[3];

    }


}

