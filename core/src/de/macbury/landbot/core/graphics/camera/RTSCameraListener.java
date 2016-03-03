package de.macbury.landbot.core.graphics.camera;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Created by macbury on 20.10.14.
 */
public interface RTSCameraListener {
  public BoundingBox getCameraBounds(BoundingBox out);
  public float getCameraElevation(RTSCameraController cameraController, Vector3 cameraPosition);
}
