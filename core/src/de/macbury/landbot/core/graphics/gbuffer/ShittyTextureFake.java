package de.macbury.landbot.core.graphics.gbuffer;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

/**
 * Created by macbury on 08.05.14.
 */
public class ShittyTextureFake extends Texture {
  private GBufferAttachment attachment;

  public ShittyTextureFake(GBufferAttachment attachment) {
    super(64, 64, Pixmap.Format.RGB565);
    try {
      dispose();
    } catch (Exception e) {

    }

    this.attachment = attachment;
  }

  @Override
  public void dispose() {
    super.dispose();
  }

  @Override
  public void load(TextureData data) {
    //super.load(data);
  }

  @Override
  protected void reload() {
    //super.reload();
  }

  @Override
  public void draw(Pixmap pixmap, int x, int y) {
    super.draw(pixmap, x, y);
  }

  @Override
  public int getWidth() {
    return attachment.getWidth();
  }

  @Override
  public int getHeight() {
    return attachment.getHeight();
  }

  @Override
  public int getDepth() {
    return attachment.getDepth();
  }

  @Override
  public TextureData getTextureData() {
    return null;
  }

  @Override
  public boolean isManaged() {
    return attachment.isManaged();
  }

  @Override
  public void bind() {
    attachment.bind();
  }

  @Override
  public void bind(int unit) {
    attachment.bind(unit);
  }

  @Override
  public TextureFilter getMinFilter() {
    return attachment.getMinFilter();
  }

  @Override
  public TextureFilter getMagFilter() {
    return attachment.getMagFilter();
  }

  @Override
  public TextureWrap getUWrap() {
    return attachment.getUWrap();
  }

  @Override
  public TextureWrap getVWrap() {
    return attachment.getVWrap();
  }

  @Override
  public int getTextureObjectHandle() {
    return attachment.getTextureObjectHandle();
  }

  @Override
  public void unsafeSetWrap(TextureWrap u, TextureWrap v) {
    attachment.unsafeSetWrap(u, v);
  }

  @Override
  public void unsafeSetWrap(TextureWrap u, TextureWrap v, boolean force) {
    attachment.unsafeSetWrap(u, v, force);
  }

  @Override
  public void setWrap(TextureWrap u, TextureWrap v) {
    attachment.setWrap(u, v);
  }

  @Override
  public void unsafeSetFilter(TextureFilter minFilter, TextureFilter magFilter) {
    attachment.unsafeSetFilter(minFilter, magFilter);
  }

  @Override
  public void unsafeSetFilter(TextureFilter minFilter, TextureFilter magFilter, boolean force) {
    attachment.unsafeSetFilter(minFilter, magFilter, force);
  }

  @Override
  public void setFilter(TextureFilter minFilter, TextureFilter magFilter) {
    attachment.setFilter(minFilter, magFilter);
  }

  @Override
  protected void delete() {
    return;
  }
}
