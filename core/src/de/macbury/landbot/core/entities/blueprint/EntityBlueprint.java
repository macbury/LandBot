package de.macbury.landbot.core.entities.blueprint;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.entities.EntityManager;
import de.macbury.landbot.core.entities.Messages;

/**
 * This blueprint is used to build all entities using {@link de.macbury.landbot.core.entities.EntityManager} and all information in
 * {@link ComponentBlueprint}
 */
public class EntityBlueprint implements Disposable {
  private Array<ComponentBlueprint> componentBlueprints;

  public EntityBlueprint(Array<ComponentBlueprint> componentBlueprints) {
    this.componentBlueprints = new Array<ComponentBlueprint>(componentBlueprints);
  }

  /**
   * Creates entity using pools from entity manager
   * @param entityManager
   * @return
   */
  public Entity create(EntityManager entityManager, Messages messages) {
    Entity entity = entityManager.createEntity();
    for (ComponentBlueprint blueprint : componentBlueprints) {
      Component component  = entityManager.createComponent(blueprint.componentKlass);
      ((Pool.Poolable)component).reset();
      blueprint.applyTo(component, entity, messages);
      entity.add(component);
    }
    return entity;
  }

  /**
   * Creates new entity and adds it to {@link EntityManager}
   * @param entityManager
   * @return
   */
  public Entity createAndAdd(EntityManager entityManager, Messages messages) {
    Entity entity = create(entityManager, messages);
    entityManager.addEntity(entity);
    return entity;
  }

  @Override
  public void dispose() {
    for (ComponentBlueprint blueprint : componentBlueprints) {
      blueprint.dispose();
    }
    componentBlueprints.clear();
  }

  public Entity createAndAdd(WorldState worldState, Messages messages) {
    return createAndAdd(worldState.entities, messages);
  }
}
