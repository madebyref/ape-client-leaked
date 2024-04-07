package net.ape.gui.components.clickgui.settings;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.ape.ApeClient;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.TextField;
import net.ape.gui.components.Tween;
import net.ape.gui.components.clickgui.ColorSlider;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class GUIColorSlider extends GuiObject {
   String name;
   int min;
   int max;
   public int value;
   double decimal;
   boolean dragging;
   List<Color> colors = Arrays.asList(
      new Color(250, 50, 56),
      new Color(242, 99, 33),
      new Color(252, 179, 22),
      new Color(5, 133, 104),
      new Color(47, 122, 229),
      new Color(126, 84, 217),
      new Color(232, 96, 152)
   );
   public int[] colorpos = new int[]{4, 33, 62, 90, 119, 148, 177};
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 12");
   class_2960 color = new class_2960("ape/colorpreview.png");
   class_2960 uparrow = new class_2960("ape/expandup.png");
   class_2960 downarrow = new class_2960("ape/expanddown.png");
   class_2960 rain1 = new class_2960("ape/rainbow_1.png");
   class_2960 rain2 = new class_2960("ape/rainbow_2.png");
   class_2960 rain3 = new class_2960("ape/rainbow_3.png");
   class_2960 rain4 = new class_2960("ape/rainbow_4.png");
   class_2960 knob = new class_2960("ape/guislider.png");
   class_2960 knobrain = new class_2960("ape/guisliderrain.png");
   public Tween positionTween = new Tween(0.2, 185.0, 185.0);
   Tween sizeTween = new Tween(0.1, 14.0, 16.0);
   TextField field = new TextField(false);
   public ColorSlider hue;
   public ColorSlider sat;
   public ColorSlider val;
   boolean expanded = false;
   public boolean custom;
   long clickTick = System.currentTimeMillis();

   public GUIColorSlider(String name, GuiObject mod, @Nullable GUIColorSlider parent) {
      this.name = name;
      this.min = 0;
      this.max = this.colors.size() - 1;
      this.decimal = 1.0;
      this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)this.max), 0.0F, 185.0F));
      mod.objects.add(this);
      this.hue = new ColorSlider(name + "Hue", mod, "Color", this);
      this.hue.visible = false;
      this.sat = new ColorSlider(name + "Saturation", mod, "Sat", this);
      this.sat.visible = false;
      this.val = new ColorSlider(name + "Value", mod, "Val", this);
      this.val.visible = false;
      this.value = 3;
      this.updateSlider(true);
   }

   public GUIColorSlider(String name, GuiObject mod) {
      this(name, mod, null);
   }

   public GUIColorSlider(String name, Module mod) {
      this(name, mod.button, null);
   }

   public boolean getRainbow() {
      return ApeClient.rainbowQueue != null && ApeClient.rainbowQueue.contains(this.hue);
   }

   public void toggleRainbow() {
      if (this.getRainbow()) {
         ApeClient.rainbowQueue.remove(this.hue);
      } else {
         ApeClient.rainbowQueue.add(this.hue);
      }

      this.custom = this.getRainbow();
      this.value = 3;
      this.hue.value = 100;
      this.sat.value = 100;
      this.val.value = 100;
      this.updateSlider(!this.custom);
   }

   public void updateSlider(boolean setValues) {
      float[] knobColor = Color.RGBtoHSB(
         this.colors.get(this.value).getRed(), this.colors.get(this.value).getGreen(), this.colors.get(this.value).getBlue(), null
      );
      if (setValues) {
         this.hue.value = Math.round(knobColor[0] * 100.0F);
         this.sat.value = Math.round(knobColor[1] * 100.0F);
         this.val.value = Math.round(knobColor[2] * 100.0F);
      }

      this.positionTween.setDestinationValue((double)class_3532.method_15363((float)(this.colorpos[this.value] - 3), 0.0F, 185.0F));
      this.hue.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.hue.value / 100.0F), 0.0F, 185.0F));
      this.sat.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.sat.value / 100.0F), 0.0F, 185.0F));
      this.val.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.val.value / 100.0F), 0.0F, 185.0F));
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         if (this.dragging) {
            this.value = (int)((float)this.min + (float)(this.max - this.min) * class_3532.method_15363((float)(mouseX - (x - 10)) / 200.0F, 0.0F, 1.0F));
            if (this.getRainbow()) {
               this.toggleRainbow();
            }

            this.updateSlider(true);
            this.custom = false;
         }

         this.render.drawString(context, this.name, (float)(x + 9), (float)(y + 11), ColorPalette.textd16.getRGB(), false);
         Color newColor = Color.getHSBColor((float)this.hue.value / 100.0F, (float)this.sat.value / 100.0F, (float)this.val.value / 100.0F);
         if (this.field.focused) {
            this.field.render(context, x + 190, y + 11);
         } else {
            RenderUtil.image(context, this.color, x + 198, y + 10, 12, 12, newColor);
         }

         int i = 0;
         int colornum = 0;

         for (Color barColor : this.colors) {
            i++;
            int size = 27 + ((i + 1) % 2 == 0 ? 1 : 0);
            RenderUtil.rect(context, x + 10 + colornum, y + 37, size, 2, barColor);
            colornum = colornum + size + 1;
         }

         boolean hovered = mouseX - x > 0 && mouseX - x < 220 && mouseY - y > 0 && mouseY - y < 50;
         RenderUtil.image(
            context, this.expanded ? this.uparrow : this.downarrow, x + 12 + (int)this.render.getWidth(this.name), y + 11, 9, 4, ColorPalette.textd31
         );
         boolean inRainbow = ApeClient.rainbowQueue.contains(this.hue);
         RenderUtil.image(context, this.rain1, x + 178, y + 10, 12, 12, inRainbow ? new Color(5, 127, 100) : ColorPalette.mainl37);
         RenderUtil.image(context, this.rain2, x + 178, y + 10, 12, 12, inRainbow ? new Color(228, 125, 43) : ColorPalette.mainl37);
         RenderUtil.image(context, this.rain3, x + 178, y + 10, 12, 12, inRainbow ? new Color(225, 46, 52) : ColorPalette.mainl37);
         RenderUtil.image(context, this.rain4, x + 178, y + 10, 12, 12, ColorPalette.mainl37);
         float width = (float)this.positionTween.getValue();
         this.sizeTween.setDestinationValue(hovered ? 16.0 : 14.0);
         RenderUtil.image(
            context, this.custom ? this.knobrain : this.knob, (int)((float)x + width + 10.0F), y + 32, 26, 12, this.custom ? Color.white : newColor
         );
         return true;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.visible) {
         int check = this.x + 12 + (int)this.render.getWidth(this.name) - 4;
         if (mouseX - (double)check > 0.0 && mouseX - (double)check < 17.0 && mouseY - (double)this.y > 7.0 && mouseY - (double)this.y < 19.0 && button == 0) {
            this.expanded = !this.expanded;
            this.hue.visible = this.expanded;
            this.sat.visible = this.expanded;
            this.val.visible = this.expanded;
            return true;
         }

         check = this.x + 178;
         if (mouseX - (double)check > 0.0 && mouseX - (double)check < 12.0 && mouseY - (double)this.y > 10.0 && mouseY - (double)this.y < 22.0 && button == 0) {
            this.toggleRainbow();
            return true;
         }

         if (mouseX - (double)this.x > 160.0
            && mouseX - (double)this.x < 220.0
            && mouseY - (double)this.y > 0.0
            && mouseY - (double)this.y < 20.0
            && button == 0) {
            Color newColor = Color.getHSBColor((float)this.hue.value / 100.0F, (float)this.sat.value / 100.0F, (float)this.val.value / 100.0F);
            this.field.value = newColor.getRed() + ", " + newColor.getGreen() + ", " + newColor.getBlue();
            this.field.focused = true;
            return true;
         }

         if (mouseX - (double)this.x > 0.0
            && mouseX - (double)this.x < 220.0
            && mouseY - (double)this.y > 20.0
            && mouseY - (double)this.y < 50.0
            && button == 0) {
            if (System.currentTimeMillis() - this.clickTick < 300L) {
               this.toggleRainbow();
               return true;
            }

            this.clickTick = System.currentTimeMillis();
            this.dragging = true;
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
               String[] strs = this.field.value.split(",");
               Color newColor;
               if (strs.length > 1 && Integer.valueOf(strs[0]) != null) {
                  newColor = new Color(Integer.valueOf(strs[0]), Integer.valueOf(strs[1].replace(" ", "")), Integer.valueOf(strs[2].replace(" ", "")));
               } else {
                  newColor = new Color(Integer.parseInt(this.field.value.replace("#", ""), 16));
               }

               if (newColor != null) {
                  float[] vals = Color.RGBtoHSB(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), null);
                  this.hue.value = Math.round(vals[0] * 100.0F);
                  this.sat.value = Math.round(vals[1] * 100.0F);
                  this.val.value = Math.round(vals[2] * 100.0F);
                  this.value = 3;
                  this.custom = true;
                  this.updateSlider(false);
               }
            } catch (Exception var5) {
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
      JSONObject data = new JSONObject();
      data.put("hue", this.hue.value);
      data.put("sat", this.sat.value);
      data.put("value", this.val.value);
      data.put("notch", this.value);
      data.put("custom", this.custom);
      data.put("rainbow", this.getRainbow());
      saved.put(this.name, data);
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.name)) {
         JSONObject data = saved.getJSONObject(this.name);
         this.value = data.getInt("notch");
         if (this.custom) {
            this.hue.value = data.getInt("hue");
            this.sat.value = data.getInt("sat");
            this.val.value = data.getInt("value");
         }

         this.updateSlider(this.custom);
         if (this.getRainbow() != data.getBoolean("rainbow")) {
            this.toggleRainbow();
         }
      }
   }
}
