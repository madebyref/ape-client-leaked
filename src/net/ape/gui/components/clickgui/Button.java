package net.ape.gui.components.clickgui;

import java.awt.Color;
import net.ape.ApeClient;
import net.ape.gui.ClickGui;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.Tween;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.json.JSONObject;

public class Button extends GuiObject {
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   class_2960 arrow = new class_2960("ape/expandright.png");
   Tween arrowTween = new Tween(0.16, 200.0, 200.0);
   class_2960 icon;
   int iconSizeX;
   int iconSizeY;
   String name;
   GuiObject window;
   boolean enabled;

   public Button(String name, class_2960 icon, int iconSizeX, int iconSizeY, GuiObject window) {
      this.name = name;
      this.icon = icon;
      this.iconSizeX = iconSizeX;
      this.iconSizeY = iconSizeY;
      this.window = window;
   }

   public Button(String name, GuiObject window) {
      this.name = name;
      this.window = window;
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         boolean hover = mouseX - this.x > 0 && mouseX - this.x < 220 && mouseY - this.y > 0 && mouseY - this.y < 40;
         RenderUtil.rect(context, x, y, 220, 40, !this.enabled && !hover ? ColorPalette.main : ColorPalette.mainl02);
         Color hoverColor = this.enabled ? ApeClient.getRenderColor((float)ind * -0.025F) : (hover ? ColorPalette.text : ColorPalette.textd16);
         this.render.drawString(context, this.name, (float)(x + (this.icon != null ? 32 : 9)), (float)(y + 15), hoverColor.getRGB(), false);
         RenderUtil.image(context, this.arrow, x + this.arrowTween.getValue(), y + 16, 4, 8, ColorPalette.mainl37);
         if (this.icon != null) {
            RenderUtil.image(context, this.icon, x + 10, y + 13, this.iconSizeX, this.iconSizeY, hoverColor);
         }

         return true;
      }
   }

   private void toggle() {
      this.enabled = !this.enabled;
      if (this.window != null) {
         this.window.visible = this.enabled;
      }

      this.arrowTween.setDestinationValue(this.enabled ? 206.0 : 200.0);
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (ClickGui.bar.expanded) {
         return false;
      } else if (this.visible
         && mouseX - (double)this.x > 0.0
         && mouseX - (double)this.x < 220.0
         && mouseY - (double)this.y > 0.0
         && mouseY - (double)this.y < 40.0) {
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

   @Override
   public void save(JSONObject saved) {
      saved.put(this.name, this.enabled);
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.name) && saved.getBoolean(this.name) != this.enabled) {
         this.toggle();
      }
   }
}
