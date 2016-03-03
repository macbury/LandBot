package de.macbury.landbot.core.graphics;

import com.badlogic.gdx.math.Vector3;

/**
 * This class describes all level of details
 */
public enum Lod {
  /**
   * For normal game rendering
   */
  High(1),
  /**
   * For reflections and effects
   */
  Low(4)
  ;
  private final static Vector3 tempPos = new Vector3();
  public final int resolution;
  Lod(int resolution) {
    this.resolution = resolution;
  }

  /**
   * Return lod dependent on distance from camera
   * @param camera
   * @param position
   * @return
   */
  /*public static Lod by(GameCamera camera, Vector3 position) {
    float distance = tempPos.set(camera.normalOrDebugPosition()).dst(position) / camera.far;

    if (distance >= 0.80f) {
      return UltraLow;
    } else if (distance >= 0.70f) {
      return Low;
    } else if (distance >= 0.60f) {
      return Medium;
    } else {
      return byDebugButton();
    }
  }

  public static Lod byDebugButton() {
    if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
      return UltraLow;
    } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
      return Low;
    } if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
      return Medium;
    }
    return High;
  }*/
}
