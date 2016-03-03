package de.macbury.landbot.core.graphics.gbuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;

import java.nio.IntBuffer;

/**
 * Created by macbury on 08.05.14.
 */
public class GBufferAttachment extends GLTexture implements Disposable {
  private int type;
  private int format;
  private int height;
  private int width;
  public int attachmentIndex;
  public int internalFormat;
  public int bufferHandle;
  private TextureRegion proxy;

  public GBufferAttachment(int attachmentIndex, IntBuffer handle, int width, int height, int internalFormat, int format, int type) {
    super(GL20.GL_TEXTURE_2D, Gdx.gl.glGenTexture());
    handle.clear();
    this.attachmentIndex  = attachmentIndex;
    this.internalFormat   = internalFormat;
    this.format           = format;
    this.type             = type;
    this.width            = width;
    this.height           = height;

    Gdx.gl.glGenRenderbuffers(1, handle);
    bufferHandle = handle.get(0);
  }

  public void bindRenderTarget() {
    GL20 gl = Gdx.gl;
    gl.glBindRenderbuffer(GL30.GL_RENDERBUFFER, bufferHandle);
    gl.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalFormat, width, height);
    if (attachmentIndex == GBuffer.ATTACHEMNT_DEPTH) {
      gl.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachmentIndex, GL30.GL_RENDERBUFFER, bufferHandle);
    } else {
      gl.glFramebufferRenderbuffer(GL30.GL_RENDERBUFFER, attachmentIndex, GL30.GL_RENDERBUFFER, bufferHandle);
    }

  }

  public void generateAndBindTexture() {
    bind();
    Gdx.gl.glTexImage2D(GL20.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, null);
    setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
    Gdx.gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, attachmentIndex, GL20.GL_TEXTURE_2D, glHandle, 0);
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getDepth() {
    return 0;
  }

  @Override
  public boolean isManaged() {
    return false;
  }

  @Override
  protected void reload() {

  }

  @Override
  public void dispose() {
    IntBuffer handle = BufferUtils.newIntBuffer(1);
    handle.put(bufferHandle);
    handle.flip();
    Gdx.gl.glDeleteRenderbuffers(1, handle);
    super.dispose();
  }

  public TextureRegion proxy() {
    if (proxy == null) {
      proxy = new TextureRegion(new ShittyTextureFake(this));
      proxy.flip(false, true);
    }

    return proxy;
  }
}
