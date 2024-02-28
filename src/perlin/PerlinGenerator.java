package perlin;

import com.flowpowered.noise.NoiseQuality;
import com.flowpowered.noise.module.source.Perlin;

import java.util.stream.IntStream;

public class PerlinGenerator {

    int seed = 0;

    public PerlinGenerator(int seed) {
        this.seed = seed;
    }

    public Perlin initPerlin(int seed, int resolution) {
        Perlin perlin = new Perlin();
        perlin.setSeed(seed);
        perlin.setFrequency((double) 1 / resolution);
        perlin.setLacunarity(0.05);
        perlin.setNoiseQuality(NoiseQuality.BEST);
        perlin.setOctaveCount(5);
        perlin.setPersistence(0.01);

        return perlin;
    }

    public double getPerlinNoise(int x, int y, int seed, int resolution) {

        Perlin perlin = initPerlin(seed, resolution);

        double value = perlin.getValue(x, y, 1);
        value++;
        value /= 2;

        return value;
    }

    public double getLayeredPerlinNoise(int x, int y, int[] resolutions, int[] weights) {
        int amount = resolutions.length;
        double value = 0;

        for (int i = 0; i < amount; i++) {
            value += getPerlinNoise(x, y, this.seed * (i + 1), resolutions[i]) * weights[i];
        }
        value /= IntStream.of(weights).sum();
        return value;
    }
}
