package de.macbury.landbot.core.graphics.camera;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.macbury.landbot.core.graphics.camera.frustrum.DebugFrustrum;

/**
 * Created by macbury on 14.08.15.
 */
public interface ICamera {
  void saveDebugFrustrum();
  boolean haveDebugFrustrum();
  DebugFrustrum getDebugFrustrum();
  void clearDebugFrustrum();
  Frustum normalOrDebugFrustrum();
  boolean boundsInFrustum(BoundingBox testBounds);
}
