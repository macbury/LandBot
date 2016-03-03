package de.macbury.landbot.core.assets;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Logger;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.WorldState;
import de.macbury.landbot.core.assets.loader.EntityBlueprintLoader;
import de.macbury.landbot.core.assets.loader.WorldLoader;
import de.macbury.landbot.core.entities.blueprint.EntityBlueprint;

/**
 * This class wraps {@link AssetManager} and extends it with proper features required by game and automaticaly map asssets to path
 */
public class Assets extends AssetManager {

  public Assets(FileHandleResolver resolver, LandBot game) {
    super(resolver);
    setLogger(new Logger("AssetManager", Application.LOG_INFO));
    setLoader(WorldState.class, new WorldLoader(resolver, game));
    setLoader(EntityBlueprint.class, new EntityBlueprintLoader(resolver));
  }
}
