package net.ape.gui.font;

import net.ape.gui.font.fontrenderer.FontManager;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;

public final class CustomFont {
   public static final FontManager FONT_MANAGER = new FontManager();
   private static final TTFFontRenderer fontRenderer = FONT_MANAGER.getFont("");

   public static void drawString(String text, double x, double y, int color, boolean shadow) {
   }

   public void drawCenteredString(String text, double x, double y, int color, boolean shadow) {
      drawString(text, x - (double)((int)getWidth(text) >> 1), y, color, shadow);
   }

   public static float getWidth(String text) {
      return fontRenderer.getWidth(text);
   }

   public float getHeight() {
      return fontRenderer.getHeight("I");
   }
}
