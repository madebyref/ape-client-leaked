package net.ape.gui.components.clickgui;

import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.utils.RenderUtil;
import net.minecraft.class_332;

public class Divider extends GuiObject {
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 11");
   String name;

   public Divider() {
   }

   public Divider(String name) {
      this.name = name;
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         RenderUtil.rect(context, x, y + (this.name == null ? 0 : 20), 220, 1, ColorPalette.mainl04);
         if (this.name != null) {
            RenderUtil.rect(context, x + 1, y, 218, 21, ColorPalette.maind02);
            this.render.drawString(context, this.name.toUpperCase(), (float)(x + 9), (float)(y + 6), ColorPalette.textd43.getRGB(), false);
         }

         return true;
      }
   }

   @Override
   public int getSize() {
      return this.name == null ? 1 : 21;
   }
}
