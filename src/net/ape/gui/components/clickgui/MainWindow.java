package net.ape.gui.components.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.ape.ApeClient;
import net.ape.gui.ClickGui;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.json.JSONObject;

public class MainWindow extends GuiObject {
   class_2960 blur = new class_2960("ape/blur.png");
   class_2960 ape = new class_2960("ape/guiape.png");
   class_2960 v4 = new class_2960("ape/guiv4.png");
   class_2960 settings = new class_2960("ape/guisettings.png");
   boolean dragging;
   double dragX;
   double dragY;

   public MainWindow() {
      this.visible = true;
      this.x = 6;
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

         for (GuiObject object : this.objects) {
            if (object.visible) {
               yLevel += object.getSize();
            }
         }

         RenderUtil.smoothRoundedRect(context, (double)x, (double)y, 220.0, (double)(4 + yLevel), 10.0, new Color(20, 20, 20));
         RenderSystem.enableBlend();
         context.method_25290(this.blur, x - 48, y - 31, 0.0F, 0.0F, 309, 45, 309, 533);
         context.method_25293(this.blur, x - 48, y + 14, 309, yLevel - 16, 0.0F, 45.0F, 309, 1, 309, 533);
         context.method_25293(this.blur, x - 48, y + yLevel - 2, 309, 45, 0.0F, 506.0F, 309, 45, 309, 533);
         context.method_25290(this.ape, x + 11, y + 12, 0.0F, 0.0F, 47, 18, 47, 18);
         RenderUtil.image(context, this.ape, x + 11, y + 12, 47, 18);
         RenderUtil.image(context, this.v4, x + 59, y + 13, 28, 16, ApeClient.getRenderColor());
         RenderUtil.image(
            context,
            this.settings,
            x + 195,
            y + 14,
            14,
            14,
            mouseX - this.x > 180 && mouseX - this.x < 220 && mouseY - this.y > 0 && mouseY - this.y < 40 ? ColorPalette.text : ColorPalette.mainl37
         );
         yLevel = 41;
         int i = 0;

         for (GuiObject objectx : this.objects) {
            if (objectx.render(mouseX, mouseY, context, x, y + yLevel, i)) {
               yLevel += objectx.getSize();
               i++;
            }
         }

         RenderSystem.enableBlend();
         return true;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      GuiObject check = (GuiObject)(ClickGui.pane.visible
         ? ClickGui.pane
         : (
            ClickGui.pane2.visible
               ? ClickGui.pane2
               : (ClickGui.pane3.visible ? ClickGui.pane3 : (ClickGui.pane4.visible ? ClickGui.pane4 : (ClickGui.pane5.visible ? ClickGui.pane5 : null)))
         ));
      if (check != null) {
         check.mouseClicked(mouseX, mouseY, button);
         return true;
      } else {
         if (button == 0) {
            if (mouseX - (double)this.x > 180.0 && mouseX - (double)this.x < 220.0 && mouseY - (double)this.y > 0.0 && mouseY - (double)this.y < 40.0) {
               ClickGui.pane.visible = true;
               return true;
            }

            if (mouseX - (double)this.x > 0.0 && mouseX - (double)this.x < 220.0 && mouseY - (double)this.y > 0.0 && mouseY - (double)this.y < 40.0) {
               this.dragging = true;
               this.dragX = (double)this.x - mouseX;
               this.dragY = (double)this.y - mouseY;
               return true;
            }
         }

         return super.mouseClicked(mouseX, mouseY, button);
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

      for (GuiObject obj : this.objects) {
         obj.save(data);
      }

      saved.put("Main", data);
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has("Main")) {
         JSONObject data = saved.getJSONObject("Main");
         this.x = data.getInt("x");
         this.y = data.getInt("y");

         for (GuiObject obj : this.objects) {
            obj.load(data);
         }
      }
   }
}
