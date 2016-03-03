package de.macbury.landbot.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.entities.EntityManager;
import de.macbury.landbot.core.entities.components.PositionComponent;
import de.macbury.landbot.core.graphics.LodModelBatch;
import de.macbury.landbot.core.graphics.camera.GameCamera;
import de.macbury.landbot.core.graphics.camera.RTSCameraController;

/**
 * This class describes all game world
 */
public class World implements Disposable {
  private static final String TAG = "World";
  public EntityManager entities;
  public RTSCameraController rtsCameraController;
  public GameCamera camera;

  public World(LandBot game) {
    this.camera               = new GameCamera();
    this.entities             = new EntityManager(this, game);

    Gdx.app.log(TAG, "Initialized");
  }

  public void render(float delta) {
    rtsCameraController.update(delta);
    entities.update(delta);
  }

  @Override
  public void dispose() {
    Gdx.app.log(TAG, "Disposing");
    rtsCameraController.dispose();
    camera  = null;
    rtsCameraController = null;
  }

  public void resize(int width, int height) {

  }

  public static class Blueprint {
    public int maxElevation;
    public long seed;
    public int width;
    public int height;
  }
}
