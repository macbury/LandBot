package de.macbury.landbot.core.assets.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.WorldState;

/**
 * Loads world
 */
public class WorldLoader extends AsynchronousAssetLoader<WorldState, WorldLoader.WorldParameter> {
  private final LandBot game;
  private WorldState.Blueprint blueprint;

  public WorldLoader(FileHandleResolver resolver, LandBot game) {
    super(resolver);
    this.game = game;
  }

  @Override
  public void loadAsync(AssetManager manager, String fileName, FileHandle file, WorldParameter parameter) {

  }

  @Override
  public WorldState loadSync(AssetManager manager, String fileName, FileHandle file, WorldParameter parameter) {
    System.gc();
    return new WorldState(game);
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, WorldParameter parameter) {
    Json json                       = new Json();
    this.blueprint                  = json.fromJson(WorldState.Blueprint.class, file);
    return null;
  }

  static public class WorldParameter extends AssetLoaderParameters<WorldState> {

  }

}
