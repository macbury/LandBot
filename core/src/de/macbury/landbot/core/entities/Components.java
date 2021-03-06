package de.macbury.landbot.core.entities;

import com.badlogic.ashley.core.ComponentMapper;
import de.macbury.landbot.core.entities.components.*;

/**
 * This class contains {@link ComponentMapper} for each component
 */
public class Components {
  public final static ComponentMapper<PositionComponent> Position                     = ComponentMapper.getFor(PositionComponent.class);
  public final static ComponentMapper<SpriteComponent>   Sprite                       = ComponentMapper.getFor(SpriteComponent.class);
  public final static ComponentMapper<BodyComponent>   Body                           = ComponentMapper.getFor(BodyComponent.class);
  public final static ComponentMapper<CPUComponent>   CPU                             = ComponentMapper.getFor(CPUComponent.class);
  public final static ComponentMapper<LandingBotSpawnComponent>   LandingBotSpawn     = ComponentMapper.getFor(LandingBotSpawnComponent.class);
  public final static ComponentMapper<CelestialBodyComponent>   CelestialBody         = ComponentMapper.getFor(CelestialBodyComponent.class);
}
