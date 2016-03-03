package de.macbury.landbot.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import de.macbury.landbot.core.TelegramEvents;
import de.macbury.landbot.core.assets.Assets;
import de.macbury.landbot.core.entities.Messages;
import de.macbury.landbot.core.graphics.camera.Overlay;
import de.macbury.landbot.core.graphics.framebuffer.Fbo;
import de.macbury.landbot.core.graphics.framebuffer.FrameBufferManager;
import de.macbury.landbot.core.graphics.framebuffer.FullScreenFrameBufferResult;
import de.macbury.landbot.core.input.InputManager;

/**
 * This manages all stuff with in game interface
 * https://github.com/kotcrab/VisEditor/wiki/VisUI
 */
public class Hud extends Stage implements Telegraph {
  private static final String SKIN_FILE = "ui/ui.json";
  private static final String SKIN_ATLAS = "ui/ui.atlas";
  private final AnimatedImage loader;
  private final Cursor arrowCursor;
  private final Cursor textCursor;
  private FullScreenFrameBufferResult fullScreenFrameBufferResult;
  private Overlay overlay;
  private Skin skin;
  private Assets assets;
  private InputManager input;
  private Vector3 tempVec = new Vector3();
  public Hud(InputManager input, Assets assets, FrameBufferManager fb, Messages messages) {
    super(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    this.input  = input;
    this.assets = assets;

    createSkin();

    input.addProcessor(this);

    this.fullScreenFrameBufferResult = new FullScreenFrameBufferResult(Fbo.FinalResult, fb);
    addActor(fullScreenFrameBufferResult);

    this.overlay        = new Overlay();
    addActor(overlay);

    DebugLabel debugLabel = new DebugLabel(skin);
    debugLabel.setAlignment(Align.left);
    debugLabel.setPosition(20, Gdx.graphics.getHeight() - 80);
    addActor(debugLabel);

    this.loader = new AnimatedImage(new Animation(0.05f, skin.getAtlas().findRegions("loader")));
    loader.setPosition( Gdx.graphics.getWidth() - 84, 20);
    addActor(loader);
    loader.setVisible(false);

    messages.addListener(this, TelegramEvents.UiShowNormalCursor);
    messages.addListener(this, TelegramEvents.UiShowTextCursor);

    this.arrowCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("ui/arrow.png")), 6, 3);
    this.textCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("ui/text_cursor.png")), 16, 16);
    Gdx.graphics.setCursor(arrowCursor);
  }

  private void createSkin() {
    skin = new Skin(new TextureAtlas(Gdx.files.internal(SKIN_ATLAS)));
    skin.add("default", createFont("ui/Dosis-Medium.ttf", 18, Color.WHITE, Color.BLACK, 1));
    skin.add("code", createFont("ui/UbuntuMono-R.ttf", 16, Color.WHITE, null, 0));
    skin.add("console", createFont("ui/UbuntuMono-R.ttf", 16, Color.WHITE, null, 0));
    skin.load(Gdx.files.internal(SKIN_FILE));
  }

  private static BitmapFont createFont(String path, int size, Color color, Color borderColor, float borderWidth) {
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = size;
    parameter.color = color;
    parameter.minFilter = Texture.TextureFilter.Linear;
    parameter.magFilter = Texture.TextureFilter.Linear;
    if (borderColor != null) {
      parameter.borderColor = borderColor;
      parameter.borderWidth = borderWidth;
    }
    BitmapFont font = generator.generateFont(parameter);
    generator.dispose();
    return font;
  }

  public Overlay getOverlay() {
    return overlay;
  }

  @Override
  public void dispose() {
    super.dispose();

    assets.unload(SKIN_FILE);
    assets = null;
    input.removeProcessor(this);
    input = null;
    skin  = null;
  }

  public Skin getSkin() {
    return skin;
  }

  /**
   * Simple loading guage showed in corner of the screen
   * @return
   */
  public AnimatedImage getLoader() {
    return loader;
  }

  public FullScreenFrameBufferResult getFullScreenFrameBufferResult() {
    return fullScreenFrameBufferResult;
  }

  public void showLoading() {
    loader.setVisible(true);
    fullScreenFrameBufferResult.setVisible(false);
  }

  public void hideLoading() {
    loader.setVisible(false);
    fullScreenFrameBufferResult.setVisible(true);
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    switch (TelegramEvents.from(msg)) {
      case UiShowTextCursor:
        Gdx.graphics.setCursor(textCursor);
        return true;

      case UiShowNormalCursor:
        Gdx.graphics.setCursor(arrowCursor);
        return true;
    }
    return false;
  }
}
