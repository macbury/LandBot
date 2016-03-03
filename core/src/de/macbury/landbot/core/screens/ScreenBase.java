package de.macbury.landbot.core.screens;


import com.badlogic.gdx.utils.Disposable;
import de.macbury.landbot.LandBot;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.graphics.framebuffer.FrameBufferManager;
import de.macbury.landbot.core.input.InputManager;
import de.macbury.landbot.core.ui.Hud;

/** <p>
 * Represents one of many application screens, such as a main menu, a settings menu, the game screen and so on.
 * </p>
 * <p>
 * Note that {@link #dispose()} is not called automatically.
 * </p>*/
public abstract class ScreenBase implements Disposable {
  protected LandBot game;
  protected Assets assets;
  protected ScreenManager screens;
  protected Messages messages;
  protected InputManager input;
  protected FrameBufferManager fb;
  protected Hud hud;

  /**
   * Links references to current {@link LandBot}
   * @param game
   */
  public void link(LandBot game) {
    this.unlink();
    this.game     = game;
    this.assets   = game.assets;
    this.screens  = game.screens;
    this.messages = game.messages;
    this.input    = game.input;
    this.fb       = game.fb;
    this.hud      = game.hud;
  }

  /**
   * Unlink references to current {@link LandBot}
   */
  public void unlink() {
    this.fb       = null;
    this.game     = null;
    this.assets   = null;
    this.screens  = null;
    this.messages = null;
    this.input    = null;
    this.hud      = null;
  }

  /**
   * Called before {@link ScreenBase#create()}. You can build assets to load here. If there are assets to load it shows loading screen
   */
  public abstract void preload();
  /**
   * Called when screen is showed
   */
  public abstract void create();

  /** Called when the screen should render itself.
   * @param delta The time in seconds since the last render. */
  public abstract void render (float delta);

  /** Called after screen show or game window resize */
  public abstract void resize (int width, int height);

}