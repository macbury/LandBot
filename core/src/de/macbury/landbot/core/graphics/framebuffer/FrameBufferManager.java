package de.macbury.landbot.core.graphics.framebuffer;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FloatFrameBuffer;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * This class manages all frame buffers in engine
 */
public class FrameBufferManager implements Disposable {
  private static final String TAG                   = "FrameBufferManager";
  private static final String COPY_FB_SHADER        = "ps-copy";
  private ObjectMap<Fbo, FrameBuffer> frameBuffers;
  private Mesh screenQuad;
  private OrthographicCamera screenCamera;
  private FrameBuffer currentFrameBuffer;

  public FrameBufferManager() {
    frameBuffers = new ObjectMap<Fbo, FrameBuffer>();
  }

  public FrameBuffer create(Fbo fbo) {
    return create(fbo, Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, Texture.TextureWrap.Repeat, Texture.TextureFilter.Linear);
  }

  public FrameBuffer get(Fbo key) {
    return  frameBuffers.get(key);
  }

  /**
   * Resizes internal camera for framebuffer use, call this in you ApplicationListener's resize.
   * @param width - new screen width
   * @param height - new screen height
   * @param resizeFramebuffers - whether all of the framebuffers should be recreated to match new screen size
   */
  public void resize(int width, int height, boolean resizeFramebuffers) {
    screenCamera = new OrthographicCamera(width, height);
    clear();

    createScreenQuad();
    createDefaultFrameBuffers();
  }

  /**
   * Creates a new Framebuffer with given params.
   * @param fbIdn - this framebuffer's identifier
   * @param format - pixel format of this framebuffer
   * @param fbWidth - desired width
   * @param fbHeight - desired height
   * @param hasDepth - whether to attach depth buffer
   */
  public FrameBuffer create(Fbo fbo, Pixmap.Format format, int fbWidth, int fbHeight, boolean hasDepth, Texture.TextureWrap textureWrap, Texture.TextureFilter filter) {
    FrameBuffer fb = frameBuffers.get(fbo);

    if (fb == null || fb.getWidth() != fbWidth || fb.getHeight() != fbHeight) {
      if (fb != null) {
        fb.dispose();
      }
      Gdx.app.log(TAG, "Creating framebuffer: " + fbo);
      fb = new FrameBuffer(format, fbWidth, fbHeight, hasDepth);
      fb.getColorBufferTexture().setFilter(filter, filter);
      fb.getColorBufferTexture().setWrap(textureWrap, textureWrap);

    }
    frameBuffers.put(fbo, fb);
    return fb;
  }

  /**
   * Creates a new Framebuffer with given params.
   * @param fbIdn - this framebuffer's identifier
   * @param format - pixel format of this framebuffer
   * @param fbWidth - desired width
   * @param fbHeight - desired height
   * @param hasDepth - whether to attach depth buffer
   */
  public FrameBuffer createFloat(Fbo fbIdn, int fbWidth, int fbHeight, boolean hasDepth, Texture.TextureWrap textureWrap, Texture.TextureFilter filter) {
    FrameBuffer fb = frameBuffers.get(fbIdn);

    if (fb == null || fb.getWidth() != fbWidth || fb.getHeight() != fbHeight) {
      if (fb != null) {
        fb.dispose();
      }
      Gdx.app.log(TAG, "Creating float framebuffer: " + fbIdn);
      fb = new FloatFrameBuffer(fbWidth, fbHeight, hasDepth);
      fb.getColorBufferTexture().setFilter(filter, filter);
      fb.getColorBufferTexture().setWrap(textureWrap, textureWrap);

    }
    frameBuffers.put(fbIdn, fb);
    return fb;
  }

  /**
   * Creates a quad which spans entire screen, used for rendering of framebuffers.
   */
  private void createScreenQuad() {
    if (screenQuad != null)
      return;
    screenQuad = new Mesh(true, 4, 6, VertexAttribute.Position(), new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, "a_color"), VertexAttribute.TexCoords(0));

    Vector3 vec0 = new Vector3(0, -1, 0);
    screenCamera.unproject(vec0);
    Vector3 vec1 = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
    screenCamera.unproject(vec1);
    screenQuad.setVertices(new float[]{vec0.x, vec0.y, 0, 1, 1, 1, 1, 0, 1,
      vec1.x, vec0.y, 0, 1, 1, 1, 1, 1, 1,
      vec1.x, vec1.y, 0, 1, 1, 1, 1, 1, 0,
      vec0.x, vec1.y, 0, 1, 1, 1, 1, 0, 0});
    screenQuad.setIndices(new short[]{0, 1, 2, 2, 3, 0});
  }

  @Override
  public void dispose() {
    clear();
  }

  private void clear() {
    for (Fbo key : frameBuffers.keys()) {
      frameBuffers.get(key).dispose();
    }
    frameBuffers.clear();
    if (screenQuad != null)
      screenQuad.dispose();
    screenQuad = null;
  }

  public void end() {
    currentFrameBuffer.end();
    currentFrameBuffer = null;
  }

  /**
   * Starts rendering in framebuffer
   * @param fbIdn
   */
  public void begin(Fbo fbIdn) {
    if (currentFrameBuffer != null)
      throw new GdxRuntimeException("Already binded other buffer!");
    currentFrameBuffer = get(fbIdn);
    if (currentFrameBuffer == null) {
      throw new GdxRuntimeException("Could not found: " + fbIdn);
    }
    currentFrameBuffer.begin();
  }

  public void createDefaultFrameBuffers() {
    create(Fbo.FinalResult);
  }


  public ObjectMap<Fbo, FrameBuffer> all() {
    return frameBuffers;
  }

  public FileHandle saveAsPng(Fbo frameBufferName) {
    this.begin(frameBufferName);
    FrameBuffer frameBuffer = get(frameBufferName);
    if (frameBuffer == null) {
      throw new GdxRuntimeException("Cannot save: " + frameBufferName);
    }
    FileHandle saveFile = Gdx.files.absolute("/tmp/fb_out.png");

    ScreenshotFactory.saveScreenshot(saveFile, frameBuffer.getWidth(), frameBuffer.getHeight());
    this.end();
    return saveFile;
  }

  public void destroy(Fbo frameBufferName) {
    if (frameBuffers.containsKey(frameBufferName)) {
      get(frameBufferName).dispose();
      frameBuffers.remove(frameBufferName);
    }

  }

  public OrthographicCamera getScreenCamera() {
    return screenCamera;
  }

  public Mesh getScreenQuad() {
    return screenQuad;
  }
}