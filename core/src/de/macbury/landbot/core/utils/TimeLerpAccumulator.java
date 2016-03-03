package de.macbury.landbot.core.utils;

/**
 * Just simple helper for calculating alpha for lerp
 */
public class TimeLerpAccumulator {
  private float accumulator;
  private float total;

  public void reset() {
    accumulator = 0;
  }

  public void start(float total) {
    reset();
    this.total = total;
  }

  public void update(float dt) {
    accumulator += dt;
    if (accumulator > total) {
      accumulator = total;
    }
  }

  public void finish() {
    this.accumulator = total;
  }

  public boolean isFinished() {
    return accumulator >= total;
  }

  public float getAlpha() {
    return Math.min(accumulator / total, 1.0f);
  }
}
