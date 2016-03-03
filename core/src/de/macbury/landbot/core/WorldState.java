package de.macbury.landbot.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.World;
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
public class WorldState implements Disposable {
  private static final String TAG = "WorldState";
  /**
   * Pixels per meter
   */
  public final static float PPM           = 5;
  public EntityManager entities;
  public OrthographicCamera camera;
  public World world;
  public OrthographicCamera b2dCamera;

  public WorldState(LandBot game) {
    this.camera               = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    this.b2dCamera            = new OrthographicCamera(Gdx.graphics.getWidth()/ PPM, Gdx.graphics.getHeight()/PPM);
    this.world                = new World(new Vector2(0, -9.8f), true);
    this.entities             = new EntityManager(this, game);
    Gdx.app.log(TAG, "Initialized");

    camera.zoom = b2dCamera.zoom = 6.0f;
  }

  public void render(float delta) {
    camera.update();
    b2dCamera.update();
    entities.update(delta);
  }

  @Override
  public void dispose() {
    Gdx.app.log(TAG, "Disposing");
    world.dispose();
    camera  = null;
  }

  public void resize(int width, int height) {
    //this.camera.setToOrtho(false, width, height);
  }

  public static class Blueprint {
    public int maxElevation;
    public long seed;
    public int width;
    public int height;
  }
}
