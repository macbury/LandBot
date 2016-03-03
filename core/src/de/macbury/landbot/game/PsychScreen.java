package de.macbury.landbot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.macbury.landbot.core.graphics.framebuffer.Fbo;
import de.macbury.landbot.core.screens.ScreenBase;

/**
 * Created on 29.02.16.
 */
public class PsychScreen extends ScreenBase {
  private World world;
  private Box2DDebugRenderer debugRenderer;
  private OrthographicCamera camera;
  private final static int VIEWPORT_WIDTH = 6;
  private final static int VIEWPORT_HEIGHT = 5;
  @Override
  public void preload() {

  }

  @Override
  public void create() {
    this.camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    camera.position.set(VIEWPORT_WIDTH/2, VIEWPORT_HEIGHT/2, 0);
    this.world = new World(new Vector2(0, 0), true);
    this.debugRenderer = new Box2DDebugRenderer();
    // Create our body definition
    BodyDef groundBodyDef =new BodyDef();
    // Set its world position
    groundBodyDef.position.set(new Vector2(0, 10));

// Create a body from the defintion and add it to the world
    Body groundBody = world.createBody(groundBodyDef);

// Create a polygon shape
    PolygonShape groundBox = new PolygonShape();
// Set the polygon shape as a box which is twice the size of our view port and 20 high
// (setAsBox takes half-width and half-height as arguments)
    groundBox.setAsBox(2, 2);
// Create a fixture from our polygon shape and add it to our ground body
    groundBody.createFixture(groundBox, 0.0f);
// Clean up after ourselves
    groundBox.dispose();
  }

  @Override
  public void render(float delta) {
    camera.update();
    world.step(1/60f, 6, 2);

    fb.begin(Fbo.FinalResult); {
      Gdx.gl.glClearColor(0,0,0,1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      debugRenderer.render(world, camera.combined);
    } fb.end();
  }

  @Override
  public void resize(int width, int height) {
    camera.viewportHeight = (VIEWPORT_WIDTH / width) * height;
    camera.update();
  }

  @Override
  public void dispose() {
    world.dispose();
  }
}
