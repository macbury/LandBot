package de.macbury.landbot.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.*;
import de.macbury.landbot.core.TelegramEvents;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.Components;
import de.macbury.landbot.core.entities.blueprint.EntityBlueprint;
import de.macbury.landbot.core.entities.components.BodyComponent;
import de.macbury.landbot.core.entities.components.PositionComponent;
import de.macbury.landbot.core.entities.systems.PhysicsSystem;
import de.macbury.landbot.core.graphics.framebuffer.Fbo;
import de.macbury.landbot.core.screens.ScreenBase;
import de.macbury.landbot.core.ui.ingame.GameplayScreenGroup;

/**
 * Simple test case of logic
 */
public class PsychEntityScreen extends ScreenBase {
  private WorldState worldState;

  @Override
  public void preload() {
    assets.load("entity:bot.json", EntityBlueprint.class);
    assets.load("entity:spawning_point.json", EntityBlueprint.class);
    assets.load("entity:test_planet.json", EntityBlueprint.class);
  }

  @Override
  public void create() {
    this.worldState = new WorldState(this.game);
    worldState.landingBotBlueprint = assets.get("entity:bot.json");
    EntityBlueprint spawnBlueprint = assets.get("entity:spawning_point.json");
    Entity spawnEntity             = spawnBlueprint.createAndAdd(worldState.entities, messages);
    Components.Position.get(spawnEntity).set(-1000*WorldState.PPM, -1000*WorldState.PPM);

    GameplayScreenGroup gameplayScreenGroup = new GameplayScreenGroup(game, worldState);
    hud.addActor(gameplayScreenGroup);
    gameplayScreenGroup.getProgramingController().show();

    EntityBlueprint testPlanetBlueprint = assets.get("entity:test_planet.json");

    testPlanetBlueprint.createAndAdd(worldState, messages);
    //createPlanets();
  }

  private void createPlanets() {
    World world = worldState.world;

    int planetRadiusInMeters = 230;

    FixtureDef planetFixtureDef  = new FixtureDef();
    planetFixtureDef.restitution = 0;
    planetFixtureDef.density     = 1;

    CircleShape planetShape = new CircleShape();
    planetShape.setRadius(6f);

    planetShape.setRadius(planetRadiusInMeters);
    planetFixtureDef.shape         = planetShape;

    BodyDef planetBodyDef    = new BodyDef();
    planetBodyDef.position.set(0,0);

    Body planetBody = world.createBody(planetBodyDef);
    planetBody.createFixture(planetFixtureDef);
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
    assets.unload("entity:spawning_point.json");
    assets.unload("entity:bot.json");
    assets.unload("entity:test_planet.json");
    worldState.dispose();
  }
}
