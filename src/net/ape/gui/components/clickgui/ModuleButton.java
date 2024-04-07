package net.ape.gui.components.clickgui;

import java.awt.Color;
import net.ape.ApeClient;
import net.ape.gui.ClickGui;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.json.JSONObject;
import org.lwjgl.glfw.GLFW;

public class ModuleButton extends GuiObject {
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   TTFFontRenderer bindrender = CustomFont.FONT_MANAGER.getFont("Arial 12");
   class_2960 dots = new class_2960("ape/dots.png");
   class_2960 bind = new class_2960("ape/bind.png");
   class_2960 pencil = new class_2960("ape/edit.png");
   class_2960 bindbkg = new class_2960("ape/bindbkg.png");
   public String text = "";
   Module module;
   boolean expanded;

   public ModuleButton(String text, Module module) {
      this.text = text;
      this.module = module;
   }

   private String safeStr() {
      String str = GLFW.glfwGetKeyName(this.module.key, -1);
      return str != null ? str.toUpperCase() : "unknown";
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         boolean hover = mouseX - this.x > 0 && mouseX - this.x < 220 && mouseY - this.y > 0 && mouseY - this.y < 40 || this.expanded;
         boolean dotshover = mouseX - this.x > 195 && mouseX - this.x < 220 && mouseY - this.y > 0 && mouseY - this.y < 40;
         Color textColor = ApeClient.textColor();
         RenderUtil.rect(
            context,
            x,
            y,
            220,
            40,
            this.module.enabled ? ApeClient.getRenderColor((float)ind * -0.025F) : (!hover && !this.expanded ? ColorPalette.main : ColorPalette.mainl02)
         );
         if (this.module.enabled) {
            RenderUtil.rect(context, x, y + 39, 220, 1, new Color(0, 0, 0, 40));
         }

         this.render
            .drawString(
               context,
               this.text,
               (float)(x + 9),
               (float)(y + 15),
               (this.module.enabled ? textColor : (hover ? ColorPalette.text : ColorPalette.textd16)).getRGB(),
               false
            );
         RenderUtil.image(context, this.dots, x + 199, y + 12, 3, 16, this.module.enabled ? textColor : (dotshover ? ColorPalette.text : ColorPalette.mainl37));
         if (hover || this.module.key != -99) {
            int bindSize = this.module.key != -99 ? Math.max((int)this.bindrender.getWidth(this.safeStr()) + 8, 20) : 20;
            int bindX = x + 184 - bindSize;
            boolean bindhover = mouseX - bindX > 0 && mouseX - bindX < bindSize && mouseY - (y + 9) > 0 && mouseY - (y + 9) < 21;
            RenderUtil.roundedRect(context, (double)bindX, (double)(y + 9), (double)bindSize, 21.0, 8.0, new Color(255, 255, 255, 20));
            if (this.module.key != -99 && !bindhover) {
               if (this.module.key != -99) {
                  this.bindrender
                     .drawString(
                        context,
                        this.safeStr(),
                        (float)(bindX + (int)((float)bindSize / 2.0F)) - this.bindrender.getWidth(this.safeStr()) / 2.0F - 1.0F,
                        (float)(y + 15),
                        (this.module.enabled ? textColor : ColorPalette.textd43).getRGB(),
                        false
                     );
               }
            } else {
               RenderUtil.image(
                  context,
                  bindhover ? this.pencil : this.bind,
                  x + 184 - (int)((float)bindSize / 2.0F + 6.0F),
                  y + 14,
                  12,
                  12,
                  this.module.enabled ? textColor : (bindhover ? ColorPalette.textd16 : ColorPalette.textd43)
               );
            }
         }

         if (this.expanded) {
            int yLevel = 0;
            int i = 0;

            for (GuiObject object : this.objects) {
               if (object.visible) {
                  yLevel += object.getSize();
               }
            }

            RenderUtil.rect(context, x, y + 40, 220, yLevel, ColorPalette.maind02);
            yLevel = 40;

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
      int bindSize = 20;
      int bindX = this.x + 184 - bindSize;
      if (mouseX - (double)bindX > 0.0
         && mouseX - (double)bindX < (double)bindSize
         && mouseY - (double)(this.y + 9) > 0.0
         && mouseY - (double)(this.y + 9) < 21.0
         && ClickGui.binding == null) {
         ClickGui.binding = this.module;
         return true;
      } else if (mouseX - (double)this.x > 195.0 && mouseX - (double)this.x < 220.0 && mouseY - (double)this.y > 0.0 && mouseY - (double)this.y < 40.0) {
         this.expanded = !this.expanded;
         return true;
      } else if (mouseX - (double)this.x > 0.0 && mouseX - (double)this.x < 220.0 && mouseY - (double)this.y > 0.0 && mouseY - (double)this.y < 40.0) {
         if (button == 0) {
            this.module.toggle();
         } else {
            this.expanded = !this.expanded;
         }

         return true;
      } else {
         return this.expanded ? super.mouseClicked(mouseX, mouseY, button) : false;
      }
   }

   @Override
   public int getSize() {
      int size = 40;
      if (this.expanded) {
         for (GuiObject obj : this.objects) {
            if (obj.visible) {
               size += obj.getSize();
            }
         }
      }

      return size;
   }

   @Override
   public void save(JSONObject saved) {
      JSONObject data = new JSONObject();
      data.put("options", new JSONObject());
      data.put("enabled", this.module.enabled);
      data.put("bind", this.module.key);
      saved.put(this.module.name, data);
      super.save(data.getJSONObject("options"));
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.module.name)) {
         JSONObject data = saved.getJSONObject(this.module.name);
         if (data.getBoolean("enabled") != this.module.enabled) {
            this.module.toggle();
         }

         this.module.key = data.getInt("bind");
         super.load(data.getJSONObject("options"));
      }
   }
}
