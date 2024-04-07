package net.ape.gui.components.clickgui;

import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.TextField;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.minecraft.class_332;
import org.json.JSONObject;

public class TextBox extends GuiObject {
   TextField field = new TextField(false);
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 12");
   String name;
   public String value = "";

   public TextBox(String name, GuiObject mod) {
      this.name = name;
      mod.objects.add(this);
   }

   public TextBox(String name, Module mod) {
      this(name, mod.button);
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         this.render.drawString(context, this.name, (float)(x + 8), (float)(y + 10), ColorPalette.textd16.getRGB(), false);
         RenderUtil.smoothRoundedRect(context, (double)(x + 10), (double)(y + 24), 200.0, 29.0, 5.0, ColorPalette.mainl02);
         this.field.render(context, x + 15, y + 34);
         return true;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.visible) {
         if (mouseX - (double)this.x > 10.0
            && mouseX - (double)this.x < 210.0
            && mouseY - (double)this.y > 24.0
            && mouseY - (double)this.y < 53.0
            && !this.field.focused) {
            this.field.focused = true;
            return true;
         }

         if (this.field.focused) {
            this.field.focused = false;
            return true;
         }
      }

      return false;
   }

   @Override
   public void keyTyped(int keyCode) {
      if (this.field.focused) {
         this.field.keyPressed(keyCode);
         this.value = this.field.value;
         if (keyCode == 257 || keyCode == 335) {
            this.field.focused = false;
         }
      }
   }

   @Override
   public void charTyped(char key) {
      if (this.field.focused) {
         this.field.charTyped(key);
         this.value = this.field.value;
      }
   }

   @Override
   public int getSize() {
      return 58;
   }

   @Override
   public void save(JSONObject saved) {
      saved.put(this.name, this.value);
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.name)) {
         this.value = saved.getString(this.name);
         this.field.value = this.value;
      }
   }
}
