package net.ape.gui.components.clickgui;

import java.awt.Color;
import java.util.List;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.ColorTween;
import net.ape.gui.components.GuiObject;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.minecraft.class_332;
import org.json.JSONObject;

public class Dropdown extends GuiObject {
   public boolean enabled;
   public String name;
   public String value;
   public List<String> list;
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   ColorTween hoverTween = new ColorTween(0.16, new Color(34, 34, 34), new Color(34, 34, 34));
   Function func;

   public Dropdown(String name, GuiObject mod, List<String> list, Function func) {
      this.name = name;
      this.list = list;
      this.value = list.get(0);
      this.func = func;
      mod.objects.add(this);
   }

   public Dropdown(String name, Module mod, List<String> list, Function func) {
      this(name, mod.button, list, func);
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         int size = this.enabled ? 31 + (this.list.size() - 1) * 24 : 31;
         boolean hovered = mouseX - x > 0 && mouseX - x < 220 && mouseY - y > 0 && mouseY - y < size + 9;
         this.hoverTween.setDestinationValue(hovered ? ColorPalette.mainl0875 : ColorPalette.mainl034, true);
         RenderUtil.smoothRoundedRect(context, (double)(x + 10), (double)(y + 4), 200.0, (double)size, 10.0, this.hoverTween.getValue());
         RenderUtil.smoothRoundedRect(context, (double)(x + 11), (double)(y + 5), 198.0, (double)(size - 2), 10.0, ColorPalette.main);
         this.render.drawString(context, this.name + " - " + this.value, (float)(x + 18), (float)(y + 14), ColorPalette.textd16.getRGB(), false);
         if (this.enabled) {
            int i = 0;

            for (String item : this.list) {
               if (!item.equals(this.value)) {
                  int yLevel = y + 8 + ++i * 24;
                  if (mouseX - x > 10 && mouseX - x < 210 && mouseY - yLevel > 0 && mouseY - yLevel < 24) {
                     RenderUtil.rect(context, x + 11, yLevel, 198, 24, ColorPalette.mainl02);
                  }

                  this.render.drawString(context, item, (float)(x + 18), (float)(yLevel + 6), ColorPalette.textd16.getRGB(), false);
               }
            }
         }

         return true;
      }
   }

   public void set(String item) {
      this.value = this.list.contains(item) ? item : this.list.get(0);
      if (this.func != null) {
         this.func.run(this.value);
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (mouseX - (double)this.x > 0.0
         && mouseX - (double)this.x < 220.0
         && mouseY - (double)this.y > 0.0
         && mouseY - (double)this.y < (double)this.getSize()
         && button == 0
         && this.visible) {
         if (this.enabled) {
            int i = 0;

            for (String item : this.list) {
               if (!item.equals(this.value)) {
                  int yLevel = this.y + 8 + ++i * 24;
                  if (mouseX - (double)this.x > 10.0 && mouseX - (double)this.x < 210.0 && mouseY - (double)yLevel > 0.0 && mouseY - (double)yLevel < 24.0) {
                     this.set(item);
                  }
               }
            }
         }

         this.enabled = !this.enabled;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getSize() {
      return (this.enabled ? 31 + (this.list.size() - 1) * 24 : 31) + 9;
   }

   @Override
   public void save(JSONObject saved) {
      saved.put(this.name, this.value);
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.name)) {
         this.set(saved.getString(this.name));
      }
   }
}
