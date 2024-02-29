package world.elementTypes;

import world.Chunk;
import world.Element;

public class Air extends Element {
    public Air(int x, int y, Chunk chunk) {
        super(x, y, new int[]{255, 255, 255, 255}, chunk);
    }
}
