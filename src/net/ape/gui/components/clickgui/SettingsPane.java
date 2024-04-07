package net.ape.gui.components.clickgui;

import net.ape.gui.ClickGui;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class SettingsPane extends GuiObject {
   String name;
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   TTFFontRenderer render2 = CustomFont.FONT_MANAGER.getFont("Arial 10");
   class_2960 close = new class_2960("ape/close.png");
   class_2960 back = new class_2960("ape/back.png");
   GuiObject parent;

   public SettingsPane(String name, @Nullable GuiObject parent) {
      this.name = name;
      this.parent = parent;
      this.visible = false;
      this.objects.add(new Divider());
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         this.y = ClickGui.main.y;
         y = ClickGui.main.y;
         int yLevel = 45;

         for (GuiObject object : ClickGui.main.objects) {
            if (object.visible) {
               yLevel += object.getSize();
            }
         }

         RenderUtil.smoothRoundedRect(
            context, (double)x, (double)y, 220.0, (double)yLevel, 10.0, this.name.equals("Settings") ? ColorPalette.maind02 : ColorPalette.main
         );
         RenderUtil.rect(context, x, y + 41, 220, yLevel - 57, ColorPalette.main);
         if (this.name.equals("Settings")) {
            this.render2.drawString(context, "Ape 4.12", (float)(x + 170), (float)(y + yLevel - 12), ColorPalette.textd43.getRGB(), false);
         }

         this.render.drawString(context, this.name, (float)(x + 35), (float)(y + 15), ColorPalette.text.getRGB(), false);
         RenderUtil.image(
            context,
            this.back,
            x + 11,
            y + 13,
            16,
            16,
            mouseX - x > 11 && mouseX - x < 27 && mouseY - y > 13 && mouseY - y < 29 ? ColorPalette.text : ColorPalette.mainl37
         );
         RenderUtil.image(
            context,
            this.close,
            x + 185,
            y + 9,
            24,
            24,
            mouseX - x > 185 && mouseX - x < 209 && mouseY - y > 9 && mouseY - y < 33 ? ColorPalette.text : ColorPalette.textd16
         );
         yLevel = 41;
         int i = 0;

         for (GuiObject objectx : this.objects) {
            if (objectx.render(mouseX, mouseY, context, x, y + yLevel, i)) {
               yLevel += objectx.getSize();
               i++;
            }
         }

         return true;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         if (mouseX - (double)this.x > 11.0 && mouseX - (double)this.x < 27.0 && mouseY - (double)this.y > 13.0 && mouseY - (double)this.y < 29.0) {
            this.visible = false;
            if (this.parent != null) {
               this.parent.visible = true;
            }

            return true;
         }

         if (mouseX - (double)this.x > 185.0 && mouseX - (double)this.x < 209.0 && mouseY - (double)this.y > 9.0 && mouseY - (double)this.y < 33.0) {
            this.visible = false;
            if (this.parent != null) {
               this.parent.visible = false;
            }

            return true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void save(JSONObject saved) {
      JSONObject data = new JSONObject();
      saved.put(this.name + "Pane", data);
      super.save(data);
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.name + "Pane")) {
         super.load(saved.getJSONObject(this.name + "Pane"));
      }
   }

   public SettingsPane get() {
      return this;
   }
}
