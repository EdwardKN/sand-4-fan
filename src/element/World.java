package element;

import java.util.Dictionary;
import java.util.Hashtable;

public class World {

    public Dictionary<String, Chunk> chunks = new Hashtable<>();


    public void initializeWorld() {
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 10; y++) {
                Chunk newChunk = new Chunk(x, y);
                chunks.put(x + "," + y, newChunk);
                newChunk.initializeChunk();
            }
        }

    }
}
