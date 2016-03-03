package de.macbury.landbot;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.physics.box2d.Box2D;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.assets.EngineFileHandleResolver;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.graphics.framebuffer.FrameBufferManager;
import de.macbury.landbot.core.input.InputManager;
import de.macbury.landbot.core.screens.ScreenManager;
import de.macbury.landbot.core.ui.Hud;
import de.macbury.landbot.game.LuaScreen;
import de.macbury.landbot.game.PsychEntityScreen;
import de.macbury.landbot.game.PsychScreen;

public class LandBot extends ApplicationAdapter {
  public final static String VERSION = "0.0.8";
  private static final String TAG    = "LandBot";
  /**
   * This class helps with input managment
   */
  public InputManager input;
  /**
   * Message comunication class used by EntityManager
   */
  public Messages messages;
  /**
   *  Loads and stores assets like textures, bitmapfonts, tile maps, sounds, music and so on.
   */
  public Assets assets;
  /**
   * Manage in game screens
   */
  public ScreenManager screens;
  /**
   * Manage framebuffers
   */
  public FrameBufferManager fb;
  /**
   * Main game ui
   */
  public Hud hud;

  @Override
  public void create () {
    //TODO handle enabling this stuff by flags...
    GLProfiler.enable();
    Gdx.app.setLogLevel(Application.LOG_DEBUG);
    Gdx.app.log(TAG, "Init...");

    Box2D.init();

    this.input      = new InputManager();
    this.assets     = new Assets(new EngineFileHandleResolver(), this);
    this.messages   = new Messages();
    this.screens    = new ScreenManager(this);
    this.fb         = new FrameBufferManager();
    this.hud        = new Hud(input, assets, fb, messages);


    screens.set(new PsychEntityScreen());

    Gdx.input.setInputProcessor(input);
  }

  @Override
  public void resize(int width, int height) {
    fb.resize(width, height, true);
    super.resize(width, height);
  }

  @Override
  public void render () {
    GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());
    messages.update();
    screens.update();

    Gdx.gl.glClearColor(1,1,1,1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    hud.act(Gdx.graphics.getDeltaTime());
    hud.draw();
  }

  @Override
  public void dispose() {
    super.dispose();
    assets.dispose();
    messages.clear();
    messages.clearListeners();
    fb.dispose();
  }
}
