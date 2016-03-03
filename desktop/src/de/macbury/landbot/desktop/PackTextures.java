package de.macbury.landbot.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created on 29.01.16.
 */
public class PackTextures {
  public static void main (String[] arg) {
    TexturePacker.Settings settings = new TexturePacker.Settings();
    settings.grid = true;
    settings.square = true;
    settings.paddingX = 2;
    settings.paddingY = 2;
    TexturePacker.process(settings, "./raw/gui", "./android/assets/ui", "ui.atlas");
    TexturePacker.process(settings, "./raw/tiles", "./android/assets/textures/", "tiles.atlas");
    TexturePacker.process(settings, "./raw/sprites", "./android/assets/textures/", "sprites.atlas");
  }
}
