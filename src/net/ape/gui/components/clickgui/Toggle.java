package net.ape.gui.components.clickgui;

import java.awt.Color;
import net.ape.ApeClient;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.ColorTween;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.Tween;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.minecraft.class_332;
import org.json.JSONObject;

public class Toggle extends GuiObject {
   public boolean enabled;
   public String name;
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   ColorTween hoverTween = new ColorTween(0.16, new Color(60, 60, 60), new Color(60, 60, 60));
   Tween positionTween = new Tween(0.16, 192.0, 192.0);
   Function func;

   public Toggle(String name, GuiObject mod, Function func) {
      this.name = name;
      this.func = func;
      mod.objects.add(this);
   }

   public Toggle(String name, Module mod, Function func) {
      this(name, mod.button, func);
   }

   public Toggle(String name, GuiObject mod, Function func, boolean defaultval) {
      this(name, mod, func);
      if (defaultval) {
         this.toggle();
      }
   }

   public Toggle(String name, Module mod, Function func, boolean defaultval) {
      this(name, mod.button, func);
      if (defaultval) {
         this.toggle();
      }
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         this.render.drawString(context, this.name, (float)(x + 8), (float)(y + 10), ColorPalette.textd16.getRGB(), false);
         boolean hovered = mouseX - x > 0 && mouseX - x < 220 && mouseY - y > 0 && mouseY - y < 30;
         this.hoverTween
            .setDestinationValue(
               this.enabled ? ApeClient.getRenderColor((float)ind * -0.075F) : (hovered ? ColorPalette.mainl37 : ColorPalette.mainl14), !this.enabled
            );
         RenderUtil.smoothRoundedRect(context, (double)(x + 190), (double)(y + 9), 22.0, 12.0, 10.0, this.hoverTween.getValue());
         RenderUtil.smoothRoundedRect(context, (double)(x + this.positionTween.getValue()), (double)(y + 11), 8.0, 8.0, 10.0, ColorPalette.main);
         return true;
      }
   }

   private void toggle() {
      this.enabled = !this.enabled;
      if (this.func != null) {
         this.func.run(this.enabled);
      }

      this.hoverTween.setDestinationValue(this.enabled ? ApeClient.getRenderColor() : ColorPalette.mainl37, true);
      this.positionTween.setDestinationValue(this.enabled ? 202.0 : 192.0);
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.visible
         && mouseX - (double)this.x > 0.0
         && mouseX - (double)this.x < 220.0
         && mouseY - (double)this.y > 0.0
         && mouseY - (double)this.y < 30.0
         && button == 0) {
         this.toggle();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getSize() {
      return 30;
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
