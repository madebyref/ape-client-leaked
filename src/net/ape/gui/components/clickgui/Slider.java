package net.ape.gui.components.clickgui;

import java.awt.Color;
import net.ape.ApeClient;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.TextField;
import net.ape.gui.components.Tween;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import org.json.JSONObject;

public class Slider extends GuiObject {
   String name;
   int min;
   int max;
   public int value;
   double decimal;
   boolean dragging;
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 12");
   Tween positionTween = new Tween(0.2, 185.0, 185.0);
   Tween sizeTween = new Tween(0.1, 14.0, 16.0);
   TextField field = new TextField(true);

   public Slider(String name, GuiObject mod, int min, int max, int defaultval, double decimal) {
      this.name = name;
      this.min = min;
      this.max = max;
      this.value = defaultval;
      this.decimal = decimal;
      this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)max), 0.0F, 185.0F));
      mod.objects.add(this);
   }

   public Slider(String name, Module mod, int min, int max, int defaultval, double decimal) {
      this(name, mod.button, min, max, defaultval, decimal);
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         if (this.dragging) {
            this.value = (int)((float)this.min + (float)(this.max - this.min) * class_3532.method_15363((float)(mouseX - (x + 15)) / 200.0F, 0.0F, 1.0F));
            this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)this.max), 0.0F, 185.0F));
         }

         this.render.drawString(context, this.name, (float)(x + 9), (float)(y + 11), ColorPalette.textd16.getRGB(), false);
         if (this.field.focused) {
            this.field.render(context, x + 190, y + 11);
         } else {
            this.render
               .drawString(
                  context,
                  String.valueOf((double)this.value / this.decimal),
                  (float)(x + 210) - this.render.getWidth(String.valueOf((double)this.value / this.decimal)),
                  (float)(y + 11),
                  ColorPalette.textd16.getRGB(),
                  false
               );
         }

         boolean hovered = mouseX - x > 0 && mouseX - x < 220 && mouseY - y > 0 && mouseY - y < 50;
         RenderUtil.rect(context, x + 10, y + 37, 200, 2, ColorPalette.mainl02);
         float width = (float)this.positionTween.getValue();
         Color guiColor = ApeClient.getRenderColor((float)ind * -0.075F);
         RenderUtil.rect(context, (float)(x + 10), (float)(y + 37), width, 2.0F, guiColor);
         RenderUtil.rect(context, (float)(x + 5) + width, (float)(y + 28), 22.0F, 20.0F, ColorPalette.maind02);
         this.sizeTween.setDestinationValue(hovered ? 16.0 : 14.0);
         RenderUtil.roundedRect(
            context,
            (double)((float)x + (16.0F - (float)this.sizeTween.getValue() / 2.0F) + width),
            (double)((float)(y + 30) + (8.0F - (float)this.sizeTween.getValue() / 2.0F)),
            (double)this.sizeTween.getValue(),
            (double)this.sizeTween.getValue(),
            (double)(this.sizeTween.getValue() + 2),
            guiColor
         );
         return true;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.visible) {
         if (mouseX - (double)this.x > 0.0
            && mouseX - (double)this.x < 220.0
            && mouseY - (double)this.y > 20.0
            && mouseY - (double)this.y < 50.0
            && button == 0) {
            this.dragging = true;
            return true;
         }

         if (mouseX - (double)this.x > 160.0
            && mouseX - (double)this.x < 220.0
            && mouseY - (double)this.y > 0.0
            && mouseY - (double)this.y < 20.0
            && button == 0) {
            this.field.value = String.valueOf((double)this.value / this.decimal);
            this.field.focused = true;
            return true;
         }

         this.field.focused = false;
      }

      return false;
   }

   @Override
   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      this.dragging = false;
      return false;
   }

   @Override
   public void keyTyped(int keyCode) {
      if (this.field.focused) {
         this.field.keyPressed(keyCode);
         if (keyCode == 257 || keyCode == 335) {
            try {
               this.value = (int)Math.floor(Double.valueOf(this.field.value) * this.decimal);
               this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)this.max), 0.0F, 185.0F));
            } catch (Exception var3) {
            }

            this.field.focused = false;
         }
      }
   }

   @Override
   public void charTyped(char key) {
      if (this.field.focused) {
         this.field.charTyped(key);
      }
   }

   @Override
   public int getSize() {
      return 50;
   }

   @Override
   public void save(JSONObject saved) {
      saved.put(this.name, this.value);
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.name)) {
         this.value = saved.getInt(this.name);
         this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)this.max), 0.0F, 185.0F));
      }
   }
}
