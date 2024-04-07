package net.ape.gui.components.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.json.JSONObject;

public class Window extends GuiObject {
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   class_2960 blur = new class_2960("ape/blur.png");
   class_2960 uparrow = new class_2960("ape/expandup.png");
   class_2960 downarrow = new class_2960("ape/expanddown.png");
   class_2960 icon;
   int iconSizeX;
   int iconSizeY;
   String name;
   boolean dragging;
   double dragX;
   double dragY;
   boolean expanded;

   public Window(String name, class_2960 icon, int iconSizeX, int iconSizeY) {
      this.name = name;
      this.icon = icon;
      this.iconSizeX = iconSizeX;
      this.iconSizeY = iconSizeY;
      this.visible = false;
      this.x = 220;
      this.y = 6;
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         if (this.dragging) {
            x = (int)((double)mouseX + this.dragX);
            y = (int)((double)mouseY + this.dragY);
            this.x = x;
            this.y = y;
         }

         int yLevel = 41;
         if (this.expanded) {
            for (GuiObject object : this.objects) {
               if (object.visible) {
                  yLevel += object.getSize();
               }
            }
         }

         RenderUtil.smoothRoundedRect(context, (double)x, (double)y, 220.0, (double)(4 + yLevel), 10.0, ColorPalette.main);
         RenderSystem.enableBlend();
         context.method_25290(this.blur, x - 48, y - 31, 0.0F, 0.0F, 309, 45, 309, 533);
         context.method_25293(this.blur, x - 48, y + 14, 309, yLevel - 16, 0.0F, 45.0F, 309, 1, 309, 533);
         context.method_25293(this.blur, x - 48, y + yLevel - 2, 309, 45, 0.0F, 506.0F, 309, 45, 309, 533);
         RenderUtil.image(context, this.icon, x + 12, y + (this.iconSizeX > 20 ? 13 : 12), this.iconSizeX, this.iconSizeY, ColorPalette.text);
         this.render.drawString(context, this.name, (float)(x + (this.iconSizeX > 20 ? 43 : 35)), (float)(y + 16), ColorPalette.text.getRGB(), false);
         Color hoverColor = mouseX - this.x > 180 && mouseX - this.x < 220 && mouseY - this.y > 0 && mouseY - this.y < 40
            ? ColorPalette.text
            : ColorPalette.mainl37;
         RenderUtil.image(context, this.expanded ? this.uparrow : this.downarrow, x + 200, y + 19, 9, 4, hoverColor);
         if (this.expanded) {
            yLevel = 41;
            int i = 0;

            for (GuiObject objectx : this.objects) {
               if (objectx.render(mouseX, mouseY, context, x, y + yLevel, i)) {
                  yLevel += objectx.getSize();
                  i++;
               }
            }
         }

         return true;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (mouseX - (double)this.x > 180.0 && mouseX - (double)this.x < 220.0 && mouseY - (double)this.y > 0.0 && mouseY - (double)this.y < 40.0) {
         this.expanded = !this.expanded;
         return true;
      } else if (mouseX - (double)this.x > 0.0 && mouseX - (double)this.x < 220.0 && mouseY - (double)this.y > 0.0 && mouseY - (double)this.y < 40.0) {
         if (button == 0) {
            this.dragging = true;
            this.dragX = (double)this.x - mouseX;
            this.dragY = (double)this.y - mouseY;
         } else {
            this.expanded = !this.expanded;
         }

         return true;
      } else {
         return this.expanded ? super.mouseClicked(mouseX, mouseY, button) : false;
      }
   }

   @Override
   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      if (this.dragging && button == 0) {
         this.dragging = false;
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   @Override
   public void save(JSONObject saved) {
      JSONObject data = new JSONObject();
      data.put("x", this.x);
      data.put("y", this.y);
      data.put("expanded", this.expanded);
      saved.put(this.name, data);
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.name)) {
         JSONObject data = saved.getJSONObject(this.name);
         this.x = data.getInt("x");
         this.y = data.getInt("y");
         this.expanded = data.getBoolean("expanded");
      }
   }
}
