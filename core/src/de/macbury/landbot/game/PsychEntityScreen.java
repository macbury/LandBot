package de.macbury.landbot.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.blueprint.EntityBlueprint;
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
    assets.load("entity:bot.json", EntityBlueprint.class);
  }

  @Override
  public void create() {
    this.worldState = new WorldState(this.game);
    EntityBlueprint botBlueprint = assets.get("entity:bot.json");
    botBlueprint.createAndAdd(worldState.entities, messages);

    //botBlueprint.createAndAdd(worldState.entities, messages);
  }

  @Override
  public void render(float delta) {
    worldState.render(delta);
  }

  @Override
  public void resize(int width, int height) {
    worldState.resize(width, height);
  }

  @Override
  public void dispose() {
    assets.unload("entity:bot.json");
    worldState.dispose();
  }
}
