package de.macbury.landbot.core.procedular;

import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

public class PerlinNoise2D {
  private final Random random;
  private final long seed;

  public PerlinNoise2D(long seed) {
    this.random = new Random(seed);
    this.seed   = seed;
  }

  public float noise(int x, int z) {
    random.setSeed(x * 49632 + z * 325176 + seed);
    return random.nextFloat() * 2f - 1f;
  }

  public float smoothNoise(int x, int z) {
    float corners = (
      noise(x-1,z-1) +
      noise(x+1,z-1) +
      noise(x+1,z+1) +
      noise(x-1,z+1)
    ) / 16f;

    float sides = (
      noise(x-1,z) +
        noise(x,z-1) +
        noise(x+1,z) +
        noise(x,z+1)
    ) / 8f;

    float center = noise(x,z) / 4f;

    return corners + sides + center;
  }

  private float interpolate(float a, float b, float blend) {
    double theta = blend *Math.PI;
    float f = (float)(1f - Math.cos(theta)) * 0.5f;
    return a * (1f - f) + b * f;
  }

  public float interpolatedNoise(float x, float z) {
    int intX = (int)x;
    int intZ = (int)z;
    float fractX = x - intX;
    float fractZ = z - intZ;

    float v1 = smoothNoise(intX, intZ);
    float v2 = smoothNoise(intX+1, intZ);
    float v3 = smoothNoise(intX, intZ+1);
    float v4 = smoothNoise(intX+1, intZ+1);

    float i1 = interpolate(v1, v2, fractX);
    float i2 = interpolate(v3, v4, fractX);

    return interpolate(i1, i2, fractZ);
  }

  public float terrainNoise(float x, float z, float amplitude, int octaves, float roughness, float ix, float iz) {
    float total = 0;
    float d     = (float)Math.pow(2, octaves - 1);
    for (int i = 0; i < octaves; i++) {
      float freq = (float)(Math.pow(2,1) / d);
      float amp  = (float)(Math.pow(roughness, i) * amplitude);
      total += interpolatedNoise(x*freq*ix, z*freq* iz) * amp;
    }
    return MathUtils.clamp(total, -amplitude, amplitude);
  }
}
