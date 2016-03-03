package de.macbury.landbot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
  private OrthographicCamera b2dCamera;
  private Box2DDebugRenderer b2dr;
  private OrthographicCamera gameCamera;
  private final static float PPM           = 20;

  private Body body;

  @Override
  public void preload() {

  }

  @Override
  public void create() {
    this.gameCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    this.world = new World(new Vector2(0, -9.8f), true);
    this.b2dr = new Box2DDebugRenderer();
    this.b2dCamera  = new OrthographicCamera(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);

    // create platform
    //BodyDef bdef = new BodyDef();
    ///bdef.position.set(0 / PPM, -400 / PPM);
    //bdef.type = BodyDef.BodyType.StaticBody;
    ///Body body = world.createBody(bdef);

   // PolygonShape shape = new PolygonShape();
   // shape.setAsBox(320 / PPM, 10 / PPM);
   // FixtureDef fdef = new FixtureDef();
  //  fdef.shape = shape;

  //  body.createFixture(fdef);
/*
    // create falling box
    BodyDef bdef = new BodyDef();
    bdef.position.set(160 / PPM, 200 / PPM);
    bdef.type = BodyDef.BodyType.DynamicBody;
    body = world.createBody(bdef);
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(10 / PPM, 10 / PPM);
    FixtureDef fdef = new FixtureDef();
    fdef.shape = shape;
    Fixture Fix = body.createFixture(fdef);
    Fix.setRestitution(0.4f);
    this.body = body;*/

  }

  @Override
  public void render(float delta) {

    world.step(1/60f, 6, 2);
    gameCamera.update();
    b2dCamera.position.set(body.getPosition().x, body.getPosition().y, 0);
    b2dCamera.update();

    fb.begin(Fbo.FinalResult); {
      Gdx.gl.glClearColor(0,0,0,1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      b2dr.render(world, b2dCamera.combined);
    } fb.end();

    if (Gdx.input.isKeyPressed(Input.Keys.E)) {
      body.applyForceToCenter(new Vector2(0, 10.5f), true);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      body.applyForceToCenter(new Vector2(0, 9.0f), true);
    }
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void dispose() {
    world.dispose();
  }
}
