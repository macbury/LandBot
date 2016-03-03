package de.macbury.landbot.core.graphics.framebuffer;

/**
 * This enum contains all fbos used by engine
 */
public enum Fbo {
  /**
   * In this framebuffer the final result of rendering that should be displayed on screen
   */
  FinalResult("landbot:final-result");
  private final String namespace;
  Fbo(String namespace) {
    this.namespace = namespace;
  }

  public String getNamespace() {
    return namespace;
  }
}
