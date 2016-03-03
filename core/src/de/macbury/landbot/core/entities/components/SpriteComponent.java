package de.macbury.landbot.core.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.entities.blueprint.ComponentBlueprint;

/**
 * This component contains all information required for rendering sprites
 */
public class SpriteComponent extends Sprite implements Component, Pool.Poolable  {
  @Override
  public void reset() {
    setTexture(null);
  }

  public static class Blueprint extends ComponentBlueprint<SpriteComponent> {
    private String atlasPath;
    private String name;
    private TextureAtlas textureAtlas;

    @Override
    public void prepareDependencies(Array<AssetDescriptor> dependencies) {
      AssetDescriptor<TextureAtlas> textureAtlasAssetDescriptor = new AssetDescriptor<TextureAtlas>(atlasPath, TextureAtlas.class);
      dependencies.add(textureAtlasAssetDescriptor);
    }

    @Override
    public void assignDependencies(Assets assets) {
      textureAtlas = assets.get(atlasPath);
    }

    @Override
    public void applyTo(SpriteComponent component, Entity target, Messages messages) {
      TextureAtlas.AtlasRegion region = textureAtlas.findRegion(name);
      component.setRegion(region);
      component.setSize(region.getRegionWidth(), region.getRegionHeight());
    }

    @Override
    public void load(JsonValue source, Json json) {
      this.atlasPath = source.getString("atlas");
      this.name      = source.getString("name");
    }

    @Override
    public void save(Json target, SpriteComponent source) {

    }

    @Override
    public void dispose() {
      textureAtlas = null;
    }
  }
}
