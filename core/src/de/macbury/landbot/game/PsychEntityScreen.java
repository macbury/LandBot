package de.macbury.landbot.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.components.BodyComponent;
import de.macbury.landbot.core.entities.components.PositionComponent;
import de.macbury.landbot.core.entities.systems.PhysicsSystem;
import de.macbury.landbot.core.graphics.framebuffer.Fbo;
import de.macbury.landbot.core.screens.ScreenBase;

/**
 * Created on 03.03.16.
 */
public class PsychEntityScreen extends ScreenBase {
  private WorldState worldState;
  private OrthographicCamera b2dCamera;
  private Box2DDebugRenderer b2dr;

  @Override
  public void preload() {

  }

  @Override
  public void create() {
    this.worldState = new WorldState(this.game);
    this.b2dr       = new Box2DDebugRenderer();
    this.b2dCamera  = new OrthographicCamera(Gdx.graphics.getWidth()/ PhysicsSystem.PPM, Gdx.graphics.getHeight()/PhysicsSystem.PPM);

    Entity boxEntity                    = worldState.entities.createEntity();
    PositionComponent positionComponent = worldState.entities.createComponent(PositionComponent.class);
    BodyComponent bodyComponent         = worldState.entities.createComponent(BodyComponent.class);
    boxEntity.add(positionComponent);
    boxEntity.add(bodyComponent);
    worldState.entities.addEntity(boxEntity);
  }

  @Override
  public void render(float delta) {
    worldState.render(delta);
    b2dCamera.update();

    fb.begin(Fbo.FinalResult); {
      Gdx.gl.glClearColor(0,0,0,1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      b2dr.render(worldState.world, b2dCamera.combined);
    } fb.end();
  }

  @Override
  public void resize(int width, int height) {
    worldState.resize(width, height);
  }

  @Override
  public void dispose() {
    worldState.dispose();
  }
}
