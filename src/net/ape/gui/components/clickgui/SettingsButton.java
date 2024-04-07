package net.ape.gui.components.clickgui;

import java.awt.Color;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.Tween;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class SettingsButton extends GuiObject {
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   class_2960 arrow = new class_2960("ape/expandright.png");
   Tween arrowTween = new Tween(0.16, 200.0, 200.0);
   String name;
   Function callback;

   public SettingsButton(String name, Function callback) {
      this.name = name;
      this.callback = callback;
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         boolean hover = mouseX - this.x > 0 && mouseX - this.x < 220 && mouseY - this.y > 0 && mouseY - this.y < 40;
         RenderUtil.rect(context, x, y, 220, 40, hover ? ColorPalette.mainl02 : ColorPalette.main);
         Color hoverColor = hover ? ColorPalette.text : ColorPalette.textd16;
         this.render.drawString(context, this.name, (float)(x + 9), (float)(y + 15), hoverColor.getRGB(), false);
         RenderUtil.image(context, this.arrow, x + this.arrowTween.getValue(), y + 16, 4, 8, ColorPalette.mainl37);
         return true;
      }
   }

   private void toggle() {
      this.callback.run(true);
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.visible && mouseX - (double)this.x > 0.0 && mouseX - (double)this.x < 220.0 && mouseY - (double)this.y > 0.0 && mouseY - (double)this.y < 40.0) {
         this.toggle();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getSize() {
      return 40;
   }
}
